# eNROLL Android SDK - Theme Customization Documentation

## UPDATED DOCUMENTATION FOR GITBOOK

---

## 🎨 10. THEME CUSTOMIZATION

The eNROLL SDK supports comprehensive UI customization through the unified `AppTheme` object, which combines color and icon customization.

### AppTheme Structure

```kotlin
data class AppTheme(
    val colors: AppColors = AppColors(),
    val icons: AppIcons = AppIcons()
)
```

---

## 10.1 COLOR CUSTOMIZATION (AppColors)

Customize the SDK's color scheme to match your brand identity.

### Available Colors

```kotlin
data class AppColors(
    val white: Color = Color(0xffffffff),
    val appBlack: Color = Color(0xff333333),
    val backGround: Color = Color(0xFFFFFFFF),
    val primary: Color = Color(0xFF1D56B8),
    val secondary: Color = Color(0xff5791DB),
    val successColor: Color = Color(0xff61CC3D),
    val warningColor: Color = Color(0xFFF9D548),
    val errorColor: Color = Color(0xFFDB305B),
    val textColor: Color = Color(0xff004194),
)
```

### Usage Example

```kotlin
val customColors = AppColors(
    primary = Color(0xFF1D56B8),
    secondary = Color(0xFF5791DB),
    backGround = Color(0xFFFFFFFF),
    textColor = Color(0xFF004194),
    errorColor = Color(0xFFDB305B),
    successColor = Color(0xFF61CC3D),
    warningColor = Color(0xFFF9D548)
)

eNROLL.init(
    // ... other parameters ...
    appTheme = AppTheme(colors = customColors)
)
```

---

## 10.2 ICON CUSTOMIZATION (AppIcons)

Customize tutorial icons, logos, and UI elements throughout the SDK. All icons are optional and fall back to beautiful default animations if not provided.

### Icon Structure Overview

```kotlin
data class AppIcons(
    val logo: LogoConfig = LogoConfig(),
    val location: LocationIcons = LocationIcons(),
    val nationalId: NationalIdIcons = NationalIdIcons(),
    val passport: PassportIcons = PassportIcons(),
    val phone: PhoneIcons = PhoneIcons(),
    val email: EmailIcons = EmailIcons(),
    val faceMatching: FaceMatchingIcons = FaceMatchingIcons(),
    val securityQuestions: SecurityQuestionsIcons = SecurityQuestionsIcons(),
    val password: PasswordIcons = PasswordIcons(),
    val signature: SignatureIcons = SignatureIcons(),
    val common: CommonIcons = CommonIcons(),
    val update: UpdateIcons = UpdateIcons(),
    val forget: ForgetIcons = ForgetIcons()
)
```

### 10.2.1 Logo Customization

Control the SDK logo displayed throughout the flow:

```kotlin
// Logo Configuration
data class LogoConfig(
    val mode: LogoMode = LogoMode.DEFAULT,
    val asset: IconSource? = null,
    val renderingMode: IconRenderingMode = IconRenderingMode.ORIGINAL
)

enum class LogoMode {
    DEFAULT,    // Show default eNROLL logo
    HIDDEN,     // Hide logo completely
    CUSTOM      // Use custom logo (requires asset)
}
```

**Example: Custom Logo**

```kotlin
val customLogo = LogoConfig(
    mode = LogoMode.CUSTOM,
    asset = IconSource.Resource(R.drawable.my_company_logo),
    renderingMode = IconRenderingMode.ORIGINAL
)

eNROLL.init(
    // ... other parameters ...
    appTheme = AppTheme(
        icons = AppIcons(logo = customLogo)
    )
)
```

**Example: Hidden Logo**

```kotlin
eNROLL.init(
    // ... other parameters ...
    appTheme = AppTheme(
        icons = AppIcons(
            logo = LogoConfig(mode = LogoMode.HIDDEN)
        )
    )
)
```

### 10.2.2 Icon Rendering Modes

Control how icons are rendered with color tinting:

```kotlin
enum class IconRenderingMode {
    ORIGINAL,   // Preserve original colors (recommended for logos/photos)
    TEMPLATE    // Apply primary color tint (recommended for icons)
}
```

### 10.2.3 Step Icon Definition

Each tutorial or UI icon follows this structure:

```kotlin
data class StepIcon(
    val source: IconSource,
    val renderingMode: IconRenderingMode = IconRenderingMode.ORIGINAL
)

sealed class IconSource {
    data class Resource(@DrawableRes val resId: Int) : IconSource()
}
```

---

## 10.3 BUSINESS FLOW ICON GROUPS

### Location Icons

