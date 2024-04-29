package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
//import co.touchlab.skie.configuration.FlowInterop

@Suppress("unused")
class KmpSkiePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("co.touchlab.skie")
            }

//            project.extensions.configure(co.touchlab.skie.plugin.configuration.SkieExtension::class.java) {
//            }
        }
    }
}
