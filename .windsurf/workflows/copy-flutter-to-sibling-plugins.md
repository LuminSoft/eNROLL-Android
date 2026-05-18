---
description: Copy Flutter plugin implementation to React Native and Capacitor siblings
---

# Flutter → Sibling Plugin Sync

Use this workflow after a Flutter plugin implementation is confirmed complete, to mirror changes to the matching React Native and Capacitor plugins.

## Product Line Mapping

| Flutter Source | React Native Target | Capacitor Target |
|---|---|---|
| `/Users/luminsoft/StudioProjects/enroll_flutter_plugin` | `/Users/luminsoft/StudioProjects/enroll-react-native` | `/Users/luminsoft/StudioProjects/enroll-capacitor` |
| `/Users/luminsoft/StudioProjects/enroll_neo_plugin` | `/Users/luminsoft/StudioProjects/enroll-neo-react-native` | `/Users/luminsoft/StudioProjects/enroll-capacitor-neo` |

## Steps

1. **Detect product line** from the Flutter repo's `.enroll-linkage.json` → `productLine` field.

2. **Compare Flutter vs native SDK** — only mirror features that are actually implemented in Flutter. Do NOT skip ahead to native features that Flutter hasn't implemented yet.

3. **Compare Flutter vs sibling plugins** — identify what the siblings are missing relative to Flutter.

4. **Implement equivalent APIs in React Native**:
   - Map Dart enums → TypeScript string literal unions in `src/types.ts`
   - Map Dart class fields → TypeScript interface properties in `src/types.ts`
   - Map new constructor params → `StartEnrollOptions` interface fields
   - Update re-exports in `src/index.ts` if new types were added
   - Update Android bridge: `android/src/main/kotlin/.../EnrollModule.kt` or similar
   - Update iOS bridge: `ios/*.swift` or `ios/**/*.swift`

5. **Implement equivalent APIs in Capacitor**:
   - Map same types to `src/definitions.ts` (Capacitor plugin interface)
   - Update Android bridge: `android/src/main/kotlin/.../EnrollPlugin.kt` or similar
   - Update iOS bridge: `ios/Sources/**/*.swift`

6. **Update Android bridge** in sibling plugins:
   - Ensure `android/build.gradle` references the same SDK artifact version as Flutter
   - Add any new native SDK calls matching what Flutter's Android bridge does

7. **Update iOS bridge** in sibling plugins:
   - **For enroll (production line)**:
     - iOS dependency is normal CocoaPods (`EnrollFramework ~> 3.0.7`)
     - Do NOT copy XCFramework files
     - Update podspec only if iOS pod version changed
   - **For enroll-neo (lite line)**:
     - iOS dependency is XCFramework bundled in repo
     - When Flutter Neo has a new/updated XCFramework, **copy** it:
       - FROM: `enroll_neo_plugin/ios/Frameworks/EnrollFramework.xcframework`
       - TO: `enroll-neo-react-native/ios/Frameworks/EnrollFramework.xcframework`
       - TO: `enroll-capacitor-neo/ios/Frameworks/EnrollFramework.xcframework`
     - Update podspec `EnrollNeoCore` version to match Flutter Neo's podspec
     - Verify XCFramework paths are correct in the podspec
     - Confirm copied XCFramework MD5 matches Flutter Neo's

8. **Update docs** in sibling plugins:
   - Update `CHANGELOG.md` with mirrored changes
   - Update `docs/api.md` if API types changed
   - Update `ENROLL_LINKAGE.md` if features or versions changed

9. **Update `.enroll-linkage.json`** in sibling plugins:
   - Update `features.exposedByThisProject`
   - Remove resolved items from `features.missingFromThisProject`
   - Update `nativeSdk.androidVersion` if changed
   - Update `nativeSdk.iosVersion` / `iosCorePodVersion` if changed
   - Update `pluginVersion`
   - Update `lastVerifiedAt`

// turbo
10. **Run sync check**:
    ```
    bash /Users/luminsoft/StudioProjects/ekyc-android/scripts/check-enroll-sync.sh
    ```

11. **Report**:
    - Files changed per repo
    - Features mirrored
    - iOS bridge notes (XCFramework copied or not, pod version changes)
    - Manual review needed (tests, platform-specific behavior)
    - Remaining gaps

## Critical Rules

- Do NOT mix enroll/Innovatrics files with enroll-neo/non-Innovatrics files.
- Do NOT remove or rename existing public APIs.
- Do NOT change package names, publishing configuration, or app IDs.
- Do NOT implement features in sibling plugins before Flutter has them.
- Enroll Flutter uses `appColors` (deprecated naming); Neo Flutter uses `enrollColors`. Preserve this naming difference.
