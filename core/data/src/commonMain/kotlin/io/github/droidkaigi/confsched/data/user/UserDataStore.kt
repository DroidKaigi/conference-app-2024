package io.github.droidkaigi.confsched.data.user

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.droidkaigi.confsched.model.TimetableItemId
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

public class UserDataStore(private val dataStore: DataStore<Preferences>) {

    private val mutableIdToken = MutableStateFlow<String?>(null)
    public val idToken: StateFlow<String?> = mutableIdToken

    private val singletonCoroutineScope: CoroutineScope = CoroutineScope(Job())

    public val getFavoriteSessionStream: StateFlow<PersistentSet<TimetableItemId>> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences: Preferences ->
                (preferences[KEY_FAVORITE_SESSION_IDS]?.split(",") ?: listOf())
                    .map { TimetableItemId(it) }
                    .toPersistentSet()
            }.stateIn(
                scope = singletonCoroutineScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = persistentSetOf(),
            )

    public suspend fun toggleFavorite(id: TimetableItemId) {
        val updatedFavorites = getFavoriteSessionStream.first().toMutableSet()

        if (updatedFavorites.contains(id)) {
            updatedFavorites.remove(id)
        } else {
            updatedFavorites.add(id)
        }
        dataStore.edit { preferences ->
            preferences[KEY_FAVORITE_SESSION_IDS] =
                updatedFavorites.joinToString(",") { it.value }
        }
    }

    public fun isAuthenticated(): Flow<Boolean?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences: Preferences ->
                preferences[KEY_AUTHENTICATED]?.toBoolean()
            }
    }

    public suspend fun setAuthenticated(authenticated: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTHENTICATED] = authenticated.toString()
        }
    }

    public suspend fun setIdToken(token: String): Unit = mutableIdToken.emit(token)

    public fun deviceId(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences: Preferences ->
                preferences[KEY_DEVICE_ID]
            }
    }

    public suspend fun setDeviceId(deviceId: String) {
        dataStore.edit { preferences ->
            preferences[KEY_DEVICE_ID] = deviceId
        }
    }

    private companion object {
        private val KEY_FAVORITE_SESSION_IDS = stringPreferencesKey("KEY_FAVORITE_SESSION_IDS")
        private val KEY_DEVICE_ID = stringPreferencesKey("KEY_DEVICE_ID")
        private val KEY_AUTHENTICATED = stringPreferencesKey("KEY_AUTHENTICATED")
    }
}
