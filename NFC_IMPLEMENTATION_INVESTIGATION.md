# NFC Passport Reading - Implementation Investigation Report

**Date**: February 8, 2026  
**SDK Version**: eNROLL 1.5.14  
**Current Innovatrics Version**: 8.17.0  

---

## 📋 Executive Summary

This investigation analyzes the requirements for implementing NFC passport reading using Innovatrics DOT SDK. The project already has partial NFC integration (commented out) and backend endpoints ready for passport data.

**Critical Finding**: Version mismatch exists - NFC module is at 8.4.0 while other modules are at 8.17.0. This requires investigation and potential upgrade.

---

## 🔍 Current State Analysis

### 1. SDK Dependencies (build.gradle)

#### ✅ Currently Active
```gradle
api "com.innovatrics.dot:dot-document:8.17.0"
api "com.innovatrics.dot:dot-face-detection-fast:8.17.0"
api "com.innovatrics.dot:dot-face-expression-neutral:8.17.0"
```

#### ❌ Currently Commented Out
```gradle
// api "com.innovatrics.dot:dot-nfc:8.4.0"
// api "com.innovatrics.dot:dot-face-passive-liveness:8.15.1"
// api "com.innovatrics.dot:dot-face-verification:8.4.0"
```

**Issue**: NFC module version (8.4.0) is **13 versions behind** current modules (8.17.0)

---

### 2. Code Integration Status

#### Files with Commented NFC Code

**`InitializeDotSdkUseCase.kt`** (Lines 12, 34)
```kotlin
//import com.innovatrics.dot.nfc.DotNfcLibrary

libraries = listOf(
    DotDocumentLibrary(),
    createDotFaceLibrary(),
//  DotNfcLibrary(),  // ❌ Commented out
)
```

**`HomeFragment.kt`** (Lines 13, 22, 30, 42-46)
```kotlin
private lateinit var nfcReadingStartButton: Button

// setupNfcReadingStartButton()  // ❌ Commented out

// private fun setupNfcReadingStartButton() {
//     nfcReadingStartButton.setOnClickListener {
//         findNavController().navigate(R.id.action_HomeFragment_to_PasswordCaptureFragment)
//     }
// }
```

---

### 3. Backend Integration - Already Implemented ✅

**`NationalIdConfirmationApi.kt`**
```kotlin
@POST("api/v1/onboarding/Passport/UploadPassportImage")
suspend fun passportUploadImage(@Body request: PersonalConfirmationUploadImageRequest): Response<NationalIDConfirmationResponse>

@POST("api/v1/onboarding/Passport/Approve")
suspend fun passportApprove(@Body request: PersonalConfirmationApproveRequest): Response<BasicResponseModel>
```

**Status**: ✅ Backend endpoints exist and are working for OCR-based passport scanning

---

### 4. Data Models - Ready for NFC Data

**`CustomerData.kt`** (PersonalConfirmationResponse.kt)
```kotlin
data class CustomerData(
    // Personal Info
    @SerializedName("fullName") var fullName: String? = null,
    @SerializedName("fullNameEn") var fullNameEn: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("firstNameEn") var firstNameEn: String? = null,
    @SerializedName("familyName") var familyName: String? = null,
    @SerializedName("familyNameEn") var familyNameEn: String? = null,
    
    // Document Info
    @SerializedName("documentNumber") var documentNumber: String? = null,
    @SerializedName("documentTypeId") var documentTypeId: String? = null,
    @SerializedName("documentTypeCode") var documentTypeCode: String? = null,
    @SerializedName("documentCode") var documentCode: String? = null,
    
    // MRZ and Biometric Data
    @SerializedName("photo") var photo: String? = null,
    @SerializedName("birthdate") var birthdate: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("nationality") var nationality: String? = null,
    @SerializedName("issueDate") var issueDate: String? = null,
    @SerializedName("expirationDate") var expirationDate: String? = null,
    @SerializedName("issuingAuthority") var issuingAuthority: String? = null,
    @SerializedName("visualZone") var visualZone: String? = null,
    // ... more fields
)
```

