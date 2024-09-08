package io.github.droidkaigi.confsched.testing.robot.verify

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import io.github.droidkaigi.confsched.droidkaigiui.Inject
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCardTitleTextTestTag
import io.github.droidkaigi.confsched.sessions.component.DropdownFilterChipTestTagPrefix
import io.github.droidkaigi.confsched.sessions.component.SearchTextFieldAppBarTextFieldTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableListTestTag
import io.github.droidkaigi.confsched.testing.robot.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.robot.ScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.SearchScreenCoreRobot.Category
import io.github.droidkaigi.confsched.testing.robot.core.SearchScreenCoreRobot.ConferenceDay
import io.github.droidkaigi.confsched.testing.robot.TimetableItemCardRobot.Language
import io.github.droidkaigi.confsched.testing.robot.core.DemoSearchWord
import io.github.droidkaigi.confsched.testing.utils.assertCountAtLeast
import io.github.droidkaigi.confsched.testing.utils.assertTextDoesNotContain
import io.github.droidkaigi.confsched.testing.utils.hasTestTag

class SearchScreenVerifyRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
) : ScreenRobot by screenRobot {
    fun checkDisplayedFilterDayChip() {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .assertCountAtLeast(ConferenceDay.entries.size)
    }

    fun checkDisplayedFilterCategoryChip() {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .assertCountAtLeast(Category.entries.size)
        waitUntilIdle()
    }

    fun checkDisplayedFilterLanguageChip() {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .assertCountAtLeast(Language.entries.size)
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

    fun checkTimetableListItemByCategory(
        category: Category,
    ) {
        val containText = if (category == Category.Other) {
            "Demo Welcome Talk"
        } else {
            category.categoryName
        }

        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertTextContains(
                value = containText,
                substring = true,
            )
        waitUntilIdle()
    }

    fun checkDemoSearchWordDisplayed() {
        checkSearchWordDisplayed(DemoSearchWord)
    }

    private fun checkSearchWordDisplayed(text: String) {
        composeTestRule
            .onNodeWithTag(SearchTextFieldAppBarTextFieldTestTag)
            .assertTextEquals(text)
    }

    fun checkTimetableListItemsHasDemoText() {
        checkTimetableListItemsHasText(DemoSearchWord)
    }

    private fun checkTimetableListItemsHasText(searchWord: String) {
        composeTestRule
            .onAllNodesWithTag(TimetableItemCardTitleTextTestTag)
            .assertAll(hasText(text = searchWord, ignoreCase = true))
    }

    fun checkTimetableListExists() {
        composeTestRule
            .onNode(hasTestTag(TimetableListTestTag))
            .assertExists()
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

    fun checkTimetableListItemsNotDisplayed() {
        composeTestRule
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertIsNotDisplayed()
    }
}