```kotlin
data class LocationIcons(
    val tutorial: StepIcon? = null,        // Location permission tutorial
    val requestAccess: StepIcon? = null,   // Request location access screen
    val accessError: StepIcon? = null,     // Location access denied
    val grab: StepIcon? = null             // Grabbing location icon
)
```

**Example:**

```kotlin
val locationIcons = LocationIcons(
    tutorial = StepIcon(
        source = IconSource.Resource(R.drawable.location_tutorial),
        renderingMode = IconRenderingMode.ORIGINAL
    )
)
```

### National ID Icons

```kotlin
data class NationalIdIcons(
    val tutorial: StepIcon? = null,           // National ID scan tutorial
    val tutorialIdOrPassport: StepIcon? = null, // ID or Passport choice tutorial
    val preScan: StepIcon? = null,            // Pre-scan instructions
    val scanError: StepIcon? = null,          // Scan error screen
    val choose: StepIcon? = null              // Choose document type
)
```

### Passport Icons

```kotlin
data class PassportIcons(
    val tutorial: StepIcon? = null,        // Passport scan tutorial
    val preScan: StepIcon? = null,         // Pre-scan instructions
    val ePassportPreScan: StepIcon? = null, // E-Passport NFC instructions
    val choose: StepIcon? = null           // Choose passport type
)
```

### Phone Icons

```kotlin
data class PhoneIcons(
    val tutorial: StepIcon? = null,    // Phone number tutorial
    val select: StepIcon? = null,      // Select phone number
    val validateOtp: StepIcon? = null  // OTP validation screen
)
```

### Email Icons

```kotlin
data class EmailIcons(
    val tutorial: StepIcon? = null,    // Email tutorial
    val select: StepIcon? = null,      // Select email
    val validateOtp: StepIcon? = null  // OTP validation screen
)
```

### Face Matching Icons

```kotlin
data class FaceMatchingIcons(
    val tutorial: StepIcon? = null,  // Face capture tutorial
    val preScan: StepIcon? = null,   // Pre-capture instructions
    val error: StepIcon? = null      // Face capture error
)
```

### Security Questions Icons

```kotlin
data class SecurityQuestionsIcons(
    val tutorial: StepIcon? = null,   // Security questions tutorial
    val authScreen: StepIcon? = null  // Auth mode screen
)
```

### Password Icons

```kotlin
data class PasswordIcons(
    val tutorial: StepIcon? = null,   // Password tutorial
    val authScreen: StepIcon? = null  // Auth mode screen
)
```

### Signature Icons

```kotlin
data class SignatureIcons(
    val tutorial: StepIcon? = null  // Signature tutorial
)
```

---

## 10.4 COMMON ICONS

Icons used across multiple screens:

### Background Icons

```kotlin
data class BackgroundIcons(
    val main: StepIcon? = null,    // Main background
    val layer1: StepIcon? = null,  // Background layer 1
    val layer2: StepIcon? = null,  // Background layer 2
    val layer3: StepIcon? = null,  // Background layer 3
    val blur: StepIcon? = null,    // Blur overlay
    val header: StepIcon? = null,  // Header background
    val footer: StepIcon? = null   // Footer background
)
```

### Popup Icons

```kotlin
data class PopupIcons(
    val background: StepIcon? = null,   // Popup background
    val warningIcon: StepIcon? = null,  // Warning icon
    val errorIcon: StepIcon? = null,    // Error icon
    val successIcon: StepIcon? = null   // Success icon
)
```

### Field Icons

Icons for input fields:

```kotlin
data class FieldIcons(
    val user: StepIcon? = null,
    val calendar: StepIcon? = null,
    val gender: StepIcon? = null,
    val issuingAuthority: StepIcon? = null,
    val nationality: StepIcon? = null,
    val num: StepIcon? = null,
    val passport: StepIcon? = null,
    val address: StepIcon? = null,
    val idCard: StepIcon? = null,
    val profession: StepIcon? = null,
    val religion: StepIcon? = null,
    val maritalStatus: StepIcon? = null
)
```

### UI Icons

General UI elements:

```kotlin
data class UiIcons(
    val visibility: StepIcon? = null,      // Show password
    val visibilityOff: StepIcon? = null,   // Hide password
    val mobile: StepIcon? = null,          // Mobile icon
    val mail: StepIcon? = null,            // Email icon
    val answer: StepIcon? = null,          // Answer icon
    val error: StepIcon? = null,           // Error icon
    val info: StepIcon? = null,            // Info icon
    val edit: StepIcon? = null,            // Edit icon
    val activePhone: StepIcon? = null      // Active phone indicator
)
```

