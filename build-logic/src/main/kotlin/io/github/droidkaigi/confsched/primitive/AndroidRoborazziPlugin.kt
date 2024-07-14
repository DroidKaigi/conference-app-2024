package io.github.droidkaigi.confsched.primitive

import io.github.takahirom.roborazzi.RoborazziExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
class AndroidRoborazziPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("io.github.takahirom.roborazzi")
                apply("com.google.devtools.ksp")
            }
            android {
                testOptions {
                    unitTests {
                        all {
                            it.jvmArgs("-noverify")
                            it.systemProperties["robolectric.graphicsMode"] = "NATIVE"
                            it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                            it.maxParallelForks = Runtime.getRuntime().availableProcessors()
                        }
                    }
                }
            }
            project.extensions.getByType<RoborazziExtension>().apply {
                generateComposePreviewRobolectricTests {
                    enable.set(true)
                    packages.add("io.github.droidkaigi.confsched")
                }
            }
            dependencies {
                testImplementation(libs.library("androidxTestEspressoEspressoCore"))
                testImplementation(libs.library("junit"))
                testImplementation(libs.library("robolectric"))
                testImplementation(libs.library("androidxTestExtJunit"))
                testImplementation(libs.library("roborazzi"))
                testImplementation(libs.library("roborazziCompose"))
                testImplementation(libs.library("composablePreviewScanner"))
                testImplementation(libs.library("roborazziPreviewScannerSupport"))
            }
        }
    }
}
