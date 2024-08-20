package io.github.droidkaigi.confsched.model

public data class Filters(
    val days: List<DroidKaigi2024Day> = emptyList(),
    val categories: List<TimetableCategory> = emptyList(),
    val sessionTypes: List<TimetableSessionType> = emptyList(),
    val languages: List<Lang> = emptyList(),
    val filterFavorite: Boolean = false,
    val searchWord: String = "",
) {

    /**
     * Checks if all filtering criteria are empty.
     *
     * @return True if all criteria are empty; false otherwise.
     */
    fun isEmpty() = days.isEmpty() &&
        categories.isEmpty() &&
        sessionTypes.isEmpty() &&
        languages.isEmpty() &&
        filterFavorite.not() &&
        searchWord.isEmpty()

    fun isNotEmpty() = isEmpty().not()
}
