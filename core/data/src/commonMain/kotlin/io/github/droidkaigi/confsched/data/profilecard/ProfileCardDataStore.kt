package io.github.droidkaigi.confsched.data.profilecard

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.droidkaigi.confsched.model.ProfileCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

public class ProfileCardDataStore(
    private val dataStore: DataStore<Preferences>,
) {
    public suspend fun save(profileCard: ProfileCard) {
        dataStore.edit { preferences ->
            preferences[KEY_PROFILE_CARD] = Json.encodeToString(profileCard.toJson())
        }
    }

    public fun get(): Flow<ProfileCard?> {
        return dataStore.data.map { preferences ->
            val cache = preferences[KEY_PROFILE_CARD] ?: return@map null
            Json.decodeFromString<ProfileCardJson>(cache)
        }.map {
            it?.toModel()
        }
    }

    private companion object {
        private val KEY_PROFILE_CARD = stringPreferencesKey("KEY_PROFILE_CARD")
    }
}
