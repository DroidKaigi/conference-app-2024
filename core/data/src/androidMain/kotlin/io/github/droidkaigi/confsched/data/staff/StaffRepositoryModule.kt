package io.github.droidkaigi.confsched.data.staff

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched.data.di.RepositoryQualifier
import io.github.droidkaigi.confsched.model.StaffRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class StaffRepositoryModule {
    @Binds
    @RepositoryQualifier
    @IntoMap
    @ClassKey(StaffRepository::class)
    public abstract fun bind(repository: StaffRepository): Any

    public companion object {
        @Provides
        @Singleton
        public fun provideStaffRepository(
            staffApi: StaffApiClient,
        ): StaffRepository {
            return DefaultStaffRepository(
                staffApi = staffApi,
            )
        }

    }
}
