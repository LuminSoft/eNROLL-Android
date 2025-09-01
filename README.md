# eNROLL Android SDK

This document is a guide to integrating the **eNROLL Android SDK** published via **GitHub Packages**.

---

## ✅ Requirements

- **Minimum Android SDK:** 24
- **Target API Level:** 35
- **Language:** Kotlin or Java

---

## 🚀 Installation

### 1. Add the SDK dependency to your `build.gradle (Module: app)`

```kotlin
dependencies {
    implementation("com.github.luminsoft:enroll-sdk:1.0.0") // Replace with latest version
}
```

📌 **Note:** The version must be explicitly specified. Use the [Releases page](https://github.com/LuminSoft/Secure-eNROLL-Android/releases) to find the latest version.

---

### 2. Add GitHub Maven repository to your `settings.gradle`

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://maven.pkg.github.com/LuminSoft/Secure-eNROLL-Android")
            credentials {
                username = "<your-github-username>"
                password = "<your-personal-access-token>"
            }
        }
    }
}
```

🛡️ **Authentication required:** GitHub Packages requires a **GitHub username + personal access token** with `read:packages` permission.

---

### 3. Add your license file

Include the license file (`enroll.license`) in your project as required.

![License Screenshot](https://lumin-soft.gitbook.io/~gitbook/image?url=https%3A%2F%2F3826285197-files.gitbook.io%2F%7E%2Ffiles%2Fv0%2Fb%2Fgitbook-x-prod.appspot.com%2Fo%2Fspaces%252FGM6tCcdsukNbOigN9U2m%252Fuploads%252FidXQqrhFFiMjXmehyKng%252FScreen%2520Shot%25202024-03-24%2520at%252010.41.22%2520AM.png%3Falt%3Dmedia%26token%3Dde6d2485-8d25-46fc-967b-2d875011f6cd&width=768&dpr=4&quality=100&sign=a4cdc785&sv=1)

---

## 📦 Import SDK

```kotlin
import com.luminsoft.enroll_sdk.*
```

---

## 🛠 Usage

### ✅ Step 1: Initialize the SDK

```kotlin
try {
    eNROLL.init(
        tenantId = "tenantId",
        tenantSecret = "tenantSecret",
        enrollMode = EnrollMode.ONBOARDING,
        environment = EnrollEnvironment.STAGING,
        enrollCallback = object : EnrollCallback {
            override fun success(enrollSuccessModel: EnrollSuccessModel) {
                Log.d(TAG, enrollSuccessModel.enrollMessage)
            }

            override fun error(enrollFailedModel: EnrollFailedModel) {
                Log.d(TAG, enrollFailedModel.failureMessage)
            }

            override fun getRequestId(requestId: String) {
                Log.d(TAG, requestId)
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
} catch (e: Exception) {
    Log.e(TAG, e.toString())
}
```

---

### ✅ Step 2: Launch the SDK

```kotlin
try {
    eNROLL.launch(this)
} catch (e: Exception) {
    Log.e(TAG, e.toString())
}
```

---

## 🔍 Parameter Reference

| Key                        | Description                                                                 |
|----------------------------|-----------------------------------------------------------------------------|
| `tenantId`                 | **Required.** Your organization tenant ID                                   |
| `tenantSecret`             | **Required.** Your organization secret                                      |
| `enrollMode`               | **Required.** `EnrollMode.ONBOARDING` or `EnrollMode.UPDATE`                |
| `environment`              | **Required.** `EnrollEnvironment.STAGING` or `EnrollEnvironment.PRODUCTION` |
| `enrollCallback`           | **Required.** Callback object to handle success/error                       |
| `localizationCode`         | **Required.** `LocalizationCode.EN` or `LocalizationCode.AR`                |
| `googleApiKey`             | Optional. Google API Key for maps                                           |
| `applicantId`              | Optional. Your application ID                                               |
| `levelOfTrustToken`        | Optional. Trust level token                                                 |
| `skipTutorial`             | Optional. Skip intro tutorial                                               |
| `appColors`                | Optional. Custom color theme                                                |
| `correlationId`            | Optional. Correlation ID for tracking                                       |
| `fontResource`             | Optional. Font resource reference                                           |
| `enrollForcedDocumentType` | Optional. Force document type (`NATIONAL_ID_OR_PASSPORT`)                   |

---

## 🧩 Extras

- 💡 Use `try/catch` for all SDK interactions.
- ✅ Always specify the version (avoid `latest`) for stability.
- 🔐 Store `tenantSecret` securely using Android Keystore or encrypted preferences.

---

## 📬 Support

Need help integrating? Contact the **LuminSoft team** via [GitHub Issues](https://github.com/LuminSoft/Secure-eNROLL-Android/issues) or email support@luminsoft.dev.