**Status**: ✅ Data model supports passport data from both OCR and NFC sources

---

### 5. Permissions - Missing NFC Permissions

**Current AndroidManifest.xml**
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

**Required for NFC** ❌ Missing:
```xml
<uses-permission android:name="android.permission.NFC" />
<uses-feature android:name="android.hardware.nfc" android:required="false" />
```

---

## 🚨 Critical Version Compatibility Issue

### Current Situation
- **Active Modules**: 8.17.0 (Document, Face Detection, Face Expression)
- **NFC Module**: 8.4.0 (13 versions behind!)
- **Gap**: 13 minor versions (8.4.0 → 8.17.0)

### Risk Assessment
**🔴 HIGH RISK**: Using mismatched SDK versions can cause:
1. Runtime crashes due to incompatible native libraries
2. API breaking changes
3. Licensing issues
4. Memory leaks or undefined behavior

### Options

#### Option 1: Use Latest NFC Version (8.17.0) ⭐ RECOMMENDED
```gradle
api "com.innovatrics.dot:dot-nfc:8.17.0"
```

**Pros**:
- ✅ Version consistency across all modules
- ✅ Latest features and bug fixes
- ✅ Better stability and support
- ✅ Future-proof

**Cons**:
- ⚠️ May have breaking API changes from 8.4.0
- ⚠️ Requires code migration
- ⚠️ Need to update samples/documentation references

**Action Required**: Check Innovatrics changelog for breaking changes between 8.4.0 and 8.17.0

#### Option 2: Downgrade All Modules to 8.4.0 ❌ NOT RECOMMENDED
**Cons**:
- ❌ Lose current features
- ❌ Potential security vulnerabilities
- ❌ No support for newer Android versions
- ❌ Missing bug fixes

---

## 📦 Implementation Requirements

### Phase 1: Sandbox (Fast Approach - Read & Send)

#### 1. Dependencies to Add
```gradle
// In eNROLL-sdk/build.gradle
api "com.innovatrics.dot:dot-nfc:8.17.0"  // Updated to match other modules
```

#### 2. Permissions to Add
```xml
<!-- In eNROLL-sdk/src/main/AndroidManifest.xml -->
<uses-permission android:name="android.permission.NFC" />
<uses-feature 
    android:name="android.hardware.nfc" 
    android:required="false" />  <!-- false = app works without NFC -->
```

#### 3. Code Changes Required

**InitializeDotSdkUseCase.kt**
```kotlin
import com.innovatrics.dot.nfc.DotNfcLibrary  // Uncomment

libraries = listOf(
    DotDocumentLibrary(),
    createDotFaceLibrary(),
    DotNfcLibrary(),  // Uncomment and verify API if version changed
)
```

#### 4. New Components to Create

**NFC Reading Module** (Similar to document/face modules)
```
/features/nfc_passport/
├── nfc_data/
│   ├── nfc_api/
│   │   └── NfcPassportApi.kt (use existing passport endpoints)
│   ├── nfc_models/
│   │   ├── NfcReadRequest.kt
│   │   └── NfcReadResponse.kt
│   └── nfc_repository/
├── nfc_domain/
│   └── usecases/
│       ├── ReadNfcPassportUseCase.kt
│       └── UploadNfcDataUseCase.kt
└── nfc_presentation/
    ├── ui/
    │   └── NfcReadingScreen.kt
    └── viewmodel/
        └── NfcPassportViewModel.kt
```

#### 5. UI/UX Components Needed
- NFC scanning screen with instructions
- "Hold passport to device" animation
- Progress indicator during NFC read
- Error handling (NFC disabled, read failed, etc.)
- Success/failure feedback

#### 6. Navigation Integration
```kotlin
// In nav_graph.xml - add NFC reading flow
<fragment
    android:id="@+id/nfcPasswordCaptureFragment"
    android:name="com.luminsoft.enroll_sdk.features.nfc_passport.nfc_presentation.ui.NfcPasswordCaptureFragment" />
```

---

### Phase 2: Backend-Managed (Future)

In future phase, backend will:
- Control NFC reading flow
- Validate NFC data server-side
- Perform chip authentication
- Verify digital signatures
- Cross-check with OCR data

