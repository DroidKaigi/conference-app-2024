package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.staff.StaffScreen
import io.github.droidkaigi.confsched.staff.StaffScreenLazyColumnTestTag
import javax.inject.Inject

class StaffScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val staffServerRobot: DefaultStaffServerRobot,
) : ScreenRobot by screenRobot,
    StaffServerRobot by staffServerRobot {
    suspend fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                StaffScreen(
                    onNavigationIconClick = { },
                    onStaffItemClick = { },
                )
            }
        }
        waitUntilIdle()
    }

    fun checkExistsStaffItem() {
        composeTestRule
            .onNode(hasTestTag(StaffScreenLazyColumnTestTag))
            .onChildren()
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
    }

    fun checkDoesNotExistsStaffItem() {
        composeTestRule
            .onNode(hasTestTag(StaffScreenLazyColumnTestTag))
            .onChildren()
            .onFirst()
            .assertDoesNotExist()
    }
}