### Terms and Conditions Icon

```kotlin
data class CommonIcons(
    val backgrounds: BackgroundIcons = BackgroundIcons(),
    val popups: PopupIcons = PopupIcons(),
    val fieldIcons: FieldIcons = FieldIcons(),
    val ui: UiIcons = UiIcons(),
    val termsAndConditions: StepIcon? = null  // Terms & conditions screen
)
```

---

## 10.5 MODE-SPECIFIC ICONS

### Update Mode Icons

```kotlin
data class UpdateIcons(
    val modeIcon: StepIcon? = null,          // Update mode main icon
    val idCard: StepIcon? = null,            // Update ID card
    val passport: StepIcon? = null,          // Update passport
    val mobile: StepIcon? = null,            // Update mobile
    val email: StepIcon? = null,             // Update email
    val device: StepIcon? = null,            // Update device
    val address: StepIcon? = null,           // Update address
    val securityQuestions: StepIcon? = null, // Update security questions
    val password: StepIcon? = null           // Update password
)
```

### Forget Mode Icons

```kotlin
data class ForgetIcons(
    val modeIcon: StepIcon? = null,          // Forget mode main icon
    val nationalId: StepIcon? = null,        // Forget via national ID
    val passport: StepIcon? = null,          // Forget via passport
    val phone: StepIcon? = null,             // Forget via phone
    val email: StepIcon? = null,             // Forget via email
    val device: StepIcon? = null,            // Forget via device
    val location: StepIcon? = null,          // Forget via location
    val securityQuestions: StepIcon? = null, // Forget via security questions
    val password: StepIcon? = null           // Forget via password
)
```

---

## 10.6 COMPLETE USAGE EXAMPLE

### Basic Theme Customization

```kotlin
val customTheme = AppTheme(
    colors = AppColors(
        primary = Color(0xFF1D56B8),
        secondary = Color(0xFF5791DB)
    ),
    icons = AppIcons(
        logo = LogoConfig(
            mode = LogoMode.CUSTOM,
            asset = IconSource.Resource(R.drawable.company_logo),
            renderingMode = IconRenderingMode.ORIGINAL
        ),
        location = LocationIcons(
            tutorial = StepIcon(
                source = IconSource.Resource(R.drawable.location_tutorial),
                renderingMode = IconRenderingMode.ORIGINAL
            )
        ),
        common = CommonIcons(
            popups = PopupIcons(
                successIcon = StepIcon(
                    source = IconSource.Resource(R.drawable.success_icon),
                    renderingMode = IconRenderingMode.TEMPLATE
                )
            )
        )
    )
)

eNROLL.init(
    tenantId = "your-tenant-id",
    tenantSecret = "your-tenant-secret",
    enrollMode = EnrollMode.ONBOARDING,
    environment = EnrollEnvironment.STAGING,
    enrollCallback = object : EnrollCallback {
        override fun success(enrollSuccessModel: EnrollSuccessModel) {
            Log.d(TAG, "Success: ${enrollSuccessModel.enrollMessage}")
        }
        override fun error(enrollFailedModel: EnrollFailedModel) {
            Log.e(TAG, "Error: ${enrollFailedModel.failureMessage}")
        }
        override fun getRequestId(requestId: String) {
            Log.d(TAG, "Request ID: $requestId")
        }
    },
    appTheme = customTheme  // ← Apply your custom theme
)

eNROLL.launch(this)
```

### Advanced: Multiple Icon Customizations

```kotlin
val advancedTheme = AppTheme(
    colors = AppColors(
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC6),
        errorColor = Color(0xFFB00020),
        successColor = Color(0xFF4CAF50)
    ),
    icons = AppIcons(
        logo = LogoConfig(mode = LogoMode.HIDDEN),
        location = LocationIcons(
            tutorial = StepIcon(
                source = IconSource.Resource(R.drawable.custom_location),
                renderingMode = IconRenderingMode.ORIGINAL
            ),
            requestAccess = StepIcon(
                source = IconSource.Resource(R.drawable.location_request),
                renderingMode = IconRenderingMode.ORIGINAL
            )
        ),
        nationalId = NationalIdIcons(
            tutorial = StepIcon(
                source = IconSource.Resource(R.drawable.id_tutorial),
                renderingMode = IconRenderingMode.ORIGINAL
            ),
            preScan = StepIcon(
                source = IconSource.Resource(R.drawable.id_prescan),
                renderingMode = IconRenderingMode.ORIGINAL
            )
        ),
        faceMatching = FaceMatchingIcons(
            tutorial = StepIcon(
                source = IconSource.Resource(R.drawable.face_tutorial),
                renderingMode = IconRenderingMode.ORIGINAL
            )
        ),
        common = CommonIcons(
            termsAndConditions = StepIcon(
                source = IconSource.Resource(R.drawable.terms_icon),
                renderingMode = IconRenderingMode.TEMPLATE
            ),
            popups = PopupIcons(
                successIcon = StepIcon(
                    source = IconSource.Resource(R.drawable.success),
                    renderingMode = IconRenderingMode.TEMPLATE
                ),
                errorIcon = StepIcon(
                    source = IconSource.Resource(R.drawable.error),
                    renderingMode = IconRenderingMode.TEMPLATE
                ),
                warningIcon = StepIcon(
                    source = IconSource.Resource(R.drawable.warning),
                    renderingMode = IconRenderingMode.TEMPLATE
                )
            )
        )
    )
)

eNROLL.init(
    // ... other parameters ...
    appTheme = advancedTheme
)
```

