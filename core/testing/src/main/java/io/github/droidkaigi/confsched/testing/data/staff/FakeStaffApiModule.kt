package io.github.droidkaigi.confsched.testing.data.staff

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched.data.staff.FakeStaffApiClient
import io.github.droidkaigi.confsched.data.staff.StaffApiClient
import io.github.droidkaigi.confsched.data.staff.StaffApiModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [StaffApiModule::class],
)
class FakeStaffApiModule {
    @Provides
    @Singleton
    fun provideStaffApi(): StaffApiClient {
        return FakeStaffApiClient()
    }
}
