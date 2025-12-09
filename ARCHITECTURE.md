# 📐 eNROLL SDK - Architecture Document

> **Master Document v1.0** - December 4, 2024
> **Classification**: Internal Technical Documentation
> **Sensitivity**: HIGH (Government Ministry + Enterprise SDK)

---

## 📋 Table of Contents
1. [Executive Summary](#executive-summary)
2. [Project Overview](#project-overview)
3. [Architecture Overview](#architecture-overview)
4. [Module Structure](#module-structure)
5. [Public API Surface](#public-api-surface)
6. [SDK Lifecycle & State Management](#sdk-lifecycle--state-management)
7. [Dependency Injection](#dependency-injection)
8. [Network Layer](#network-layer)
9. [Feature Modules](#feature-modules)
10. [Security Considerations](#security-considerations)
11. [Known Limitations](#known-limitations)
12. [Upcoming Changes Analysis](#upcoming-changes-analysis)

---

## 1. Executive Summary

### What is eNROLL SDK?
eNROLL is an **Android SDK (AAR library)** for electronic Know Your Customer (eKYC) processes. It enables mobile applications to:
- **Onboard new users** (identity verification)
- **Authenticate existing users** (biometric auth)
- **Update user profiles**
- **Handle forgotten credentials**
- **Sign digital contracts**

### Who Uses It?
- **Government ministries** (primary client - highest sensitivity)
- **Enterprise clients** (banks, financial institutions)
- **Integration via JitPack** (GitHub releases)

### Tech Stack
| Component | Technology |
|-----------|------------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Architecture | Clean Architecture + MVVM |
| DI Framework | Koin 3.4.3 |
| Networking | Retrofit 2.9.0 + OkHttp |
| Biometrics | Innovatrics DOT SDK 8.16.1 |
| Navigation | Jetpack Navigation Compose |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 |

---

## 2. Project Overview

### Repository Structure
```
ekyc-android/
├── app/                          # Demo/Test application (NOT shipped)
│   ├── src/main/java/.../
│   │   └── MainActivity.kt       # Integration example
│   └── src/main/assets/          # Environment configs (gitignored)
│
├── eNROLL-sdk/                   # MAIN SDK LIBRARY (shipped to clients)
│   ├── src/main/java/com/luminsoft/enroll_sdk/
│   │   ├── Enroll.kt             # Public API re-exports
│   │   ├── EnrollMain*Activity.kt # Mode-specific activities
│   │   ├── sdk/                  # Public entry point
│   │   ├── core/                 # Shared infrastructure
│   │   ├── features/             # Onboarding features
│   │   ├── features_auth/        # Authentication features
│   │   ├── features_update/      # Update features
│   │   ├── features_forget/      # Forgot password features
│   │   ├── features_sign_contract/ # Contract signing
│   │   ├── innovitices/          # Biometric integration
│   │   ├── main*/                # Main flow coordinators
│   │   └── ui_components/        # Shared UI
│   │
│   ├── src/main/res/             # Resources (layouts, strings, etc.)
│   └── build.gradle              # SDK build config & dependencies
│
├── PROJECT_RULES.md              # Mandatory development rules
├── ARCHITECTURE.md               # THIS DOCUMENT
└── build.gradle                  # Root build config
```

### Version Information
| File | Current Version | Purpose |
|------|-----------------|---------|
| `eNROLL-sdk/build.gradle` | 1.5.04 | JitPack publishing |
| `BuildInfo.kt` | (varies) | Runtime version check |
| `app/build.gradle` | (varies) | Demo app versioning |

---

## 3. Architecture Overview

### High-Level Flow
```
┌─────────────────────────────────────────────────────────────────┐
│                        HOST APPLICATION                         │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  1. eNROLL.init(...)  →  2. eNROLL.launch(activity)     │    │
│  └─────────────────────────────────────────────────────────┘    │
└─────────────────────────────┬───────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        eNROLL SDK                                │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  EnrollMainActivity (Dispatcher)                         │    │
│  │  ├─► EnrollMainOnBoardingActivity  (ONBOARDING mode)    │    │
│  │  ├─► EnrollMainAuthActivity        (AUTH mode)          │    │
│  │  ├─► EnrollMainUpdateActivity      (UPDATE mode)        │    │
│  │  ├─► EnrollMainForgetActivity      (FORGET mode)        │    │
│  │  └─► EnrollMainSignContractActivity (SIGN_CONTRACT)     │    │
│  └─────────────────────────────────────────────────────────┘    │
│                              │                                   │
│                              ▼                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Feature Modules (Clean Architecture)                    │    │
│  │  ├── national_id/  ├── face_capture/  ├── email/        │    │
│  │  ├── phone/        ├── location/      ├── password/     │    │
│  │  └── security_questions/ ...                            │    │
│  └─────────────────────────────────────────────────────────┘    │
│                              │                                   │
│                              ▼                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Core Infrastructure                                     │    │
│  │  ├── Network (Retrofit + OkHttp + Encryption)           │    │
│  │  ├── Models (Public API contracts)                       │    │
│  │  └── Utils (Helpers, Security)                          │    │
│  └─────────────────────────────────────────────────────────┘    │
│                              │                                   │
│                              ▼                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  EnrollCallback → Host App                               │    │
│  │  ├── success(EnrollSuccessModel)                         │    │
│  │  ├── error(EnrollFailedModel)                            │    │
│  │  └── getRequestId(String)                                │    │
│  └─────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
```

### Architecture Layers
```
┌────────────────────────────────────────────────────────┐
│  PRESENTATION LAYER                                     │
│  ├── Activities (EnrollMain*Activity)                   │
│  ├── Composables (UI components)                        │
│  └── ViewModels (state management)                      │
├────────────────────────────────────────────────────────┤
│  DOMAIN LAYER                                           │
│  ├── Use Cases (*UseCase.kt)                            │
│  └── Repository Interfaces                              │
├────────────────────────────────────────────────────────┤
│  DATA LAYER                                             │
│  ├── Repository Implementations                         │
│  ├── Remote Data Sources (API calls)                    │
│  └── Local Data Sources (if any)                        │
├────────────────────────────────────────────────────────┤
│  CORE LAYER                                             │
│  ├── Network (Retrofit, Interceptors)                   │
│  ├── Models (DTOs, Domain models)                       │
│  └── Utilities (Helpers, Extensions)                    │
└────────────────────────────────────────────────────────┘
```

---

## 4. Module Structure

### Core Module (`core/`)
```
core/
├── failures/                 # Error handling
│   └── SomeFailure.kt
├── models/                   # 🔴 PUBLIC API CONTRACTS
│   ├── BuildInfo.kt          # SDK version
│   ├── EnrollCallback.kt     # 🔴 Client callback interface
│   ├── EnrollEnvironment.kt  # 🔴 STAGING/PRODUCTION enum
│   ├── EnrollFailedModel.kt  # 🔴 Error response model
│   ├── EnrollForcedDocumentType.kt # Document type enum
│   ├── EnrollMode.kt         # 🔴 SDK modes enum
│   ├── EnrollSuccessModel.kt # 🔴 Success response model
│   ├── LocalizationCode.kt   # 🔴 Language enum (EN/AR)
│   └── SDKModule.kt          # Koin DI module
├── network/                  # Network infrastructure
│   ├── AuthInterceptor.kt    # Auth header injection
│   ├── RetroClient.kt        # Retrofit singleton
│   ├── EncryptedConverterFactory.kt # Request encryption
│   └── CustomTrustManager.kt # SSL handling
├── sdk/
│   └── EnrollSDK.kt          # 🔴 SINGLETON STATE HOLDER
└── utils/                    # Utilities
    ├── EncryptionHelper.kt   # Encryption utilities
    ├── RootDetectionUtil.kt  # Rooted device check
    ├── ResourceProvider.kt   # Resource access
    └── WifiService.kt        # Network status
```

### SDK Entry Point (`sdk/`)
```
sdk/
└── eNROLL.kt                 # 🔴 MAIN PUBLIC ENTRY POINT
    ├── init(...)             # Initialize SDK with config
    └── launch(activity)      # Start SDK flow
```

### Feature Modules
Each feature follows Clean Architecture:
```
features/national_id_confirmation/
├── national_id_confirmation_di/     # Koin DI module
│   └── nationalIdConfirmationModule.kt
├── national_id_confirmation_data/   # Data layer
│   ├── national_id_confirmation_api/
│   │   └── NationalIdConfirmationApi.kt
│   ├── national_id_confirmation_models/
│   │   └── NationalIdConfirmationRequest.kt
│   └── national_id_confirmation_repository/
│       └── NationalIdConfirmationRepositoryImpl.kt
├── national_id_confirmation_domain/  # Domain layer
│   ├── national_id_confirmation_repository/
│   │   └── NationalIdConfirmationRepository.kt (interface)
│   └── national_id_confirmation_use_case/
│       └── NationalIdConfirmationUseCase.kt
├── national_id_confirmation_navigation/ # Navigation
│   └── NationalIdConfirmationNavigation.kt
└── national_id_confirmation_presentation/ # UI
    ├── ui/
    │   └── NationalIdConfirmationScreen.kt
    └── view_model/
        └── NationalIdConfirmationViewModel.kt
```

### Feature List
| Module | Description | Risk Level |
|--------|-------------|------------|
| `national_id_confirmation/` | ID card scanning & OCR | 🔴 HIGH (PII) |
| `face_capture/` | Face biometric capture | 🔴 HIGH (Biometric) |
| `email/` | Email verification | 🟡 MEDIUM |
| `phone_numbers/` | Phone verification | 🟡 MEDIUM |
| `security_questions/` | Security Q&A setup | 🟡 MEDIUM |
| `setting_password/` | Password setup | 🟡 MEDIUM |
| `location/` | GPS location capture | 🟡 MEDIUM |
| `check_aml/` | AML screening | 🟡 MEDIUM |
| `device_data/` | Device fingerprinting | 🟢 LOW |
| `terms_and_conditions/` | T&C acceptance | 🟢 LOW |
| `electronic_signature/` | E-signature | 🟡 MEDIUM |

---

## 5. Public API Surface

### 🔴 CRITICAL: Files That Define Client Contract

These files, if changed, will **break client integrations**:

#### 1. Main Entry Point (`Enroll.kt`)
```kotlin
// Re-exports for simple imports by clients
package com.luminsoft.enroll_sdk

typealias EnrollCallback = EnrollCallback
typealias EnrollEnvironment = EnrollEnvironment
typealias EnrollFailedModel = EnrollFailedModel
typealias EnrollMode = EnrollMode
typealias EnrollSuccessModel = EnrollSuccessModel
typealias LocalizationCode = LocalizationCode
typealias eNROLL = eNROLL
typealias AppColors = com.luminsoft.enroll_sdk.ui_components.theme.AppColors
```

#### 2. SDK Entry Point (`sdk/eNROLL.kt`)
```kotlin
object eNROLL {
    fun init(
        tenantId: String,                    // Required
        tenantSecret: String,                // Required
        applicantId: String = "",            // Required for AUTH
        levelOfTrustToken: String = "",      // Required for AUTH
        enrollMode: EnrollMode,              // Required
        environment: EnrollEnvironment = EnrollEnvironment.STAGING,
        localizationCode: LocalizationCode = LocalizationCode.EN,
        enrollCallback: EnrollCallback? = null,
        googleApiKey: String? = "",
        skipTutorial: Boolean = false,
        appColors: AppColors = AppColors(),
        correlationId: String = "",
        requestId: String = "",
        templateId: String = "",             // Required for SIGN_CONTRACT
        contractParameters: String = "",
        fontResource: Int? = 0,
        enrollForcedDocumentType: EnrollForcedDocumentType? = ...,
    )
    
    fun launch(activity: Activity)
}
```

#### 3. Callback Interface (`core/models/EnrollCallback.kt`)
```kotlin
interface EnrollCallback {
    fun success(enrollSuccessModel: EnrollSuccessModel)
    fun error(enrollFailedModel: EnrollFailedModel)
    fun getRequestId(requestId: String)
}
```

#### 4. Response Models
```kotlin
// EnrollSuccessModel.kt
data class EnrollSuccessModel(
    val enrollMessage: String,
    val documentId: String? = null,
    val applicantId: String? = null
)

// EnrollFailedModel.kt
data class EnrollFailedModel(
    val failureMessage: String,
    val error: Any? = null,
    val applicantId: String? = null
)
```

#### 5. Enums
```kotlin
// EnrollMode.kt
enum class EnrollMode {
    UPDATE, ONBOARDING, AUTH, FORGET_PROFILE_DATA, SIGN_CONTRACT
}

// EnrollEnvironment.kt
enum class EnrollEnvironment {
    STAGING, PRODUCTION
}

// LocalizationCode.kt
enum class LocalizationCode {
    EN, AR
}
```

#### 6. Theming (`AppColors`)
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

### Client Integration Example
```kotlin
// This is how clients use the SDK
eNROLL.init(
    tenantId = "...",
    tenantSecret = "...",
    enrollMode = EnrollMode.ONBOARDING,
    environment = EnrollEnvironment.PRODUCTION,
    enrollCallback = object : EnrollCallback {
        override fun success(model: EnrollSuccessModel) { ... }
        override fun error(model: EnrollFailedModel) { ... }
        override fun getRequestId(id: String) { ... }
    },
    appColors = AppColors(primary = Color.Blue)
)
eNROLL.launch(this)
```

---

## 6. SDK Lifecycle & State Management

### Current Implementation

#### State Holder (`EnrollSDK.kt`)
```kotlin
object EnrollSDK {
    // Organization info (set via init)
    var tenantId = ""
    var tenantSecret = ""
    var applicantId = ""
    var levelOfTrustToken = ""
    var googleApiKey = ""
    var correlationId = ""
    var requestId = ""
    var contractTemplateId = ""
    var contractParameters = ""
    var serverPublicKey = ""
    var updateSteps = arrayListOf<String>()
    
    // SDK config (set via init)
    var environment = EnrollEnvironment.STAGING
    var localizationCode = LocalizationCode.AR
    var skipTutorial = false
    var appColors = AppColors()
    var fontResource = 0
    var enrollCallback: EnrollCallback? = null
    var enrollMode: EnrollMode = EnrollMode.ONBOARDING
    var enrollForcedDocumentType: EnrollForcedDocumentType? = ...
}
```

### ⚠️ Current Lifecycle Issues

#### Issue 1: No State Reset
```kotlin
// Current flow:
eNROLL.init(...)  // Sets state in EnrollSDK singleton
eNROLL.launch()   // Launches SDK
// SDK finishes via callback
// But EnrollSDK state is NEVER cleared!

// On second launch:
eNROLL.init(...)  // Overwrites state
eNROLL.launch()   // May have stale data from previous session
```

#### Issue 2: No Koin Cleanup
```kotlin
// In EnrollMainOnBoardingActivity.onCreate():
GlobalContext.getOrNull() ?: startKoin { ... }
// Koin is started but NEVER stopped
// On re-launch, old singletons persist
```

#### Issue 3: Network State Persistence
```kotlin
// In RetroClient:
object RetroClient {
    private var baseUrl = ""
    internal var token = ""  // Token persists across sessions!
}
```

#### Issue 4: No Close/Dispose Method
Currently there is **NO** method to:
- Clean up SDK state
- Stop ongoing operations
- Reset for new session

### Lifecycle Flow Diagram
```
Current (Problematic):
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│   init()    │  →   │   launch()  │  →   │  callback   │
│ Sets state  │      │ Starts flow │      │  (finish)   │
└─────────────┘      └─────────────┘      └─────────────┘
                                                │
                                                ▼
                                          STATE PERSISTS
                                          (no cleanup)

Desired (For upcoming change):
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│   init()    │  →   │   launch()  │  →   │  callback   │
│ Sets state  │      │ Starts flow │      │  (finish)   │
└─────────────┘      └─────────────┘      └─────────────┘
       │                                        │
       │            ┌─────────────┐             │
       └───────────►│   close()   │◄────────────┘
                    │ Cleans state│
                    └─────────────┘
                          │
                          ▼
                    STATE CLEARED
                    (ready for re-init)
```

---

## 7. Dependency Injection

### Koin Setup
Koin modules are loaded in main activity:
```kotlin
// In EnrollMainOnBoardingActivity.kt
private fun getKoin(activity: ComponentActivity): Koin {
    return GlobalContext.getOrNull() ?: startKoin {
        androidContext(activity.applicationContext)
        modules(
            // Forget flow modules
            forgetLocationModule,
            forgetPasswordModule,
            lostDeviceIdModule,
            
            // Auth modules  
            passwordAuthModule,
            mailAuthModule,
            phoneAuthModule,
            locationAuthModule,
            checkExpiryDateAuthModule,
            checkIMEIAuthModule,
            faceCaptureAuthModule,
            securityQuestionAuthModule,
            
            // Onboarding modules
            termsConditionsModule,
            checkAmlModule,
            deviceDataModule,
            emailModule,
            faceCaptureModule,
            locationModule,
            nationalIdConfirmationModule,
            phoneNumbersModule,
            securityQuestionsModule,
            passwordModule,
            electronicSignatureModule,
            
            // Core
            sdkModule,
            mainModule,
            mainAuthModule,
            mainForgetModule,
            mainUpdateModule,
            mainSignContractModule,
            
            // Update modules
            deviceIdAuthUpdateModule,
            faceCaptureAuthUpdateModule,
            updateLocationModule,
            emailUpdateModule,
            phoneUpdateModule,
            // ... more modules
        )
    }.koin
}
```

### ⚠️ DI Concerns for Re-launch
1. **No `stopKoin()` call** - Modules stay loaded
2. **Singletons persist** - Old instances reused
3. **ViewModels may have stale state**

---

## 8. Network Layer

### Architecture
```
┌─────────────────────────────────────────────────────────┐
│                    API Call Flow                         │
├─────────────────────────────────────────────────────────┤
│  Repository → UseCase → API Interface → RetroClient     │
│                                              │          │
│                                              ▼          │
│                                      OkHttpClient       │
│                                              │          │
│                              ┌───────────────┴───────┐  │
│                              │    Interceptors       │  │
│                              │  ├─ AuthInterceptor   │  │
│                              │  └─ LoggingInterceptor│  │
│                              └───────────────────────┘  │
│                                              │          │
│                                              ▼          │
│                                    Encrypted Request    │
│                                              │          │
│                                              ▼          │
│                                      Backend API        │
└─────────────────────────────────────────────────────────┘
```

### Endpoints
```kotlin
// EnrollSDK.kt
fun getApisUrl(): String {
    return when (environment) {
        STAGING -> "https://enrollstg.luminsoft.net:7400/SecureOnBoarding/"
        PRODUCTION -> "https://enrollgateway.luminsoft.net:443/SecureOnBoarding/"
    }
}
```

### Security Features
1. **Request Encryption** - `EncryptedConverterFactory`
2. **Bearer Token Auth** - `AuthInterceptor`
3. **Custom SSL Trust Manager** - `CustomTrustManager`
4. **Root Detection** - Blocks rooted devices

---

## 9. Feature Modules

### Onboarding Flow Features
| Step | Feature | Module |
|------|---------|--------|
| 1 | Terms & Conditions | `terms_and_conditions/` |
| 2 | National ID Scan | `national_id_confirmation/` |
| 3 | Face Capture | `face_capture/` |
| 4 | Email Verification | `email/` |
| 5 | Phone Verification | `phone_numbers/` |
| 6 | Location Capture | `location/` |
| 7 | Security Questions | `security_questions/` |
| 8 | Password Setup | `setting_password/` |
| 9 | AML Check | `check_aml/` |
| 10 | E-Signature | `electronic_signature/` |

### Biometrics (Innovatrics DOT)
```
innovitices/
├── documentautocapture/  # ID document scanning
├── faceautocapture/      # Face detection
├── smileliveness/        # Liveness check (smile)
├── magnifeyeliveness/    # Liveness check (eye)
└── core/                 # DOT SDK wrapper
```

---

## 10. Security Considerations

### Security Measures in Place ✅
| Measure | Implementation |
|---------|----------------|
| Root Detection | `RootDetectionUtil.isDeviceRooted()` |
| Request Encryption | `EncryptedConverterFactory` |
| Bearer Token Auth | `AuthInterceptor` |
| SSL/TLS | Custom `TrustManager` |
| Screen Security | Portrait-only, fullscreen |

### Security Concerns ⚠️
| Risk | Status | Recommendation |
|------|--------|----------------|
| Hardcoded public key | ⚠️ In code | Move to secure storage |
| Token in singleton | ⚠️ Persists | Clear on close |
| No certificate pinning | ⚠️ Missing | Add pinning |
| Debug logging | ⚠️ In release | Remove for production |

### Sensitive Data Handled
- **PII**: Name, address, ID numbers
- **Biometrics**: Face images
- **Documents**: ID card images
- **Credentials**: Passwords, security answers
- **Location**: GPS coordinates

---

## 11. Known Limitations

### Current Limitations
1. **No SDK close/dispose method** - Can't clean up gracefully
2. **Single instance assumption** - Can't run multiple SDK flows
3. **State persists across launches** - May cause issues on re-launch
4. **No pause/resume support** - SDK must complete in one session
5. **Portrait only** - No landscape support
6. **Minimum SDK 24** - No support for older devices

### Technical Debt
1. Koin modules not properly scoped
2. No proper Activity lifecycle handling
3. Deprecated API usage (`updateConfiguration`)
4. Some TODO comments in code
5. Hardcoded strings (public key, URLs)

---

## 12. Upcoming Changes Analysis

### Proposed Feature: SDK Lifecycle Management
**Goal**: Allow launch, close, and re-launch of SDK multiple times

### Required Changes (High-Level)

#### 1. Add `close()` Method to `eNROLL.kt`
```kotlin
object eNROLL {
    fun init(...) { /* existing */ }
    fun launch(activity: Activity) { /* existing */ }
    
    // NEW: Clean shutdown
    fun close() {
        // 1. Clear EnrollSDK state
        // 2. Stop Koin
        // 3. Clear network tokens
        // 4. Cancel ongoing operations
        // 5. Finish SDK activities
    }
}
```

#### 2. Add State Reset to `EnrollSDK.kt`
```kotlin
object EnrollSDK {
    // Existing properties...
    
    // NEW: Reset all state
    fun reset() {
        tenantId = ""
        tenantSecret = ""
        applicantId = ""
        // ... reset all properties
    }
}
```

#### 3. Network Layer Cleanup
```kotlin
object RetroClient {
    fun reset() {
        token = ""
        baseUrl = ""
    }
}
```

#### 4. Koin Lifecycle Management
```kotlin
// On close:
if (GlobalContext.getKoinApplicationOrNull() != null) {
    stopKoin()
}
```

### Risk Assessment for Upcoming Change
| Aspect | Risk Level | Mitigation |
|--------|------------|------------|
| Public API change | 🟢 LOW | Adding method (non-breaking) |
| State management | 🟡 MEDIUM | Thorough testing required |
| Koin lifecycle | 🟡 MEDIUM | May affect ongoing ViewModels |
| Activity stack | 🟡 MEDIUM | Must handle back stack properly |
| Client impact | 🟢 LOW | Optional method, backward compatible |

### Backward Compatibility
✅ **SAFE** - Adding `close()` is **non-breaking**:
- Existing clients don't need to call it
- SDK behavior unchanged if not called
- Pure addition to API surface

---

## Appendix A: File Quick Reference

### Critical Files (DO NOT MODIFY WITHOUT REVIEW)
```
🔴 eNROLL-sdk/src/main/java/com/luminsoft/enroll_sdk/
   ├── Enroll.kt                          # Public API facade
   ├── sdk/eNROLL.kt                      # Main entry point
   └── core/models/
       ├── EnrollCallback.kt              # Client callback
       ├── EnrollMode.kt                  # Mode enum
       ├── EnrollEnvironment.kt           # Environment enum
       ├── EnrollSuccessModel.kt          # Success response
       ├── EnrollFailedModel.kt           # Error response
       └── LocalizationCode.kt            # Language enum
```

### State Holders (Must Reset on Close)
```
🟡 EnrollSDK.kt                           # Main state singleton
🟡 RetroClient.kt                         # Network state
🟡 WifiService.kt                         # Network service
🟡 ResourceProvider.kt                    # Resource singleton
```

### Activities (Lifecycle Affected)
```
EnrollMainActivity.kt                     # Dispatcher
EnrollMainOnBoardingActivity.kt           # Onboarding flow
EnrollMainAuthActivity.kt                 # Auth flow
EnrollMainUpdateActivity.kt               # Update flow
EnrollMainForgetActivity.kt               # Forget flow
EnrollMainSignContractActivity.kt         # Contract flow
```

---

## Appendix B: Dependency Graph

```
eNROLL-sdk
├── androidx.navigation (2.9.1)
├── com.google.android.material (1.12.0)
├── com.innovatrics.dot (8.16.1)
│   ├── dot-document
│   ├── dot-face-detection-fast
│   └── dot-face-expression-neutral
├── retrofit2 (2.9.0)
│   └── converter-gson
├── okhttp3 (4.8.0)
│   └── logging-interceptor
├── koin (3.4.3)
│   ├── koin-core
│   ├── koin-android
│   └── koin-androidx-compose
├── androidx.compose (1.8.3)
│   ├── compose-ui
│   ├── compose-material3
│   └── compose-navigation
├── firebase (34.1.0 BOM)
│   ├── firebase-config
│   └── firebase-analytics
├── lottie-compose (6.0.1)
├── arrow-core (1.2.0)
└── coil-compose (2.3.0)
```

---

**Document Version**: 1.0
**Last Updated**: December 4, 2024
**Author**: Ahmed sleem
