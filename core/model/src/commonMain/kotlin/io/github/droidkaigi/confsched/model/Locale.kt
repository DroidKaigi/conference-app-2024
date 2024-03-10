package io.github.droidkaigi.confsched.model

public enum class Locale {
    JAPAN,
    OTHER,
}

public expect fun getDefaultLocale(): Locale
