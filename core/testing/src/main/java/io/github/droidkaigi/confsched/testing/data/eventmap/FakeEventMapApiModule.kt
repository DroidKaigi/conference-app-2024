package io.github.droidkaigi.confsched.testing.data.eventmap

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched.data.eventmap.EventMapApiClient
import io.github.droidkaigi.confsched.data.eventmap.EventMapApiModule
import io.github.droidkaigi.confsched.data.eventmap.FakeEventMapApiClient
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [EventMapApiModule::class],
)
class FakeEventMapApiModule {
    @Provides
    @Singleton
    fun provideEventMapApi(): EventMapApiClient {
        return FakeEventMapApiClient()
    }
}
