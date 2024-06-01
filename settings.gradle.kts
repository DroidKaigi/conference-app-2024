pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev/")
        }
    }
}
rootProject.name = "conference-app-2024"
include(
    ":app-android",
    ":app-ios-shared",
    ":feature:main",
    ":feature:sessions",
    ":feature:contributors",
    ":core:designsystem",
    ":core:data",
    ":core:model",
    ":core:ui",
    ":core:testing",
    ":core:testing-manifest",
    ":core:common",
)
