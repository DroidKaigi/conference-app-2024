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

    fun getThemeKey(): String = name.enTitle.lowercase()
}

val TimetableRoom.nameAndFloor: String
    get() {
        val basementFloorString = MultiLangText(jaTitle = "地下1階", enTitle = "B1F")
        val floor1FString = MultiLangText(jaTitle = "1階", enTitle = "1F")
        val floor = when (type) {
            RoomType.RoomF -> floor1FString.currentLangTitle
            RoomType.RoomG -> floor1FString.currentLangTitle
            RoomType.RoomH -> basementFloorString.currentLangTitle
            RoomType.RoomI -> basementFloorString.currentLangTitle
            RoomType.RoomJ -> basementFloorString.currentLangTitle
            // Assume the room on the first day.
            RoomType.RoomIJ -> basementFloorString.currentLangTitle
        }
        return "${name.currentLangTitle} ($floor)"
    }
