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
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow

sealed interface FavoritesScreenEvent {
    data class Bookmark(val timetableItem: TimetableItem) : FavoritesScreenEvent

    data object AllFilter : FavoritesScreenEvent
    data object Day1Filter : FavoritesScreenEvent
    data object Day2Filter : FavoritesScreenEvent
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
    var currentDayFilters by remember { mutableStateOf(emptySet<DroidKaigi2024Day>()) }
    val favoritesSheetUiState by rememberUpdatedState(
        favoritesSheet(
            favoriteSessions = favoriteSessions,
            allFilterSelected = allFilterSelected,
            selectedDayFilters = currentDayFilters.toPersistentSet(),
        ),
    )

    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Bookmark -> {
                    sessionsRepository.toggleBookmark(event.timetableItem.id)
                }

                AllFilter -> {
                    allFilterSelected = true
                    currentDayFilters = emptySet()
                }

                Day1Filter, Day2Filter -> {
                    allFilterSelected = false

                    val dayType = if (event is Day1Filter) {
                        ConferenceDay1
                    } else {
                        ConferenceDay2
                    }

                    currentDayFilters = if (currentDayFilters.contains(dayType) && currentDayFilters.size >= 2) {
                        currentDayFilters - dayType
                    } else {
                        currentDayFilters + dayType
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
    selectedDayFilters: PersistentSet<DroidKaigi2024Day>,
): FavoritesSheetUiState {
    val filteredSessions by rememberUpdatedState(
        favoriteSessions
            .filtered(
                Filters(
                    filterFavorite = true,
                    days = selectedDayFilters.toList(),
                ),
            ),
    )

    return if (filteredSessions.isEmpty()) {
        FavoritesSheetUiState.Empty(
            currentDayFilter = selectedDayFilters.toPersistentList(),
            allFilterSelected = allFilterSelected,
        )
    } else {
        FavoritesSheetUiState.FavoriteListUiState(
            currentDayFilter = selectedDayFilters.toPersistentList(),
            allFilterSelected = allFilterSelected,
            timeTable = filteredSessions,
        )
    }
}
