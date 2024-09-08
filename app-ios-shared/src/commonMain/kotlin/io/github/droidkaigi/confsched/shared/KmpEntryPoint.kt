package io.github.droidkaigi.confsched.shared

import io.github.droidkaigi.confsched.data.BaseUrl
import io.github.droidkaigi.confsched.data.auth.Authenticator
import io.github.droidkaigi.confsched.data.dataModule
import io.github.droidkaigi.confsched.data.remoteconfig.RemoteConfigApi
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.reflect.KClass

class KmpEntryPoint {
    private val defaultBaseUrl = "https://ssot-api.droidkaigi.jp/"

    // Please EntryPoint.get() instead of this property
    lateinit var koinApplication: KoinApplication

    fun init(
        remoteConfigApi: RemoteConfigApi,
        authenticator: Authenticator,
    ) = initForTest(
        remoteConfigApi = remoteConfigApi,
        authenticator = authenticator,
        dataModuleOverride = module { },
    )

    fun initForTest(
        remoteConfigApi: RemoteConfigApi,
        authenticator: Authenticator,
        dataModuleOverride: Module,
    ) {
        stopKoin()
        koinApplication = startKoin {
            modules(
                dataModule,
                module {
                    single {
                        BaseUrl(defaultBaseUrl)
                    }
                    single {
                        remoteConfigApi
                    }
                    single {
                        authenticator
                    }
                },
                dataModuleOverride
            )
        }
    }

    inline fun <reified T : Any> get(): T {
        return koinApplication.koin.get(T::class)
    }

    fun <T : Any> get(clazz: KClass<T>): T {
        return koinApplication.koin.get(clazz)
    }
}
