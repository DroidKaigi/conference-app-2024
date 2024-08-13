package io.github.droidkaigi.confsched.data.profilecard

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched.data.user.ProfileCardDataStoreQualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ProfileCardDataStoreModule {
    @Singleton
    @Provides
    fun provideProfileCardDataStore(
        @ProfileCardDataStoreQualifier
        dataStore: DataStore<Preferences>,
    ): ProfileCardDataStore {
        return ProfileCardDataStore(dataStore)
    }
}
