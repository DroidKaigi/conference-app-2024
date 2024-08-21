package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

sealed interface RoomTheme {
    data object Iguana : RoomTheme {
        override val primaryColor = Color(0xFF45E761)
        override val containerColor = Color(0xFF45E761).copy(alpha = 0.1f)
        override val dimColor = Color(0xFF132417)
    }

    data object Hedgehog : RoomTheme {
        override val primaryColor = Color(0xFFFF974B)
        override val containerColor = Color(0xFFFF974B).copy(alpha = 0.1f)
        override val dimColor = Color(0xFF251C15)
    }

    data object Giraffe : RoomTheme {
        override val primaryColor = Color(0xFFDDD33C)
        override val containerColor = Color(0xFFDDD33C).copy(alpha = 0.1f)
        override val dimColor = Color(0xFF222213)
    }

    data object Flamingo : RoomTheme {
        override val primaryColor = Color(0xFFBB85FF)
        override val containerColor = Color(0xFFBB85FF).copy(alpha = 0.1f)
        override val dimColor = Color(0xFF1E1A27)
    }

    data object Jellyfish : RoomTheme {
        override val primaryColor = Color(0xFF44ADE7)
        override val containerColor = Color(0xFF44ADE7).copy(alpha = 0.1f)
        override val dimColor = Color(0xFF121E25)
    }

    data object Lunch : RoomTheme {
        override val primaryColor = Color(0xFFC1C8C9)
        override val containerColor = Color(0xFFC1C8C9).copy(alpha = 0.1f)
        override val dimColor = Color(0xFF121E25)
    }

    val primaryColor: Color
    val containerColor: Color
    val dimColor: Color

    companion object {
        fun ofOrNull(roomName: String): RoomTheme? {
            return when (roomName.lowercase()) {
                "iguana" -> Iguana
                "hedgehog" -> Hedgehog
                "giraffe" -> Giraffe
                "flamingo" -> Flamingo
                "jellyfish" -> Jellyfish
                "lunch" -> Lunch
                else -> null
            }
        }
    }
}

@Suppress("CompositionLocalAllowlist")
val LocalRoomTheme: ProvidableCompositionLocal<RoomTheme> = staticCompositionLocalOf<RoomTheme> {
    error("No RoomTheme provided")
}

@Composable
fun ProvideRoomTheme(roomName: String, content: @Composable () -> Unit) {
    val roomTheme = RoomTheme.ofOrNull(roomName) ?: RoomTheme.Iguana
    CompositionLocalProvider(LocalRoomTheme provides roomTheme) {
        content()
    }
}
