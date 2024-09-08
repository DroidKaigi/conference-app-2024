package io.github.droidkaigi.confsched.testing.robot.core

import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.Inject
import io.github.droidkaigi.confsched.sessions.SearchScreen
import io.github.droidkaigi.confsched.testing.robot.DefaultDeviceSetupRobot
import io.github.droidkaigi.confsched.testing.robot.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.robot.DefaultTimetableItemCardRobot
import io.github.droidkaigi.confsched.testing.robot.DefaultTimetableServerRobot
import io.github.droidkaigi.confsched.testing.robot.DeviceSetupRobot
import io.github.droidkaigi.confsched.testing.robot.ScreenRobot
import io.github.droidkaigi.confsched.testing.robot.action.SearchScreenActionRobot
import io.github.droidkaigi.confsched.testing.robot.verify.SearchScreenVerifyRobot
import io.github.droidkaigi.confsched.testing.robot.TimetableItemCardRobot
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot

const val DemoSearchWord = "Demo"

class SearchScreenCoreRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
    private val deviceSetupRobot: DefaultDeviceSetupRobot,
    timetableItemRobot: DefaultTimetableItemCardRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot,
    DeviceSetupRobot by deviceSetupRobot,
    TimetableItemCardRobot by timetableItemRobot {
    @Inject lateinit var actionRobot: SearchScreenActionRobot
    @Inject lateinit var verifyRobot: SearchScreenVerifyRobot

    enum class ConferenceDay(
        val day: Int,
        val dateText: String,
    ) {
        Day1(1, "9/12"),
        Day2(2, "9/13"),
    }

    enum class Category(
        val categoryName: String,
    ) {
        AppArchitecture("App Architecture en"),
        JetpackCompose("Jetpack Compose en"),
        Other("Other en"),
    }

    fun setupSearchScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                SearchScreen(
                    onTimetableItemClick = {},
                    onBackClick = {},
                )
            }
        }
        waitUntilIdle()
    }
}
