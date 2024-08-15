package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.profilecard.ProfileCardCardScreenTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardCreateButtonTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardEditButtonTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardEditScreenTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardNicknameTextFieldTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardOccupationTextFieldTestTag
import io.github.droidkaigi.confsched.profilecard.ProfileCardScreen
import javax.inject.Inject

class ProfileCardScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    profileCardRepositoryRobot: DefaultProfileCardRepositoryRobot,
) : ScreenRobot by screenRobot,
    ProfileCardRepositoryRobot by profileCardRepositoryRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                ProfileCardScreen()
            }
        }
        waitUntilIdle()
    }

    fun checkEditScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardEditScreenTestTag))
            .assertIsDisplayed()
    }

    fun inputNickName(nickName: String) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardNicknameTextFieldTestTag))
            .performTextInput(nickName)
    }

    fun checkNickName(nickName: String) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardNicknameTextFieldTestTag))
            .assertTextEquals(nickName)
    }

    fun inputOccupation(occupation: String) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardOccupationTextFieldTestTag))
            .performTextInput(occupation)
    }

    fun checkOccupation(occupation: String) {
        composeTestRule
            .onNode(hasTestTag(ProfileCardOccupationTextFieldTestTag))
            .assertTextEquals(occupation)
    }

    fun clickCreateButton() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardCreateButtonTestTag))
            .performClick()
        wait5Seconds()
    }

    fun checkCardScreenDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardCardScreenTestTag))
            .assertIsDisplayed()
    }

    fun clickEditButton() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardEditButtonTestTag))
            .performClick()
        wait5Seconds()
    }

    fun scrollProfile() {
        composeTestRule
            .onNode(hasTestTag(ProfileCardEditScreenTestTag))
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height * 4F / 5,
                    endY = visibleSize.height / 5F,
                )
            }
    }
}
