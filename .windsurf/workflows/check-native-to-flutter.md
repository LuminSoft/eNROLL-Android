---
description: Check whether the matching Flutter plugin has implemented all native SDK features
---

# /check-native-to-flutter

After native SDK changes, compare the matching native SDK branch against the matching Flutter plugin.
Flutter is always the first plugin implementation after native changes.
Do **not** update React Native or Capacitor from this command.

## Product Line Mapping

| Product Line | Native Branch | Flutter Plugin |
|---|---|---|
| enroll | `release/production` | `/Users/luminsoft/StudioProjects/enroll_flutter_plugin` |
| enroll-neo | `development-lumin-sdk` | `/Users/luminsoft/StudioProjects/enroll_neo_plugin` |

## Rules

- Native SDK is the source of truth for available features.
- Flutter is the source of truth for what is ready to mirror to sibling plugins.
- Do NOT update React Native or Capacitor in this workflow.
- Do NOT copy XCFramework in this workflow.
- Do NOT implement anything unless explicitly asked. Only report findings.

## Steps

1. **Detect or ask for product line**: enroll or enroll-neo.
   - Check current git branch of `/Users/luminsoft/StudioProjects/ekyc-android`.
   - `release/production` → enroll → check `enroll_flutter_plugin`.
   - `development-lumin-sdk` → enroll-neo → check `enroll_neo_plugin`.
   - If on a different branch, ask which product line to check.

2. **Inspect native SDK public APIs/features** at `eNROLL-sdk/src/main/java/com/luminsoft/enroll_sdk/`:
   - `sdk/eNROLL.kt` → `init()` parameters (the public SDK entry point)
   - `core/models/EnrollMode.kt` → available modes
   - `core/models/EnrollEnvironment.kt` → environments
   - `core/models/EnrollForcedDocumentType.kt` → forced doc types
   - `core/models/BuildInfo.kt` → SDK version
   - `core/models/EnrollCallback.kt` → callback interface
   - `ui_components/theme/AppTheme.kt` / `AppColors.kt` / `AppIcons.kt` → theming
   - `main/main_data/main_models/get_onboaring_configurations/EkycStepType.kt` → exit step types
   - `features/` directory → available feature modules

3. **Inspect matching Flutter plugin APIs/features**:
   - For enroll: `lib/enroll_plugin.dart`, `lib/constants/enroll_mode.dart`, `lib/constants/enroll_step_type.dart`, `lib/constants/enroll_icons.dart`
   - For enroll-neo: `lib/enroll_neo_plugin.dart`, `lib/constants/enroll_mode.dart`, `lib/constants/enroll_step_type.dart`, `lib/constants/enroll_icons.dart`
   - Check `android/src/main/kotlin/` for Android bridge code
   - Check `ios/Classes/` for iOS bridge code

4. **Compare**:
   - Native available features
   - Flutter exposed APIs
   - Android Flutter bridge implementation
   - iOS Flutter bridge implementation
   - Docs coverage
   - Native SDK dependency version (`android/build.gradle` vs `BuildInfo.kt`)

5. **Report**:
   - Implemented in Flutter
   - Missing in Flutter
   - Android bridge gaps
   - iOS bridge gaps
   - Docs gaps
   - Version mismatch

6. **Update metadata only if stale**: `.enroll-linkage.json` and `ENROLL_LINKAGE.md` in the Flutter plugin only.

7. **Run sync checker**: `bash /Users/luminsoft/StudioProjects/ekyc-android/scripts/check-enroll-sync.sh`

8. **Stop before RN/Capacitor work**. Use `/copy-flutter-to-sibling-plugins` separately.

## Naming Notes

- Enroll Flutter uses `appColors` (deprecated naming) for theming.
- Neo Flutter uses `enrollColors` / `enrollTheme`.
- Document naming differences, do not rename.
- Both native branches have `FORGET_PROFILE_DATA` mode. Neither Flutter plugin currently exposes it.