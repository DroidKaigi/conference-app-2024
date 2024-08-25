package io.github.droidkaigi.confsched.testing.robot

import android.graphics.RenderNode
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.profilecard.ProfileCardCardScreenTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardCreateButtonTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardEditButtonTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardEditScreenColumnTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardLinkTextFieldTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardNicknameTextFieldTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardOccupationTextFieldTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardScreen
import io.github.droidkaigi.confsched.profilecard.ProfileCardShareButtonTestTag
import io.github.droidkaigi.confsched.profilecard.component.ProfileCardFlipCardBackTestTag
import io.github.droidkaigi.confsched.profilecard.component.ProfileCardFlipCardFrontTestTag
import io.github.droidkaigi.confsched.profilecard.component.ProfileCardFlipCardTestTag
import org.robolectric.util.ReflectionHelpers
import javax.inject.Inject

class ProfileCardScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    profileCardRepositoryRobot: DefaultProfileCardDataStoreRobot,
) : ScreenRobot by screenRobot,
    ProfileCardDataStoreRobot by profileCardRepositoryRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                ProfileCardScreen(
                    onClickShareProfileCard = { _, _ -> },
                )
            }
        }
        waitUntilIdle()
        // Render correctly
        // See HardwareRenderingScreenshot.getRenderNode
        ReflectionHelpers
            .callInstanceMethod<RenderNode>(
                robotTestRule.composeTestRule.activity.window.decorView,
                "updateDisplayListIfDirty",
            )
        waitUntilIdle()
    }

    fun inputNickName(
        nickName: String,
    ) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardNicknameTextFieldTestTag))
            .performTextInput(nickName)
        wait5Seconds()
    }

    fun inputOccupation(
        occupation: String,
    ) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardOccupationTextFieldTestTag))
            .performTextInput(occupation)
        wait5Seconds()
    }

    fun inputLink(
        link: String,
    ) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardLinkTextFieldTestTag))
            .performTextInput(link)
        wait5Seconds()
    }

    fun scrollToTestTag(
        testTag: String,
    ) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardEditScreenColumnTestTag))
            .performScrollToNode(hasTestTag(testTag))
        waitUntilIdle()
    }

    fun clickEditButton() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardEditButtonTestTag))
            .performClick()
        wait5Seconds()
    }

    fun flipProfileCard() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardFlipCardTestTag))
            .performClick()
        waitUntilIdle()
    }

    fun checkCreateButtonDisabled() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardCreateButtonTestTag))
            .assertIsNotEnabled()
    }

    fun checkCreateButtonEnabled() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardCreateButtonTestTag))
            .assertIsEnabled()
    }

    fun checkEditScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardEditScreenColumnTestTag))
            .assertIsDisplayed()
    }

    fun checkNickName(
        nickName: String,
    ) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardNicknameTextFieldTestTag))
            .assertTextEquals(nickName)
    }

    fun checkOccupation(
        occupation: String,
    ) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardOccupationTextFieldTestTag))
            .assertTextEquals(occupation)
    }

    fun checkLink(
        link: String,
    ) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardLinkTextFieldTestTag))
            .assertTextEquals(link)
    }

    fun checkShareProfileCardButtonEnabled() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardShareButtonTestTag))
            .assertIsEnabled()
    }

    fun checkCardScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardCardScreenTestTag))
            .assertIsDisplayed()
    }

    fun checkProfileCardFrontDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardFlipCardFrontTestTag))
            .assertIsDisplayed()
    }

    fun checkProfileCardBackDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardFlipCardBackTestTag))
            .assertIsDisplayed()
    }
}
