package io.github.droidkaigi.confsched.model

public enum class FontFamily(
    val displayName: String,
    val fileName: String? = null,
) {
    DotGothic16Regular(
        displayName = "DotGothic16",
        fileName = "dot_gothic16_regular",
    ),
    SystemDefault(
        displayName = "System Default",
    ),
}
