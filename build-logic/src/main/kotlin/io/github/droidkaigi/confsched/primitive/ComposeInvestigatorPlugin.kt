package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project

// This plugin is used for debugging re-composition.
// Therefore, it may appear to be unused, but it is sometimes used.
@Suppress("unused")
class ComposeInvestigatorPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
//                apply("land.sungbin.composeinvestigator")
            }
        }
    }
}
