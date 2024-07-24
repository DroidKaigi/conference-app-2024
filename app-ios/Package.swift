// swift-tools-version: 5.10
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "app-ios",
    defaultLocalization: "en",
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
        .library(
            name: "TimetableDetailFeature",
            targets: ["TimetableDetailFeature"]
        ),
        .library(
            name: "AboutFeature",
            targets: ["AboutFeature"]
        ),
        .library(
            name: "FavoriteFeature",
            targets: ["FavoriteFeature"]
        ),
        .library(
            name: "StaffFeature",
            targets: ["StaffFeature"]
        ),
        .library(
            name: "SponsorFeature",
            targets: ["SponsorFeature"]
        ),
        .library(
            name: "ContributorFeature",
            targets: ["ContributorFeature"]
        ),
        .library(
            name: "CommonComponents",
            targets: ["CommonComponents"]
        ),
    ],
    dependencies: [
        .package(url: "https://github.com/pointfreeco/swift-composable-architecture.git", exact: "1.10.2"),
        .package(url: "https://github.com/firebase/firebase-ios-sdk.git", exact: "10.26.0"),
        .package(url: "https://github.com/cybozu/LicenseList.git", exact: "0.7.0"),
        .package(url: "https://github.com/SwiftGen/SwiftGenPlugin", from: "6.6.2"),
    ],
    targets: [
        .target(
            name: "App",
            dependencies: [
                .aboutFeature,
                .contributorFeature,
                .favoriteFeature,
                .staffFeature,
                .sponsorFeature,
                .timetableFeature,
                .timetableDetailFeature,
                .tca,
                .kmpClient,
                .licenseList,
            ]
        ),
        .testTarget(
            name: "AppTests",
            dependencies: [.app]
        ),

        .target(
            name: "KMPClient",
            dependencies: [
                .kmpModule,
                .firebaseAuth,
                .firebaseRemoteConfig,
                .tca,
            ]
        ),
        
        .target(
            name: "EventKitClient",
            dependencies: [
                .tca
            ]
        ),

        .target(
            name: "TimetableFeature",
            dependencies: [
                .kmpModule,
                .firebaseAuth,
                .firebaseRemoteConfig,
                .tca,
                .commonComponents,
            ]
        ),
        .testTarget(
            name: "TimetableTests",
            dependencies: [
                .app,
                .timetableFeature,
                .firebaseAuth,
                .firebaseRemoteConfig,
                .tca
            ]
        ),
        .target(
            name: "TimetableDetailFeature",
            dependencies: [
                .tca,
                .theme,
                .commonComponents,
                .kmpClient,
                .kmpModule,
                .eventKitClient,
            ]
        ),
        .testTarget(
            name: "TimetableDetailFeatureTests",
            dependencies: [.timetableDetailFeature, .tca, .kmpModule]
        ),
        .target(
            name: "AboutFeature",
            dependencies: [
                .tca,
                .commonComponents,
            ]
        ),
        .testTarget(
            name: "AboutFeatureTests",
            dependencies: [
                .aboutFeature,
                .tca
            ]
        ),
        .target(
            name: "FavoriteFeature",
            dependencies: [
                .tca,
                .kmpClient,
            ]
        ),
        .testTarget(
            name: "FavoriteFeatureTests",
            dependencies: [
                .favoriteFeature,
                .tca
            ]
        ),
        .target(
            name: "Theme",
            resources: [
                .process("Resources"),
                .process("swiftgen.yml"),
            ],
            plugins: [.plugin(name: "SwiftGenPlugin", package: "SwiftGenPlugin")]
        ),
        .target(
            name: "StaffFeature",
            dependencies: [ 
                .tca,
                .kmpClient,
                .theme,
                .commonComponents
            ]
        ),
        .testTarget(
            name: "StaffFeatureTests",
            dependencies: [
                .staffFeature,
                .tca
            ]
        ),
        .target(
            name: "SponsorFeature",
            dependencies: [
                .tca,
                .kmpClient,
                .theme
            ]
        ),
        .testTarget(
            name: "SponsorFeatureTests",
            dependencies: [
                .sponsorFeature,
                .tca
            ]
        ),
        .target(
            name: "ContributorFeature",
            dependencies: [ 
                .tca,
                .kmpClient,
            ]
        ),
        .testTarget(
            name: "ContributorFeatureTests",
            dependencies: [
                .contributorFeature,
                .tca
            ]
        ),
        .target(name: "CommonComponents", dependencies: [.theme, .kmpModule]),
        // Please run ./gradlew app-ios-shared:assembleSharedXCFramework first
        .binaryTarget(name: "KmpModule", path: "../app-ios-shared/build/XCFrameworks/debug/shared.xcframework"),
    ]
)

package.targets
    .filter { ![.system, .binary, .plugin, .macro].contains($0.type) }
    .forEach { target in
        var settings = SwiftSetting.allCases

        if target.name != "KMPClient" {
            // https://www.swift.org/documentation/concurrency/
            settings.append(.enableExperimentalFeature("StrictConcurrency"))
        }
        target.swiftSettings = settings
    }

extension Target.Dependency {
    static let app: Target.Dependency = "App"
    static let timetableDetailFeature: Target.Dependency = "TimetableDetailFeature"
    static let timetableFeature: Target.Dependency = "TimetableFeature"
    static let aboutFeature: Target.Dependency = "AboutFeature"
    static let favoriteFeature: Target.Dependency = "FavoriteFeature"
    static let staffFeature: Target.Dependency = "StaffFeature"
    static let sponsorFeature: Target.Dependency = "SponsorFeature"
    static let contributorFeature: Target.Dependency = "ContributorFeature"
    static let kmpModule: Target.Dependency = "KmpModule"
    static let kmpClient: Target.Dependency = "KMPClient"
    static let eventKitClient: Target.Dependency = "EventKitClient"
    static let theme: Target.Dependency = "Theme"
    static let commonComponents: Target.Dependency = "CommonComponents"

    static let firebaseAuth: Target.Dependency = .product(name: "FirebaseAuth", package: "firebase-ios-sdk")
    static let firebaseRemoteConfig: Target.Dependency = .product(name: "FirebaseRemoteConfig", package: "firebase-ios-sdk")
    static let tca: Target.Dependency = .product(name: "ComposableArchitecture", package: "swift-composable-architecture")
    static let licenseList: Target.Dependency = .product(name: "LicenseList", package: "LicenseList")
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
