package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.kotlinAndroidOptions(block: KotlinAndroidProjectExtension.() -> Unit) {
    (this as ExtensionAware).extensions.configure(block)
}

fun DependencyHandlerScope.ksp(
    artifact: MinimalExternalModuleDependency,
) {
    add("ksp", artifact)
}

fun DependencyHandlerScope.kspTest(
    artifact: MinimalExternalModuleDependency,
) {
    add("kspTest", artifact)
}