**Requirements for Phase 2**:
1. Backend API changes to accept raw NFC data
2. Server-side validation logic
3. Chip authentication infrastructure
4. ICAO PKI certificate handling

---

## 🔐 NFC Passport Reading Technical Details

### What NFC Provides (vs OCR)

| Data Type | OCR (Visual) | NFC (Chip) | Priority |
|-----------|-------------|-----------|----------|
| Full Name | ✅ | ✅ | NFC more accurate |
| Date of Birth | ✅ | ✅ | NFC verified |
| Document Number | ✅ | ✅ | Must match |
| Expiry Date | ✅ | ✅ | NFC source of truth |
| Nationality | ✅ | ✅ | NFC verified |
| Photo | ✅ (scan) | ✅ (chip) | NFC higher quality |
| MRZ Code | ✅ | ✅ | Required for chip access |
| Digital Signature | ❌ | ✅ | Security critical |
| Chip Authentication | ❌ | ✅ | Anti-tampering |
| Biometric Templates | ❌ | ✅ (if present) | Enhanced security |

### NFC Reading Process

1. **MRZ Scan First** (Required)
   - Read MRZ from passport using OCR (already implemented)
   - Extract: Document Number, Date of Birth, Expiry Date
   - Calculate MRZ check digits
   - Use as "key" to unlock NFC chip

2. **NFC Chip Reading**
   - Basic Access Control (BAC) using MRZ data
   - Read DG1 (MRZ data)
   - Read DG2 (Facial image)
   - Read DG15 (Public key for Active Authentication)
   - Optional: DG3-DG16 based on passport type

3. **Data Validation**
   - Compare NFC MRZ with OCR MRZ
   - Verify chip signature
   - Validate photo quality
   - Check document authenticity

---

## 🏗️ Architecture Integration

### Current Architecture
```
features/
├── national_id_confirmation/  ✅ Handles National ID & Passport OCR
├── face_capture/              ✅ Liveness + Face capture
└── [other features]
```

### Proposed NFC Integration

**Option A: Extend Existing Module** ⭐ RECOMMENDED
```
features/national_id_confirmation/
├── national_id_onboarding/     ✅ Existing OCR flow
├── national_id_confirmation_data/
├── national_id_confirmation_domain/
└── nfc_passport/               ✨ NEW - NFC reading
    ├── nfc_data/
    ├── nfc_domain/
    └── nfc_presentation/
```

**Option B: Separate Module**
```
features/
├── national_id_confirmation/   ✅ Existing
├── nfc_passport_reading/       ✨ NEW - Standalone
```

**Recommendation**: Option A - keeps passport logic together

---

## 📝 Implementation Checklist

### Step 1: Version Migration (CRITICAL FIRST STEP)
- [ ] Check Innovatrics changelog 8.4.0 → 8.17.0
- [ ] Identify breaking API changes
- [ ] Test compatibility with existing code
- [ ] Update all references to new API

### Step 2: Dependencies & Permissions
- [ ] Add `dot-nfc:8.17.0` to build.gradle
- [ ] Add NFC permissions to AndroidManifest.xml
- [ ] Sync Gradle and verify no conflicts
- [ ] Test app builds successfully

### Step 3: Core SDK Integration
- [ ] Uncomment `DotNfcLibrary()` in InitializeDotSdkUseCase
- [ ] Update import statements
- [ ] Verify SDK initialization succeeds
- [ ] Add NFC availability check

### Step 4: UI Implementation
- [ ] Create NFC reading screen
- [ ] Add password input for MRZ (DOB, expiry, doc number)
- [ ] Implement NFC scan UI flow
- [ ] Add progress indicators
- [ ] Handle NFC errors gracefully

### Step 5: Business Logic
- [ ] Create NfcPassportViewModel
- [ ] Implement ReadNfcPassportUseCase
- [ ] Add data validation logic
- [ ] Map NFC data to CustomerData model

### Step 6: Backend Integration
- [ ] Use existing `passportUploadImage()` API
- [ ] Send NFC data via PersonalConfirmationUploadImageRequest
- [ ] Add flag to indicate data source (NFC vs OCR)
- [ ] Implement approval flow

