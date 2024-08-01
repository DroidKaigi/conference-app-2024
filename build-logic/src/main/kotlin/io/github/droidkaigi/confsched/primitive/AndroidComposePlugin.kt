package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugin("composeCompiler").pluginId)
            }
            android {
                buildFeatures.compose = true
            }
            dependencies {
                implementation(platform(libs.library("composeBom")))
                implementation(libs.library("androidxCoreKtx"))
                implementation(libs.library("composeUi"))
                implementation(libs.library("rin"))
                implementation(libs.library("composeMaterial"))
                implementation(libs.library("composeMaterial3"))
                implementation(libs.library("composeUiToolingPreview"))
                implementation(libs.library("androidxLifecycleLifecycleRuntimeKtx"))
                implementation(libs.library("androidxActivityActivityCompose"))
                implementation(libs.library("lottieCompose"))
                testImplementation(libs.library("junit"))
                testImplementation(libs.library("androidxTestExtJunit"))
                testImplementation(libs.library("androidxTestEspressoEspressoCore"))
                testImplementation(libs.library("composeUiTestJunit4"))
                debugImplementation(libs.library("composeUiTooling"))
                debugImplementation(libs.library("composeUiTestManifest"))
                lintChecks(libs.library("composeLintCheck"))
            }
        }
    }
}
