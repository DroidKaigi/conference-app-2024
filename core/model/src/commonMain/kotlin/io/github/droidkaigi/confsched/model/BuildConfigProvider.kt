package io.github.droidkaigi.confsched.model

interface BuildConfigProvider {
    val versionName: String
    val debugBuild: Boolean
}
