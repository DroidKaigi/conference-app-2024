package io.github.droidkaigi.confsched

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class],
)
@Module
class FakeAppModule {
    @Provides
    @Singleton
    fun provideClockProvider(): ClockProvider = object : ClockProvider {
        override fun clock(): Clock = object : Clock {
            override fun now() = Instant.parse("2024-09-12T11:00:00.00Z")
        }
    }
}
