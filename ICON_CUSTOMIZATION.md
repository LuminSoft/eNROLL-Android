# eNROLL Android SDK — Icon Customization Guide

> **Version**: 2.2 (Size Validation + Forget Flow Fix)
> **Last Updated**: March 2026

---

## Overview

The eNROLL SDK allows customization of most icons and images shown during the onboarding cycle. Icons are grouped **by business step** so that each key corresponds to exactly one screen purpose. See the **Deferred / Not Yet Wired** section at the bottom for keys that are defined in the public API but not yet routed through the resolver layer.

All custom icons are optional — when a key is `null` (the default), the SDK renders its built-in asset.

---

## Icon Size Validation

Every custom icon asset is validated at resolve time through `validateIconResource()`. The following checks are applied **centrally** — all icon paths (logo, tutorial, pre-scan, feature, field, UI, update/forget step) flow through the same validation.

| Check | Limit | Behavior on Failure |
|-------|-------|--------------------|
| Drawable resource exists | — | `Log.w` + fallback to default |
| Drawable can be inflated | — | `Log.w` + fallback to default |
| Width ≤ max | **2048 px** | `Log.w` + fallback to default |
| Height ≤ max | **2048 px** | `Log.w` + fallback to default |
| Estimated ARGB_8888 memory ≤ max | **20 MB** | `Log.w` + fallback to default |

- **No crash** — invalid or oversized icons are silently replaced with the built-in default.
- **No hard-fail** — SDK init is never blocked by a bad icon.
- Constants: `MAX_ICON_WIDTH_PX`, `MAX_ICON_HEIGHT_PX`, `MAX_ICON_MEMORY_BYTES` in `IconResolver.kt`.

---

## Quick Start

Pass an `AppIcons` instance inside `AppTheme` when calling `eNROLL.init()`:

```kotlin
import com.luminsoft.enroll_sdk.ui_components.theme.*

eNROLL.init(
    // ... other params ...
    appTheme = AppTheme(
        colors = AppColors(),
        icons = AppIcons(
            logo = LogoConfig(
                mode = LogoMode.CUSTOM,
                asset = IconSource.Resource(R.drawable.my_logo),
                renderingMode = IconRenderingMode.ORIGINAL
            ),
            location = LocationIcons(
                tutorial = StepIcon(source = IconSource.Resource(R.drawable.my_location_tutorial)),
                requestAccess = StepIcon(source = IconSource.Resource(R.drawable.my_location_request)),
            ),
            nationalId = NationalIdIcons(
                preScan = StepIcon(source = IconSource.Resource(R.drawable.my_ni_prescan)),
            ),
        )
    )
)
```

---

## Core Concepts

### `StepIcon`

Every customizable icon is a `StepIcon`:

```kotlin
data class StepIcon(
    val source: IconSource,
    val renderingMode: IconRenderingMode = IconRenderingMode.ORIGINAL
)
```

### `IconSource`

Currently supports Android drawable resources:

```kotlin
sealed class IconSource {
    data class Resource(@DrawableRes val resId: Int) : IconSource()
}
```

### `IconRenderingMode`

Controls how your custom icon asset is colorized when displayed.

#### `ORIGINAL` (default)

Renders your asset **exactly as designed** — all colors, gradients, and transparency are preserved.

**Use for:**
- Full-color illustrations (tutorial screens, onboarding pages)
- Company logos and brand assets
- Photos or detailed artwork
- Any asset where color accuracy matters

**Asset format:** PNG, WebP, or multi-color vector XML.

```kotlin
// Full-color tutorial illustration — colors stay exactly as designed
location = LocationIcons(
    tutorial = StepIcon(
        source = IconSource.Resource(R.drawable.my_location_illustration),
        renderingMode = IconRenderingMode.ORIGINAL
    )
)
```

#### `TEMPLATE`

