package io.github.droidkaigi.confsched.data.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.Settings
import io.github.droidkaigi.confsched.model.SettingsRepository

internal class DefaultSettingsRepository(
    private val settingsDataStore: SettingsDataStore,
) : SettingsRepository {
    @Composable
    override fun settings(): Settings {
        val settings by remember {
            settingsDataStore.get()
        }.safeCollectAsRetainedState(Settings.Loading)

        return settings
    }

    override suspend fun save(settings: Settings.Exists) {
        settingsDataStore.save(settings)
    }
}
