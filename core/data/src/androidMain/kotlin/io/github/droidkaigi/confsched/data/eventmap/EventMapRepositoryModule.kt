package io.github.droidkaigi.confsched.data.eventmap

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.model.EventMapRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class EventMapRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(EventMapRepository::class)
    public abstract fun bind(repository: EventMapRepository): Any

    public companion object {
        @Provides
        @Singleton
        public fun provideEventMapRepository(
            eventMapApi: EventMapApiClient,
        ): EventMapRepository {
            return DefaultEventMapRepository(
                eventMapApi = eventMapApi,
            )
        }
    }
}
