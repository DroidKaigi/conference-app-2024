package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

sealed interface ProfileCardScreenTheme {
    data object Default : ProfileCardScreenTheme {
        override val primaryColor = Color(0xFFB4FF79)
        override val containerColor = Color(0xFFC0FF8E)
    }

    data object Orange : ProfileCardScreenTheme {
        override val primaryColor = Color(0xFFFEB258)
        override val containerColor = Color(0xFFFFBB69)
    }

    data object Yellow : ProfileCardScreenTheme {
        override val primaryColor = Color(0xFFFCF65F)
        override val containerColor = Color(0xFFFFFA77)
    }

    data object Pink : ProfileCardScreenTheme {
        override val primaryColor = Color(0xFF6FD7F8)
        override val containerColor = Color(0xFFFFA0C9)
    }

    data object Blue : ProfileCardScreenTheme {
        override val primaryColor = Color(0xFFB4FF79)
        override val containerColor = Color(0xFF93E5FF)
    }

    data object White : ProfileCardScreenTheme {
        override val primaryColor = Color(0xFFF9F9F9)
        override val containerColor = Color.White
    }

    val primaryColor: Color
    val containerColor: Color

    companion object {
        fun ofOrNull(profileCardTheme: String): ProfileCardScreenTheme? {
            return when (profileCardTheme) {
                "Iguana" -> Default
                "Hedgehog" -> Orange
                "Giraffe" -> Yellow
                "Flamingo" -> Pink
                "Jellyfish" -> Blue
                else -> White
            }
        }
    }
}

@Suppress("CompositionLocalAllowlist")
val LocalProfileCardScreenTheme: ProvidableCompositionLocal<ProfileCardScreenTheme> =
    staticCompositionLocalOf {
        error("No RoomTheme provided")
    }

@Composable
fun ProvideProfileCardScreenTheme(profileCardTheme: String, content: @Composable () -> Unit) {
    val profileCardTheme = ProfileCardScreenTheme.ofOrNull(profileCardTheme) ?: ProfileCardScreenTheme.Default
    CompositionLocalProvider(LocalProfileCardScreenTheme provides profileCardTheme) {
        content()
    }
}
