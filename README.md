# eNROLL Android SDK (via JitPack)

This guide explains how to integrate the **eNROLL Android SDK** distributed as a prebuilt AAR through **JitPack**.  
The repository that hosts the release artifacts is **LuminSoft/eNROLL-Android** (carrier branch).

---

## ✅ Requirements

- **Minimum Android SDK:** 24
- **Target API level:** 35
- **Language:** Kotlin or Java
- **Gradle:** AGP 8.x recommended

---

## 🚀 Installation (JitPack)

### 1) Add JitPack to your repositories

**Kotlin DSL (recommended)** — `settings.gradle.kts`
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```

**Groovy DSL** — `settings.gradle`
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```

> If your project still uses `allprojects { repositories { ... } }`, add `maven { url "https://jitpack.io" }` there instead.

### 2) Add the SDK dependency

**Kotlin DSL** — `app/build.gradle.kts`
```kotlin
dependencies {
    implementation("com.github.LuminSoft:eNROLL-sdk:<version>")
}
```

**Groovy DSL** — `app/build.gradle`
```groovy
dependencies {
    implementation "com.github.LuminSoft:eNROLL-sdk:<version>"
}
```

🔎 **Find the latest version:** Visit the JitPack page of the repo and pick the latest tag (e.g., `v1.3.91.4`).  
Example:
```kotlin
implementation("com.github.LuminSoft:eNROLL-sdk:1.3.91.4")
```

---

## 🔐 Security notes (strongly recommended)

- **Never hard‑code secrets** (e.g., `tenantSecret`) in source. Load them from secure storage (Android Keystore / EncryptedSharedPreferences) or from a backend at runtime.
- **Obfuscate your app** with R8/ProGuard in release builds. Keep the SDK’s public API (see rules below).
- **Turn off verbose logging in release** builds. Avoid logging tokens/PII.
- **Use TLS and consider cert pinning** for your API host (e.g., with OkHttp CertificatePinner) if risk model requires it.
- **Rotate tokens** regularly and scope them to the minimum permissions.
- **Protect screenshots** if sensitive screens are shown (e.g., `window.setFlags(LayoutParams.FLAG_SECURE, ...)`).

### ProGuard / R8 keep rules (consumer)
If your build shrinks/obfuscates code and you face model/reflective access issues, add:
```
-keep class com.luminsoft.enroll_sdk.** { *; }
-dontwarn com.luminsoft.enroll_sdk.**
```
> If your SDK already ships `consumer-proguard-rules.pro`, Gradle will merge them automatically and you may not need the rules above.

---

## 📜 Android Manifest & permissions

The SDK may require standard permissions such as:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
```
These are typically merged automatically if declared in the SDK’s AAR. If your build complains about missing permissions/components, declare them in your app manifest.

---

## 📦 Import & basic usage

```kotlin
import com.luminsoft.enroll_sdk.*

try {
    eNROLL.init(
        tenantId = "tenantId",
        tenantSecret = "tenantSecret",
        enrollMode = EnrollMode.ONBOARDING,
        environment = EnrollEnvironment.STAGING,
        enrollCallback = object : EnrollCallback {
            override fun success(enrollSuccessModel: EnrollSuccessModel) {
                Log.d("eNROLL", enrollSuccessModel.enrollMessage)
            }
            override fun error(enrollFailedModel: EnrollFailedModel) {
                Log.d("eNROLL", enrollFailedModel.failureMessage)
            }
            override fun getRequestId(requestId: String) {
                Log.d("eNROLL", requestId)
            }
        },
        localizationCode = LocalizationCode.EN,
        googleApiKey = "googleApiKey",
        skipTutorial = false,
        appColors = AppColors(),
        applicantId = "applicationIdText",
        levelOfTrustToken = "levelOfTrustTokenText",
        correlationId = "correlationId",
        fontResource = R.font.itim_regular,
        enrollForcedDocumentType = EnrollForcedDocumentType.NATIONAL_ID_OR_PASSPORT
    )
    eNROLL.launch(this)
} catch (e: Exception) {
    Log.e("eNROLL", e.toString())
}
```

### Parameter reference (quick)
| Key                        | Description                                                                 |
|----------------------------|-----------------------------------------------------------------------------|
| `tenantId`                 | **Required.** Your organization tenant ID                                   |
| `tenantSecret`             | **Required.** Your organization secret                                      |
| `enrollMode`               | **Required.** `EnrollMode.ONBOARDING` or `EnrollMode.UPDATE`                |
| `environment`              | **Required.** `EnrollEnvironment.STAGING` or `EnrollEnvironment.PRODUCTION` |
| `enrollCallback`           | **Required.** Callback to handle success/error                              |
| `localizationCode`         | **Required.** `LocalizationCode.EN` or `LocalizationCode.AR`                |
| `googleApiKey`             | Optional. Google API Key for maps                                           |
| `applicantId`              | Optional. Application ID                                                    |
| `levelOfTrustToken`        | Optional. Trust level token                                                 |
| `skipTutorial`             | Optional. Skip intro tutorial                                               |
| `appColors`                | Optional. Custom color theme                                                |
| `correlationId`            | Optional. Correlation ID for tracking                                       |
| `fontResource`             | Optional. Font resource reference                                           |
| `enrollForcedDocumentType` | Optional. Force document type (`NATIONAL_ID_OR_PASSPORT`)                   |

---

## 🧩 Troubleshooting

- **Could not resolve com.github.LuminSoft:eNROLL-sdk:** Ensure `maven("https://jitpack.io")` is present in your repositories *above* your module dependency resolution.
- **Dependencies missing (AndroidX classes not found):** Add common AndroidX libs your app relies on, e.g.:
  ```kotlin
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.activity:activity-ktx:1.9.2")
  implementation("androidx.fragment:fragment-ktx:1.8.3")
  // If your navigation components are used in host app:
  // implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
  // implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
  ```
- **ProGuard/R8 class not found:** Use the keep rules above.
- **Crash at runtime:** Check you passed valid `tenantId/tenantSecret`, and that the app has camera/network permissions.

---

## 🔎 Verifying the artifact

JitPack serves the AAR for a given tag. You can manually download and verify:
```bash
curl -fL -o eNROLL-sdk-<version>.aar \
  "https://jitpack.io/com/github/LuminSoft/eNROLL-sdk/<version>/eNROLL-sdk-<version>.aar"

shasum -a 256 eNROLL-sdk-<version>.aar
```
Compare the hash with the value shown on your Release notes (if provided).

---

## 📬 Support

- Open an issue on **GitHub**: `https://github.com/LuminSoft/eNROLL-Android/issues`
- Email: **support@luminsoft.dev**

---

### Changelog

See the GitHub Releases for version history.
