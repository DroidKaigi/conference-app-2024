package io.github.droidkaigi.confsched.droidkaigiui

import io.github.droidkaigi.confsched.model.RoomIcon
import io.github.droidkaigi.confsched.model.TimetableRoom
import org.jetbrains.compose.resources.DrawableResource

val TimetableRoom.icon: DrawableResource?
    get() {
        return when (name.enTitle) {
            "Flamingo" -> RoomIcon.Rhombus.toResDrawable()
            "Giraffe" -> RoomIcon.Circle.toResDrawable()
            "Hedgehog" -> RoomIcon.Diamond.toResDrawable()
            "Iguana" -> RoomIcon.Square.toResDrawable()
            "Jellyfish" -> RoomIcon.Triangle.toResDrawable()
            else -> RoomIcon.Rhombus.toResDrawable()
        }
    }
