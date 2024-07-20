package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.ScreenRobot
import io.github.droidkaigi.confshed.about.AboutScreen
import io.github.droidkaigi.confshed.about.AboutTestTag
import javax.inject.Inject

class AboutScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
) : ScreenRobot by screenRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                AboutScreen(
                    onAboutItemClick = {},
                )
            }
        }
        waitUntilIdle()
    }

    fun checkDetailScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutTestTag.DetailScreen.SCREEN))
            .assertIsDisplayed()
    }

    fun checkCreditsScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutTestTag.CreditsScreen.SCREEN))
            .assertIsDisplayed()
    }

    fun checkOthersScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutTestTag.OthersScreen.SCREEN))
            .assertIsDisplayed()
    }

    fun checkFooterLinksScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutTestTag.FooterLinksScreen.SCREEN))
            .assertIsDisplayed()
    }
}
