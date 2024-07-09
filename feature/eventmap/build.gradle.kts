plugins {
    id("droidkaigi.convention.kmpfeature")
}

android.namespace = "io.github.droidkaigi.confsched.feature.eventMap"
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.model)
                implementation(projects.core.ui)
                implementation(libs.kotlinxCoroutinesCore)
                implementation(projects.core.designsystem)
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
