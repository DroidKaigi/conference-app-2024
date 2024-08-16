package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import io.github.droidkaigi.confsched.contributors.ContributorsItemTestTag
import io.github.droidkaigi.confsched.contributors.ContributorsScreen
import io.github.droidkaigi.confsched.contributors.component.ContributorsItemImageTestTag
import io.github.droidkaigi.confsched.contributors.component.ContributorsUserNameTextTestTag
import io.github.droidkaigi.confsched.model.Contributor
import io.github.droidkaigi.confsched.model.fakes
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

    fun checkExistsContributorItem(
        fromTo: Pair<Int, Int>,
    ) {
        val contributorsList = Contributor.fakes().subList(fromTo.first, fromTo.second)
        contributorsList.forEach { contributor ->
            composeTestRule
                .onNode(hasTestTag(ContributorsItemTestTag.plus(contributor.id)))
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

    fun checkDoesNotExistsContributorItem() {
        val contributor = Contributor.fakes().first()
        composeTestRule
            .onNode(hasTestTag(ContributorsItemTestTag.plus(contributor.id)))
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
