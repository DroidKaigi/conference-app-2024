package io.github.droidkaigi.confsched.testing.robot

import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sponsors.SponsorsScreen
import javax.inject.Inject

class SponsorsScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val sponsorsServerRobot: DefaultSponsorsServerRobot,
) : ScreenRobot by screenRobot,
    SponsorsServerRobot by sponsorsServerRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                SponsorsScreen(
                    onNavigationIconClick = {},
                    onSponsorsItemClick = {},
                )
            }
        }
    }
}
