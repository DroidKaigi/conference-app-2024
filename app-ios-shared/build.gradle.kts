import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
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

                    var added = false
                    if (project.properties["app.ios.shared.debug"] == "true") {
                        if (this.target.name == "iosSimulatorArm64" && this.debuggable) {
                            xcf.add(this)
                            added = true
                        }
                    } else {
                        xcf.add(this)
                        added = true
                    }
                    if (added) {
                        logger.lifecycle("framework '${this.name} ${this.target}' will be in XCFramework")
                    }

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
                api(projects.feature.contributors)
                api(projects.feature.sessions)
                implementation(libs.kotlinxCoroutinesCore)
            }
        }
    }
}

