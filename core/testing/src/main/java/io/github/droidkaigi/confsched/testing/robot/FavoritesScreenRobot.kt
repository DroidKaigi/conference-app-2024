package io.github.droidkaigi.confsched.testing.robot

import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.favorites.FavoritesScreen
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.DefaultTimetableServerRobot
import io.github.droidkaigi.confsched.testing.ScreenRobot
import io.github.droidkaigi.confsched.testing.TimetableServerRobot
import javax.inject.Inject

class FavoritesScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot {

    fun setupFavoritesScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                FavoritesScreen(
                    onNavigationIconClick = {},
                    onTimetableItemClick = {},
                )
            }
        }
    }
}
