# eNROLL Android SDK — Linkage & Sync Guide

## What This Repo Is

The **native Android SDK** for the eNROLL eKYC platform. It serves **both** product lines:

| Product Line | Branch | Android Artifact | iOS Distribution | Uses Innovatrics |
|---|---|---|---|---|
| **enroll** (production) | `release/production` | `com.github.LuminSoft:eNROLL-Android` | CocoaPods (`EnrollFramework ~> 3.0.7`) | Yes |
| **enroll-neo** (lite) | `development-lumin-sdk` | `com.github.LuminSoft:eNROLL-Lite-Android` | XCFramework + `EnrollNeoCore` pod | No (Lumin-OCR) |

## Current Versions

| Product Line | Android Version | iOS Pod | Innovatrics / OCR |
|---|---|---|---|
| enroll | v1.5.24 | EnrollFramework ~> 3.0.7 | Innovatrics 9.0.2 |
| enroll-neo | v1.3.2 | EnrollNeoCore 1.0.17 | Lumin-OCR 0.0.40 |

## Sibling Plugin Repos

### Enroll (production) line
| Plugin | Path | Type |
|---|---|---|
| enroll_flutter_plugin | `/Users/luminsoft/StudioProjects/enroll_flutter_plugin` | Flutter |
| enroll-react-native | `/Users/luminsoft/StudioProjects/enroll-react-native` | React Native |
| enroll-capacitor | `/Users/luminsoft/StudioProjects/enroll-capacitor` | Capacitor |

### Enroll Neo line
| Plugin | Path | Type |
|---|---|---|
| enroll_neo_plugin | `/Users/luminsoft/StudioProjects/enroll_neo_plugin` | Flutter |
| enroll-neo-react-native | `/Users/luminsoft/StudioProjects/enroll-neo-react-native` | React Native |
| enroll-capacitor-neo | `/Users/luminsoft/StudioProjects/enroll-capacitor-neo` | Capacitor |

## Native SDK Modes (both branches)

`ONBOARDING`, `AUTH`, `UPDATE`, `FORGET_PROFILE_DATA`, `SIGN_CONTRACT`

## Native SDK API Features

theming, iconCustomization, localization, forcedDocumentType, exitStep, correlationId, skipTutorial, requestIdTracking, contractSigning, callbacks

## How to Update When Native SDK Changes

1. Bump version in `eNROLL-sdk/build.gradle` and `BuildInfo.kt`
2. Build AAR via release script: `./scripts/release.sh <version>`
3. Publish to JitPack (create GitHub tag)
4. Update `.enroll-linkage.json` in this repo
5. **Update Flutter plugin first** — `/check-native-to-flutter`
6. **Then mirror to siblings** — `/copy-flutter-to-sibling-plugins`
7. Run sync check: `bash scripts/check-enroll-sync.sh`

## Available Workflows

| Command | Purpose |
|---|---|
| `/check-native-to-flutter` | Check if Flutter plugin covers all native features |
| `/copy-flutter-to-sibling-plugins` | Mirror Flutter impl to RN + Capacitor |
| `/sync-plugin-changes` | Convenience: runs both above in sequence |
| `/update-sdk-version` | Bump SDK version across all plugins |

## Naming Conventions

- Enroll Flutter uses `appColors` (deprecated naming) for theming
- Neo Flutter uses `enrollColors` / `enrollTheme` for theming
- Do NOT rename across product lines — document the difference

## Where to Update Docs

- `README.md` — setup/installation
- `CHANGELOG.md` — version history
- `ICON_CUSTOMIZATION.md` — icon/theme API
- `.enroll-linkage.json` — machine-readable metadata
- `ENROLL_LINKAGE.md` — this file