---

## 10.7 ICON ASSET REQUIREMENTS

### Asset Format
- **Supported formats**: PNG, JPEG, WebP, Vector XML drawables
- **Recommended**: Vector drawables (XML) for scalability

### Asset Size Limits
- **Max dimensions**: 2048 × 2048 pixels
- **Max memory**: 20 MB per icon
- **Recommendation**: Keep icons under 500 KB for optimal performance

### Asset Location
Place your drawable assets in:
```
app/src/main/res/drawable/
app/src/main/res/drawable-xxxhdpi/  (for PNGs)
```

### Best Practices

1. **Use ORIGINAL mode for photos/complex graphics**
   ```kotlin
   StepIcon(
       source = IconSource.Resource(R.drawable.company_logo),
       renderingMode = IconRenderingMode.ORIGINAL
   )
   ```

2. **Use TEMPLATE mode for simple icons**
   ```kotlin
   StepIcon(
       source = IconSource.Resource(R.drawable.icon),
       renderingMode = IconRenderingMode.TEMPLATE  // Applies primary color
   )
   ```

3. **Fallback Behavior**: If a custom icon fails validation (missing, too large, corrupt), the SDK automatically falls back to the default animated icon with a warning in logcat.

---

## 10.8 MIGRATION GUIDE

### From Old API (appColors only)

**Before:**
```kotlin
eNROLL.init(
    // ... parameters ...
    appColors = AppColors(primary = Color(0xFF1D56B8))
)
```

**After (Recommended):**
```kotlin
eNROLL.init(
    // ... parameters ...
    appTheme = AppTheme(
        colors = AppColors(primary = Color(0xFF1D56B8)),
        icons = AppIcons(/* optional icon customizations */)
    )
)
```

**Note**: The old `appColors` parameter is still supported but deprecated. Use `appTheme` for new integrations.

---

## 10.9 UPDATED VALUES DESCRIPTION TABLE

| Keys              | Values                                                                                                                                                                                                   |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `appTheme`        | **Optional**. Unified theme object combining colors and icons. Replaces deprecated `appColors` parameter.                                                                                                |
| `appColors`       | **Deprecated**. Use `appTheme.colors` instead. Collection of SDK colors (primary, secondary, background, success, warning, error, text). Still supported for backward compatibility.                     |
| `appIcons`        | **Deprecated**. Use `appTheme.icons` instead. Collection of custom icons for tutorials, logos, and UI elements. All icons are optional with automatic fallback to defaults. Still supported for backward compatibility. |

---

## 10.10 TROUBLESHOOTING

### Custom Icons Not Showing

1. **Check logcat for warnings**: `IconResolver: Drawable resource not found`
2. **Verify drawable exists**: Ensure the resource ID is correct
3. **Check asset size**: Icons exceeding 2048×2048px or 20MB are rejected
4. **Validate drawable format**: Corrupt or invalid drawables fall back to defaults

### Colors Not Applied

1. **Verify Color format**: Use `Color(0xFFRRGGBB)` format
2. **Check theme usage**: Pass `appTheme` parameter to `eNROLL.init()`
3. **Confirm initialization**: Theme must be set before calling `eNROLL.launch()`

---

## 📚 RELATED DOCUMENTATION

- [Icon Customization Deep Dive](ICON_CUSTOMIZATION.md)
- [Flutter Plugin Theme Setup](../flutter/theme_customization.md)
- [iOS SDK Theme Configuration](../ios/theme_customization.md)

---

**Last Updated**: March 17, 2026  
**SDK Version**: 1.5.19+  
**Maintained By**: LuminSoft Development Team
