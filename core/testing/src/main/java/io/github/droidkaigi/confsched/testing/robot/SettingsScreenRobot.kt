package io.github.droidkaigi.confsched.testing.robot

import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.settings.SettingsScreen
import javax.inject.Inject

class SettingsScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
) : ScreenRobot by screenRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                SettingsScreen(
                    onNavigationIconClick = {}
                )
            }
        }
        waitUntilIdle()
    }
}
