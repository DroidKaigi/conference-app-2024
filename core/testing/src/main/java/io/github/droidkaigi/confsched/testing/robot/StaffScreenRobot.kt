package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Staff
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.staff.StaffItemTestTag
import io.github.droidkaigi.confsched.staff.StaffScreen
import io.github.droidkaigi.confsched.staff.component.StaffItemImageTestTag
import io.github.droidkaigi.confsched.staff.component.StaffItemUserNameTextTestTag
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

    fun checkExistsStaffItem(
        fromTo: Pair<Int, Int>,
    ) {
        val staffList = Staff.fakes().subList(fromTo.first, fromTo.second)
        staffList.forEach { staff ->
            composeTestRule
                .onNode(hasTestTag(StaffItemTestTag.plus(staff.id)))
                .assertExists()
                .assertIsDisplayed()

            composeTestRule
                .onNode(
                    matcher = hasTestTag(StaffItemImageTestTag.plus(staff.username)),
                    useUnmergedTree = true,
                )
                .assertExists()
                .assertIsDisplayed()
                .assertContentDescriptionEquals(staff.username)

            composeTestRule
                .onNode(
                    matcher = hasTestTag(StaffItemUserNameTextTestTag.plus(staff.username)),
                    useUnmergedTree = true,
                )
                .assertExists()
                .assertIsDisplayed()
                .assertTextEquals(staff.username)
        }
    }

    fun checkDoesNotExistsStaffItem() {
        val staff = Staff.fakes().first()
        composeTestRule
            .onNode(hasTestTag(StaffItemTestTag.plus(staff.id)))
            .assertDoesNotExist()

        composeTestRule
            .onNode(
                matcher = hasTestTag(StaffItemImageTestTag.plus(staff.username)),
                useUnmergedTree = true,
            )
            .assertDoesNotExist()

        composeTestRule
            .onNode(
                matcher = hasTestTag(StaffItemUserNameTextTestTag.plus(staff.username)),
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
