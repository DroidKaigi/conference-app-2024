package io.github.droidkaigi.confsched.testing.robot

import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.staff.StaffScreen
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.ScreenRobot
import javax.inject.Inject

class StaffScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
): ScreenRobot by screenRobot {
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
