package io.github.droidkaigi.confsched.testing.data.auth

import io.github.droidkaigi.confsched.data.auth.AuthApi

class FakeAuthApi : AuthApi {
    override suspend fun authIfNeeded() {
        // Do nothing
    }
}
