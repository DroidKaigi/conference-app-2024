package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.github.takahirom.roborazzi.Dump
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailBookmarkIconTestTag
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailMessageRowTestTag
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailMessageRowTextTestTag
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreen
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.sessions.component.DescriptionMoreButtonTestTag
import io.github.droidkaigi.confsched.sessions.component.SummaryCardTextTag
import io.github.droidkaigi.confsched.sessions.component.TargetAudienceSectionTestTag
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailContentArchiveSectionSlideButtonTestTag
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailContentArchiveSectionTestTag
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailContentArchiveSectionVideoButtonTestTag
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailContentTargetAudienceSectionBottomTestTag
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailHeadlineTestTag
import io.github.droidkaigi.confsched.sessions.navigation.TimetableItemDetailDestination
import javax.inject.Inject

class TimetableItemDetailScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
    private val fontScaleRobot: DefaultFontScaleRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot,
    FontScaleRobot by fontScaleRobot {

    suspend fun setupScreenContent(
        sessionId: String = FakeSessionsApiClient.defaultSessionId,
    ) {
        robotTestRule.setContentWithNavigation<TimetableItemDetailDestination>(
            startDestination = { TimetableItemDetailDestination(sessionId) },
        ) {
            KaigiTheme {
                TimetableItemDetailScreen(
                    onNavigationIconClick = { },
                    onLinkClick = { },
                    onCalendarRegistrationClick = { },
                    onShareClick = { },
                    onFavoriteListClick = { },
                )
            }
        }
        waitUntilIdle()
    }

    suspend fun setupScreenContentWithLongDescription() =
        setupScreenContent(FakeSessionsApiClient.defaultSessionIdWithLongDescription)

    suspend fun clickBookmarkButton() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailBookmarkIconTestTag))
            .performClick()
        waitUntilIdle()
    }

    fun scroll() {
        composeTestRule
            .onRoot()
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height * 3F / 4,
                    endY = visibleSize.height / 4F,
                )
            }
    }

    fun scrollLazyColumnByIndex(
        index: Int,
    ) {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailScreenLazyColumnTestTag))
            .performScrollToIndex(index)
    }

    fun scrollToMiddleOfScreen() {
        composeTestRule
            .onRoot()
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height / 2F,
                    endY = visibleSize.height / 7F,
                )
            }
    }

    fun scrollToBeforeAssetSection() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailScreenLazyColumnTestTag))
            .performScrollToNode(
                hasTestTag(
                    TimetableItemDetailContentTargetAudienceSectionBottomTestTag,
                ),
            )
    }

    fun scrollToAssetSection() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailScreenLazyColumnTestTag))
            .performScrollToNode(hasTestTag(TimetableItemDetailContentArchiveSectionTestTag))
    }

    fun scrollToMessageRow() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailScreenLazyColumnTestTag))
            .performScrollToNode(hasTestTag(TimetableItemDetailMessageRowTestTag))

        // FIXME Without this, you won't be able to scroll to the exact middle of the message section.
        composeTestRule.onRoot().performTouchInput {
            swipeUp(startY = centerY, endY = centerY - 175)
        }
        waitUntilIdle()
    }

    fun checkScreenCapture() {
        composeTestRule
            .onNode(isRoot())
            .captureRoboImage()
    }

    fun checkAccessibilityCapture() {
        composeTestRule
            .onRoot()
            .captureRoboImage(
                roborazziOptions = RoborazziOptions(
                    captureType = RoborazziOptions.CaptureType.Dump(
                        explanation = Dump.AccessibilityExplanation,
                    ),
                ),
            )
    }

    fun checkSessionDetailTitle() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailHeadlineTestTag))
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals(FakeSessionsApiClient.defaultSession.title.ja!!)
    }

    fun checkBookmarkedSession() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailBookmarkIconTestTag))
            .assertContentDescriptionEquals("Bookmarked")
    }

    fun checkUnBookmarkSession() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailBookmarkIconTestTag))
            .assertContentDescriptionEquals("Not Bookmarked")
    }

    fun checkTargetAudience() {
        composeTestRule
            .onNode(hasTestTag(TargetAudienceSectionTestTag))
            .onChildren()
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Target Audience")
    }

    fun checkSummaryCardTexts() {
        val titles = listOf(
            "Date/Time",
            "Location",
            "Supported Languages",
            "Category",
        )
        titles.forEach { title ->
            composeTestRule
                .onNode(hasTestTag(SummaryCardTextTag.plus(title)))
                .assertExists()
                .assertIsDisplayed()
                .assertTextContains(
                    value = title,
                    substring = true,
                )
        }
    }

    fun checkDisplayingMoreButton() {
        composeTestRule
            .onNode(hasTestTag(DescriptionMoreButtonTestTag))
            .assertExists()
            .assertIsDisplayed()
    }

    fun checkBothAssetButtonDisplayed() {
        checkSlideAssetButtonDisplayed()
        checkVideoAssetButtonDisplayed()
    }

    fun checkAssetSectionDoesNotDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemDetailContentArchiveSectionTestTag))
            .onFirst()
            .assertIsNotDisplayed()
    }

    fun checkOnlySlideAssetButtonDisplayed() {
        checkSlideAssetButtonDisplayed()
        checkVideoAssetButtonDoesNotDisplayed()
    }

    fun checkOnlyVideoAssetButtonDisplayed() {
        checkSlideAssetButtonDoesNotDisplayed()
        checkVideoAssetButtonDisplayed()
    }

    private fun checkSlideAssetButtonDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag))
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    private fun checkVideoAssetButtonDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag))
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    private fun checkSlideAssetButtonDoesNotDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag))
            .onFirst()
            .assertIsNotDisplayed()
            .assertDoesNotExist()
    }

    private fun checkVideoAssetButtonDoesNotDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag))
            .onFirst()
            .assertIsNotDisplayed()
            .assertDoesNotExist()
    }

    fun checkMessageDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemDetailMessageRowTextTestTag))
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("This session has been canceled.")
    }

    companion object {
        val defaultSessionId = FakeSessionsApiClient.defaultSession.id
    }
}
