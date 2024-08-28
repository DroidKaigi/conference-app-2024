# DroidKaigi2024 iOS App

Official iOS App for DroidKaigi 2024.

## Getting started

1. Setup "Requirements > Must"
2. `make bootstrap`

## Requirements

### Must

- Xcode 15.4 or higher

  - You can install via [Xcodes](https://www.xcodes.app/) (Recommended)

- JDK 17
  - You can install via [SDKMAN](https://sdkman.io)

### Option

- Homebrew
  - for Build output formatting

## Project structure

This iOS app's almost all of source code is located in Swift Package Manager source. This structure is inspired by [isowords](https://github.com/pointfreeco/isowords).
The feature modules in this iOS app are made independent of each other. (e.g. `TimetableFeature` shouldn't depend on `TimetableDetailFeature`. )

## For your information

- Now DroidKaigi 2024 iOS App supports only Xcode. Build and run from Android Studio may not work.

## Trouble shooting

### Case1: In Rosetta 2 environment, `$ make bootstrap` fails.

In an environment where Rosetta 2 is enabled, the following error may occur when executing the setup command.

```shell
Error: Cannot install under Rosetta 2 in ARM default prefix (/opt/homebrew)!
To rerun under ARM use:
    arch -arm64 brew install ...
To install under x86_64, install Homebrew into /usr/local.
Installing xcbeautify has failed!
Homebrew Bundle failed! 1 Brewfile dependency failed to install.
Check for differences in your Brewfile.lock.json!
```

In an environment where Rosetta 2 is enabled, you need to change the installation location of Homebrew to the following.

- 🙅‍♀️ `/opt/homebrew`
- 🙆‍♀️ `/usr/local`

### Case2: Couldn't build iOS project because of JVM version is incompatible.

When building an iOS project with Xcode, the error that the JVM version is different may occur as shown below.

```shell
FAILURE: Build failed with an exception.

* What went wrong:
A problem occurred configuring project ':app-android'.
> Could not resolve all dependencies for configuration ':app-android:classpath'.
   > Could not resolve project :build-logic.
     Required by:
         project :app-android
      > Dependency requires at least JVM runtime version 17. This build uses a Java 13 JVM.

* Try:
> Run this build using a Java 17 or newer JVM.
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
> Get more help at https://help.gradle.org.
```

Sometimes an error will occur if the JDK versions do not match. In the case of a mismatch error, it is necessary to explicitly set JAVA_HOME.

```shell
% echo $JAVA_HOME
/Users/[your-name]/.sdkman/candidates/java/current
```

You select Xcode > Settings > Locations and set your $JAVA_HOME path manually.

JFYI: https://qiita.com/nacho4d/items/6d04c9287c55c26fca95

### Case3: You are using x86_64 Mac and want to build the project

If you are using x86_64 Mac and want to build the project, you need to specify the architecture when building XCFramework.

```shell
./gradlew app-ios-shared:assembleSharedXCFramework -Papp.ios.shared.arch=x86_64 --no-configuration-cache
```

Then, you can build the project with Xcode.

### Case4: Want to build the project for the device

If you want to build the project for the device, you need to specify the architecture when building XCFramework.

```shell
./gradlew app-ios-shared:assembleSharedXCFramework -Papp.ios.shared.arch=arm64 --no-configuration-cache
```

Then, you can build the project with Xcode.
