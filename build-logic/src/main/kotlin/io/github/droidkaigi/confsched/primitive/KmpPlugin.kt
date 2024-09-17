package io.github.droidkaigi.confsched.primitive

import com.google.devtools.ksp.gradle.KspTaskNative
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class KmpPlugin : Plugin<Project> {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")

                withPlugin(libs.plugin("kspGradlePlugin").pluginId) {
                    tasks.withType<KspTaskNative>().configureEach {
                        notCompatibleWithConfigurationCache("Configuration chache not supported for a system property read at configuration time")
                    }
                }
            }
            tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
                compilerOptions.jvmTarget.set(JVM_11)
            }
            tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>().configureEach {
                notCompatibleWithConfigurationCache("Configuration chache not supported for a system property read at configuration time")
            }
            kotlin {
                with(sourceSets) {
                    commonMain {
                        dependencies {
                            implementation(libs.findLibrary("kermit").get())
                        }
                    }
                }
                compilerOptions {
                    freeCompilerArgs.addAll(
                        listOf(
                            "-Xcontext-receivers",
                            "-Xexpect-actual-classes",
                        )
                    )
                }
            }
        }
    }
}

fun Project.kotlin(action: KotlinMultiplatformExtension.() -> Unit) {
    extensions.configure(action)
}
