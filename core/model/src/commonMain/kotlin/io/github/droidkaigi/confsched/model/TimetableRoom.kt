package io.github.droidkaigi.confsched.model

@Immutable
data class TimetableRooms(val rooms: List<TimetableRoom>)

data class TimetableRoom(
    val id: Int,
    val name: MultiLangText,
    val type: RoomType,
    val sort: Int,
) : Comparable<TimetableRoom> {
    override fun compareTo(other: TimetableRoom): Int {
        if (sort < 900 && other.sort < 900) {
            return name.currentLangTitle.compareTo(other.name.currentLangTitle)
        }
        return sort.compareTo(other.sort)
    }

    fun getThemeKey(isLunch: Boolean? = false): String = if (isLunch==true) "lunch" else name.enTitle.lowercase()

    // TODO: Names are updated but the shapes need to be checked
    fun getShape(): Shapes {
        return when (name.enTitle) {
            "Flamingo" -> {
                Shapes.SQUARE
            }
            "Giraffe" -> {
                Shapes.CIRCLE
            }
            "Hedgehog" -> {
                Shapes.SHARP_DIAMOND
            }
            "Iguana" -> {
                Shapes.DIAMOND
            }
            "Jellyfish" -> {
                Shapes.DIAMOND
            }
            else -> {
                Shapes.SQUARE
            }
        }
    }

    enum class Shapes {
        SQUARE,
        CIRCLE,
        SHARP_DIAMOND,
        DIAMOND,
    }
}

val TimetableRoom.nameAndFloor: String
    get() {
        val basementFloorString = MultiLangText(jaTitle = "地下1階", enTitle = "B1F")
        val floor = when (type) {
            RoomType.RoomF -> basementFloorString.currentLangTitle
            RoomType.RoomG -> basementFloorString.currentLangTitle
            RoomType.RoomH -> basementFloorString.currentLangTitle
            RoomType.RoomI -> "1F"
            RoomType.RoomJ -> "1F"
            // Assume the room on the first day.
            RoomType.RoomIJ -> "1F"
        }
        return "${name.currentLangTitle} ($floor)"
    }
