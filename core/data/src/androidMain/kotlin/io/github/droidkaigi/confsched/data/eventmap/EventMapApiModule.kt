package io.github.droidkaigi.confsched.data.eventmap

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
public class EventMapApiModule {
    @Provides
    public fun provideEventMapApi(): EventMapApiClient {
        return FakeEventMapApiClient()
    }
}
