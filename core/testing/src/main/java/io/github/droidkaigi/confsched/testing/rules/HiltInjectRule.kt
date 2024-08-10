package io.github.droidkaigi.confsched.testing.rules

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class HiltInjectRule(private val rule: HiltAndroidRule) : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        rule.inject()
    }
}
