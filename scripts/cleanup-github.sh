#!/bin/bash

# eNROLL SDK GitHub Cleanup Script
# Use this to remove source code from GitHub if accidentally pushed
# Usage: ./scripts/cleanup-github.sh

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

GITHUB_REPO="https://github.com/LuminSoft/eNROLL-Android.git"
TEMP_DIR="/tmp/enroll-github-cleanup"

echo -e "${YELLOW}⚠️  GitHub Cleanup Script${NC}"
echo "This will remove ALL content from GitHub and restore release-only structure."
read -p "Are you sure? (type 'yes' to confirm): " confirm
if [ "$confirm" != "yes" ]; then
    echo "Cancelled."
    exit 1
fi

# Create fresh repo
rm -rf "$TEMP_DIR"
mkdir -p "$TEMP_DIR"
cd "$TEMP_DIR"
git init
git remote add origin "$GITHUB_REPO"

# Create minimal files
cat > README.md << 'EOF'
# eNROLL

This document is a guide for eNROLL Android SDK.

## INSTALLATION

Add JitPack repository to your project's `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:

```gradle
dependencies {
    implementation 'com.github.LuminSoft:eNROLL-Android:VERSION'
}
```

## LICENSE

Copyright © LuminSoft. All rights reserved.
EOF

cat > jitpack.yml << 'EOF'
jdk:
  - openjdk17

install:
  - |
    export RAW_VERSION="${VERSION}"
    export CLEAN_VERSION="${RAW_VERSION#v}"
    export AAR_FILE="eNROLL-sdk-${CLEAN_VERSION}.aar"
    export POM_FILE="pom-${CLEAN_VERSION}.xml"
    echo "RAW_VERSION=$RAW_VERSION CLEAN_VERSION=$CLEAN_VERSION"
    echo "Downloading AAR: $AAR_FILE"
    curl -fL --retry 3 -o "$AAR_FILE" "https://github.com/LuminSoft/eNROLL-Android/releases/download/${RAW_VERSION}/${AAR_FILE}"
    ls -lh "$AAR_FILE"
    echo "Downloading POM: $POM_FILE"
    curl -fL --retry 3 -o "$POM_FILE" "https://github.com/LuminSoft/eNROLL-Android/releases/download/${RAW_VERSION}/${POM_FILE}"
    ls -lh "$POM_FILE"
    echo "Installing with POM dependencies..."
    mvn install:install-file -Dfile="$AAR_FILE" -DpomFile="$POM_FILE"
EOF

git add -A
git commit -m "Cleanup: Remove source code, restore release-only structure"
git push origin main:master --force

echo -e "${GREEN}✅ GitHub cleaned! Only README.md and jitpack.yml remain.${NC}"
echo ""
echo "Next steps:"
echo "1. Run release script to add new version: ./scripts/release.sh X.Y.Z"
