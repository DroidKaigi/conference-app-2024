package io.github.droidkaigi.confsched.testing.data.remoteconfig

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched.data.di.RemoteConfigModule
import io.github.droidkaigi.confsched.data.remoteconfig.FakeRemoteConfigApi
import io.github.droidkaigi.confsched.data.remoteconfig.RemoteConfigApi

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteConfigModule::class],
)
class FakeRemoteConfigApiModule {
    @Provides
    fun provideRemoteConfigApi(): RemoteConfigApi {
        return FakeRemoteConfigApi()
    }
}
