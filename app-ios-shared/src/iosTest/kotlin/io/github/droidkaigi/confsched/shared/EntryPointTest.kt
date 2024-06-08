package io.github.droidkaigi.confsched.shared

import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.data.achievements.AchievementsDataStore
import io.github.droidkaigi.confsched.data.auth.AuthApi
import io.github.droidkaigi.confsched.data.auth.Authenticator
import io.github.droidkaigi.confsched.data.auth.User
import io.github.droidkaigi.confsched.model.AchievementRepository
import io.github.droidkaigi.confsched.data.remoteconfig.RemoteConfigApi
import io.github.droidkaigi.confsched.data.sessions.SessionCacheDataStore
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.SponsorsRepository
import io.github.droidkaigi.confsched.model.StaffRepository
import io.ktor.client.HttpClient
import kotlin.test.Test
import kotlin.test.assertNotNull

class EntryPointTest {
    @Test
    fun get() {
        val kmpEntryPoint = KmpEntryPoint()
        kmpEntryPoint.initForTest(
            remoteConfigApi = object : RemoteConfigApi {
                override suspend fun getBoolean(key: String): Boolean {
                    return true
                }

                override suspend fun getString(key: String): String {
                    return "default"
                }
            },
            authenticator = object : Authenticator {
                override suspend fun currentUser(): User? {
                    return null
                }

                override suspend fun signInAnonymously(): User? {
                    return null
                }
            },
            testOverrideModules
        )
        // Check finer dependencies first to debug easily
        assertNotNull(kmpEntryPoint.get<UserDataStore>())
        assertNotNull(kmpEntryPoint.get<HttpClient>())
        assertNotNull(kmpEntryPoint.get<Authenticator>())
        assertNotNull(kmpEntryPoint.get<AuthApi>())
        assertNotNull(kmpEntryPoint.get<SessionsApiClient>())
        assertNotNull(kmpEntryPoint.get<SessionCacheDataStore>())
        assertNotNull(kmpEntryPoint.get<AchievementsDataStore>())

        assertNotNull(kmpEntryPoint.get<SessionsRepository>())
        assertNotNull(kmpEntryPoint.get<AchievementRepository>())
        assertNotNull(kmpEntryPoint.get<ContributorsRepository>())
        assertNotNull(kmpEntryPoint.get<SponsorsRepository>())
        assertNotNull(kmpEntryPoint.get<StaffRepository>())
        assertNotNull(kmpEntryPoint.get<Repositories>())
    }
}
