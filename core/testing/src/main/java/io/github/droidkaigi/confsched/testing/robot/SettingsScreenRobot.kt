package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.dotGothic16FontFamily
import io.github.droidkaigi.confsched.model.FontFamily.DotGothic16Regular
import io.github.droidkaigi.confsched.model.FontFamily.SystemDefault
import io.github.droidkaigi.confsched.model.Settings.DoesNotExists
import io.github.droidkaigi.confsched.model.Settings.Exists
import io.github.droidkaigi.confsched.model.Settings.Loading
import io.github.droidkaigi.confsched.settings.SettingsScreen
import io.github.droidkaigi.confsched.settings.component.SettingsItemRowCurrentValueTextTestTag
import io.github.droidkaigi.confsched.settings.section.SettingsAccessibilityUseFontFamilyTestTag
import io.github.droidkaigi.confsched.settings.section.SettingsAccessibilityUseFontFamilyTestTagPrefix
import javax.inject.Inject

class SettingsScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    settingsDataStoreRobot: DefaultSettingsDataStoreRobot,
) : ScreenRobot by screenRobot,
    SettingsDataStoreRobot by settingsDataStoreRobot {
    private enum class FontFamily(
        val displayName: String,
    ) {
        DotGothic16Regular("DotGothic"),
        SystemDefault("System Default"),
    }

    fun setupScreenContent() {
        robotTestRule.setContent {
            val settings by remember { get() }.safeCollectAsRetainedState(Loading)

            val fontFamily = when (settings) {
                DoesNotExists, Loading -> dotGothic16FontFamily()
                is Exists -> {
                    when ((settings as Exists).useFontFamily) {
                        DotGothic16Regular -> dotGothic16FontFamily()
                        SystemDefault -> null
                    }
                }
            }

            KaigiTheme(
                fontFamily = fontFamily,
            ) {
                SettingsScreen(
                    onNavigationIconClick = {},
                )
            }
        }
        waitUntilIdle()
    }

    fun clickUseFontItem() {
        composeTestRule
            .onNode(hasTestTag(SettingsAccessibilityUseFontFamilyTestTag))
            .performClick()
        waitUntilIdle()
    }

    fun clickSystemDefaultFontItem() {
        composeTestRule
            .onNode(hasTestTag(SettingsAccessibilityUseFontFamilyTestTagPrefix.plus(FontFamily.SystemDefault.displayName)))
            .performClick()
        waitUntilIdle()
    }

    fun clickDotGothicFontItem() {
        composeTestRule
            .onNode(hasTestTag(SettingsAccessibilityUseFontFamilyTestTagPrefix.plus(FontFamily.DotGothic16Regular.displayName)))
            .performClick()
        waitUntilIdle()
    }

    fun checkAllDisplayedAvailableFont() {
        FontFamily.entries.forEach { fontFamily ->
            composeTestRule
                .onNode(hasTestTag(SettingsAccessibilityUseFontFamilyTestTagPrefix.plus(fontFamily.displayName)))
                .assertIsDisplayed()
                .assertExists()
                .assertTextEquals(fontFamily.displayName)
        }
    }

    fun checkEnsureThatSystemDefaultFontIsUsed() {
        composeTestRule
            .onNode(
                matcher = hasTestTag(SettingsItemRowCurrentValueTextTestTag),
                useUnmergedTree = true,
            )
            .assertTextEquals(FontFamily.SystemDefault.displayName)
    }

    fun checkEnsureThatDotGothicFontIsUsed() {
        composeTestRule
            .onNode(
                matcher = hasTestTag(SettingsItemRowCurrentValueTextTestTag),
                useUnmergedTree = true,
            )
            .assertTextEquals(FontFamily.DotGothic16Regular.displayName)
    }
}
