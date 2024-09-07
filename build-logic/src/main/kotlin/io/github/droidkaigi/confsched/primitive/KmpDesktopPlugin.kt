package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

@Suppress("unused")
class KmpDesktopPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            with(pluginManager) {
                apply("droidkaigi.primitive.kmp")
                apply("droidkaigi.primitive.kmp.compose")
            }

            kotlin {
                jvm("desktop")

                with(sourceSets) {
                    getByName("desktopMain").apply {
                        dependencies {
                            implementation(compose.dependencies.desktop.currentOs)
                        }
                    }
                }
            }

            val compose = extensions["compose"] as org.jetbrains.compose.ComposeExtension
            compose.extensions.configure<org.jetbrains.compose.desktop.DesktopExtension> {
                application {
                    mainClass = "io.github.droidkaigi.confsched.DesktopKaigiAppKt"

                    nativeDistributions {
                        // TODO: set output formats of each platform
                        targetFormats(TargetFormat.Dmg, /*TargetFormat.Msi, TargetFormat.Deb*/)
                        packageName = "io.github.droidkaigi.confsched"
                        packageVersion = "1.0.0"

                        val iconsRoot = project.file("iconFiles")
                        macOS {
                            // TODO: set an icon file ( set a temporal icon currently. )
                            iconFile.set(iconsRoot.resolve("desktop-icon.png"))
                        }
                        // Setup Windows and Linux configuration if needed.
//            windows {
//                iconFile.set(iconsRoot.resolve("icon-windows.ico"))
//                menuGroup = "Compose Examples"
//                // see https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
//                upgradeUuid = "18159995-d967-4CD2-8885-77BFA97CFA9F"
//            }
//            linux {
//                iconFile.set(iconsRoot.resolve("icon-linux.png"))
//            }
                    }
                }
            }
        }
    }
}
