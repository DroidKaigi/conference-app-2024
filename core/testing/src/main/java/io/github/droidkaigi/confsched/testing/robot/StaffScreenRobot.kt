package io.github.droidkaigi.confsched.testing.robot

import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.staff.StaffScreen
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
}
