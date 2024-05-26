package io.github.droidkaigi.confsched.primitive

import co.touchlab.skie.configuration.DefaultArgumentInterop
import co.touchlab.skie.configuration.FlowInterop
import co.touchlab.skie.configuration.SealedInterop
import co.touchlab.skie.configuration.SuspendInterop
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KmpSkiePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("co.touchlab.skie")
            }

            project.extensions.configure(co.touchlab.skie.plugin.configuration.SkieExtension::class.java) {
                features {
                    group {
                        coroutinesInterop.set(true)
                        SuspendInterop.Enabled(true)
                        FlowInterop.Enabled(true)
//                        DefaultArgumentInterop.Enabled(true)
                        SealedInterop.Enabled(true)
                    }
                }
            }
        }
    }
}
