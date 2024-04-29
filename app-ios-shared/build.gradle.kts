import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.kmp.skie")
}

kotlin {
    val frameworkName = "shared"
    val xcf = XCFramework(frameworkName)

    targets.filterIsInstance<KotlinNativeTarget>()
        .forEach {
            it.binaries {
                framework {
                    baseName = frameworkName
                    // compose for iOS(skiko) needs to be static library
                    isStatic = true
                    embedBitcode(BitcodeEmbeddingMode.DISABLE)
                    binaryOption("bundleId", "io.github.droidkaigi.confsched.shared")
                    binaryOption("bundleVersion", version.toString())
                    binaryOption("bundleShortVersionString", version.toString())

                    val includeToXCF = if (project.properties["app.ios.shared.debug"] == "true") {
                        this.target.name == "iosSimulatorArm64" && this.debuggable
                    } else {
                        true
                    }
                    if (includeToXCF) {
                        xcf.add(this)
                        logger.lifecycle("framework '${this.name} ${this.target}' will be in XCFramework")
                    }

                    export(projects.feature.main)
                    export(projects.feature.sessions)
                    export(projects.feature.contributors)
                    export(projects.core.model)
                    export(projects.core.data)
                }
            }
        }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.model)
                api(projects.core.data)
                api(projects.core.ui)
                api(projects.feature.main)
                api(projects.feature.sessions)
                api(projects.feature.contributors)
                implementation(libs.kotlinxCoroutinesCore)
            }
        }
    }
}
