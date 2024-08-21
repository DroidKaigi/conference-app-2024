package io.github.droidkaigi.confsched

import android.app.Application
import android.content.Context

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object AppContext {
    private lateinit var application: Application

    fun setUp(context: Context) {
        application = context as Application
    }

    fun get(): Context {
        check(::application.isInitialized) { "Application context isn't initialized" }
        return application.applicationContext
    }
}
