package io.github.droidkaigi.confsched.data.eventmap

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.droidkaigi.confsched.data.NetworkService

@Module
@InstallIn(SingletonComponent::class)
public class EventMapApiModule {
    @Provides
    public fun provideEventMapApi(
        networkService: NetworkService,
        ktorfit: Ktorfit,
    ): EventMapApiClient {
        return DefaultEventMapApiClient(
            networkService = networkService,
            ktorfit = ktorfit,
        )
    }
}
