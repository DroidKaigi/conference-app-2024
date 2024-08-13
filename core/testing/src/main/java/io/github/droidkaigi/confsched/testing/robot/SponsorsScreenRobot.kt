package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sponsors.SponsorsScreen
import io.github.droidkaigi.confsched.sponsors.SponsorsScreenTestTag
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

    fun checkSponsorsDisplayed() {
        composeTestRule
            .onNode(hasTestTag(SponsorsScreenTestTag))
            .assertIsDisplayed()
    }

    fun checkErrorSnackbarDisplayed() {
        composeTestRule
            .onNode(hasText("Fake IO Exception"))
            .isDisplayed()
    }
}
