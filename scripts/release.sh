#!/bin/bash

# eNROLL SDK Release Script
# Usage: ./scripts/release.sh <version>
# Example: ./scripts/release.sh 1.5.14

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PROJECT_DIR="/Users/luminsoft/StudioProjects/ekyc-android"
GITHUB_REPO="https://github.com/LuminSoft/eNROLL-Android.git"
TEMP_DIR="/tmp/enroll-github-release"

# Check if version is provided
if [ -z "$1" ]; then
    echo -e "${RED}Error: Version number required${NC}"
    echo "Usage: ./scripts/release.sh <version>"
    echo "Example: ./scripts/release.sh 1.5.14"
    exit 1
fi

VERSION="$1"
AAR_FILE="eNROLL-sdk-${VERSION}.aar"
POM_FILE="pom-${VERSION}.xml"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  eNROLL SDK Release Script v${VERSION}${NC}"
echo -e "${BLUE}========================================${NC}"

# Step 1: Update version numbers
echo -e "\n${YELLOW}Step 1: Checking version numbers...${NC}"
echo "Please ensure you have updated:"
echo "  - eNROLL-sdk/build.gradle (version = '${VERSION}')"
echo "  - BuildInfo.kt (SDK_VERSION = \"${VERSION}\")"
read -p "Have you updated the version numbers? (y/n): " confirm
if [ "$confirm" != "y" ]; then
    echo -e "${RED}Please update version numbers first, then run this script again.${NC}"
    exit 1
fi

# Step 2: Build AAR and POM
echo -e "\n${YELLOW}Step 2: Building release artifacts...${NC}"
cd "$PROJECT_DIR"
./gradlew :eNROLL-sdk:assembleRelease :eNROLL-sdk:publishToMavenLocal

# Step 3: Copy artifacts with version naming
echo -e "\n${YELLOW}Step 3: Copying artifacts...${NC}"
cp eNROLL-sdk/build/outputs/aar/eNROLL-sdk-release.aar "./${AAR_FILE}"
cp eNROLL-sdk/build/publications/release/pom-default.xml "./${POM_FILE}"
echo -e "${GREEN}Created: ${AAR_FILE}${NC}"
echo -e "${GREEN}Created: ${POM_FILE}${NC}"

# Step 4: Clone GitHub repo
echo -e "\n${YELLOW}Step 4: Preparing GitHub repository...${NC}"
rm -rf "$TEMP_DIR"
git clone "$GITHUB_REPO" "$TEMP_DIR"
cd "$TEMP_DIR"

# Step 5: Update release files
echo -e "\n${YELLOW}Step 5: Updating release files...${NC}"
rm -f eNROLL-sdk-*.aar pom-*.xml
cp "${PROJECT_DIR}/${AAR_FILE}" .
cp "${PROJECT_DIR}/${POM_FILE}" .

# Step 6: Commit and push
echo -e "\n${YELLOW}Step 6: Committing changes...${NC}"
git add -A
git commit -m "Release v${VERSION}"
git push origin master

# Step 7: Create and push tag
echo -e "\n${YELLOW}Step 7: Creating tag v${VERSION}...${NC}"
git tag "v${VERSION}"
git push origin "v${VERSION}" 2>/dev/null || {
    echo -e "${YELLOW}Tag exists, deleting and recreating...${NC}"
    git push origin --delete "v${VERSION}"
    git push origin "v${VERSION}"
}

# Step 8: Copy files to Desktop for GitHub Release
echo -e "\n${YELLOW}Step 8: Copying files to Desktop...${NC}"
cp "${PROJECT_DIR}/${AAR_FILE}" ~/Desktop/
cp "${PROJECT_DIR}/${POM_FILE}" ~/Desktop/

# Done
echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}  Release v${VERSION} prepared successfully!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}FINAL STEP: Create GitHub Release manually${NC}"
echo ""
echo "1. Go to: https://github.com/LuminSoft/eNROLL-Android/releases/new"
echo "2. Select tag: v${VERSION}"
echo "3. Title: Release v${VERSION}"
echo "4. Attach files from Desktop:"
echo "   - ${AAR_FILE}"
echo "   - ${POM_FILE}"
echo "5. Click 'Publish release'"
echo ""
echo "After publishing, verify at:"
echo "  https://jitpack.io/#LuminSoft/eNROLL-Android/${VERSION}"
echo ""
