package io.github.droidkaigi.confsched.data.sponsors

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.model.SponsorsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class SponsorsRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(SponsorsRepository::class)
    public abstract fun bind(repository: SponsorsRepository): Any

    public companion object {
        @Provides
        @Singleton
        public fun provideSponsorsRepository(
            sponsorsApi: SponsorsApiClient,
        ): SponsorsRepository {
            return DefaultSponsorsRepository(
                sponsorsApi = sponsorsApi,
            )
        }
    }
}
