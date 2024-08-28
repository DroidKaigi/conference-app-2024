plugins {
    id("droidkaigi.convention.kmpfeature")
    id("droidkaigi.primitive.kmp.serialization")
}

android.namespace = "io.github.droidkaigi.confsched.feature.eventmap"
roborazzi.generateComposePreviewRobolectricTests.packages = listOf("io.github.droidkaigi.confsched.eventmap")
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.model)
                implementation(projects.core.droidkaigiui)
                implementation(projects.core.designsystem)

                implementation(libs.kotlinxCoroutinesCore)
                implementation(libs.kotlinSerializationJson)
                implementation(libs.moleculeRuntime)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }
        androidUnitTest {
            dependencies {
                implementation(projects.core.testing)
            }
        }
    }
}
