# 🛡️ eNROLL SDK - Project Rules & Guidelines

> **CRITICAL**: This SDK is used by government ministries and enterprise clients for user onboarding (eKYC).
> Every change must be treated as high-risk and reviewed thoroughly.

---

## 📋 Project Classification

| Attribute | Value |
|-----------|-------|
| **Project Type** | Production SDK (AAR Library) |
| **Sensitivity Level** | HIGH (Government + Enterprise) |
| **Consumers** | Ministry clients, Enterprise apps |
| **Distribution** | JitPack (GitHub releases) |
| **Versioning** | Semantic Versioning (x.y.z) |

---

## 🚨 Mandatory Rules (Enforced on Every Change)

### 1. No Breaking Changes Without Migration Plan
- ❌ **NEVER** remove or rename public API methods
- ❌ **NEVER** change method signatures without deprecation cycle
- ❌ **NEVER** modify data classes that clients consume
- ✅ Add new methods alongside old ones
- ✅ Use `@Deprecated` annotation with migration instructions
- ✅ Maintain at least 2 versions of backward compatibility

### 2. Every PR/Change Must Include
- [ ] **Description**: What changed and why
- [ ] **Changelog Entry**: User-facing description
- [ ] **Risk Assessment**: Low/Medium/High with justification
- [ ] **Backward Compatibility Check**: Impact on existing clients
- [ ] **Test Verification**: Manual or automated test results
- [ ] **Azure task breakdown**: Add taks for every change with time and all things azure task need

### 3. Version Bump Requirements
| Change Type | Version Bump | Example |
|-------------|--------------|---------|
| Bug fix (no API change) | PATCH | 1.1.23 → 1.1.24 |
| New feature (backward compatible) | MINOR | 1.1.23 → 1.2.0 |
| Breaking change | MAJOR | 1.1.23 → 2.0.0 |

### 4. Security Requirements
- [ ] No hardcoded credentials or API keys
- [ ] No logging of sensitive data (PII, tokens, biometrics)
- [ ] All network calls over HTTPS only
- [ ] Encryption for stored sensitive data
- [ ] Biometric data never leaves device
- [ ] Certificate pinning for API calls

### 5. Code Quality Standards
- [ ] Follow Kotlin coding conventions
- [ ] No compiler warnings in production code
- [ ] All public APIs must have KDoc documentation
- [ ] No TODO comments in release code
- [ ] Proper error handling (no silent failures)

---

## 🏗️ Architecture Rules

### Module Boundaries
```
app/                    → Demo/Testing app (NOT part of SDK)
eNROLL-sdk/            → Main SDK library (shipped to clients)
  ├── sdk/             → Public entry points (eNROLL.kt)
  ├── core/            → Shared utilities, models, network
  ├── features/        → Feature modules (clean architecture)
  ├── innovitices/     → Biometric SDK integration
  └── ui_components/   → Reusable UI components
```

### Public API Surface (⚠️ HIGH RISK)
These files define the SDK contract - changes here affect ALL clients:

| File | Purpose | Risk Level |
|------|---------|------------|
| `eNROLL.kt` | Main SDK entry point | 🔴 CRITICAL |
| `EnrollCallback.kt` | Client callbacks | 🔴 CRITICAL |
| `EnrollMode.kt` | SDK modes enum | 🔴 CRITICAL |
| `EnrollEnvironment.kt` | Environment config | 🟡 HIGH |
| `AppColors.kt` | Theming interface | 🟢 MEDIUM |

### Internal Code (Lower Risk)
- Feature implementations
- UI components
- Network layer internals
- View models

---

## 📦 Release Process

### Pre-Release Checklist
- [ ] All tests passing
- [ ] No breaking changes (or migration plan ready)
- [ ] Version numbers updated in:
  - `app/build.gradle` (versionCode, versionName)
  - `eNROLL-sdk/build.gradle` (version)
  - `BuildInfo.kt` (SDK_VERSION)
- [ ] Changelog updated
- [ ] Release notes prepared
- [ ] Security review completed

### Release Steps
1. Create signed APK for testing
2. Upload to Firebase App Distribution
3. QA verification
4. Commit and push to GitHub
5. Create version tag (vX.Y.Z)
6. JitPack automatically publishes

---

## 🔍 Review Requirements

### For Every Change
1. **Impact Analysis**: What modules/files are affected?
2. **API Surface Check**: Does this change any public API?
3. **Backward Compatibility**: Will existing clients break?
4. **Security Review**: Any sensitive data handling?
5. **Performance Check**: Any memory/CPU concerns?

### Review Report Template
```markdown
## Change Summary
[Brief description]

## Files Changed
- [file1]: [what changed]
- [file2]: [what changed]

## Risk Assessment
**Level**: Low/Medium/High
**Reason**: [explanation]

## Backward Compatibility
**Impact**: None/Minor/Breaking
**Migration Required**: Yes/No
**Steps**: [if applicable]

## Recommendations
- [action items]
```

---

## 🚫 Forbidden Actions

1. ❌ Deleting public API methods without deprecation
2. ❌ Changing SDK initialization signature
3. ❌ Modifying callback interfaces
4. ❌ Exposing internal classes as public
5. ❌ Adding required parameters to existing methods
6. ❌ Changing enum values order or names
7. ❌ Hardcoding credentials or secrets
8. ❌ Logging sensitive user data
9. ❌ Disabling security features
10. ❌ Releasing without version bump

---

## 📞 Escalation

### When to Escalate
- Any change to public API surface
- Security-related modifications
- Performance-critical changes
- Changes affecting multiple features
- Unclear requirements or edge cases

### How to Escalate
1. Document the concern clearly
2. List options with pros/cons
3. Provide recommendation
4. Wait for explicit approval before proceeding

---

## 📝 Documentation Requirements

### Code Documentation
- All public classes: KDoc with description
- All public methods: KDoc with @param, @return, @throws
- Complex logic: Inline comments explaining "why"

### Project Documentation
- README.md: Setup and usage
- CHANGELOG.md: Version history
- PROJECT_RULES.md: This file
- ARCHITECTURE.md: System design (to be created)

---

## 🔐 Sensitive Modules (Extra Caution)

| Module | Sensitivity | Reason |
|--------|-------------|--------|
| Biometrics (face, fingerprint) | 🔴 CRITICAL | PII, privacy laws |
| Document scanning (ID, passport) | 🔴 CRITICAL | Government ID data |
| Network layer | 🟡 HIGH | API keys, tokens |
| Encryption utilities | 🟡 HIGH | Data protection |
| User data models | 🟡 HIGH | PII storage |

---

**Last Updated**: December 4, 2024
**Maintained By**: Development Team
**Review Frequency**: Before each release
