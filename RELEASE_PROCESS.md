# 🚀 eNROLL SDK Release Process

This document describes the complete process to release a new version of eNROLL SDK to JitPack.

## 📋 Overview

**Architecture:**
- **Azure DevOps**: Contains ALL source code (private)
- **GitHub**: Contains ONLY release files (public, for JitPack)
- **JitPack**: Distributes SDK to clients via Maven

**GitHub Repository Structure (ONLY these files):**
```
eNROLL-Android/
├── README.md           # Installation guide
├── jitpack.yml         # JitPack build configuration
├── eNROLL-sdk-X.Y.Z.aar    # Pre-built SDK artifact
└── pom-X.Y.Z.xml       # Maven POM with dependencies
```

⚠️ **NEVER push source code to GitHub** - it's a public repository!

---

## 🔄 Release Steps

### Step 1: Update Version Numbers (Azure)

Update version in these files:

**1. `eNROLL-sdk/build.gradle`** (line ~153):
```gradle
version = 'X.Y.Z'
```

**2. `eNROLL-sdk/src/main/java/com/luminsoft/enroll_sdk/core/models/BuildInfo.kt`**:
```kotlin
const val SDK_VERSION = "X.Y.Z"
```

### Step 2: Build Release Artifacts

```bash
cd /Users/luminsoft/StudioProjects/ekyc-android

# Build AAR and generate POM
./gradlew :eNROLL-sdk:assembleRelease :eNROLL-sdk:publishToMavenLocal

# Copy artifacts with version naming
cp eNROLL-sdk/build/outputs/aar/eNROLL-sdk-release.aar ./eNROLL-sdk-X.Y.Z.aar
cp eNROLL-sdk/build/publications/release/pom-default.xml ./pom-X.Y.Z.xml
```

### Step 3: Commit to Azure

```bash
git add .
git commit -m "Release vX.Y.Z - [description]"
git push azure feature/your-branch
```

### Step 4: Update GitHub (Release Files Only)

**Option A: Use the release script (Recommended)**
```bash
./scripts/release.sh X.Y.Z
```

**Option B: Manual Process**

```bash
# Clone GitHub repo to temp folder
rm -rf /tmp/enroll-github-release
git clone https://github.com/LuminSoft/eNROLL-Android.git /tmp/enroll-github-release
cd /tmp/enroll-github-release

# Remove old version files (keep only README.md and jitpack.yml)
rm -f eNROLL-sdk-*.aar pom-*.xml

# Copy new version files
cp /Users/luminsoft/StudioProjects/ekyc-android/eNROLL-sdk-X.Y.Z.aar .
cp /Users/luminsoft/StudioProjects/ekyc-android/pom-X.Y.Z.xml .

# Commit and push
git add -A
git commit -m "Release vX.Y.Z - [description]"
git push origin master

# Create and push tag
git tag vX.Y.Z
git push origin vX.Y.Z
```

### Step 5: Create GitHub Release

1. Go to: https://github.com/LuminSoft/eNROLL-Android/releases/new
2. Select tag: `vX.Y.Z`
3. Title: `Release vX.Y.Z`
4. Description: Release notes
5. **Attach files:**
   - `eNROLL-sdk-X.Y.Z.aar`
   - `pom-X.Y.Z.xml`
6. Click **"Publish release"**

### Step 6: Verify JitPack

1. Go to: https://jitpack.io/#LuminSoft/eNROLL-Android/X.Y.Z
2. Wait for build to complete (2-5 minutes)
3. Status should show **"Get it"** (green)

---

## 🛠️ Quick Release Script

Use the automated script for faster releases:

```bash
# From project root
./scripts/release.sh 1.5.14
```

This script will:
1. Build the AAR and POM
2. Clone GitHub repo
3. Update release files
4. Commit and push
5. Create tag
6. Copy files to Desktop for GitHub Release creation

---

## ⚠️ Common Issues

### Issue: JitPack shows cached failed build
**Solution:** Bump version number (e.g., 1.5.13 → 1.5.14). JitPack cache is very persistent.

### Issue: Source code accidentally pushed to GitHub
**Solution:** Run cleanup script:
```bash
./scripts/cleanup-github.sh
```

### Issue: GitHub Release not found by JitPack
**Solution:** Ensure you created a **GitHub Release** (not just a tag) with the AAR and POM files attached.

---

## 📁 File Locations

| File | Location |
|------|----------|
| Source Code | Azure DevOps (private) |
| Built AAR | `eNROLL-sdk/build/outputs/aar/eNROLL-sdk-release.aar` |
| Generated POM | `eNROLL-sdk/build/publications/release/pom-default.xml` |
| Release Script | `scripts/release.sh` |

---

## 🔐 Security Reminders

- ❌ NEVER push source code to GitHub
- ❌ NEVER push `.env` or credential files
- ✅ GitHub should ONLY contain: README.md, jitpack.yml, AAR, POM
- ✅ All source code stays on Azure DevOps

---

## 📞 Troubleshooting

If something goes wrong:

1. Check JitPack build log: `https://jitpack.io/com/github/LuminSoft/eNROLL-Android/X.Y.Z/build.log`
2. Verify GitHub Release has files attached
3. Bump version if cache issues persist
4. Contact: [Team Contact]

---

**Last Updated:** December 30, 2024
