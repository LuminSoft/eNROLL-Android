#!/usr/bin/env bash
# =============================================================================
# check-enroll-sync.sh — Cross-repo sync checker for eNROLL SDK ecosystem
#
# Checks: Android SDK version, iOS distribution type/version, XCFramework
# consistency, Flutter-vs-sibling feature parity, docs, and branch alignment.
#
# Usage:
#   bash /Users/luminsoft/StudioProjects/ekyc-android/scripts/check-enroll-sync.sh
# =============================================================================

set -euo pipefail

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'
BOLD='\033[1m'

OK="${GREEN}✔${NC}"
WARN="${YELLOW}⚠${NC}"
FAIL="${RED}✘${NC}"

NATIVE_SDK_PATH="/Users/luminsoft/StudioProjects/ekyc-android"
NATIVE_LINKAGE="${NATIVE_SDK_PATH}/.enroll-linkage.json"

ENROLL_FLUTTER="/Users/luminsoft/StudioProjects/enroll_flutter_plugin"
ENROLL_RN="/Users/luminsoft/StudioProjects/enroll-react-native"
ENROLL_CAP="/Users/luminsoft/StudioProjects/enroll-capacitor"

NEO_FLUTTER="/Users/luminsoft/StudioProjects/enroll_neo_plugin"
NEO_RN="/Users/luminsoft/StudioProjects/enroll-neo-react-native"
NEO_CAP="/Users/luminsoft/StudioProjects/enroll-capacitor-neo"

TOTAL_CHECKS=0; PASSED=0; WARNINGS=0; FAILURES=0

pass()  { ((TOTAL_CHECKS++)); ((PASSED++));   echo -e "    ${OK}  $1"; }
warn()  { ((TOTAL_CHECKS++)); ((WARNINGS++)); echo -e "    ${WARN}  $1"; }
fail()  { ((TOTAL_CHECKS++)); ((FAILURES++)); echo -e "    ${FAIL}  $1"; }

# ---------------------------------------------------------------------------
# JSON helpers (python3, available on macOS)
# ---------------------------------------------------------------------------
json_field() {
  python3 -c "
import json
with open('$1') as f: data = json.load(f)
keys = '$2'.split('.')
val = data
for k in keys:
    if isinstance(val, dict) and k in val: val = val[k]
    else: val = ''; break
if isinstance(val, list): print(','.join(str(v) for v in val))
else: print(val)
" 2>/dev/null || echo ""
}

json_array() {
  python3 -c "
import json
with open('$1') as f: data = json.load(f)
keys = '$2'.split('.')
val = data
for k in keys:
    if isinstance(val, dict) and k in val: val = val[k]
    else: val = []; break
if isinstance(val, list):
    for item in val: print(item)
" 2>/dev/null
}

json_array_count() {
  python3 -c "
import json
with open('$1') as f: data = json.load(f)
keys = '$2'.split('.')
val = data
for k in keys:
    if isinstance(val, dict) and k in val: val = val[k]
    else: val = []; break
print(len(val) if isinstance(val, list) else 0)
" 2>/dev/null || echo "0"
}

# ---------------------------------------------------------------------------
# Extract actual SDK version from android/build.gradle
# ---------------------------------------------------------------------------
extract_gradle_sdk_version() {
  local gradle_file="$1/android/build.gradle"
  [[ -f "$gradle_file" ]] && grep -oE "eNROLL-(Android|Lite-Android):[v]?[0-9]+\.[0-9]+\.[0-9]+" "$gradle_file" 2>/dev/null | head -1 | sed 's/.*://' || echo ""
}

# ---------------------------------------------------------------------------
# Extract iOS pod version from podspec
# ---------------------------------------------------------------------------
extract_ios_pod_version() {
  local plugin_path="$1"
  local podspec
  podspec=$(find "$plugin_path" -maxdepth 2 -name "*.podspec" 2>/dev/null | head -1)
  if [[ -n "$podspec" ]]; then
    grep -oE "EnrollFramework.*['\"][~>]*\s*[0-9]+\.[0-9]+\.[0-9]+" "$podspec" 2>/dev/null | grep -oE "[0-9]+\.[0-9]+\.[0-9]+" | head -1 || echo ""
  fi
}

