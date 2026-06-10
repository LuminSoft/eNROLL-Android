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
# Discover public configuration symbols added on the current feature branch.
# Override ENROLL_SYNC_BASE_REF when comparing against a branch other than
# release/production.
# ---------------------------------------------------------------------------
extract_new_public_api_symbols() {
  local base_ref="${ENROLL_SYNC_BASE_REF:-release/production}"
  local public_api_file="eNROLL-sdk/src/main/java/com/luminsoft/enroll_sdk/ui_components/theme/AppTheme.kt"
  local entry_point_file="eNROLL-sdk/src/main/java/com/luminsoft/enroll_sdk/sdk/eNROLL.kt"

  if ! git -C "$NATIVE_SDK_PATH" rev-parse --verify --quiet "$base_ref" >/dev/null; then
    return
  fi

  local base_commit
  base_commit=$(git -C "$NATIVE_SDK_PATH" merge-base HEAD "$base_ref")

  python3 - "$NATIVE_SDK_PATH" "$base_commit" "$public_api_file" "$entry_point_file" <<'PY'
import pathlib
import re
import subprocess
import sys

repo = pathlib.Path(sys.argv[1])
base_commit = sys.argv[2]
relative_file = sys.argv[3]
entry_point_file = sys.argv[4]

current = (repo / relative_file).read_text()
base = subprocess.run(
    ["git", "-C", str(repo), "show", f"{base_commit}:{relative_file}"],
    check=False,
    capture_output=True,
    text=True,
).stdout
current_entry_point = (repo / entry_point_file).read_text()
base_entry_point = subprocess.run(
    ["git", "-C", str(repo), "show", f"{base_commit}:{entry_point_file}"],
    check=False,
    capture_output=True,
    text=True,
).stdout

def public_config_symbols(source):
    data_classes = {}
    enum_classes = set(re.findall(
        r"^\s*enum\s+class\s+([A-Za-z_]\w*)",
        source,
        re.MULTILINE,
    ))
    lines = source.splitlines()
    index = 0
    while index < len(lines):
        match = re.match(r"^\s*data\s+class\s+([A-Za-z_]\w*)\s*\(", lines[index])
        if not match:
            index += 1
            continue

        class_name = match.group(1)
        fields = []
        index += 1
        while index < len(lines) and not re.match(r"^\s*\)", lines[index]):
            field = re.match(
                r"^\s*val\s+([A-Za-z_]\w*)\s*:\s*([A-Za-z_]\w*)",
                lines[index],
            )
            if field:
                fields.append((field.group(1), field.group(2)))
            index += 1
        data_classes[class_name] = fields
        index += 1

    symbols = set()
    pending = ["AppTheme"]
    visited = set()
    while pending:
        class_name = pending.pop()
        if class_name in visited or class_name not in data_classes:
            continue
        visited.add(class_name)
        if class_name != "AppTheme":
            symbols.add(class_name)
        for field_name, field_type in data_classes[class_name]:
            symbols.add(field_name)
            if field_type in data_classes or field_type in enum_classes:
                symbols.add(field_type)
                pending.append(field_type)
    return symbols

def init_parameters(source):
    parameters = set()
    in_init = False
    for line in source.splitlines():
        if re.match(r"^\s*fun\s+init\s*\(", line):
            in_init = True
            continue
        if in_init and re.match(r"^\s*\)", line):
            break
        if in_init:
            parameter = re.match(r"^\s*([A-Za-z_]\w*)\s*:", line)
            if parameter:
                parameters.add(parameter.group(1))
    return parameters

added_symbols = (
    public_config_symbols(current) - public_config_symbols(base)
) | (
    init_parameters(current_entry_point) - init_parameters(base_entry_point)
)
for symbol in sorted(added_symbols):
    print(symbol)
PY
}

plugin_contains_symbol() {
  local plugin_path="$1"
  local symbol="$2"
  local search_paths=()
  local candidate

  for candidate in android ios lib src; do
    [[ -d "${plugin_path}/${candidate}" ]] && search_paths+=("${plugin_path}/${candidate}")
  done

  [[ ${#search_paths[@]} -gt 0 ]] &&
    grep -RIsq \
      --include='*.kt' --include='*.java' --include='*.swift' \
      --include='*.dart' --include='*.ts' --include='*.tsx' \
      --exclude-dir=Frameworks \
      -- "$symbol" "${search_paths[@]}"
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

  # --- Public API additions on the native feature branch ---
  local new_public_symbols
  new_public_symbols=$(extract_new_public_api_symbols)
  if [[ -n "$new_public_symbols" ]]; then
    local missing_public_symbols=()
    while IFS= read -r public_symbol; do
      [[ -z "$public_symbol" ]] && continue
      if ! plugin_contains_symbol "$plugin_path" "$public_symbol"; then
        missing_public_symbols+=("$public_symbol")
      fi
    done <<< "$new_public_symbols"

    if [[ ${#missing_public_symbols[@]} -eq 0 ]]; then
      pass "New native public API: exposed"
    else
      local missing_public_summary
      printf -v missing_public_summary '%s, ' "${missing_public_symbols[@]}"
      missing_public_summary="${missing_public_summary%, }"
      warn "New native public API not found in plugin: ${missing_public_summary}"
    fi
  else
    pass "New native public API: no additions vs ${ENROLL_SYNC_BASE_REF:-release/production}"
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

  # --- README.md ---
  if [[ -f "${plugin_path}/README.md" ]]; then
    pass "README.md: present"
  else
    fail "README.md: MISSING"
  fi

  # --- Feature names in README (best-effort) ---
  local exposed_features
  exposed_features=$(json_array "$linkage" "features.exposedByThisProject")
  if [[ -n "$exposed_features" && -f "${plugin_path}/README.md" ]]; then
    local readme_content undocumented_count=0
    readme_content=$(cat "${plugin_path}/README.md")
    while IFS= read -r feat; do
      [[ -z "$feat" ]] && continue
      # Normalize: SIGN_CONTRACT → signContract, AUTH → auth, etc.
      local lower_feat
      lower_feat=$(echo "$feat" | tr '[:upper:]' '[:lower:]' | sed 's/_//g')
      local readme_lower
      readme_lower=$(echo "$readme_content" | tr '[:upper:]' '[:lower:]' | sed 's/_//g')
      if ! echo "$readme_lower" | grep -q "$lower_feat"; then
        ((undocumented_count++))
      fi
    done <<< "$exposed_features"
    if [[ $undocumented_count -eq 0 ]]; then
      pass "README coverage: all exposed features mentioned"
    else
      warn "README coverage: ${undocumented_count} exposed feature(s) not found in README"
    fi
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
