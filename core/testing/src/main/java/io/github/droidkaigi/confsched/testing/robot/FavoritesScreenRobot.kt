package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onFirst
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.favorites.FavoritesScreen
import io.github.droidkaigi.confsched.favorites.FavoritesScreenUiState
import io.github.droidkaigi.confsched.favorites.section.FavoritesScreenEmptyViewTestTag
import io.github.droidkaigi.confsched.favorites.section.FavoritesSheetUiState.FavoriteListUiState
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Inject

class FavoritesScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot {

    fun setupFavoritesScreenEmptyContent() {
        robotTestRule.setContent {
            KaigiTheme {
                FavoritesScreen(
                    onNavigationIconClick = {},
                    onTimetableItemClick = {},
                )
            }
        }
    }

    fun setupFavoritesScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                providePresenterDefaults { userMessageStateHolder ->
                    FavoritesScreen(
                        onNavigationIconClick = {},
                        onTimetableItemClick = {},
                        uiState = FavoritesScreenUiState(
                            favoritesSheetUiState = FavoriteListUiState(
                                allFilterSelected = true,
                                currentDayFilter = persistentListOf(),
                                timeTable = Timetable.fake(),
                            ),
                            userMessageStateHolder = userMessageStateHolder,
                        ),
                    )
                }
            }
        }
    }

    fun checkEmptyViewDisplayed() {
        composeTestRule
            .onNode(hasTestTag(FavoritesScreenEmptyViewTestTag))
            .assertIsDisplayed()
    }

    fun checkFavoriteTimetableListItemsDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertIsDisplayed()
    }
}
