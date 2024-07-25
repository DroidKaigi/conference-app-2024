package io.github.droidkaigi.confsched

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.droidkaigi.confsched.data.di.ServerEnvironmentModule
import io.github.droidkaigi.confsched2024.BuildConfig

@HiltAndroidApp
class App : Application(), ServerEnvironmentModule.HasServerEnvironment {
    override val serverEnvironment: ServerEnvironmentModule.ServerEnvironment =
        ServerEnvironmentModule.ServerEnvironment(
            BuildConfig.SERVER_URL,
        )
}