### Step 7: Navigation & Flow
- [ ] Uncomment NFC button in HomeFragment
- [ ] Add navigation graph entries
- [ ] Define NFC → Next Step routing
- [ ] Handle back navigation

### Step 8: Testing
- [ ] Test with real passports
- [ ] Verify MRZ extraction accuracy
- [ ] Test NFC reading on multiple devices
- [ ] Validate data sent to backend
- [ ] Error scenario testing

### Step 9: Security & Compliance
- [ ] Ensure no sensitive NFC data logged
- [ ] Implement data encryption if needed
- [ ] Follow GDPR/privacy requirements
- [ ] Add security documentation

### Step 10: Documentation
- [ ] Update SDK documentation
- [ ] Add NFC usage guide
- [ ] Document supported passport types
- [ ] Create troubleshooting guide

---

## ❓ Critical Questions Before Implementation

### 1. Version Compatibility
**Q**: What are the breaking changes between Innovatrics 8.4.0 and 8.17.0 for NFC module?  
**Action**: Review Innovatrics changelog and migration guide  
**Risk**: HIGH - Could require significant code changes

### 2. Licensing
**Q**: Does your current Innovatrics license include NFC reading?  
**Check**: `app/src/main/res/raw/iengine.lic`  
**Action**: Verify with Innovatrics that NFC module is activated  
**Risk**: HIGH - App will crash if module not licensed

### 3. Backend Requirements
**Q**: Does backend API accept NFC-specific data fields?  
**Current**: Uses same endpoints as OCR passport  
**Action**: Confirm `CustomerData` fields cover all NFC data  
**Risk**: MEDIUM - May need backend updates

### 4. MRZ Reading Flow
**Q**: Should users scan MRZ first (via OCR) then read NFC chip?  
**Recommended**: Yes - MRZ required to unlock chip  
**Alternative**: Manual entry of MRZ data  
**Risk**: LOW - Standard passport NFC workflow

### 5. Passport Types Supported
**Q**: Which passport types will be supported?  
**Options**:
- ePassports only (with chip) ✅
- All passports (fallback to OCR) ✅
- Specific countries only?  
**Action**: Define supported document list  
**Risk**: MEDIUM - Affects UX and error handling

### 6. Device Compatibility
**Q**: What happens on devices without NFC?  
**Recommendation**: Fallback to OCR-only mode  
**Implementation**: Runtime NFC availability check  
**Risk**: LOW - Already have OCR fallback

### 7. Data Source Preference
**Q**: If both OCR and NFC succeed, which data is used?  
**Recommendation**: Prefer NFC data (more secure)  
**Backend**: May need `dataSource` field in API  
**Risk**: LOW - Business logic decision

### 8. Error Handling
**Q**: What happens if NFC read fails?  
**Options**:
- Retry NFC reading
- Fallback to OCR
- Ask user to enter manually
- Reject and request physical document  
**Risk**: MEDIUM - Affects user experience

### 9. Phase 1 Scope
**Q**: For sandbox, do we implement full NFC or minimal version?  
**Minimal**: Read MRZ + Photo, send to backend  
**Full**: Read all DG fields, validate signatures  
**Recommendation**: Start minimal, expand later  
**Risk**: LOW - Can iterate

### 10. Testing Resources
**Q**: Do you have access to ePassports for testing?  
**Required**: Multiple passport types  
**Alternative**: Innovatrics may provide test data  
**Risk**: MEDIUM - Cannot validate without real passports

---

## 📊 Effort Estimation

### Phase 1: Sandbox Implementation

| Task | Estimated Time | Complexity | Risk |
|------|---------------|------------|------|
| Version compatibility check | 4-8 hours | Medium | High |
| Dependencies & permissions | 1-2 hours | Low | Low |
| SDK initialization | 2-4 hours | Low | Medium |
| NFC UI screens | 8-16 hours | Medium | Low |
| ViewModel & business logic | 8-16 hours | Medium | Medium |
| Backend integration | 4-8 hours | Low | Low |
| Navigation & flow | 4-8 hours | Low | Low |
| Testing & debugging | 16-32 hours | High | High |
| Documentation | 4-8 hours | Low | Low |

