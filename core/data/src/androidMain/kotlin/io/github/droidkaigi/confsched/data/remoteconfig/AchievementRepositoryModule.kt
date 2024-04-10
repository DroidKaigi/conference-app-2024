package io.github.droidkaigi.confsched.data.remoteconfig

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.achievements.AchievementsDataStore
import io.github.droidkaigi.confsched.data.achievements.DefaultAchievementRepository
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.model.AchievementRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class AchievementRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(AchievementRepository::class)
    public  abstract fun bindAchievementRepository(
        repository: AchievementRepository,
    ): Any

    public companion object {
        @Provides
        @Singleton
        public fun provideAchievementRepositorySingleton(
            remoteConfigApi: RemoteConfigApi,
            achievementsDataStore: AchievementsDataStore,
        ): AchievementRepository {
            return DefaultAchievementRepository(
                remoteConfigApi = remoteConfigApi,
                achievementsDataStore = achievementsDataStore,
            )
        }
    }
}
