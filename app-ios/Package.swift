// swift-tools-version: 5.10
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "app-ios",
    platforms: [
        .iOS(.v17),
    ],
    products: [
        .library(
            name: "App",
            targets: ["App"]
        ),
    ],
    targets: [
        .target(
            name: "App",
            dependencies: [
                "KmpModule",
            ]
        ),
        .testTarget(
            name: "AppTests",
            dependencies: ["App"]
        ),
        // For debug
        // Please run ./gradlew app-ios-shared:linkDebugFrameworkIosSimulatorArm64 first
        .binaryTarget(name: "KmpModule", path: "../app-ios-shared/build/bin/iosSimulatorArm64/debugFramework/shared.framework"),
        // For release
        // Please run ./gradlew app-ios-shared:assembleSharedXCFramework first
        //   .binaryTarget(name: "KmpModule", path: "../app-ios-shared/build/XCFrameworks/release/shared.xcframework"),
    ]
)
