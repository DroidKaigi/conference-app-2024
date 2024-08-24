package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.Inject
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCardTitleTextTestTag
import io.github.droidkaigi.confsched.sessions.SearchScreen
import io.github.droidkaigi.confsched.sessions.component.DropdownFilterChipTestTagPrefix
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersFilterCategoryChipTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersFilterDayChipTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersFilterLanguageChipTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersLazyRowTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchTextFieldAppBarTextFieldTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableListTestTag
import io.github.droidkaigi.confsched.testing.utils.assertCountAtLeast
import io.github.droidkaigi.confsched.testing.utils.assertTextDoesNotContain
import io.github.droidkaigi.confsched.testing.utils.hasTestTag

const val DemoSearchWord = "Demo"

class SearchScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
    private val deviceSetupRobot: DefaultDeviceSetupRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot,
    DeviceSetupRobot by deviceSetupRobot {
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

    enum class Language(
        val tagName: String,
    ) {
        MIXED("MIXED"),
        JAPANESE("JA"),
        ENGLISH("EN"),
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

    fun inputDemoSearchWord() {
        inputSearchWord(DemoSearchWord)
    }

    private fun inputSearchWord(text: String) {
        composeTestRule
            .onNodeWithTag(SearchTextFieldAppBarTextFieldTestTag)
            .performTextInput(text)
        waitUntilIdle()
    }

    fun scrollToFilterLanguageChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersLazyRowTestTag))
            .performScrollToNode(hasTestTag(SearchFiltersFilterLanguageChipTestTag))
        waitUntilIdle()
    }

    fun clickFilterDayChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersFilterDayChipTestTag))
            .performClick()
        waitUntilIdle()
    }

    fun clickFilterCategoryChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersFilterCategoryChipTestTag))
            .performClick()
        waitUntilIdle()
    }

    fun clickFilterLanguageChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersFilterLanguageChipTestTag))
            .performClick()
        waitUntilIdle()
    }

    fun clickConferenceDay(
        clickDay: ConferenceDay,
    ) {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .filter(matcher = hasText(clickDay.dateText))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    fun clickCategory(
        category: Category,
    ) {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .filter(matcher = hasText(category.categoryName))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    fun clickLanguage(
        language: Language,
    ) {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .filter(matcher = hasText(language.tagName))
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

    fun checkTimetableListItemByLanguage(
        language: Language,
    ) {
        val doesNotContains = Language.entries.filterNot { it == language }

        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertTextContains(language.tagName)

        doesNotContains.forEach { doesNotContain ->
            composeTestRule
                .onAllNodes(hasTestTag(TimetableItemCardTestTag))
                .onFirst()
                .assertTextDoesNotContain(doesNotContain.tagName)
        }
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
