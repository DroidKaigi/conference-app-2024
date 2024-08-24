import org.jetbrains.compose.ComposePlugin.CommonComponentsDependencies
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.kmp.skie")
    id("droidkaigi.primitive.kmp.roborazzi")
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
                        (this.target.name == "iosSimulatorArm64" && this.debuggable) || (this.target.name == "iosArm64" && this.debuggable)
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
                    export(CommonComponentsDependencies.resources)
                }
            }
        }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.model)
                api(projects.core.data)
                api(projects.core.droidkaigiui)
                api(projects.feature.main)
                api(projects.feature.sessions)
                api(projects.feature.eventmap)
                api(projects.feature.sponsors)
                api(projects.feature.settings)
                api(projects.feature.contributors)
                api(projects.feature.profilecard)
                api(projects.feature.about)
                api(projects.feature.staff)
                api(projects.feature.favorites)
                implementation(libs.kotlinxCoroutinesCore)
                implementation(libs.skieAnnotation)
            }
        }
        iosTest {
            dependencies {
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
                implementation(libs.roborazziIos)
            }
        }
    }
}
