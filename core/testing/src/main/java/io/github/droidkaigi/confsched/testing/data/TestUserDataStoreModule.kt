package io.github.droidkaigi.confsched.testing.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.data.user.UserDataStoreModule
import io.github.droidkaigi.confsched.data.user.UserDataStoreQualifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [UserDataStoreModule::class])
class TestUserDataStoreModule {

    @Provides
    @Singleton
    public fun provideUserDataStore(
        @UserDataStoreQualifier
        dataStore: DataStore<Preferences>,
        testDispatcher: TestDispatcher,
    ): UserDataStore {
        return UserDataStore(
            dataStore = dataStore,
            coroutineScope = CoroutineScope(testDispatcher + Job()),
        )
    }
}
