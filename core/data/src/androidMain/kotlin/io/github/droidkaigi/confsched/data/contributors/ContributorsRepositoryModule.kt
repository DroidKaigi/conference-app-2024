package io.github.droidkaigi.confsched.data.contributors

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched.model.ContributorsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public class ContributorsRepositoryModule {
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
