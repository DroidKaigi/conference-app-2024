package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performScrollToIndex
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Staff
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.staff.StaffItemTestTagPrefix
import io.github.droidkaigi.confsched.staff.StaffScreen
import io.github.droidkaigi.confsched.staff.StaffScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.staff.component.StaffItemImageTestTag
import io.github.droidkaigi.confsched.staff.component.StaffItemUserNameTextTestTag
import io.github.droidkaigi.confsched.testing.utils.assertCountAtLeast
import io.github.droidkaigi.confsched.testing.utils.hasTestTag
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

    fun scrollToIndex10() {
        composeTestRule
            .onNode(hasTestTag(StaffScreenLazyColumnTestTag))
            .performScrollToIndex(10)
    }

    fun checkShowFirstAndSecondStaffs() {
        checkRangeStaffItemsDisplayed(
            fromTo = 0..2,
        )
    }

    private fun checkRangeStaffItemsDisplayed(
        fromTo: IntRange,
    ) {
        val staffList = Staff.fakes().subList(fromTo.first, fromTo.last)
        staffList.forEach { staff ->
            composeTestRule
                .onNode(hasTestTag(StaffItemTestTagPrefix.plus(staff.id)))
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

    fun checkStaffItemsDisplayed() {
        // Check there are two staffs
        composeTestRule
            .onAllNodes(hasTestTag(StaffItemTestTagPrefix, substring = true))
            .assertCountAtLeast(2)
    }

    fun checkDoesNotFirstStaffItemDisplayed() {
        val staff = Staff.fakes().first()
        composeTestRule
            .onNode(hasTestTag(StaffItemTestTagPrefix.plus(staff.id)))
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
