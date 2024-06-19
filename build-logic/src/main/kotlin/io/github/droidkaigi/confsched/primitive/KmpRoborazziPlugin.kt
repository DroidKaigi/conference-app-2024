package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.kotlin.dsl.get

@Suppress("unused")
class KmpRoborazziPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("io.github.takahirom.roborazzi")
                apply("com.google.devtools.ksp")
            }
            if (plugins.hasPlugin("com.android.library")) {
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
            }
            kotlin {
                if (plugins.hasPlugin("com.android.library")) {
                    sourceSets.getByName("androidUnitTest") {
                        dependencies {
                            implementation(libs.library("androidxTestEspressoEspressoCore"))
                            implementation(libs.library("junit"))
                            implementation(libs.library("robolectric"))
                            implementation(libs.library("androidxTestExtJunit"))
                            implementation(libs.library("roborazzi"))
                            implementation(libs.library("roborazziCompose"))
                        }
                    }
                }
            }
        }
    }
}
