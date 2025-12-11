# User Story: Orange Dependency Compatibility Fix

## Story ID: 151XXX

## Title
Android - Fix SDK Dependency Conflicts for Orange Integration

---

## Description

**As an** integrating mobile developer (Orange team),

**I want** the eNROLL SDK to be compatible with our application's dependency versions,

**so that** the SDK does not crash at runtime due to API signature mismatches when integrated into our app.

---

## Priority
**1 - Critical** (Production blocker)

---

## Risk
**High** - Runtime crashes affecting production integration

---

## Story Points
**5**

---

## Fields Validations
N/A

---

## Acceptance Criteria

1. The SDK must work with the following dependency versions without crashes:
   - Kotlin 1.9.25
   - Lottie Compose 6.6.2
   - Material3 1.3.1
   - Compose UI 1.7.4
   - Compose Compiler 1.5.15

2. No `NoSuchMethodError` crashes when launching the SDK

3. All existing SDK functionality must continue to work

4. New SDK version published to JitPack and accessible via:
   ```gradle
   implementation 'com.github.LuminSoft:eNROLL-Android:1.5.10'
   ```

5. Backward compatibility maintained for existing integrations

---

## Problem Analysis

### Root Cause
When Orange upgraded their app's dependencies to newer versions, Gradle's dependency resolution selected the highest version of each shared library at runtime. Since our SDK was compiled against older versions, the method signatures expected by our compiled code no longer existed in the newer runtime libraries.

### Errors Encountered
1. **NoSuchMethodError: LottieAnimation** - Caused by Lottie API changes between v6.0.1 and v6.6.2
2. **NoSuchMethodError: BasicAlertDialog** - Caused by Material3 API changes between v1.2.1 and v1.3.1

### Technical Explanation
This is standard Gradle behavior when transitive dependencies have version conflicts. The host app's higher versions override the SDK's lower versions at runtime, but the SDK's compiled bytecode still references the old method signatures.

---

## Solution Implementation

### Task 1: Upgrade SDK Dependencies
**Status:** ✅ Completed

Updated `eNROLL-sdk/build.gradle`:

| Library          | Previous Version | Updated Version |
|------------------|------------------|-----------------|
| Kotlin           | 1.9.0            | 1.9.25          |
| Lottie Compose   | 6.0.1            | 6.6.2           |
| Material3        | 1.2.1            | 1.3.1           |
| Compose UI       | 1.6.8            | 1.7.4           |
| Compose Compiler | 1.5.1            | 1.5.15          |

### Task 2: Update Root Kotlin Version
**Status:** ✅ Completed

Updated `build.gradle` (root):
```gradle
id 'org.jetbrains.kotlin.android' version '1.9.25' apply false
```

### Task 3: Update Compose Compiler Extension
**Status:** ✅ Completed

Updated `eNROLL-sdk/build.gradle`:
```gradle
composeOptions {
    kotlinCompilerExtensionVersion '1.5.15'
}
```

### Task 4: Build and Test SDK
**Status:** ✅ Completed

- Clean build: `./gradlew clean :eNROLL-sdk:assembleRelease`
- Verified no compilation errors
- Tested with Orange's dependency versions in demo app

### Task 5: Publish to JitPack
**Status:** ✅ Completed

- Version: 1.5.10
- JitPack URL: https://jitpack.io/#LuminSoft/eNROLL-Android/1.5.10
- Build Status: ✅ Green

### Task 6: Fix JitPack Configuration
**Status:** ✅ Completed

Fixed `jitpack.yml` to install artifacts to correct location:
- Removed `-DlocalRepositoryPath=build/repo`
- Now installs to default `~/.m2/repository`

### Task 7: Push Source Code to Azure
**Status:** ✅ Completed

- Branch: `feature/orange-upgrade`
- Remote: `azure`

### Task 8: Notify Orange Team
**Status:** ✅ Completed

- Formal email sent with:
  - Problem description
  - Root cause explanation
  - Resolution details
  - New SDK version
  - Integration instructions

---

## Files Modified

### eNROLL-sdk/build.gradle
- Line 46: `kotlinCompilerExtensionVersion '1.5.15'`
- Line 70: `implementation 'com.airbnb.android:lottie-compose:6.6.2'`
- Line 81: `implementation 'androidx.compose.material3:material3:1.3.1'`
- Line 96: `implementation 'androidx.compose.ui:ui:1.7.4'`
- Line 100: `implementation "androidx.compose.ui:ui-tooling-preview:1.7.4"`
- Line 121: `version = '1.5.10'`

### build.gradle (root)
- Line 6: `id 'org.jetbrains.kotlin.android' version '1.9.25' apply false`

### jitpack.yml
- Fixed artifact installation path

---

## Testing

### Test Scenario 1: Compile with Orange Versions
**Result:** ✅ Pass
- App compiles without errors with Orange's dependency versions

### Test Scenario 2: Launch SDK
**Result:** ✅ Pass
- SDK launches without NoSuchMethodError crashes

### Test Scenario 3: JitPack Integration
**Result:** ✅ Pass
- `implementation 'com.github.LuminSoft:eNROLL-Android:1.5.10'` works
- HTTP 200 response from JitPack artifact URL

---

## Lessons Learned

1. **Dependency version conflicts** can cause runtime crashes even when compile succeeds
2. **JitPack configuration** must install to `~/.m2/repository` for artifacts to be found
3. **Pre-built AAR approach** requires proper POM file or manual dependency additions by consumers
4. **Communication** with integration partners about dependency upgrades helps prevent issues

---

## Recommendation

For future dependency upgrades by integration partners:
1. Request advance notice of planned dependency changes
2. Test SDK functionality in staging environment before production
3. Maintain regular SDK updates to stay compatible with latest libraries

---

## Related Links

- JitPack: https://jitpack.io/#LuminSoft/eNROLL-Android/1.5.10
- GitHub: https://github.com/LuminSoft/eNROLL-Android
- Azure: https://dev.azure.com/ExcelSystems/eKYC/_git/ekyc-android

---

## Timeline

| Date       | Action                                      |
|------------|---------------------------------------------|
| 2024-12-09 | Issue reported by Orange                    |
| 2024-12-09 | Root cause identified                       |
| 2024-12-09 | Dependencies upgraded                       |
| 2024-12-09 | SDK v1.5.10 published to JitPack           |
| 2024-12-09 | Orange team notified                        |

---

## Assignee
Ahmed Sleem (ahmed.selim@luminsoft.net)

## Updated By
Development Team

## Status
✅ **COMPLETED**
