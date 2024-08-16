package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performScrollToIndex
import io.github.droidkaigi.confsched.contributors.ContributorsItemTestTagPrefix
import io.github.droidkaigi.confsched.contributors.ContributorsScreen
import io.github.droidkaigi.confsched.contributors.ContributorsTestTag
import io.github.droidkaigi.confsched.contributors.component.ContributorsItemImageTestTag
import io.github.droidkaigi.confsched.contributors.component.ContributorsUserNameTextTestTag
import io.github.droidkaigi.confsched.model.Contributor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.testing.utils.assertCountAtLeast
import io.github.droidkaigi.confsched.testing.utils.hasTestTag
import io.github.droidkaigi.confsched.ui.Inject

class ContributorsScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    contributorsServerRobot: DefaultContributorsServerRobot,
) : ScreenRobot by screenRobot,
    ContributorsServerRobot by contributorsServerRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            ContributorsScreen(
                onNavigationIconClick = { },
                onContributorsItemClick = { },
            )
        }
    }

    fun scrollToIndex10() {
        composeTestRule
            .onNode(hasTestTag(ContributorsTestTag))
            .performScrollToIndex(10)
    }

    fun checkRangeContributorItemsDisplayed(
        fromTo: IntRange,
    ) {
        val contributorsList = Contributor.fakes().subList(fromTo.first, fromTo.last)
        contributorsList.forEach { contributor ->
            composeTestRule
                .onNode(hasTestTag(ContributorsItemTestTagPrefix.plus(contributor.id)))
                .assertExists()
                .assertIsDisplayed()

            composeTestRule
                .onNode(
                    matcher = hasTestTag(ContributorsItemImageTestTag.plus(contributor.username)),
                    useUnmergedTree = true,
                )
                .assertExists()
                .assertIsDisplayed()
                .assertContentDescriptionEquals(contributor.username)

            composeTestRule
                .onNode(
                    matcher = hasTestTag(ContributorsUserNameTextTestTag.plus(contributor.username)),
                    useUnmergedTree = true,
                )
                .assertExists()
                .assertIsDisplayed()
                .assertTextEquals(contributor.username)
        }
    }

    fun checkContributorItemsDisplayed() {
        // Check there are two contributors
        composeTestRule
            .onAllNodes(hasTestTag(ContributorsItemTestTagPrefix, substring = true))
            .assertCountAtLeast(2)
    }

    fun checkDoesNotFirstContributorItemDisplayed() {
        val contributor = Contributor.fakes().first()
        composeTestRule
            .onNode(hasTestTag(ContributorsItemTestTagPrefix.plus(contributor.id)))
            .assertDoesNotExist()

        composeTestRule
            .onNode(
                matcher = hasTestTag(ContributorsItemImageTestTag.plus(contributor.username)),
                useUnmergedTree = true,
            )
            .assertDoesNotExist()

        composeTestRule
            .onNode(
                matcher = hasTestTag(ContributorsUserNameTextTestTag.plus(contributor.username)),
                useUnmergedTree = true,
            )
            .assertDoesNotExist()
    }

    fun checkErrorSnackbarDisplayed() {
        composeTestRule
            .onNode(
                hasText("Fake IO Exception"),
                useUnmergedTree = true,
            ).assertIsDisplayed()
    }
}
