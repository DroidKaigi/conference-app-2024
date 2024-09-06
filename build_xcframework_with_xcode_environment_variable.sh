#!/bin/bash

# Check if SDK_NAME and ARCHS environment variables are set
if [ -z "$SDK_NAME" ] || [ -z "$ARCHS" ]; then
    echo "Error: SDK_NAME or ARCHS environment variable is not set"
    exit 1
fi

# Initialize the target architecture
target_arch=""

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

# Check if a valid target architecture was determined
if [ -z "$target_arch" ]; then
    echo "Error: Unable to determine target architecture from SDK_NAME=$SDK_NAME and ARCHS=$ARCHS"
    exit 1
fi

# Execute Gradle command with the determined architecture
echo "Building for architecture: $target_arch"
./gradlew assembleSharedXCFramework --no-configuration-cache -Papp.ios.shared.arch=$target_arch

echo "Build completed for $target_arch"
