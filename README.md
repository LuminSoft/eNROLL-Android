

# eNROLL 

This document is a guide for eNROLL Android SDK. In addition, following the below steps will help you learn how to add and use (eNROLL SDK) in your Android Application.



## REQUIREMENTS

- Minimum Android SDK 34
- Target API level 35


## 2. INSTALLATION

1-  Add eNROLL SDK dependency to the build.gradle (Module :app) file:

```bash
dependencies {
    implementation("com.github.LuminSoft:eNROLL-Android:latest")
}
```

- You can find the latest version  https://github.com/LuminSoft/eNROLL-Android/releases


2- Add Maven Repository in settings.gradle file

```bash
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("http://maven.innovatrics.com/releases")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
```




3- Add license file to your project:

![App Screenshot](https://lumin-soft.gitbook.io/~gitbook/image?url=https%3A%2F%2F3826285197-files.gitbook.io%2F%7E%2Ffiles%2Fv0%2Fb%2Fgitbook-x-prod.appspot.com%2Fo%2Fspaces%252FGM6tCcdsukNbOigN9U2m%252Fuploads%252FidXQqrhFFiMjXmehyKng%252FScreen%2520Shot%25202024-03-24%2520at%252010.41.22%2520AM.png%3Falt%3Dmedia%26token%3Dde6d2485-8d25-46fc-967b-2d875011f6cd&width=768&dpr=4&quality=100&sign=a4cdc785&sv=1)

## 3. IMPORT

```bash
import com.luminsoft.enroll_sdk.*
```

## 4. USAGE

### Step 1: Initialize the SDK and create a callback object:


ℹ️ eNROLL.init function is used for Initializing eNROLL SDK instance to use it.

ℹ️ It’s a throws function so please put it in a try…catch blocs.

ℹ️ EnrollCallback object contains Success, Error  and Get Request ID call backs

```bash
        try {
            eNROLL.init(
                tenantId = "tenantId",
                tenantSecret = "tenantSecret",
                enrollMode = EnrollMode.ONBOARDING,
                environment = EnrollEnvironment.STAGING,
                enrollCallback = object :
                    EnrollCallback {
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
                correlationId = "correlationId"

                )
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
```


### Step 2: launch SDK:


eNROLL.launch function is used for launching eNROLL SDK.

ℹ️ It’s a throws function so please put it in a try…catch blocs.

```bash
try {
    eNROLL.launch(this)
} catch (e: Exception) {
    Log.e(TAG, e.toString())
}
```


## 5. VALUES DESCRIPTION


.
| Keys.     | Values                                                                                                                                                             |
| :-------- |:-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `tenantId` | **Required**. Write your organization tenant id                                                                                                                    |
| `tenantSecret` | **Required**. Write your organization tenant secret.                                                                                                               |
| `enrollMode`       | **Required**. Mode of the SDK.                                                                                                                                     |
| `environment`      | **Required**. Select the EnrollEnvironment: EnrollEnvironment.STAGING  for staging and EnrollEnvironment.PRODUCTION for production.                                |
| `enrollCallback`   | **Required**. Callback function to receive success and error response.                                                                                             |
| `localizationCode` | **Required**. Select your language code LocalizationCode.EN for English, and LocalizationCode.AR for Arabic. The default value is English.                         |
| `googleApiKey` | **Optional**. Google Api Key to view the user current location on the map.                                                                                         |
| `applicantId` | **Optional**. Write your Application id.                                                                                                                           |
| `levelOfTrustToken` | **Optional**. Write your Organization level of trust.                                                                                                              |
| `skipTutorial` | **Optional**. Choose to ignore the tutorial or not.                                                                                                                |
| `appColors` | **Optional**. Collection of the app colors that you could override like (primary - secondary - backGround - successColor - warningColor - errorColor - textColor). |
| `correlationId` | **Optional**. Correlation ID to connect your User ID with our Request ID                                                                                           |





