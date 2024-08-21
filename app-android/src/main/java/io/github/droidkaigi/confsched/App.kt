package io.github.droidkaigi.confsched

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import io.github.droidkaigi.confsched.data.di.ServerEnvironmentModule
import io.github.droidkaigi.confsched2024.BuildConfig

@HiltAndroidApp
class App : Application(), ServerEnvironmentModule.HasServerEnvironment {
    override val serverEnvironment: ServerEnvironmentModule.ServerEnvironment =
        ServerEnvironmentModule.ServerEnvironment(
            BuildConfig.SERVER_URL,
        )

    override fun onCreate() {
        super.onCreate()
        AppContext.setUp(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}
