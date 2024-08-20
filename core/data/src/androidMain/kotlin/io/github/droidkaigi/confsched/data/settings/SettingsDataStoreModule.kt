package io.github.droidkaigi.confsched.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched.data.user.SettingsDataStoreQualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public class SettingsDataStoreModule {
    @Singleton
    @Provides
    public fun provideSettingsDataStore(
        @SettingsDataStoreQualifier
        dataStore: DataStore<Preferences>,
    ): SettingsDataStore {
        return DefaultSettingsDataStore(dataStore)
    }
}
