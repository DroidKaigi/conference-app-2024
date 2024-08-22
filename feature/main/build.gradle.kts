plugins {
    id("droidkaigi.convention.kmpfeature")
}

android.namespace = "io.github.droidkaigi.confsched.feature.main"
roborazzi.generateComposePreviewRobolectricTests.packages = listOf("io.github.droidkaigi.confsched.main")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.materialIconsExtended)
                implementation(projects.core.model)
                implementation(projects.core.designsystem)
                implementation(projects.core.droidkaigiui)
                implementation(libs.haze)
            }
        }
    }
}
