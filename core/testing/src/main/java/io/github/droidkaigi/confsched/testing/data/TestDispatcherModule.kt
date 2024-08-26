package io.github.droidkaigi.confsched.testing.data

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched.di.DispatcherModule
import io.github.droidkaigi.confsched.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DispatcherModule::class])
class TestDispatcherModule {

    @Provides
    @Singleton
    fun provideTestDispatcher(): TestDispatcher = StandardTestDispatcher()

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = StandardTestDispatcher()
}