extract_neo_core_version() {
  local plugin_path="$1"
  local podspec
  podspec=$(find "$plugin_path" -maxdepth 2 -name "*.podspec" 2>/dev/null | head -1)
  if [[ -n "$podspec" ]]; then
    grep -oE "EnrollNeoCore.*['\"][0-9]+\.[0-9]+\.[0-9]+" "$podspec" 2>/dev/null | grep -oE "[0-9]+\.[0-9]+\.[0-9]+" | head -1 || echo ""
  fi
}

# ---------------------------------------------------------------------------
# XCFramework MD5 check
# ---------------------------------------------------------------------------
xcf_md5() {
  local xcf_path="$1/ios/Frameworks/EnrollFramework.xcframework/ios-arm64/EnrollFramework.framework/EnrollFramework"
  [[ -f "$xcf_path" ]] && md5 -q "$xcf_path" 2>/dev/null || echo "NOT_FOUND"
}

# ===========================================================================
# HEADER
# ===========================================================================
echo ""
echo -e "${BOLD}${BLUE}╔══════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BOLD}${BLUE}║          eNROLL Ecosystem Sync Check                        ║${NC}"
echo -e "${BOLD}${BLUE}╚══════════════════════════════════════════════════════════════╝${NC}"
echo ""

if [[ ! -f "$NATIVE_LINKAGE" ]]; then
  echo -e "  ${FAIL} Native SDK .enroll-linkage.json not found at: ${NATIVE_LINKAGE}"
  exit 1
fi

ENROLL_ANDROID_VER=$(json_field "$NATIVE_LINKAGE" "productLines.enroll.androidVersion")
NEO_ANDROID_VER=$(json_field "$NATIVE_LINKAGE" "productLines.enroll-neo.androidVersion")
ENROLL_IOS_POD=$(json_field "$NATIVE_LINKAGE" "productLines.enroll.iosPodVersion")
NEO_IOS_CORE=$(json_field "$NATIVE_LINKAGE" "productLines.enroll-neo.iosCorePodVersion")

echo -e "${CYAN}Native SDK Versions:${NC}"
echo -e "  enroll  Android: ${BOLD}${ENROLL_ANDROID_VER}${NC}  |  iOS pod: ${BOLD}${ENROLL_IOS_POD}${NC}"
echo -e "  neo     Android: ${BOLD}${NEO_ANDROID_VER}${NC}    |  iOS EnrollNeoCore: ${BOLD}${NEO_IOS_CORE}${NC}"
echo ""

