package io.github.droidkaigi.confsched.testing.data.contributors

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched.data.contributors.ContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.ContributorsApiModule
import io.github.droidkaigi.confsched.data.contributors.FakeContributorsApiClient
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ContributorsApiModule::class],
)
class FakeContributorsApiModule {
    @Provides
    @Singleton
    fun provideSessionsApi(): ContributorsApiClient {
        return FakeContributorsApiClient()
    }
}
