---
description: Sync Flutter plugin changes to React Native and Capacitor siblings
---

# Cross-Plugin Sync Workflow

This is a convenience alias. Use `/copy-flutter-to-sibling-plugins` for the full implementation workflow.

## Quick Reference

| Flutter Source | React Native Target | Capacitor Target |
|---|---|---|
| `enroll_flutter_plugin` | `enroll-react-native` | `enroll-capacitor` |
| `enroll_neo_plugin` | `enroll-neo-react-native` | `enroll-capacitor-neo` |

## Native SDK Branches

| Product Line | Branch | Android Artifact | iOS Distribution |
|---|---|---|---|
| enroll | `release/production` | `com.github.LuminSoft:eNROLL-Android` | CocoaPods (`EnrollFramework ~> 3.0.7`) |
| enroll-neo | `development-lumin-sdk` | `com.github.LuminSoft:eNROLL-Lite-Android` | XCFramework (bundled) + `EnrollNeoCore` pod |

## Steps

1. Read the Flutter plugin's `.enroll-linkage.json` to detect product line.
2. Run `/check-native-to-flutter` to confirm Flutter is up to date with native.
   - This includes verifying and updating Flutter docs (see docs gate in that workflow).
3. Run `/copy-flutter-to-sibling-plugins` to mirror Flutter → RN + Capacitor.
   - This includes updating docs in all sibling plugins (see docs gate in that workflow).

4. **Verify docs across ALL plugins** (required — combined workflow must check all docs):
   - Native SDK: verify native docs (README, API notes) reflect current features
   - Flutter: verify Flutter docs match implemented features
   - React Native: verify RN docs (README, `docs/api.md`, TypeScript types) match mirrored features
   - Capacitor: verify Capacitor docs (README, `src/definitions.ts`) match mirrored features
   - Linkage metadata: verify `docs.missingDocsForFeatures` is empty in all plugins
   - Report any non-empty `docs.missingDocsForFeatures` as **warnings** or **failures**

// turbo
5. Run sync check:
   ```
   bash /Users/luminsoft/StudioProjects/ekyc-android/scripts/check-enroll-sync.sh
   ```

## Critical Rules

- Flutter is always updated FIRST after native SDK changes.
- React Native and Capacitor must NOT be updated until Flutter is confirmed.
- For enroll-neo iOS: copy XCFramework from Flutter Neo to sibling plugins.
- For enroll iOS: use CocoaPods only, no XCFramework copying.
- Do NOT mix production/Innovatrics code with Neo/non-Innovatrics code.
- Do NOT remove or rename public APIs.
- Do NOT consider the sync complete while any plugin has missing docs for implemented features.