Replaces **all colors** in the asset with the SDK theme color (primary, secondary, etc. depending on where the icon is used). The entire shape becomes a single flat color.

**Use for:**
- Simple monochrome icons (like Material Icons)
- Icons that should match the app's theme color automatically
- Small UI elements (buttons, input field icons, navigation icons)

**Asset format:** Single-color vector XML or monochrome PNG with transparent background.

> **Important**: Do NOT use `TEMPLATE` with full-color illustrations or photos — the result will be a solid-colored silhouette.

```kotlin
// Simple vector icon — will be tinted to match theme primary color
common = CommonIcons(
    ui = UiIcons(
        edit = StepIcon(
            source = IconSource.Resource(R.drawable.ic_edit),
            renderingMode = IconRenderingMode.TEMPLATE
        )
    )
)
```

#### Which mode should I use?

| Asset Type | Recommended Mode | Result |
|------------|-----------------|--------|
| Colorful illustration (PNG/WebP) | `ORIGINAL` | Exact colors preserved |
| Company logo | `ORIGINAL` | Brand colors preserved |
| Simple single-color vector icon | `TEMPLATE` | Tinted to theme color |
| Photo or detailed artwork | `ORIGINAL` | Full detail preserved |
| Monochrome PNG icon | `TEMPLATE` | Tinted to theme color |
| Multi-color vector illustration | `ORIGINAL` | All colors preserved |

### `LogoConfig`

Controls the SDK logo on splash screens and the app bar:

```kotlin
data class LogoConfig(
    val mode: LogoMode = LogoMode.DEFAULT,   // DEFAULT | HIDDEN | CUSTOM
    val asset: IconSource? = null,
    val renderingMode: IconRenderingMode = IconRenderingMode.ORIGINAL
)
```

---

## Full `AppIcons` API Reference

```kotlin
data class AppIcons(
    val logo: LogoConfig,
    val location: LocationIcons,
    val nationalId: NationalIdIcons,
    val passport: PassportIcons,
    val phone: PhoneIcons,
    val email: EmailIcons,
    val faceMatching: FaceMatchingIcons,
    val securityQuestions: SecurityQuestionsIcons,
    val password: PasswordIcons,
    val signature: SignatureIcons,
    val common: CommonIcons,
    val update: UpdateIcons,
    val forget: ForgetIcons,
)
```

---

## Step-by-Step Icon Groups

### `location: LocationIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager illustration | `step_00_location_*` (3-layer) |
| `requestAccess` | "Allow location access" screen | `step_00_location_*` (3-layer) |
| `accessError` | "Location access denied" screen | `step_00_location_*` (3-layer) |
| `grab` | Location captured / map display | `location_saved_*` (3-layer) |

### `nationalId: NationalIdIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager (national ID only flow) | `step_01_national_id_*` (3-layer) |
| `tutorialIdOrPassport` | Onboarding pager (combined ID-or-passport flow) | `step_01_national_id_*` (3-layer) |
| `preScan` | Pre-scan instruction screen | `step_01_national_id_*` (3-layer) |
| `scanError` | Scan error / retry screen | `invalid_ni_icon_*` (3-layer) |
| `choose` | Document type chooser (select national ID) | varies |

### `passport: PassportIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager illustration | `step_01_passport_*` (3-layer) |
| `preScan` | Pre-scan instruction (regular passport) | `step_01_passport_*` (3-layer) |
| `ePassportPreScan` | Pre-scan instruction (e-Passport / NFC) | `step_01_passport_*` (3-layer) |
| `choose` | Document type chooser (select passport) | varies |

### `phone: PhoneIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager illustration | `step_03_phone_otp_*` (3-layer) |
| `select` | Phone number selection screen | `select_phone_number*` (3-layer) |
| `validateOtp` | SMS OTP validation screen | `validate_sms_otp_*` (3-layer) |

