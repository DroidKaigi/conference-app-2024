package io.github.droidkaigi.confsched.data.contributors

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.model.ContributorsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class ContributorsRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(ContributorsRepository::class)
    public abstract fun bind(repository: ContributorsRepository): Any

    public companion object {
        @Provides
        @Singleton
        public fun provideContributorsRepository(
            contributorsApi: ContributorsApiClient,
        ): ContributorsRepository {
            return DefaultContributorsRepository(
                contributorsApi = contributorsApi,
            )
        }
    }
}