# ===========================================================================
# Check a single plugin
# ===========================================================================
check_plugin() {
  local plugin_path="$1"
  local expected_android_ver="$2"
  local expected_branch="$3"
  local expected_ios_dist="$4"        # pod | xcframework
  local flutter_path="$5"             # path to Flutter sibling for comparison
  local plugin_name
  plugin_name=$(basename "$plugin_path")
  local linkage="${plugin_path}/.enroll-linkage.json"

  echo -e "  ${BOLD}${CYAN}▸ ${plugin_name}${NC}"

  if [[ ! -f "$linkage" ]]; then
    fail "Missing .enroll-linkage.json"
    echo ""; return
  fi

  local project_type
  project_type=$(json_field "$linkage" "projectType")
  local is_flutter=false
  [[ "$project_type" == "flutter-plugin" ]] && is_flutter=true

  # --- Android SDK Version ---
  local declared_ver
  declared_ver=$(json_field "$linkage" "nativeSdk.androidVersion")
  local gradle_ver
  gradle_ver=$(extract_gradle_sdk_version "$plugin_path")

  if [[ "$gradle_ver" == "$expected_android_ver" ]]; then
    pass "Android SDK (gradle):  ${gradle_ver}"
  elif [[ -n "$gradle_ver" ]]; then
    fail "Android SDK (gradle):  ${gradle_ver} — expected ${expected_android_ver}"
  fi

  if [[ "$declared_ver" == "$expected_android_ver" ]]; then
    pass "Android SDK (linkage): ${declared_ver}"
  else
    fail "Android SDK (linkage): ${declared_ver} — expected ${expected_android_ver}"
  fi

  # --- Branch ---
  local declared_branch
  declared_branch=$(json_field "$linkage" "nativeSdk.branch")
  if [[ "$declared_branch" == "$expected_branch" ]]; then
    pass "Branch: ${declared_branch}"
  else
    fail "Branch: ${declared_branch} — expected ${expected_branch}"
  fi

  # --- iOS Distribution Type ---
  local declared_ios_dist
  declared_ios_dist=$(json_field "$linkage" "nativeSdk.iosDistribution")
  if [[ "$declared_ios_dist" == "$expected_ios_dist" ]]; then
    pass "iOS distribution: ${declared_ios_dist}"
  else
    fail "iOS distribution: ${declared_ios_dist} — expected ${expected_ios_dist}"
  fi

  # --- iOS Version/XCFramework checks ---
  if [[ "$expected_ios_dist" == "pod" ]]; then
    local actual_ios_ver
    actual_ios_ver=$(extract_ios_pod_version "$plugin_path")
    if [[ -n "$actual_ios_ver" ]]; then
      pass "iOS pod version: ${actual_ios_ver}"
    fi
  elif [[ "$expected_ios_dist" == "xcframework" ]]; then
    local actual_neo_core
    actual_neo_core=$(extract_neo_core_version "$plugin_path")
    if [[ -n "$actual_neo_core" ]]; then
      if [[ "$actual_neo_core" == "$NEO_IOS_CORE" ]]; then
        pass "EnrollNeoCore pod: ${actual_neo_core}"
      else
        fail "EnrollNeoCore pod: ${actual_neo_core} — expected ${NEO_IOS_CORE}"
      fi
    fi

    # XCFramework presence
    if [[ -d "${plugin_path}/ios/Frameworks/EnrollFramework.xcframework" ]]; then
      pass "XCFramework: present"
    else
      fail "XCFramework: MISSING at ios/Frameworks/EnrollFramework.xcframework"
    fi

    # XCFramework consistency vs Flutter Neo (only for non-Flutter plugins)
    if [[ "$is_flutter" == "false" && -n "$flutter_path" ]]; then
      local flutter_md5 plugin_md5
      flutter_md5=$(xcf_md5 "$flutter_path")
      plugin_md5=$(xcf_md5 "$plugin_path")
      if [[ "$flutter_md5" == "$plugin_md5" ]]; then
        pass "XCFramework MD5: matches Flutter Neo"
      elif [[ "$plugin_md5" == "NOT_FOUND" ]]; then
        fail "XCFramework binary: NOT FOUND"
      else
        fail "XCFramework MD5: ${plugin_md5:0:12}... ≠ Flutter ${flutter_md5:0:12}... — needs copy"
      fi
    fi
  fi

  # --- Flutter vs Sibling feature comparison ---
  if [[ "$is_flutter" == "false" && -n "$flutter_path" && -f "${flutter_path}/.enroll-linkage.json" ]]; then
    local flutter_linkage="${flutter_path}/.enroll-linkage.json"
    local flutter_feat_count plugin_feat_count
    flutter_feat_count=$(json_array_count "$flutter_linkage" "features.implementedInFlutter")
    plugin_feat_count=$(json_array_count "$linkage" "features.exposedByThisProject")
    if [[ "$plugin_feat_count" -ge "$flutter_feat_count" ]]; then
      pass "Feature parity vs Flutter: ${plugin_feat_count} exposed / ${flutter_feat_count} in Flutter"
    else
      warn "Feature gap vs Flutter: ${plugin_feat_count} exposed / ${flutter_feat_count} in Flutter"
    fi
  fi

  # --- Missing Features ---
  local missing
  missing=$(json_array "$linkage" "features.missingFromThisProject")
  if [[ -z "$missing" ]]; then
    pass "Missing features: none"
  else
    while IFS= read -r item; do
      if [[ "$item" == *"OUTDATED"* ]]; then
        fail "Issue: ${item}"
      else
        warn "Missing: ${item}"
      fi
    done <<< "$missing"
  fi

  # --- Missing Docs ---
  local missing_docs
  missing_docs=$(json_array "$linkage" "docs.missingDocsForFeatures")
  if [[ -z "$missing_docs" ]]; then
    pass "Docs: complete"
  else
    while IFS= read -r item; do
      warn "Missing docs: ${item}"
    done <<< "$missing_docs"
  fi

  # --- ENROLL_LINKAGE.md ---
  if [[ -f "${plugin_path}/ENROLL_LINKAGE.md" ]]; then
    pass "ENROLL_LINKAGE.md: present"
  else
    warn "ENROLL_LINKAGE.md: missing"
  fi

  echo ""
}

