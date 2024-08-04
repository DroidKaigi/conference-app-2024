package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import io.github.droidkaigi.confsched.about.AboutScreen
import io.github.droidkaigi.confsched.about.section.AboutCreditsSectionTestTag
import io.github.droidkaigi.confsched.about.section.AboutDetailSectionTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterLinksSectionTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersSectionTestTag
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
            .onNode(hasTestTag(AboutDetailSectionTestTag.Section))
            .assertIsDisplayed()
    }

    fun checkCreditsSectionTitleDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutCreditsSectionTestTag.Title))
            .assertIsDisplayed()
    }

    fun checkOthersSectionTitleDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutOthersSectionTestTag.Title))
            .assertIsDisplayed()
    }

    fun checkFooterLinksSectionDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutFooterLinksSectionTestTag.Section))
            .assertIsDisplayed()
    }
}
