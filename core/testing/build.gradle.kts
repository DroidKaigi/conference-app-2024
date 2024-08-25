plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.android")
    id("droidkaigi.primitive.kmp.android.hilt")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.detekt")
}

android.namespace = "io.github.droidkaigi.confsched.core.testing"

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(projects.core.model)
                implementation(projects.core.designsystem)
                implementation(projects.core.data)
                implementation(projects.core.droidkaigiui)
                implementation(projects.feature.main)
                implementation(projects.feature.sessions)
                implementation(projects.feature.profilecard)
                implementation(projects.feature.about)
                implementation(projects.feature.staff)
                implementation(projects.feature.sponsors)
                implementation(projects.feature.settings)
                implementation(projects.feature.favorites)
                implementation(projects.feature.eventmap)
                implementation(projects.feature.contributors)
                implementation(libs.daggerHiltAndroidTesting)
                implementation(libs.roborazzi)
                implementation(libs.kermit)
                implementation(libs.coilTest)
                api(projects.core.testingManifest)
                api(libs.composeNavigation)
                api(libs.roborazziRule)
                api(libs.roborazziCompose)
                api(libs.robolectric)
                api(libs.composeUiTestJunit4)
                implementation(libs.composeMaterialWindowSize)
                implementation(libs.composablePreviewScanner)
                implementation(libs.composablePreviewScannerJvm)
                implementation(libs.roborazziPreviewScannerSupport)
            }

        }
    }
}
