package io.github.droidkaigi.confsched.data.staff

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.droidkaigi.confsched.data.NetworkService

@Module
@InstallIn(SingletonComponent::class)
public class StaffApiModule {

    @Provides
    public fun providesStaffApiClient(
        networkService: NetworkService,
        ktorfit: Ktorfit,
    ): StaffApiClient {
        return DefaultStaffApiClient(
            networkService = networkService,
            ktorfit = ktorfit,
        )
    }
}
