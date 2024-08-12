package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.profilecard.ProfileCardCardScreenTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardEditScreenTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardScreen
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
            .onNode(hasTestTag(ProfileCardEditScreenTestTag))
            .assertIsDisplayed()
    }

    fun checkCardScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardCardScreenTestTag))
            .assertIsDisplayed()
    }
}
