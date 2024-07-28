package io.github.droidkaigi.confsched.primitive

import io.github.droidkaigi.confsched.convention.buildComposeMetricsParameters
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class KmpComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
            }
            if (plugins.hasPlugin("com.android.library")) {
                android {
                    buildFeatures.compose = true
                    composeOptions {
                        kotlinCompilerExtensionVersion = libs.version("composeCompiler")
                    }
                }
            }
            val compose = extensions.get("compose") as org.jetbrains.compose.ComposeExtension
            compose.extensions.configure<ResourcesExtension> {
                publicResClass = false
            }
            kotlin {
                with(sourceSets) {
                    getByName("commonMain").apply {
                        dependencies {
                            implementation(compose.dependencies.runtime)
                            implementation(compose.dependencies.foundation)
                            implementation(compose.dependencies.material3)
                            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                            implementation(compose.dependencies.components.resources)
                            implementation(libs.library("rin"))
                            implementation(libs.library("composeNavigation"))
                            implementation(libs.library("composeMaterialWindowSize"))
                            implementation(libs.library("androidxLifecycleViewModel"))
                            implementation(libs.library("androidxLifecycleViewModelCompose"))
                            implementation(libs.library("androidxLifecycleCommon"))
                        }
                    }
                    find { it.name == "androidMain" }
                        ?.apply {
                            dependencies {
                                implementation(libs.library("androidxActivityActivityCompose"))
                                implementation(libs.library("composeUiToolingPreview"))
                            }
                        }
                }
            }

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    freeCompilerArgs = freeCompilerArgs + buildComposeMetricsParameters()
                }
            }
        }
    }
}
