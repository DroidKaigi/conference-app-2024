plugins {
    id("droidkaigi.primitive.kmp.desktop")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.designsystem)
                implementation(compose.ui)
            }
        }
        desktopMain {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinxCoroutinesSwing)
            }
        }
    }
}
