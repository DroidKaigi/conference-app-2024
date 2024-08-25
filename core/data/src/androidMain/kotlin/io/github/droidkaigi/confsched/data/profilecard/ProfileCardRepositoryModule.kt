package io.github.droidkaigi.confsched.data.profilecard

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.di.IoDispatcher
import io.github.droidkaigi.confsched.model.ProfileCardRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProfileCardRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(ProfileCardRepository::class)
    abstract fun bindProfileCardRepository(profileCardRepository: ProfileCardRepository): Any

    companion object {
        @Singleton
        @Provides
        fun provideProfileCardRepository(
            profileCardDataStore: ProfileCardDataStore,
            @IoDispatcher ioDispatcher: CoroutineDispatcher,
        ): ProfileCardRepository {
            return DefaultProfileCardRepository(profileCardDataStore, ioDispatcher)
        }
    }
}
