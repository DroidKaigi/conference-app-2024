package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project

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
