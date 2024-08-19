package io.github.droidkaigi.confsched.model

public enum class FontFamily(
    val displayName: String,
    val fileName: String,
) {
    DotGothic16Regular(
        displayName = "DotGothic",
        fileName = "dot_gothic16_regular",
    ),
    NotoSansJPRegular(
        displayName = "NotoSans",
        fileName = "noto_sans_jp_regular",
    ),
}
