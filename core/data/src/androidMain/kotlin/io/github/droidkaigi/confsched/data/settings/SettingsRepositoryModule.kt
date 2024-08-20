package io.github.droidkaigi.confsched.data.settings

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.model.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SettingsRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(SettingsRepository::class)
    abstract fun bindSettingsRepository(settingsRepository: SettingsRepository): Any

    companion object {
        @Singleton
        @Provides
        fun provideSettingsRepository(
            settingsDataStore: SettingsDataStore,
        ): SettingsRepository {
            return DefaultSettingsRepository(settingsDataStore)
        }
    }
}
