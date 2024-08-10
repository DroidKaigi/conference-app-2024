package io.github.droidkaigi.confsched.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.favorites.FavoritesScreenEvent.AllFilter
import io.github.droidkaigi.confsched.favorites.FavoritesScreenEvent.Bookmark
import io.github.droidkaigi.confsched.favorites.FavoritesScreenEvent.Day1Filter
import io.github.droidkaigi.confsched.favorites.FavoritesScreenEvent.Day2Filter
import io.github.droidkaigi.confsched.favorites.section.FavoritesSheetUiState
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day.ConferenceDay1
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day.ConferenceDay2
import io.github.droidkaigi.confsched.model.Filters
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.localSessionsRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface FavoritesScreenEvent {
    data class Bookmark(val timetableItem: TimetableItem): FavoritesScreenEvent

    data object AllFilter: FavoritesScreenEvent
    data object Day1Filter: FavoritesScreenEvent
    data object Day2Filter: FavoritesScreenEvent
}

@Composable
fun favoritesScreenPresenter(
    events: Flow<FavoritesScreenEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
): FavoritesScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    val favoriteSessions by rememberUpdatedState(
        sessionsRepository
            .timetable()
            .filtered(Filters(filterFavorite = true)),
    )
    var allFilterSelected by remember { mutableStateOf(true) }
    var selectedDayFilters by remember { mutableStateOf(emptySet<DroidKaigi2024Day>()) }
    val favoritesSheetUiState by rememberUpdatedState(
        favoritesSheet(
            favoriteSessions = favoriteSessions,
            allFilterSelected = allFilterSelected,
            selectedDayFilters = selectedDayFilters,
        )
    )

    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Bookmark -> {
                    sessionsRepository.toggleBookmark(event.timetableItem.id)
                }

                AllFilter -> {
                    if (allFilterSelected.not()) {
                        allFilterSelected = true
                        selectedDayFilters = emptySet()
                    }
                }

                Day1Filter, Day2Filter -> {
                    val dayType = when (event) {
                        Day1Filter -> ConferenceDay1
                        Day2Filter -> ConferenceDay2
                        else -> throw IllegalStateException()
                    }
                    if (selectedDayFilters.contains(dayType)) {
                        selectedDayFilters = selectedDayFilters - dayType
                        if (selectedDayFilters.isEmpty()) {
                            allFilterSelected = true
                        }
                    } else {
                        allFilterSelected = false
                        selectedDayFilters = selectedDayFilters + dayType
                    }
                }
            }
        }
    }

    FavoritesScreenUiState(
        favoritesSheetUiState = favoritesSheetUiState,
        userMessageStateHolder = userMessageStateHolder,
    )
}

@Composable
private fun favoritesSheet(
    favoriteSessions: Timetable,
    allFilterSelected: Boolean,
    selectedDayFilters: Set<DroidKaigi2024Day>,
): FavoritesSheetUiState {
    val filteredSessions by rememberUpdatedState(
        favoriteSessions
            .filtered(Filters(days = selectedDayFilters.toList())),
    )

    return if (favoriteSessions.isEmpty()) {
        FavoritesSheetUiState.Empty(
            allFilterSelected = allFilterSelected,
            day1FilterSelected = allFilterSelected.not() && selectedDayFilters.contains(ConferenceDay1),
            day2FilterSelected = allFilterSelected.not() && selectedDayFilters.contains(ConferenceDay2),
        )
    } else {
        FavoritesSheetUiState.FavoriteListUiState(
            allFilterSelected = allFilterSelected,
            day1FilterSelected = allFilterSelected.not() && selectedDayFilters.contains(ConferenceDay1),
            day2FilterSelected = allFilterSelected.not() && selectedDayFilters.contains(ConferenceDay2),
            timeTable = filteredSessions,
        )
    }
}
