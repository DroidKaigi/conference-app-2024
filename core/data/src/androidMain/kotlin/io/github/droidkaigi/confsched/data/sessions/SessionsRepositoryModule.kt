package io.github.droidkaigi.confsched.data.sessions

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.model.SessionsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class SessionsRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(SessionsRepository::class)
    public abstract fun bindSessionsRepository(sessionsRepository: SessionsRepository): Any

    public companion object {

        @JvmStatic
        @Provides
        @Singleton
        public fun provideSessionsRepositorySingleton(
            sessionsApi: SessionsApiClient,
            userDataStore: UserDataStore,
            sessionCacheDataStore: SessionCacheDataStore,
        ): SessionsRepository {
            return DefaultSessionsRepository(
                sessionsApi = sessionsApi,
                userDataStore = userDataStore,
                sessionCacheDataStore = sessionCacheDataStore,
            )
        }
    }
}
