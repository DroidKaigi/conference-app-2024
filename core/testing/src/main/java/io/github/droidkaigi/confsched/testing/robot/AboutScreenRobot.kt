package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import io.github.droidkaigi.confsched.about.AboutScreen
import io.github.droidkaigi.confsched.about.section.AboutCreditsTitleTestTag
import io.github.droidkaigi.confsched.about.section.AboutDetailTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterLinksTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersTitleTestTag
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
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
            .onNode(hasTestTag(AboutDetailTestTag))
            .assertIsDisplayed()
    }

    fun checkCreditsSectionTitleDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutCreditsTitleTestTag))
            .assertIsDisplayed()
    }

    fun checkOthersSectionTitleDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutOthersTitleTestTag))
            .assertIsDisplayed()
    }

    fun checkFooterLinksSectionDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutFooterLinksTestTag))
            .assertIsDisplayed()
    }
}
