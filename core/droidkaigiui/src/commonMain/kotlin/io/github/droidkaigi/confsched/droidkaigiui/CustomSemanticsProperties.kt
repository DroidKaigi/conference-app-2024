package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.ui.semantics.SemanticsPropertyKey
import io.github.droidkaigi.confsched.model.Lang

object CustomSemanticsProperties {
    val SessionLanguage = SemanticsPropertyKey<Lang>("SessionLanguage")
}
