package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import io.github.droidkaigi.confsched.about.AboutScreen
import io.github.droidkaigi.confsched.about.AboutTestTag
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.ScreenRobot
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

    fun checkDetailSectionDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutTestTag.DetailSection.Name))
            .assertIsDisplayed()
    }

    fun checkCreditsSectionDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutTestTag.CreditsSection.Name))
            .assertIsDisplayed()
    }

    fun checkOthersSectionDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutTestTag.OthersSection.Name))
            .assertIsDisplayed()
    }

    fun checkFooterLinksSectionDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutTestTag.FooterLinksSection.Name))
            .assertIsDisplayed()
    }
}
