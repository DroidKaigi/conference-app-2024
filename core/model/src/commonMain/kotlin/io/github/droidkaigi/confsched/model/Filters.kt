package io.github.droidkaigi.confsched.model

public data class Filters(
    val days: List<DroidKaigi2024Day> = emptyList(),
    val categories: List<TimetableCategory> = emptyList(),
    val sessionTypes: List<TimetableSessionType> = emptyList(),
    val languages: List<Lang> = emptyList(),
    val filterFavorite: Boolean = false,
    val searchWord: String = "",
) {

    fun isEmpty() = days.isEmpty() &&
        categories.isEmpty() &&
        sessionTypes.isEmpty() &&
        languages.isEmpty() &&
        searchWord.isEmpty()

    fun isNotEmpty() = isEmpty().not()
}
