package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.droidkaigi.confsched.designsystem.theme.hallColors
import io.github.droidkaigi.confsched.model.RoomType.RoomA
import io.github.droidkaigi.confsched.model.RoomType.RoomB
import io.github.droidkaigi.confsched.model.RoomType.RoomC
import io.github.droidkaigi.confsched.model.RoomType.RoomD
import io.github.droidkaigi.confsched.model.RoomType.RoomDE
import io.github.droidkaigi.confsched.model.RoomType.RoomE
import io.github.droidkaigi.confsched.model.TimetableRoom

internal val TimetableRoom.color: Color
    @Composable get() {
        val colors = hallColors()

        return when (type) {
            RoomA -> colors.hallA
            RoomB -> colors.hallB
            RoomC -> colors.hallC
            RoomD -> colors.hallD
            RoomE -> colors.hallE
            // The color of D is set as workaround.
            RoomDE -> colors.hallD
            else -> Color.White
        }
    }
