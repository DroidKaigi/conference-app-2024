package io.github.droidkaigi.confsched.data.settings

import io.github.droidkaigi.confsched.model.FontFamily
import io.github.droidkaigi.confsched.model.Settings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SettingsJson(
    @SerialName("use_font_family_name")
    val useFontFamilyName: String,
)

internal fun SettingsJson.toModel() = Settings.Exists(
    useFontFamily = FontFamily.entries.find { it.fileName == useFontFamilyName } ?: FontFamily.DotGothic16Regular,
)

internal fun Settings.Exists.toJson() = SettingsJson(
    useFontFamilyName = useFontFamily.fileName,
)
