package io.github.droidkaigi.confsched.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
public annotation class IoDispatcher

@InstallIn(SingletonComponent::class)
@Module
public class DispatcherModule {
    @IoDispatcher
    @Provides
    public fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
