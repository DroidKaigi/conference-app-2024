package io.github.droidkaigi.confsched.model

public data class MultiLangText(
    val jaTitle: String,
    val enTitle: String,
) {
    val currentLangTitle: String get() = getByLang(defaultLang())

    public fun getByLang(lang: Lang): String {
        return if (lang == Lang.JAPANESE) {
            jaTitle
        } else {
            enTitle
        }
    }

    public companion object
}
