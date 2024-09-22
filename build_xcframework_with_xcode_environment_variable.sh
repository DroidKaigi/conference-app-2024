#!/bin/bash

# Check if SDK_NAME and ARCHS environment variables are set
if [ -z "$SDK_NAME" ] || [ -z "$ARCHS" ]; then
    echo "Error: SDK_NAME or ARCHS environment variable is not set"
    exit 1
fi

# Initialize the target architecture and configuration (debug vs. release)
target_arch=""
target_configuration=""

# Determine the target architecture based on SDK_NAME and ARCHS
if [[ "$SDK_NAME" == *"simulator"* ]]; then
    if [[ "$ARCHS" == *"arm64"* ]]; then
        target_arch="arm64SimulatorDebug"
    elif [[ "$ARCHS" == *"x86_64"* ]]; then
        target_arch="x86_64"
    fi
elif [[ "$SDK_NAME" == *"iphoneos"* ]] && [[ "$ARCHS" == *"arm64"* ]]; then
    target_arch="arm64"
fi

# Assign the correct framework to build based on Xcode's configuration (or build all on unknown values)
if [[ "$CONFIGURATION" == *"Debug"* ]]; then
  target_configuration="Debug"
elif [[ "$CONFIGURATION" == *"Release"* ]]; then
  target_configuration="Release"
fi

# Check if a valid target architecture was determined
if [ -z "$target_arch" ]; then
    echo "Error: Unable to determine target architecture from SDK_NAME=$SDK_NAME and ARCHS=$ARCHS"
    exit 1
fi

# Execute Gradle command with the determined architecture
echo "Building for architecture: $target_arch"
./gradlew assembleShared${target_configuration}XCFramework --no-configuration-cache -Papp.ios.shared.arch=$target_arch

echo "Build completed for $target_arch"
