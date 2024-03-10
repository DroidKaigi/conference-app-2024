package io.github.droidkaigi.confsched.data.auth

public interface AuthApi {
    public suspend fun authIfNeeded()
}
