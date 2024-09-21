package io.github.droidkaigi.confsched.testing.robot.action

import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import io.github.droidkaigi.confsched.droidkaigiui.Inject
import io.github.droidkaigi.confsched.sessions.component.DropdownFilterChipTestTagPrefix
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersFilterCategoryChipTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersFilterDayChipTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersFilterLanguageChipTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchFiltersLazyRowTestTag
import io.github.droidkaigi.confsched.sessions.component.SearchTextFieldAppBarTextFieldTestTag
import io.github.droidkaigi.confsched.testing.robot.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.robot.ScreenRobot
import io.github.droidkaigi.confsched.testing.robot.TimetableItemCardRobot.Language
import io.github.droidkaigi.confsched.testing.robot.core.DemoSearchWord
import io.github.droidkaigi.confsched.testing.robot.core.SearchScreenCoreRobot.Category
import io.github.droidkaigi.confsched.testing.robot.core.SearchScreenCoreRobot.ConferenceDay
import io.github.droidkaigi.confsched.testing.utils.hasTestTag

class SearchScreenActionRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
) : ScreenRobot by screenRobot {
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
}
