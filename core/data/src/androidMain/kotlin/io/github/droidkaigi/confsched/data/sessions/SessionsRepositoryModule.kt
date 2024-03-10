package io.github.droidkaigi.confsched.data.sessions

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.model.SessionsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public class SessionsRepositoryModule {
    @Provides
    @Singleton
    public fun provideSessionsRepository(
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
