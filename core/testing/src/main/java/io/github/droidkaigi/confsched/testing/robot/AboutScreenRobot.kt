package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import io.github.droidkaigi.confsched.about.AboutScreen
import io.github.droidkaigi.confsched.about.AboutScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.about.section.AboutCreditsContributorsItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutCreditsSponsorsItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutCreditsStaffItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutCreditsTitleTestTag
import io.github.droidkaigi.confsched.about.section.AboutDetailTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterLinksTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersCodeOfConductItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersLicenseItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersPrivacyPolicyItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersSettingsItemTestTag
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

    fun scrollToCreditsSection() {
        composeTestRule
            .onNode(hasTestTag(AboutScreenLazyColumnTestTag))
            .performScrollToNode(hasTestTag(AboutCreditsStaffItemTestTag))

        // FIXME Without this, you won't be able to scroll to the exact middle of the credits section.
        composeTestRule.onRoot().performTouchInput {
            swipeUp(startY = centerY, endY = centerY - 200)
        }
        waitUntilIdle()
    }

    fun scrollToOthersSection() {
        composeTestRule
            .onNode(hasTestTag(AboutScreenLazyColumnTestTag))
            .performScrollToNode(hasTestTag(AboutOthersTitleTestTag))
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

    fun checkCreditsSectionContentsDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutCreditsContributorsItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasTestTag(AboutCreditsStaffItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasTestTag(AboutCreditsSponsorsItemTestTag))
            .assertExists()
            .assertIsDisplayed()
    }

    fun checkOthersSectionTitleDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutOthersTitleTestTag))
            .assertIsDisplayed()
    }

    fun checkOthersSectionContentsDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutOthersCodeOfConductItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasTestTag(AboutOthersLicenseItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasTestTag(AboutOthersPrivacyPolicyItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasTestTag(AboutOthersSettingsItemTestTag))
            .assertExists()
            .assertIsDisplayed()
    }

    // TODO https://github.com/DroidKaigi/conference-app-2024/issues/670
    fun checkFooterLinksSectionDisplayed() {
        composeTestRule
            .onNode(hasTestTag(AboutFooterLinksTestTag))
            .assertIsDisplayed()
    }
}
