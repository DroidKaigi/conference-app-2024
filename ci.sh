#!/bin/sh
# this is now in GHA
# ./gradlew testDebugUnitTest
./gradlew detekt --auto-correct
./gradlew assembleSharedXCFramework --no-configuration-cache
./gradlew iosSimulatorArm64Test --no-configuration-cache

# iOS build (Xcode 15.3)
cd app-ios
PROJECT=App/App.xcodeproj
PLATFORM_IOS="iOS Simulator,name=iPhone 15 Pro,OS=17.4"

set -o pipefail && xcodebuild build \
-project $PROJECT \
-scheme "DroidKaigi2024App" \
-configuration Debug \
-destination platform="$PLATFORM_IOS"
