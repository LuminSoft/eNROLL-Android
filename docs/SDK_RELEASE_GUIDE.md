# eNROLL Android SDK - Release Guide

## Overview

This document describes how to release a new version of the eNROLL Android SDK to GitHub and JitPack.

---

## Prerequisites

- Access to GitHub repository: https://github.com/LuminSoft/eNROLL-Android
- Access to Azure repository: https://dev.azure.com/ExcelSystems/eKYC/_git/ekyc-android.git
- Android Studio with the project configured
- Git configured with both remotes (`origin` for GitHub, `azure` for Azure)

---

## Release Process

### Step 1: Update SDK Version

Update the version in `eNROLL-sdk/build.gradle`:

```gradle
afterEvaluate {
    publishing {
        publications {
            release(MavenPublication) {
                from components.release
                groupId = 'com.luminsoft'
                artifactId = 'eNROLL'
                version = 'X.Y.Z'  // <-- Update this
            }
        }
    }
}
```

### Step 2: Build the AAR

```bash
cd /path/to/ekyc-android
./gradlew clean :eNROLL-sdk:assembleRelease
```

The AAR will be generated at:
```
eNROLL-sdk/build/outputs/aar/eNROLL-sdk-release.aar
```

### Step 3: Copy AAR with Version Name

```bash
cp eNROLL-sdk/build/outputs/aar/eNROLL-sdk-release.aar ~/Downloads/eNROLL-sdk-X.Y.Z.aar
```

### Step 4: Push Source Code to Azure

```bash
# Ensure you're on the correct branch
git checkout feature/your-branch

# Commit your changes
git add .
git commit -m "Your commit message"

# Push to Azure
git push azure feature/your-branch
```

### Step 5: Update GitHub Master Branch

The GitHub `master` branch contains only release files (not full source):

```bash
# Stash any local changes
git stash

# Switch to master
git checkout master

# Ensure it matches GitHub
git pull origin master

# The master branch should only contain:
# - README.md
# - jitpack.yml
# - eNROLL-sdk-X.Y.Z.aar (the new AAR)
```

### Step 6: Update jitpack.yml (if needed)

The `jitpack.yml` should look like this:

```yaml
jdk:
  - openjdk17

install:
  - |
    export RAW_VERSION="${VERSION}"
    export CLEAN_VERSION="${RAW_VERSION#v}"
    export FILE="eNROLL-sdk-${CLEAN_VERSION}.aar"
    echo "RAW_VERSION=$RAW_VERSION CLEAN_VERSION=$CLEAN_VERSION FILE=$FILE"
    curl -fL --retry 3 -o "$FILE" "https://github.com/LuminSoft/eNROLL-Android/releases/download/${RAW_VERSION}/${FILE}"
    ls -lh "$FILE"
    mvn install:install-file -Dfile="$FILE" -DgroupId=com.github.LuminSoft -DartifactId=eNROLL-Android -Dversion="${CLEAN_VERSION}" -Dpackaging=aar
```

### Step 7: Commit and Push to GitHub

```bash
git add .
git commit -m "Release vX.Y.Z - Description"
git push origin master
```

### Step 8: Create Git Tag

```bash
git tag X.Y.Z
git push origin X.Y.Z
```

### Step 9: Create GitHub Release

1. Go to: https://github.com/LuminSoft/eNROLL-Android/releases/new
2. **Choose tag**: Select `X.Y.Z`
3. **Release title**: `vX.Y.Z - Description`
4. **Attach binary**: Upload `eNROLL-sdk-X.Y.Z.aar`
5. **Pre-release**: Check if it's a pre-release version
6. Click **Publish release**

### Step 10: Verify JitPack Build

1. Go to: https://jitpack.io/#LuminSoft/eNROLL-Android/X.Y.Z
2. Wait for the build to complete
3. Verify the status shows green "Get it" button
4. Test the dependency in a project:

```gradle
implementation 'com.github.LuminSoft:eNROLL-Android:X.Y.Z'
```

---

## Verification Commands

### Check AAR exists on JitPack:
```bash
curl -sI "https://jitpack.io/com/github/LuminSoft/eNROLL-Android/X.Y.Z/eNROLL-Android-X.Y.Z.aar" | head -1
# Should return: HTTP/2 200
```

### Check build log:
Visit: https://jitpack.io/#LuminSoft/eNROLL-Android/X.Y.Z (click on Log icon)

---

## Troubleshooting

### "No build artifacts found"
- Ensure the AAR is attached to the GitHub Release
- Check that the AAR filename matches: `eNROLL-sdk-X.Y.Z.aar`
- Verify `jitpack.yml` installs to `~/.m2/repository` (default)

### "File not found" during JitPack build
- The AAR must be attached to the GitHub Release as a binary
- Check the download URL in `jitpack.yml` matches the release structure

### Build timeout
- Create a new tag (X.Y.Z+1) and try again
- JitPack sometimes caches failed builds

---

## Repository Structure

### GitHub (`origin`) - master branch
```
├── README.md
├── jitpack.yml
└── eNROLL-sdk-X.Y.Z.aar
```

### Azure (`azure`) - feature branches
```
├── app/                    # Demo app
├── eNROLL-sdk/            # SDK source code
├── build.gradle
├── settings.gradle
└── ...
```

---

## Version History

| Version | Date       | Description                                    |
|---------|------------|------------------------------------------------|
| 1.5.10  | 2024-12-09 | Orange compatibility (Kotlin 1.9.25, Lottie 6.6.2, Material3 1.3.1, Compose 1.7.4) |
| 1.5.01  | 2024-10-30 | Previous stable release                        |

---

## Contact

For questions about the release process, contact the LuminSoft Development Team.
