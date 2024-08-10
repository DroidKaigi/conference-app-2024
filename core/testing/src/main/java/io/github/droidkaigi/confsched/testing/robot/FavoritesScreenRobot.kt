package io.github.droidkaigi.confsched.testing.robot

import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.favorites.FavoritesScreen
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
