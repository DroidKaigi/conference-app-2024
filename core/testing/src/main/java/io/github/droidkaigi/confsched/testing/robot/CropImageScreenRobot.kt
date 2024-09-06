package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.profilecard.CropImageScreen
import io.github.droidkaigi.confsched.profilecard.CropImageScreenTestTag
import javax.inject.Inject

class CropImageScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
) : ScreenRobot by screenRobot {

    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                CropImageScreen(
                    onNavigationIconClick = {},
                    onBackWithConfirm = {},
                )
            }
        }
        waitUntilIdle()
    }

    fun checkScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(CropImageScreenTestTag))
            .assertIsDisplayed()
    }
}
