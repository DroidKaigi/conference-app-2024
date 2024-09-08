plugins {
    id("droidkaigi.primitive.kmp.desktop")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinxCoroutinesCore)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.kotlinxCoroutinesSwing)
            }
        }
    }
}
