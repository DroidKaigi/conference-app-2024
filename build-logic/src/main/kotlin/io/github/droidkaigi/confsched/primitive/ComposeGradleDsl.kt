package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
    extensions.configure<ComposeCompilerGradlePluginExtension>(block)
}

val Project.compose: ComposeExtension
    get() = extensions["compose"] as ComposeExtension

fun ComposeExtension.resources(block: ResourcesExtension.() -> Unit) {
    extensions.configure<ResourcesExtension>(block)
}
