package io.github.droidkaigi.confsched.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.Filters
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.localSessionsRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface FavoritesScreenEvent

@Composable
fun favoritesScreenPresenter(
    events: Flow<FavoritesScreenEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
): FavoritesScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    val favoriteSessions = sessionsRepository
        .timetable()
        .filtered(Filters(filterFavorite = true))
    val favoritesSheetUiState by rememberUpdatedState(
        favoritesSheet(
            favoriteSessions = favoriteSessions,
        )
    )

    SafeLaunchedEffect(Unit) {
        events.collect {}
    }

    FavoritesScreenUiState(
        favoritesSheetUiState = favoritesSheetUiState,
        userMessageStateHolder = userMessageStateHolder,
    )
}

@Composable
private fun favoritesSheet(favoriteSessions: Timetable): FavoritesSheetUiState {
    return if (favoriteSessions.isEmpty()) {
        FavoritesSheetUiState.Empty(
            allFilterSelected = true,
            workDayFilterSelected = true,
            day1FilterSelected = true,
            day2FilterSelected = true,
        )
    } else {
        FavoritesSheetUiState.FavoriteListUiState(
            allFilterSelected = true,
            workDayFilterSelected = true,
            day1FilterSelected = true,
            day2FilterSelected = true,
            timeTable = favoriteSessions,
        )
    }
}
