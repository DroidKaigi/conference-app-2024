#!/bin/sh

cd "$CI_PRIMARY_REPOSITORY_PATH"

# Enalbe skip plugin validation
defaults write com.apple.dt.Xcode IDESkipPackagePluginFingerprintValidatation -bool YES
defaults write com.apple.dt.Xcode IDESkipMacroFingerprintValidation -bool YES

SOURCE_PACKAGES_PATH="$CI_PRIMARY_REPOSITORY_PATH/app-ios/SourcePackages"
xcodebuild -resolvePackageDependencies -project $CI_PROJECT_FILE_PATH -scheme DroidKaigi2024App -derivedDataPath $CI_DERIVED_DATA_PATH -clonedSourcePackagesDirPath $SOURCE_PACKAGES_PATH

# workaround for xcode cloud
brew install mint

mint install cybozu/LicenseList
mint install SwiftGen/SwiftGen

mint run LicenseList "$CI_PRIMARY_REPOSITORY_PATH/app-ios/Sources/App/Resources" $SOURCE_PACKAGES_PATH

# generate code
export DERIVED_SOURCES_DIR="$CI_PRIMARY_REPOSITORY_PATH/app-ios/Sources/Theme/"
mint run SwiftGen --config "$CI_PRIMARY_REPOSITORY_PATH/app-ios/Sources/Theme/swiftgen.yml"