### `email: EmailIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager illustration | `step_04_mail_otp_*` (3-layer) |
| `select` | Email address selection screen | `select_mail_*` (3-layer) |
| `validateOtp` | Email OTP validation screen | `validate_mail_otp_*` (3-layer) |

### `faceMatching: FaceMatchingIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager illustration | `step_02_smile_liveness_*` (3-layer) |
| `preScan` | Pre-scan instruction before face capture | `step_02_smile_liveness_*` (3-layer) |
| `error` | Capture error / retry screen | `face_recognition_capture_error_*` (3-layer) |

### `securityQuestions: SecurityQuestionsIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager illustration | `step_06_security_questions_*` (3-layer) |
| `authScreen` | Auth / auth-update working screen | `step_06_security_questions_*` (3-layer) |

### `password: PasswordIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager illustration | `step_07_password_*` (3-layer) |
| `authScreen` | Auth / auth-update working screen | `step_07_password_*` (3-layer) |

### `signature: SignatureIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `tutorial` | Onboarding pager illustration | `step_08_signature_*` (3-layer) |

---

## Common / Shared Icons

### `common: CommonIcons`

```kotlin
data class CommonIcons(
    val backgrounds: BackgroundIcons,
    val popups: PopupIcons,
    val fieldIcons: FieldIcons,
    val ui: UiIcons,
    val termsAndConditions: StepIcon?,
)
```

### `common.backgrounds: BackgroundIcons`

| Key | Usage | Default Drawable | Default Tint |
|-----|-------|-----------------|--------------|
| `main` | Main screen background (single-image override) | `main_lightblue_bg01/02/03` (3-layer) | *(see layers)* |
| `layer1` | Background layer 1 (used when `main` is null) | `main_lightblue_bg01` | Primary color |
| `layer2` | Background layer 2 (used when `main` is null) | `main_lightblue_bg02` | Secondary color |
| `layer3` | Background layer 3 (used when `main` is null) | `main_lightblue_bg03` | Primary color @ 40% alpha |
| `blur` | Blurred overlay background | `blured_bg` | None |
| `header` | App bar header background | `header_shapes` | Primary color |
| `footer` | "Sponsored by" footer logo | `horizontal_footer` | None |

**Note**: Each layer respects `StepIcon.renderingMode`:
- `TEMPLATE` (default): applies the default tint shown above
- `ORIGINAL`: renders the asset with original colors, no tint

### `common.popups: PopupIcons`

| Key | Screen | Default Drawable |
|-----|--------|-----------------|
| `background` | Dialog header background | `pop_up_header` |
| `successIcon` | Success dialog icon | `success_popup_icon` |
| `warningIcon` | Warning dialog icon | `alert_popup_icon` |
| `errorIcon` | Error dialog icon | `error_popup_icon` |
| `errorSign` | Error sign (reserved) | — |
| `successSign` | Success sign (reserved) | — |
| `warningSign` | Warning sign (reserved) | — |

### `common.fieldIcons: FieldIcons`

Used in OCR confirmation and NFC result screens:

| Key | Default Drawable | Usage |
|-----|-----------------|-------|
| `user` | `user_icon` | Name fields |
| `calendar` | `calendar_icon` | Date fields |
| `gender` | `gender_icon` | Gender field |
| `nationality` | `nationality_icon` | Nationality field |
| `num` | `factory_num_icon` | Factory/document number |
| `address` | `address_icon` | Address fields |
| `idCard` | `id_card_icon` | National ID number field |
| `passport` | `passport_icon` | Passport document number |
| `issuingAuthority` | `issuing_authurity_icon` | Issuing authority field |
| `profession` | `profession_icon` | Profession field (OCR back) |
| `religion` | `religion_icon` | Religion field (OCR back) |
| `maritalStatus` | `marital_status_icon` | Marital status field (OCR back) |

### `common.ui: UiIcons`

General UI icons wired through `resolveUiIcon()` across all relevant screens:

