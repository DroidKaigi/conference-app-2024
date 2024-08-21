package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.favorites.FavoritesScreen
import io.github.droidkaigi.confsched.favorites.section.FavoritesScreenEmptyViewTestTag
import io.github.droidkaigi.confsched.model.TimetableItemId
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardBookmarkButtonTestTag
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalIsWideWidthScreen
import javax.inject.Inject

class FavoritesScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
    private val userDataStore: UserDataStore,
    private val deviceQualifierRobot: DefaultDeviceQualifierRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot,
    DeviceQualifierRobot by deviceQualifierRobot {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    fun setupFavoritesScreenContent() {
        robotTestRule.setContent {
            val windowSize = calculateWindowSizeClass()
            val isWideWidthScreen = when (windowSize.widthSizeClass) {
                WindowWidthSizeClass.Compact -> false
                WindowWidthSizeClass.Medium -> true
                WindowWidthSizeClass.Expanded -> true
                else -> false
            }
            CompositionLocalProvider(LocalIsWideWidthScreen provides isWideWidthScreen) {
                KaigiTheme {
                    FavoritesScreen(
                        onTimetableItemClick = {},
                    )
                }
            }
        }
        waitUntilIdle()
    }

    suspend fun setupFavoriteSession() {
        userDataStore.toggleFavorite(
            TimetableItemId(FakeSessionsApiClient.defaultSessionId),
        )
        waitUntilIdle()
    }

    fun checkTimetableListItemsDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertIsDisplayed()
        waitUntilIdle()
    }

    fun clickFirstSessionBookmark() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardBookmarkButtonTestTag))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    fun checkEmptyViewDisplayed() {
        composeTestRule
            .onNode(hasTestTag(FavoritesScreenEmptyViewTestTag))
            .assertIsDisplayed()
    }

    fun checkErrorSnackbarDisplayed() {
        composeTestRule
            .onNode(hasText("Fake IO Exception"))
            .isDisplayed()
    }
}
