package io.github.droidkaigi.confsched.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched.compose.safeCollectAsState
import io.github.droidkaigi.confsched.model.AchievementRepository
import io.github.droidkaigi.confsched.model.localAchievementRepository
import io.github.droidkaigi.confsched.ui.ComposeViewModel
import io.github.droidkaigi.confsched.ui.KmpViewModelLifecycle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

sealed interface MainScreenEvent {}

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val savedStateHandle: SavedStateHandle,
    private val viewModelLifecycle: KmpViewModelLifecycle,
) : ViewModel(),
    ComposeViewModel<MainScreenEvent, MainScreenUiState> by ComposeViewModel(
        viewModelLifecycle = viewModelLifecycle,
        content = { events ->
            mainScreenViewModel(
                events = events,
                achievementRepository = achievementRepository,
            )
        },
    )

@Composable
fun mainScreenViewModel(
    events: Flow<MainScreenEvent>,
    achievementRepository: AchievementRepository = localAchievementRepository(),
): MainScreenUiState {
    val isAchievementsEnabled: Boolean by achievementRepository
        .getAchievementEnabledStream()
        .safeCollectAsState(
            initial = false
        )
    return MainScreenUiState(
        isAchievementsEnabled = isAchievementsEnabled,
    )
}