| Key | Default Drawable | Wired Screens |
|-----|-----------------|---------------|
| `visibility` | `visibility_icon` | All password input fields (show) |
| `visibilityOff` | `visibility_off_icon` | All password input fields (hide) |
| `mobile` | `mobile_icon` | Phone number selection lists |
| `mail` | `mail_icon` | Email input fields, email selection lists, electronic signature |
| `answer` | `answer_icon` | Security question answer fields |
| `error` | `error_icon` | Delete-item indicators in selection lists |
| `info` | `info_icon` | Security question dropdown, document pre-scan description |
| `edit` | `edit_icon` | OCR name-edit trailing icon |
| `activePhone` | `active_phone` | Default phone/email indicator in lists |

---

## Update & Forget Mode Icons

### `update: UpdateIcons`

Shown in the Update step-list screen:

| Key | Default Drawable |
|-----|-----------------|
| `modeIcon` | `update_icon` |
| `idCard` | `update_id_card_icon` |
| `passport` | `update_passport` |
| `mobile` | `update_mobile_icon` |
| `email` | `update_mail_icon` |
| `device` | `update_device_icon` |
| `address` | `update_address_icon` |
| `securityQuestions` | `update_answer_icon` |
| `password` | `update_password_icon` |

### `forget: ForgetIcons`

Shown in the Forget Profile Data step-list screen:

| Key | Default Drawable |
|-----|------------------|
| `modeIcon` | `forget_icon` |
| `nationalId` | `update_id_card_icon` |
| `passport` | `update_passport` |
| `phone` | `forget_phone` |
| `email` | `forget_mail` |
| `device` | `forget_device` |
| `location` | `forget_location` |
| `securityQuestions` | `update_answer_icon` |
| `password` | `forget_password` |

---

## Full Example

```kotlin
eNROLL.init(
    // ... credentials and mode ...
    appTheme = AppTheme(
        colors = AppColors(/* ... */),
        icons = AppIcons(
            // Logo
            logo = LogoConfig(
                mode = LogoMode.CUSTOM,
                asset = IconSource.Resource(R.drawable.my_company_logo),
                renderingMode = IconRenderingMode.ORIGINAL
            ),

            // Location step
            location = LocationIcons(
                tutorial = StepIcon(source = IconSource.Resource(R.drawable.my_loc_tutorial)),
                requestAccess = StepIcon(source = IconSource.Resource(R.drawable.my_loc_request)),
                accessError = StepIcon(source = IconSource.Resource(R.drawable.my_loc_error)),
                grab = StepIcon(source = IconSource.Resource(R.drawable.my_loc_grab)),
            ),

            // National ID step
            nationalId = NationalIdIcons(
                tutorial = StepIcon(source = IconSource.Resource(R.drawable.my_ni_tutorial)),
                preScan = StepIcon(source = IconSource.Resource(R.drawable.my_ni_prescan)),
                scanError = StepIcon(source = IconSource.Resource(R.drawable.my_ni_error)),
            ),

            // Passport step
            passport = PassportIcons(
                preScan = StepIcon(source = IconSource.Resource(R.drawable.my_passport_prescan)),
                ePassportPreScan = StepIcon(source = IconSource.Resource(R.drawable.my_epassport_prescan)),
            ),

            // Face matching step
            faceMatching = FaceMatchingIcons(
                preScan = StepIcon(source = IconSource.Resource(R.drawable.my_face_prescan)),
                error = StepIcon(source = IconSource.Resource(R.drawable.my_face_error)),
            ),

            // Phone OTP step
            phone = PhoneIcons(
                select = StepIcon(source = IconSource.Resource(R.drawable.my_phone_select)),
                validateOtp = StepIcon(source = IconSource.Resource(R.drawable.my_phone_otp)),
            ),

            // Email OTP step
            email = EmailIcons(
                select = StepIcon(source = IconSource.Resource(R.drawable.my_email_select)),
                validateOtp = StepIcon(source = IconSource.Resource(R.drawable.my_email_otp)),
            ),

            // Common shared assets
            common = CommonIcons(
                backgrounds = BackgroundIcons(
                    footer = StepIcon(source = IconSource.Resource(R.drawable.my_footer)),
                ),
                popups = PopupIcons(
                    successIcon = StepIcon(source = IconSource.Resource(R.drawable.my_success)),
                    errorIcon = StepIcon(source = IconSource.Resource(R.drawable.my_error)),
                ),
            ),
        )
    )
)
```

