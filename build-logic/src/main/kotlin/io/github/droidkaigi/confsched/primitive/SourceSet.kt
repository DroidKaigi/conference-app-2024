package io.github.droidkaigi.confsched.primitive

import org.gradle.api.Project
import java.util.Properties

enum class Arch(val arch: String?) {
    ARM("arm64"),
    ARM_SIMULATOR_DEBUG("arm64SimulatorDebug"),
    X86("x86_64"),
    ALL(null),
    ;

    companion object {
        fun findByArch(arch: String?): Arch {
            println("input arch: $arch")
            return values().firstOrNull { it.arch == arch } ?: ALL
        }
    }
}

val Project.activeArch
    get() = Arch.findByArch(
        rootProject.layout.projectDirectory.file("local.properties").asFile.takeIf { it.exists() }
            ?.let {
                Properties().apply {
                    load(it.reader(Charsets.UTF_8))
                }.getProperty("arch")
            } ?: properties["app.ios.shared.arch"] as? String ?: System.getenv("arch")
    )
