plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.android")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.kmp.android.hilt")
    id("droidkaigi.primitive.detekt")
}

// https://github.com/DroidKaigi/conference-app-2024/issues/485#issuecomment-2304251937
// (KT-66568)[https://youtrack.jetbrains.com/issue/KT-66568/w-KLIB-resolver-The-same-uniquename...-found-in-more-than-one-library]
android.namespace = "io.github.droidkaigi.confsched.core.droidkaigiui"

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.animation.graphics.android)
            }
        }

        commonMain {
            dependencies {
                implementation(projects.core.model)
                implementation(projects.core.designsystem)
                implementation(projects.core.data)
                implementation(compose.materialIconsExtended)
                implementation(libs.kermit)
                api(projects.core.common)
                api(libs.coil)
                api(libs.coilNetwork)
                api(libs.kotlinxDatetime)
                implementation(libs.moleculeRuntime)
                implementation(libs.coreBundle)
            }
        }
    }
}
