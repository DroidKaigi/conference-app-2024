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
                    // Workaround for the error of
                    // static member 'shared' cannot be used on instance of type 'Res'
                    // https://slack-chats.kotlinlang.org/t/18882065/should-skie-work-with-the-multiplatform-resources-functional#60c3f44d-5e92-430c-9f05-3e3b34bb30a0
                    val resourceClasses = listOf(
                        "conference_app_2024.feature.about.generated.resources.Res",
                        "conference_app_2024.feature.contributors.generated.resources.Res",
                        "conference_app_2024.feature.eventmap.generated.resources.Res",
                        "conference_app_2024.feature.main.generated.resources.Res",
                        "conference_app_2024.feature.profilecard.generated.resources.Res",
                        "conference_app_2024.feature.sessions.generated.resources.Res",
                        "conference_app_2024.feature.staff.generated.resources.Res",
                        "conference_app_2024.core.designsystem.generated.resources.Res",
                        "conference_app_2024.core.data.generated.resources.Res",
                        "conference_app_2024.core.model.generated.resources.Res",
                        "conference_app_2024.appiosshared.generated.resources.Res",
                    )
                    resourceClasses.forEach { resourceClass ->
                        group(resourceClass) {
                            SuspendInterop.Enabled(false)
                        }
                    }

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
