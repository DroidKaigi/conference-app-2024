package io.github.droidkaigi.confsched.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.droidkaigi.confsched.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

public class DefaultSettingsDataStore(
    private val dataStore: DataStore<Preferences>,
) : SettingsDataStore {
    public override suspend fun save(settings: Settings.Exists) {
        dataStore.edit { preferences ->
            preferences[KEY_SETTINGS] = Json.encodeToString(settings.toJson())
        }
    }

    public override fun get(): Flow<Settings> {
        return dataStore.data.map { preferences ->
            val cache = preferences[KEY_SETTINGS] ?: return@map null
            Json.decodeFromString<SettingsJson>(cache)
        }.map {
            it?.toModel() ?: Settings.defaultValue()
        }
    }

    private companion object {
        private val KEY_SETTINGS = stringPreferencesKey("KEY_SETTINGS")
    }
}

public interface SettingsDataStore {
    public suspend fun save(settings: Settings.Exists)
    public fun get(): Flow<Settings>
}
