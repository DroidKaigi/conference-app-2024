package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.ScreenRobot
import io.github.droidkaigi.confshed.profilecard.ProfileCardScreen
import io.github.droidkaigi.confshed.profilecard.ProfileCardTestTag
import javax.inject.Inject

class ProfileCardScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
) : ScreenRobot by screenRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                ProfileCardScreen()
            }
        }
        waitUntilIdle()
    }

    fun checkEditScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardTestTag.EditScreen.SCREEN))
            .assertIsDisplayed()
    }

    fun checkCardScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardTestTag.CardScreen.SCREEN))
            .assertIsDisplayed()
    }
}
