package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performScrollToNode
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Plan
import io.github.droidkaigi.confsched.model.Plan.GOLD
import io.github.droidkaigi.confsched.model.Plan.PLATINUM
import io.github.droidkaigi.confsched.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.sponsors.SponsorsScreen
import io.github.droidkaigi.confsched.sponsors.component.SponsorItemImageTestTag
import io.github.droidkaigi.confsched.sponsors.section.SponsorsListLazyVerticalGridTestTag
import io.github.droidkaigi.confsched.sponsors.section.SponsorsListSponsorHeaderTestTagPrefix
import io.github.droidkaigi.confsched.sponsors.section.SponsorsListSponsorItemTestTagPrefix
import io.github.droidkaigi.confsched.testing.utils.assertCountAtLeast
import io.github.droidkaigi.confsched.testing.utils.hasTestTag
import javax.inject.Inject

class SponsorsScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val sponsorsServerRobot: DefaultSponsorsServerRobot,
) : ScreenRobot by screenRobot,
    SponsorsServerRobot by sponsorsServerRobot {
    enum class SponsorType(
        val typeName: String,
    ) {
        Platinum("PLATINUM SPONSORS"),
        Gold("GOLD SPONSORS"),
        Supporters("SUPPORTERS"),
    }

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

    fun scrollToGoldSponsorsHeader() {
        scrollToSponsorHeader(SponsorType.Gold)
    }

    fun scrollToSupportersSponsorsHeader() {
        scrollToSponsorHeader(SponsorType.Supporters)
    }

    private fun scrollToSponsorHeader(
        sponsorType: SponsorType,
    ) {
        composeTestRule
            .onNode(hasTestTag(SponsorsListLazyVerticalGridTestTag))
            .performScrollToNode(
                hasTestTag(SponsorsListSponsorHeaderTestTagPrefix.plus(sponsorType.typeName)),
            )
    }

    fun scrollBottom() {
        composeTestRule
            .onNode(hasTestTag(SponsorsListLazyVerticalGridTestTag))
            .performScrollToNode(
                hasTestTag(SponsorsListSponsorItemTestTagPrefix.plus(Sponsor.fakes().last().name)),
            )
    }

    fun checkDisplayPlatinumSponsors() {
        checkSponsorItemsDisplayedByRangeAndSponsorType(
            sponsorType = SponsorType.Platinum,
            fromTo = 0..2,
        )
    }

    fun checkDisplayGoldSponsors() {
        checkSponsorItemsDisplayedByRangeAndSponsorType(
            sponsorType = SponsorType.Gold,
            fromTo = 0..2,
        )
    }

    fun checkDisplaySupportersSponsors() {
        checkSponsorItemsDisplayedByRangeAndSponsorType(
            sponsorType = SponsorType.Supporters,
            fromTo = 0..2,
        )
    }

    private fun checkSponsorItemsDisplayedByRangeAndSponsorType(
        sponsorType: SponsorType,
        fromTo: IntRange,
    ) {
        val sponsorList = Sponsor.fakes().filter { it.plan.toSponsorType() == sponsorType }
            .subList(fromTo.first, fromTo.last)
        sponsorList.forEach { sponsor ->
            composeTestRule
                .onNode(hasTestTag(SponsorsListSponsorItemTestTagPrefix.plus(sponsor.name)))
                .assertExists()
                .assertIsDisplayed()

            composeTestRule
                .onNode(
                    hasTestTag(SponsorsListSponsorItemTestTagPrefix.plus(sponsor.name)),
                    true,
                )
                .onChildren()
                .filter(matcher = hasTestTag(SponsorItemImageTestTag))
                .onFirst()
                .assertExists()
                .assertIsDisplayed()
                .assertContentDescriptionEquals("${sponsor.name} sponsor logo")
        }
    }

    fun checkSponsorItemsDisplayed() {
        // Check there are two sponsors
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = SponsorsListSponsorItemTestTagPrefix,
                    substring = true,
                ),
            )
            .assertCountAtLeast(2)
    }

    fun checkDoesNotSponsorItemsDisplayed() {
        val sponsor = Sponsor.fakes().first()
        composeTestRule
            .onNode(
                hasTestTag(
                    testTag = SponsorsListSponsorItemTestTagPrefix,
                    substring = true,
                ),
            )
            .assertDoesNotExist()

        composeTestRule
            .onNode(
                matcher = hasTestTag(SponsorItemImageTestTag.plus(sponsor.name)),
                useUnmergedTree = true,
            )
            .assertDoesNotExist()
    }

    fun checkErrorSnackbarDisplayed() {
        composeTestRule
            .onNode(hasText("Fake IO Exception"))
            .isDisplayed()
    }

    private fun Plan.toSponsorType() = when (this) {
        PLATINUM -> SponsorType.Platinum
        GOLD -> SponsorType.Gold
        SUPPORTER -> SponsorType.Supporters
    }
}
