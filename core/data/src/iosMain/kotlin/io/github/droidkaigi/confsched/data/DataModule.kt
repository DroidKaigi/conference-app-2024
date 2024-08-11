package io.github.droidkaigi.confsched.data

import de.jensklingenberg.ktorfit.Ktorfit
import io.github.droidkaigi.confsched.data.auth.AuthApi
import io.github.droidkaigi.confsched.data.auth.DefaultAuthApi
import io.github.droidkaigi.confsched.data.contributors.ContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsRepository
import io.github.droidkaigi.confsched.data.core.defaultJson
import io.github.droidkaigi.confsched.data.core.defaultKtorConfig
import io.github.droidkaigi.confsched.data.sessions.DefaultSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.DefaultSessionsRepository
import io.github.droidkaigi.confsched.data.sessions.SessionCacheDataStore
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.data.sponsors.DefaultSponsorsApiClient
import io.github.droidkaigi.confsched.data.sponsors.DefaultSponsorsRepository
import io.github.droidkaigi.confsched.data.sponsors.SponsorsApiClient
import io.github.droidkaigi.confsched.data.staff.DefaultStaffApiClient
import io.github.droidkaigi.confsched.data.staff.DefaultStaffRepository
import io.github.droidkaigi.confsched.data.staff.StaffApiClient
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.SponsorsRepository
import io.github.droidkaigi.confsched.model.StaffRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import kotlin.reflect.KClass

public class BaseUrl(internal val baseUrl: String)

public interface Repositories {
    public val map: Map<KClass<*>, Any>
}

public class DefaultRepositories(
    public override val map: Map<KClass<*>, Any>,
) : Repositories

@OptIn(ExperimentalForeignApi::class)
public val dataModule: Module = module {
    single {
        HttpClient(Darwin) {
            engine {}
            defaultKtorConfig(get(), get())
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.d { message }
                    }
                }

                level = LogLevel.BODY
            }
        }
    }
    single {
        defaultJson()
    }
    single {
        Ktorfit
            .Builder()
            .httpClient(get<HttpClient>())
            .baseUrl(get<BaseUrl>().baseUrl)
            .build()
    }
    single {
        val dataStore = createDataStore(
            coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/confsched2024.preferences_pb"
            },
        )
        UserDataStore(dataStore)
    }
    single {
        val dataStore = createDataStore(
            coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/confsched2024.cache.preferences_pb"
            },
        )
        SessionCacheDataStore(dataStore, get())
    }

    singleOf(::DefaultAuthApi) bind AuthApi::class
    singleOf(::DefaultSessionsApiClient) bind SessionsApiClient::class
    singleOf(::DefaultContributorsApiClient) bind ContributorsApiClient::class
    singleOf(::DefaultSponsorsApiClient) bind SponsorsApiClient::class
    singleOf(::DefaultStaffApiClient) bind StaffApiClient::class

    singleOf(::NetworkService)
    singleOf(::DefaultSessionsRepository) bind SessionsRepository::class
    singleOf(::DefaultContributorsRepository) bind ContributorsRepository::class
    singleOf(::DefaultStaffRepository) bind StaffRepository::class
    singleOf(::DefaultSponsorsRepository) bind SponsorsRepository::class
    single<Repositories> {
        DefaultRepositories(
            mapOf(
                SessionsRepository::class to get<SessionsRepository>(),
                ContributorsRepository::class to get<ContributorsRepository>(),
                StaffRepository::class to get<StaffRepository>(),
                SponsorsRepository::class to get<SponsorsRepository>(),
            ),
        )
    }
}
