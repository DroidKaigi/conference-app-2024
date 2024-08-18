package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sessions.SearchScreen
import io.github.droidkaigi.confsched.sessions.component.SearchTextFieldAppBarTextFieldTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableListTestTag
import io.github.droidkaigi.confsched.ui.Inject
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardTitleTextTestTag

class SearchScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot {
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

    fun inputSearchWord(text: String) {
        composeTestRule
            .onNodeWithTag(SearchTextFieldAppBarTextFieldTestTag)
            .performTextInput(text)
        waitUntilIdle()
    }

    fun checkSearchWordDisplayed(text: String) {
        composeTestRule
            .onNodeWithTag(SearchTextFieldAppBarTextFieldTestTag)
            .assertTextEquals(text)
    }

    fun checkTimetableListItemsHasText(searchWord: String) {
        composeTestRule
            .onAllNodesWithTag(TimetableItemCardTitleTextTestTag)
            .assertAll(hasText(text = searchWord, ignoreCase = true))
    }

    fun checkTimetableListDisplayed() {
        composeTestRule
            .onNode(hasTestTag(TimetableListTestTag))
            .assertIsDisplayed()
    }

    fun checkTimetableListItemsDisplayed() {
        composeTestRule
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertIsDisplayed()
    }
}