---

## Design Principles

1. **One key = one screen purpose.** No icon key is reused across unrelated screens.
2. **Business-flow grouping.** Icons are grouped by the step they belong to (`location`, `nationalId`, `faceMatching`, etc.), not by rendering category.
3. **Null = default.** Every key defaults to `null`. The SDK falls back to its built-in drawable.
4. **ORIGINAL by default.** Custom icons are rendered as-is. Use `IconRenderingMode.TEMPLATE` only if you want the SDK to tint your icon with the theme's primary color.
5. **Backward compatible.** Adding new keys never breaks existing integrations.

---

## Architecture (Internal)

| File | Purpose |
|------|---------|
| `AppIcons.kt` | Public data model — all icon group classes |
| `IconResolver.kt` | Internal composables: `ResolvedImage`, `ResolvedStepIcon`, `resolvedPainter`, `resolveFieldIcon`, `resolveUpdateStepIcon`, `resolveForgetStepIcon` |
| `Theme.kt` | `MaterialTheme.appIcons` extension property |
| `AppTheme.kt` | `AppTheme` wrapper combining `AppColors` + `AppIcons` |

### How Resolution Works

1. **Multi-layer icons** (tutorial/feature screens): `ResolvedStepIcon` checks if a `StepIcon` is provided → renders custom icon, else renders the default 3-layer `ImagesBox`.
2. **Single-layer icons** (backgrounds, popups, footer): `ResolvedImage` checks if a `StepIcon` is provided → renders custom drawable, else renders the default drawable.
3. **Field icons**: `resolveFieldIcon()` maps a drawable resource ID to the corresponding `common.fieldIcons.*` key. `resolvedPainter()` returns the custom painter or falls back to the default.
4. **Update/Forget list icons**: `resolveUpdateStepIcon()` / `resolveForgetStepIcon()` map drawable IDs to `update.*` / `forget.*` keys.

### Key Flow

```
Client passes AppIcons → stored in EnrollSDK
  → EKYCsDKTheme wraps it in LocalAppIcons CompositionLocal
    → Feature screens read via MaterialTheme.appIcons.*
      → Resolver composables pick custom icon or fallback
```

---

## iOS Parity Reference

| Android Key | iOS AppAssets Equivalent |
|-------------|------------------------|
| `location.tutorial` | `locationStepImage` |
| `location.requestAccess` | `locationStepImage` |
| `location.grab` | `locationSavedImage` |
| `nationalId.tutorial` | `personalIdStepImage` |
| `nationalId.preScan` | `nationalIdPrescanImage` |
| `nationalId.scanError` | `invalidNationalIdImage` |
| `passport.preScan` | `passportPrescanImage` |
| `faceMatching.tutorial` | `faceRecognitionStepImage` |
| `faceMatching.preScan` | `faceRecognitionStepImage` |
| `faceMatching.error` | `faceRecognitionCaptureError` |
| `phone.select` | `selectPhoneImage` |
| `phone.validateOtp` | `validateSmsOtpImage` |
| `email.select` | `selectEmailImage` |
| `email.validateOtp` | `validateEmailOtpImage` |
| `common.backgrounds.main` | `background` |
| `common.backgrounds.layer1` | `background` (layer 1 / primary-tinted fallback) |
| `common.backgrounds.layer2` | `background` (layer 2 / secondary-tinted fallback) |
| `common.backgrounds.layer3` | `background` (layer 3 / primary 40% alpha fallback) |
| `common.backgrounds.blur` | `blurBackground` |
| `common.backgrounds.header` | `header` |
| `common.popups.background` | `popupBackground` |
| `common.popups.successIcon` | `pupSuccessIcon` |
| `common.popups.warningIcon` | `popupWarningIcon` |
| `common.popups.errorIcon` | `popupErrorIcon` |
| `common.fieldIcons.user` | `userIcon` |
| `common.fieldIcons.calendar` | `calendarIcon` |
| `common.fieldIcons.gender` | `genderIcon` |
| `update.modeIcon` | `updateScreenIcon` |
| `forget.modeIcon` | `forgetModeImage` |

