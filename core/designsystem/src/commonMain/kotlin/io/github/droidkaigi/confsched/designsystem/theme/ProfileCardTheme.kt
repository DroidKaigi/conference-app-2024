package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

sealed interface ProfileCardTheme {
    data object Default : ProfileCardTheme {
        override val primaryColor = Color(0xFFB4FF79)
        override val containerColor = Color(0xFFC0FF8E)
    }

    data object Orange : ProfileCardTheme {
        override val primaryColor = Color(0xFFFEB258)
        override val containerColor = Color(0xFFFFBB69)
    }

    data object Yellow : ProfileCardTheme {
        override val primaryColor = Color(0xFFFCF65F)
        override val containerColor = Color(0xFFFFFA77)
    }

    data object Pink : ProfileCardTheme {
        override val primaryColor = Color(0xFFFF8EBD)
        override val containerColor = Color(0xFFFFA0C9)
    }

    data object Blue : ProfileCardTheme {
        override val primaryColor = Color(0xFF6FD7F8)
        override val containerColor = Color(0xFF93E5FF)
    }

    data object White : ProfileCardTheme {
        override val primaryColor = Color(0xFFF9F9F9)
        override val containerColor = Color.White
    }

    val primaryColor: Color
    val containerColor: Color

    companion object {
        fun of(profileCardType: String): ProfileCardTheme {
            return when (profileCardType) {
                "Iguana" -> Default
                "Hedgehog" -> Orange
                "Giraffe" -> Yellow
                "Flamingo" -> Pink
                "Jellyfish" -> Blue
                "None" -> White
                else -> Default
            }
        }
    }
}

@Suppress("CompositionLocalAllowlist")
val LocalProfileCardTheme: ProvidableCompositionLocal<ProfileCardTheme> =
    staticCompositionLocalOf {
        error("No RoomTheme provided")
    }

@Composable
fun ProvideProfileCardTheme(profileCardType: String, content: @Composable () -> Unit) {
    val profileCardTheme = ProfileCardTheme.of(profileCardType)
    CompositionLocalProvider(LocalProfileCardTheme provides profileCardTheme) {
        content()
    }
}
