package io.github.droidkaigi.confsched.shared

import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import org.koin.dsl.module

val testOverrideModules = module {
    single<SessionsApiClient> { FakeSessionsApiClient() }
}
