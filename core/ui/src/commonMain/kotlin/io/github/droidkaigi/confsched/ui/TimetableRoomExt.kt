package io.github.droidkaigi.confsched.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.droidkaigi.confsched.model.TimetableRoom

val TimetableRoom.icon: ImageVector
    get() {
        // TODO: Replace with the real icons. Probably need to embed them.
        return when (name.enTitle) {
            "Flamingo" -> Icons.Filled.Square
            "Giraffe" -> Icons.Filled.Circle
            "Hedgehog" -> Icons.Filled.Star
            "Iguana" -> Icons.Filled.Thermostat
            "Jellyfish" -> Icons.Filled.Star
            else -> Icons.Filled.Star
        }
    }
