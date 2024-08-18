package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories

interface SettingsRepository {
    @Composable
    fun settings(): Settings
    suspend fun save(settings: Settings.Exists)
}

@Composable
fun localSettingsRepository() : SettingsRepository {
    return LocalRepositories.current[SettingsRepository::class] as SettingsRepository
}