**Total**: 51-102 hours (6-13 days)  
**Recommended**: 10 days with buffer for issues

---

## 🎯 Recommended Implementation Approach

### Week 1: Foundation & Version Migration
**Days 1-2**: Version compatibility investigation
- Review Innovatrics changelog 8.4.0 → 8.17.0
- Identify API changes
- Create migration plan

**Days 3-4**: Core integration
- Update dependencies to 8.17.0
- Add NFC library initialization
- Verify SDK loads correctly

**Day 5**: Permissions & basic UI
- Add NFC permissions
- Create basic NFC reading screen
- Test NFC availability detection

### Week 2: Implementation & Testing
**Days 6-8**: Full implementation
- Implement NFC reading flow
- Create view models and use cases
- Integrate with backend APIs
- Handle errors and edge cases

**Days 9-10**: Testing & refinement
- Test with real passports
- Fix bugs and issues
- Update documentation
- Prepare for deployment

---

## 🔗 Reference Links

1. **Innovatrics Documentation**
   - Main docs: https://developers.innovatrics.com/digital-onboarding/technical/remote/dot-android-nfc/latest/documentation/
   - Android samples: https://github.com/innovatrics/dot-android-sdk-samples
   - iOS samples (for reference): https://github.com/innovatrics/dot-ios-sdk-samples/tree/main/DotSdkSamples/Samples/NfcReading

2. **Project Files to Review**
   - `@/Users/luminsoft/StudioProjects/ekyc-android/eNROLL-sdk/build.gradle`
   - `@/Users/luminsoft/StudioProjects/ekyc-android/eNROLL-sdk/src/main/AndroidManifest.xml`
   - `@/Users/luminsoft/StudioProjects/ekyc-android/eNROLL-sdk/src/main/java/com/luminsoft/enroll_sdk/innovitices/InitializeDotSdkUseCase.kt`
   - `@/Users/luminsoft/StudioProjects/ekyc-android/eNROLL-sdk/src/main/java/com/luminsoft/enroll_sdk/features/national_id_confirmation/`

---

## ⚠️ Risk Mitigation

### High-Priority Risks

1. **Version Mismatch** (CRITICAL)
   - **Risk**: SDK crashes or undefined behavior
   - **Mitigation**: Upgrade to 8.17.0 first, test thoroughly
   - **Timeline**: Address before any NFC implementation

2. **License Limitations**
   - **Risk**: NFC module not included in license
   - **Mitigation**: Verify with Innovatrics immediately
   - **Timeline**: Before purchasing devices for testing

3. **Testing Availability**
   - **Risk**: Cannot test without real ePassports
   - **Mitigation**: Acquire test passports or use Innovatrics test data
   - **Timeline**: Before development starts

---

## 📋 Next Actions

### Immediate (Before Development)
1. ✅ Review this investigation report
2. ⏳ Check Innovatrics changelog for 8.4.0 → 8.17.0 breaking changes
3. ⏳ Verify NFC module in current Innovatrics license
4. ⏳ Confirm backend API can handle NFC data
5. ⏳ Decide on Phase 1 scope (minimal vs full)

### After Approval
1. Create Azure DevOps tasks based on checklist
2. Set up development environment
3. Begin version migration
4. Implement NFC feature
5. Test and deploy to sandbox

---

## 📌 Summary

**Current Status**: 
- ✅ Backend APIs ready
- ✅ Data models ready  
- ❌ NFC module commented out (outdated version)
- ❌ NFC permissions missing
- ❌ NFC UI not implemented

**Main Blocker**: Version compatibility (8.4.0 vs 8.17.0)

**Recommended Path**: 
1. Upgrade NFC to 8.17.0
2. Implement minimal NFC reading (MRZ + Photo)
3. Send to existing backend
4. Iterate based on feedback

**Estimated Timeline**: 10 days for Phase 1

---

**Generated**: February 8, 2026  
**Next Review**: After version compatibility check
