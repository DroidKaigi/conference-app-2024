package io.github.droidkaigi.confsched.data.about

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.model.AboutRepository
import io.github.droidkaigi.confsched.model.BuildConfigProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class AboutRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(AboutRepository::class)
    public abstract fun bind(repository: AboutRepository): Any

    public companion object {
        @Provides
        @Singleton
        public fun provideAboutRepository(
            buildConfigProvider: BuildConfigProvider,
        ): AboutRepository {
            return DefaultAboutRepository(
                buildConfigProvider = buildConfigProvider,
            )
        }
    }
}
