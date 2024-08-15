package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import io.github.droidkaigi.confsched.contributors.ContributorsScreen
import io.github.droidkaigi.confsched.contributors.ContributorsScreenTestTag
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

    fun checkContributorsDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ContributorsScreenTestTag))
            .assertIsDisplayed()
    }

    fun checkErrorSnackbarDisplayed() {
        composeTestRule
            .onNode(
                hasText("Fake IO Exception"),
                useUnmergedTree = true,
            ).assertIsDisplayed()
    }
}
