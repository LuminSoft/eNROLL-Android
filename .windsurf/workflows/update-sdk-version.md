---
description: Update native SDK version across all plugin repos
---

# SDK Version Update Workflow

Use this workflow when the native SDK has a new release and all dependent plugins need updating.

## Branch → Product Line Mapping

| Branch | Product Line | Android Artifact | iOS |
|---|---|---|---|
| `release/production` | enroll | `com.github.LuminSoft:eNROLL-Android` | CocoaPods |
| `development-lumin-sdk` | enroll-neo | `com.github.LuminSoft:eNROLL-Lite-Android` | XCFramework |

## Steps

1. **Confirm the new version** by reading:
   - `eNROLL-sdk/build.gradle` — `version` in `afterEvaluate.publishing`
   - `eNROLL-sdk/src/main/java/com/luminsoft/enroll_sdk/core/models/BuildInfo.kt` — `SDK_VERSION`
   - `.enroll-linkage.json` — update `productLines.<line>.androidVersion`

2. **Determine the product line** from the current branch:
   - `release/production` → enroll line
   - `development-lumin-sdk` → enroll-neo line

3. **Update Flutter plugin first** (Flutter is always first after native):
   - Update `android/build.gradle` — change the SDK dependency version
   - For enroll-neo: also check if XCFramework or `EnrollNeoCore` pod version needs updating
   - Update `.enroll-linkage.json` — set `nativeSdk.androidVersion` to new version
   - Run `/check-native-to-flutter` to detect any new features

4. **Update sibling plugins** only after Flutter is confirmed:
   - Run `/copy-flutter-to-sibling-plugins`
   - For each sibling plugin:
     - Update `android/build.gradle` SDK dependency version
     - Update `.enroll-linkage.json` with new version
     - For enroll-neo: copy XCFramework from Flutter plugin + update `EnrollNeoCore` pod version

5. **If new features were added** to the native SDK:
   - Update `features.nativeAvailable` in native SDK's `.enroll-linkage.json`
   - Add to `features.missingFromThisProject` in plugins that don't expose it yet
   - Implement in Flutter first, then mirror to siblings

// turbo
6. **Run sync check**:
   ```
   bash /Users/luminsoft/StudioProjects/ekyc-android/scripts/check-enroll-sync.sh
   ```

7. **Report** version updates applied and any remaining issues.
