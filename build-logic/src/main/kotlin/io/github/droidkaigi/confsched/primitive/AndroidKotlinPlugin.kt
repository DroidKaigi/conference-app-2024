package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11

@Suppress("unused")
class AndroidKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
            }
            tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
                compilerOptions {
                    jvmTarget.set(JVM_11)
                }
            }

            android {
                kotlinAndroidOptions {
                    compilerOptions {
                        // Treat all Kotlin warnings as errors (disabled by default)
                        allWarningsAsErrors.set(properties["warningsAsErrors"] as? Boolean ?: false)

                        freeCompilerArgs.addAll(listOf(
//                                "-opt-in=kotlin.RequiresOptIn",
                            // Enable experimental coroutines APIs, including Flow
//                                 "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                            "-Xcontext-receivers"
                        ))

                        jvmTarget.set(JVM_11)
                    }
                }
            }
            dependencies {
                implementation(libs.library("kotlinxCoroutinesCore"))
                // Fix https://youtrack.jetbrains.com/issue/KT-41821
                implementation(libs.library("kotlinxAtomicfu"))
            }
        }
    }
}
