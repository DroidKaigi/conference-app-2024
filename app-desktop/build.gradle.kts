plugins {
    id("droidkaigi.primitive.kmp.desktop")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinxCoroutinesCore)
                implementation(compose.ui)
                implementation(compose.components.uiToolingPreview)
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
