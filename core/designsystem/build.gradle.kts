import com.github.takahirom.roborazzi.ExperimentalRoborazziApi

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.android")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.kmp.android.hilt")
    id("droidkaigi.primitive.kmp.roborazzi")
    id("droidkaigi.primitive.detekt")
}

android.namespace = "io.github.droidkaigi.confsched.core.designsystem"
@OptIn(ExperimentalRoborazziApi::class)
roborazzi.generateComposePreviewRobolectricTests.packages = listOf("io.github.droidkaigi.confsched.designsystem")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.components.uiToolingPreview)
            }
        }

        androidMain {
            dependencies {
                api(compose.preview)
                // Fix https://youtrack.jetbrains.com/issue/KT-41821
                implementation(libs.kotlinxAtomicfu)
            }
        }
    }
}

dependencies {
    implementation(libs.kermit)
    lintChecks(libs.composeLintCheck)
}
