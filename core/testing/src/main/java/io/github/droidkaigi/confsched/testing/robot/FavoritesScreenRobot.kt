package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCardBookmarkButtonTestTag
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.favorites.FavoritesScreen
import io.github.droidkaigi.confsched.favorites.FavoritesScreenTestTag
import io.github.droidkaigi.confsched.favorites.section.FavoritesScreenEmptyViewTestTag
import io.github.droidkaigi.confsched.model.TimetableItemId
import javax.inject.Inject

class FavoritesScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
    private val userDataStore: UserDataStore,
    private val deviceSetupRobot: DefaultDeviceSetupRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot,
    DeviceSetupRobot by deviceSetupRobot {

    fun setupFavoritesScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                FavoritesScreen(
                    onTimetableItemClick = {},
                )
            }
        }
        waitUntilIdle()
    }

    suspend fun setupSingleFavoriteSession() {
        userDataStore.toggleFavorite(
            TimetableItemId(FakeSessionsApiClient.defaultSessionId),
        )
    }

    suspend fun setupFavoriteSessions() {
        FakeSessionsApiClient.defaultSessionIds.forEach {
            userDataStore.toggleFavorite(TimetableItemId(it))
        }
        waitUntilIdle()
    }

    fun checkTimetableListItemsDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertIsDisplayed()
        waitUntilIdle()
    }

    fun checkTimetableListFirstItemNotDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertIsNotDisplayed()
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

    fun scrollFavorites() {
        composeTestRule
            .onNode(hasTestTag(FavoritesScreenTestTag))
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height * 4F / 5,
                    endY = visibleSize.height / 5F,
                )
            }
    }
}
