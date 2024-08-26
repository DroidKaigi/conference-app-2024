package io.github.droidkaigi.confsched.primitive

import com.google.devtools.ksp.gradle.KspTaskNative
import io.github.droidkaigi.confsched.primitive.Arch.ALL
import io.github.droidkaigi.confsched.primitive.Arch.ARM
import io.github.droidkaigi.confsched.primitive.Arch.ARM_SIMULATOR_DEBUG
import io.github.droidkaigi.confsched.primitive.Arch.X86
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

@Suppress("unused")
class KmpKtorfitPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugin("kspGradlePlugin").pluginId)
                apply("de.jensklingenberg.ktorfit")
            }

            configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
                version = libs.library("ktorfitKsp").versionConstraint.requiredVersion
            }

            kotlin {
                sourceSets["commonMain"].apply {
                    dependencies {
                        implementation(libs.library("ktorfitLib"))
                    }
                }
            }

            dependencies {
                val iosConfigs = when (activeArch) {
                    ARM -> listOf(
                        "IosArm64",
                        "IosSimulatorArm64"
                    )

                    ARM_SIMULATOR_DEBUG -> listOf("IosSimulatorArm64")
                    X86 -> listOf("IosX64")
                    ALL -> listOf(
                        "IosArm64",
                        "IosX64",
                        "IosSimulatorArm64",
                    )
                }
                (listOf("CommonMainMetadata", "Android") + iosConfigs).forEach {
                    add("ksp$it", libs.library("ktorfitKsp"))
                }
            }

            // https://github.com/DroidKaigi/conference-app-2024/issues/485#issuecomment-2304251937
            tasks.withType<KspTaskNative>().configureEach {
                notCompatibleWithConfigurationCache("Configuration chache not supported for a system property read at configuration time")

            }
            tasks.withType<KotlinNativeLink>().configureEach {
                notCompatibleWithConfigurationCache("Configuration chache not supported for a system property read at configuration time")
            }
        }
    }
}
