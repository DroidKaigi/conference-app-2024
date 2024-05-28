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
            name: "AppExperiments",
            targets: ["AppExperiments"]
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
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .testTarget(
            name: "AppTests",
            dependencies: [.app]
        ),
        .target(
            name: "AppExperiments",
            dependencies: [
                .kmpModule,
            ]
        ),

        .target(
            name: "TimetableFeature",
            dependencies: [
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .testTarget(
            name: "TimetableTests",
            dependencies: [
                "TimetableFeature",
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),

        // Please run ./gradlew app-ios-shared:assembleSharedXCFramework first
        .binaryTarget(name: "KmpModule", path: "../app-ios-shared/build/XCFrameworks/debug/shared.xcframework"),
    ]
)

let swiftCommonSettings = SwiftSetting.allCases
    // https://www.swift.org/documentation/concurrency/
    + [.enableExperimentalFeature("StrictConcurrency")]

package.targets
    .filter { ![.system, .binary, .plugin, .macro].contains($0.type) }
    .forEach { target in
        var settings = target.swiftSettings ?? []
        settings.append(contentsOf: swiftCommonSettings)
        target.swiftSettings = settings
    }

extension Target.Dependency {
    static let app: Target.Dependency = "App"
    static let kmpModule: Target.Dependency = "KmpModule"
}

/// ref: https://github.com/treastrain/swift-upcomingfeatureflags-cheatsheet?tab=readme-ov-file#short
extension SwiftSetting {
    static let forwardTrailingClosures: Self = .enableUpcomingFeature("ForwardTrailingClosures")              // SE-0286, Swift 5.3,  SwiftPM 5.8+
    static let existentialAny: Self = .enableUpcomingFeature("ExistentialAny")                                // SE-0335, Swift 5.6,  SwiftPM 5.8+
    static let bareSlashRegexLiterals: Self = .enableUpcomingFeature("BareSlashRegexLiterals")                // SE-0354, Swift 5.7,  SwiftPM 5.8+
    static let conciseMagicFile: Self = .enableUpcomingFeature("ConciseMagicFile")                            // SE-0274, Swift 5.8,  SwiftPM 5.8+
    static let importObjcForwardDeclarations: Self = .enableUpcomingFeature("ImportObjcForwardDeclarations")  // SE-0384, Swift 5.9,  SwiftPM 5.9+
    static let disableOutwardActorInference: Self = .enableUpcomingFeature("DisableOutwardActorInference")    // SE-0401, Swift 5.9,  SwiftPM 5.9+
    static let deprecateApplicationMain: Self = .enableUpcomingFeature("DeprecateApplicationMain")            // SE-0383, Swift 5.10, SwiftPM 5.10+
    static let isolatedDefaultValues: Self = .enableUpcomingFeature("IsolatedDefaultValues")                  // SE-0411, Swift 5.10, SwiftPM 5.10+
    static let globalConcurrency: Self = .enableUpcomingFeature("GlobalConcurrency")                          // SE-0412, Swift 5.10, SwiftPM 5.10+
}

extension SwiftSetting: CaseIterable {
    public static var allCases: [Self] {[.forwardTrailingClosures, .existentialAny, .bareSlashRegexLiterals, .conciseMagicFile, .importObjcForwardDeclarations, .disableOutwardActorInference, .deprecateApplicationMain, .isolatedDefaultValues, .globalConcurrency]}
}
