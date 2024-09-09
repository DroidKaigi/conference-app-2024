import org.gradle.api.tasks.Delete

plugins {
    alias(libs.plugins.androidGradlePlugin) apply false
    alias(libs.plugins.androidGradleLibraryPlugin) apply false
    alias(libs.plugins.kotlinGradlePlugin) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinxKover) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

buildscript {
    configurations.all {
        resolutionStrategy.eachDependency {
            when {
                requested.name == "javapoet" -> useVersion("1.13.0")
            }
        }
    }
}

allprojects {
    configurations.all {
        resolutionStrategy {
            // When the fixed version is released, remove this block
            // https://issuetracker.google.com/issues/341880461
            force("androidx.compose.foundation:foundation:1.8.0-alpha01")
        }
    }
}
