# DroidKaigi2024 iOS App

Official iOS App for DroidKaigi 2024.

## Getting started

1. Setup "Requirements > Must"
2. `make open`

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
The feature modules in this iOS app are made independent of each other. (e.g. `TimetableFeature` shouldn't depend on `TiemtableDetailFeature`. )

### App Module

```mermaid

```

## For your information

- Now DroidKaigi 2024 iOS App supports only Xcode. Build and run from Android Studio may not work.


