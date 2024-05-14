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
        .library(
            name: "TimetableFeature",
            targets: ["TimetableFeature"]
        ),
    ],
    dependencies: [
        .package(url: "https://github.com/pointfreeco/swift-composable-architecture.git", exact: "1.10.2"),
    ],
    targets: [
        .target(
            name: "App",
            dependencies: [
                "TimetableFeature",
                .kmpModule,
            ]
        ),
        .testTarget(
            name: "AppTests",
            dependencies: [.app]
        ),

        .target(
            name: "TimetableFeature",
            dependencies: [
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),

        // Please run ./gradlew app-ios-shared:assembleSharedXCFramework first
        .binaryTarget(name: "KmpModule", path: "build/shared.xcframework"),
    ]
)

extension Target.Dependency {
    static let app: Target.Dependency = "App"
    static let kmpModule: Target.Dependency = "KmpModule"
}
