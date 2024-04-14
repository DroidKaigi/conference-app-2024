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
                "KmmModule",
            ]
        ),
        .testTarget(
            name: "AppTests",
            dependencies: ["App"]
        ),
        .binaryTarget(name: "KmmModule", path: "build/shared.xcframework"),
    ]
)
