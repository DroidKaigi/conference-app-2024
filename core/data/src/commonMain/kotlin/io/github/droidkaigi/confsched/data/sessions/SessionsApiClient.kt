package io.github.droidkaigi.confsched.data.sessions

import io.github.droidkaigi.confsched.data.sessions.response.SessionsAllResponse

public interface SessionsApiClient {
    public suspend fun sessionsAllResponse(): SessionsAllResponse
}
