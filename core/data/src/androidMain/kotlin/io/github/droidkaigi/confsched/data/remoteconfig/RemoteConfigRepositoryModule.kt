package io.github.droidkaigi.confsched.data.remoteconfig

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched.data.achievements.AchievementsDataStore
import io.github.droidkaigi.confsched.data.achievements.DefaultAchievementRepository
import io.github.droidkaigi.confsched.data.contributors.AchievementRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public class RemoteConfigRepositoryModule {
    @Provides
    @Singleton
    public fun provideRemoteConfigRepository(
        remoteConfigApi: RemoteConfigApi,
        achievementsDataStore: AchievementsDataStore,
    ): AchievementRepository {
        return DefaultAchievementRepository(
            remoteConfigApi = remoteConfigApi,
            achievementsDataStore = achievementsDataStore,
        )
    }
}
