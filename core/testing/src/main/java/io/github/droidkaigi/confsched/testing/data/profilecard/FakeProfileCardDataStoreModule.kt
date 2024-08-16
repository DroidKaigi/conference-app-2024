package io.github.droidkaigi.confsched.testing.data.profilecard

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched.data.profilecard.FakeProfileCardDataStore
import io.github.droidkaigi.confsched.data.profilecard.ProfileCardDataStore
import io.github.droidkaigi.confsched.data.profilecard.ProfileCardDataStoreModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProfileCardDataStoreModule::class],
)
class FakeProfileCardDataStoreModule {
    @Singleton
    @Provides
    public fun provideProfileCardDataStore(): ProfileCardDataStore {
        return FakeProfileCardDataStore()
    }
}