---

## Deferred / Not Yet Wired

The following keys are **defined in the public API** but are **not yet routed** through the resolver layer. Overriding them will have no visible effect until a future release wires them.

| Group | Key | Reason |
|-------|-----|--------|
| `common.popups` | `errorSign` | Reserved for future sign-style popup variant |
| `common.popups` | `successSign` | Reserved for future sign-style popup variant |
| `common.popups` | `warningSign` | Reserved for future sign-style popup variant |

All other public keys listed above are fully wired and functional.

---

## Changelog

### v2.2 — Size Validation + Forget Flow Fix (March 2026)
- **New**: Icon size validation in `validateIconResource()` — rejects oversized assets (>2048px or >20MB) with warning + fallback
- **Wired**: `common.backgrounds.layer1/2/3` now routed through `BackGroundView` with per-layer `renderingMode` support (ORIGINAL preserves colors, TEMPLATE applies default tints)
- **Fix**: `ForgetIcons` now includes all 8 forget step types (`nationalId`, `passport`, `securityQuestions` were missing)
- **Fix**: `resolveForgetStepIcon()` no longer maps NationalID step drawable to `modeIcon`
- **Cleanup**: Removed stale KDoc references to render-time try/catch

### v2.1 — Full UI Icon Wiring (March 2026)
- **Wired**: All `common.ui` keys (`visibility`, `visibilityOff`, `mobile`, `mail`, `answer`, `error`, `info`, `edit`, `activePhone`) now routed through `resolveUiIcon()` across all screen files
- **Wired**: `profession`, `religion`, `maritalStatus` field icons now routed through `resolveFieldIcon()` in OCR back screens
- **Fix**: `nationalId.choose` and `passport.choose` now correctly used for document chooser (was reusing `preScan`)
- **New**: `resolveUiIcon()` helper function in `IconResolver.kt`

### v2.0 — Business-Flow Grouped API (March 2026)
- **Breaking**: Replaced `OnboardingStepIcons` + `FeatureScreenIcons` split with per-step groups (`LocationIcons`, `NationalIdIcons`, `PassportIcons`, etc.)
- **Breaking**: Renamed `ForgetModeIcons` → `ForgetIcons`, `UpdateModeIcons` → `UpdateIcons`
- **Breaking**: Moved `backgrounds`, `popups`, `fields`, `ui` under `CommonIcons`
- **New keys**: `location.requestAccess`, `location.accessError`, `passport.ePassportPreScan`, `faceMatching.preScan`, `securityQuestions.authScreen`, `password.authScreen`
- **Fix**: Eliminated all `tutorial` key reuse — each key now maps to exactly one screen
- **Fix**: Completed `resolveFieldIcon` coverage (added `address`, `idCard`, `passport`, `issuingAuthority`)

### v1.0 — Initial Icon Customization
- Added `AppIcons` model with `OnboardingStepIcons` and `FeatureScreenIcons`
- Added `ResolvedImage`, `ResolvedStepIcon` composables
- Integrated custom icons into all feature screens, splash screens, dialogs, field icons, update/forget list screens
