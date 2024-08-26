package io.github.droidkaigi.confsched.droidkaigiui

import conference_app_2024.core.designsystem.generated.resources.ic_circle
import conference_app_2024.core.designsystem.generated.resources.ic_diamond
import conference_app_2024.core.designsystem.generated.resources.ic_rhombus
import conference_app_2024.core.designsystem.generated.resources.ic_square
import conference_app_2024.core.designsystem.generated.resources.ic_triangle
import io.github.droidkaigi.confsched.designsystem.DesignSystemRes
import io.github.droidkaigi.confsched.model.RoomIcon
import io.github.droidkaigi.confsched.model.RoomIcon.Circle
import io.github.droidkaigi.confsched.model.RoomIcon.Diamond
import io.github.droidkaigi.confsched.model.RoomIcon.None
import io.github.droidkaigi.confsched.model.RoomIcon.Rhombus
import io.github.droidkaigi.confsched.model.RoomIcon.Square
import io.github.droidkaigi.confsched.model.RoomIcon.Triangle
import org.jetbrains.compose.resources.DrawableResource

fun RoomIcon.toResDrawable(): DrawableResource? = when (this) {
    Square -> DesignSystemRes.drawable.ic_square
    Circle -> DesignSystemRes.drawable.ic_circle
    Diamond -> DesignSystemRes.drawable.ic_diamond
    Rhombus -> DesignSystemRes.drawable.ic_rhombus
    Triangle -> DesignSystemRes.drawable.ic_triangle
    None -> null
}
