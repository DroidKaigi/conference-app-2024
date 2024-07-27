package io.github.droidkaigi.confsched.primitive

import io.github.takahirom.roborazzi.RoborazziExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
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
                        all { test ->
                            test.systemProperties["robolectric.logging.enabled"] = "true"
                            test.jvmArgs("-noverify")
                            test.systemProperties["robolectric.graphicsMode"] = "NATIVE"
                            test.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                            test.maxParallelForks = Runtime.getRuntime().availableProcessors()
                            test.testLogging {
                                events.addAll(listOf(STARTED, PASSED, SKIPPED, FAILED))
                                showCauses = true
                                showExceptions = true
                                showStandardStreams = true
                                exceptionFormat = FULL
                            }
                        }
                    }
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
                testImplementation(libs.library("composablePreviewScannerJvm"))
                testImplementation(libs.library("roborazziPreviewScannerSupport"))
            }
        }
    }
}
