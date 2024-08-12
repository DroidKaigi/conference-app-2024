plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.desktop")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.ui)
                api(compose.components.resources)
                api(compose.components.uiToolingPreview)
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
