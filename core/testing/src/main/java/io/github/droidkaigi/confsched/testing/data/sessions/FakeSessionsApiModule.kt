package io.github.droidkaigi.confsched.testing.data.sessions

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.SessionsApiModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SessionsApiModule::class],
)
class FakeSessionsApiModule {
    @Provides
    @Singleton
    fun provideSessionsApi(): SessionsApiClient {
        return FakeSessionsApiClient()
    }
}
