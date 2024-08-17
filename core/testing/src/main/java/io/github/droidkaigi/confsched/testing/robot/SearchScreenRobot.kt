package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sessions.SearchScreen
import io.github.droidkaigi.confsched.sessions.component.DropdownFilterChipTestTagPrefix
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersFilterDayChipTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchTextFieldAppBarTextFieldTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableListTestTag
import io.github.droidkaigi.confsched.testing.utils.assertCountAtLeast
import io.github.droidkaigi.confsched.testing.utils.assertTextDoesNotContain
import io.github.droidkaigi.confsched.testing.utils.hasTestTag
import io.github.droidkaigi.confsched.ui.Inject
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardTitleTextTestTag

class SearchScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot {
        enum class ConferenceDay(
            val day: Int,
            val dateText: String,
        ) {
            Day1(1, "9/12"),
            Day2(2, "9/13"),
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

    fun inputSearchWord(text: String) {
        composeTestRule
            .onNodeWithTag(SearchTextFieldAppBarTextFieldTestTag)
            .performTextInput(text)
        waitUntilIdle()
    }

    fun clickFilterDayChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersFilterDayChipTestTag))
            .performClick()
        waitUntilIdle()
    }

    fun clickConferenceDay(
        clickDay: ConferenceDay
    ) {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                )
            )
            .filter(matcher = hasText(clickDay.dateText))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    fun checkDisplayedFilterDayChip() {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                )
            )
            .assertCountAtLeast(ConferenceDay.entries.size)
    }

    fun checkTimetableListItemByConferenceDay(
        checkDay: ConferenceDay,
    ) {
        val (contain, doesNotContain) = if (checkDay == ConferenceDay.Day1) {
            listOf(ConferenceDay.Day1, ConferenceDay.Day2)
        } else {
            listOf(ConferenceDay.Day2, ConferenceDay.Day1)
        }

        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertTextContains("Demo Welcome Talk ${contain.day}")
            .assertTextDoesNotContain("Demo Welcome Talk ${doesNotContain.day}")
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