# ===========================================================================
# ENROLL (production / Innovatrics)
# ===========================================================================
echo -e "${BOLD}${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BOLD}${BLUE}  ENROLL — production / Innovatrics                           ${NC}"
echo -e "${BOLD}${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "  Branch: ${BOLD}release/production${NC}  |  Android: ${BOLD}${ENROLL_ANDROID_VER}${NC}  |  iOS: ${BOLD}CocoaPods${NC}"
echo ""

check_plugin "$ENROLL_FLUTTER" "$ENROLL_ANDROID_VER" "release/production" "pod" ""
check_plugin "$ENROLL_RN"      "$ENROLL_ANDROID_VER" "release/production" "pod" "$ENROLL_FLUTTER"
check_plugin "$ENROLL_CAP"     "$ENROLL_ANDROID_VER" "release/production" "pod" "$ENROLL_FLUTTER"

# ===========================================================================
# ENROLL NEO — development-lumin-sdk / non-Innovatrics
# ===========================================================================
echo -e "${BOLD}${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BOLD}${BLUE}  ENROLL NEO — development-lumin-sdk / non-Innovatrics        ${NC}"
echo -e "${BOLD}${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "  Branch: ${BOLD}development-lumin-sdk${NC}  |  Android: ${BOLD}${NEO_ANDROID_VER}${NC}  |  iOS: ${BOLD}XCFramework${NC}"
echo ""

check_plugin "$NEO_FLUTTER" "$NEO_ANDROID_VER" "development-lumin-sdk" "xcframework" ""
check_plugin "$NEO_RN"      "$NEO_ANDROID_VER" "development-lumin-sdk" "xcframework" "$NEO_FLUTTER"
check_plugin "$NEO_CAP"     "$NEO_ANDROID_VER" "development-lumin-sdk" "xcframework" "$NEO_FLUTTER"

# ===========================================================================
# Summary
# ===========================================================================
echo -e "${BOLD}${BLUE}━━━ Summary ━━━${NC}"
echo -e "  Total checks: ${TOTAL_CHECKS}"
echo -e "  ${OK}  Passed:   ${PASSED}"
echo -e "  ${WARN}  Warnings: ${WARNINGS}"
echo -e "  ${FAIL}  Failures: ${FAILURES}"
echo ""

if [[ $FAILURES -gt 0 ]]; then
  echo -e "${RED}${BOLD}Some checks FAILED. Review and fix the issues above.${NC}"
  exit 1
elif [[ $WARNINGS -gt 0 ]]; then
  echo -e "${YELLOW}${BOLD}All critical checks passed, but there are warnings to address.${NC}"
  exit 0
else
  echo -e "${GREEN}${BOLD}All checks passed! Ecosystem is in sync.${NC}"
  exit 0
fi
