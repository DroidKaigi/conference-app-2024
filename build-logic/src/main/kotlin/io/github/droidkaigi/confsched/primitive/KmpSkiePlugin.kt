package io.github.droidkaigi.confsched.primitive

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
                    // Workaround for the error of
                    // static member 'shared' cannot be used on instance of type 'Res'
                    // https://slack-chats.kotlinlang.org/t/18882065/should-skie-work-with-the-multiplatform-resources-functional#60c3f44d-5e92-430c-9f05-3e3b34bb30a0
                    rootProject.allprojects {
                        val project = this
                        val resClassName = "conference_app_2024" + project.path
                            .replace(":", ".")
                            .replace("-", "_") + ".generated.resources.Res"
                        group(resClassName) {
                            SuspendInterop.Enabled(false)
                        }
                    }
                }
            }
        }
    }
}
