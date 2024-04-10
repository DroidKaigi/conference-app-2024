package io.github.droidkaigi.confsched.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.droidkaigi.confsched.compose.safeCollectAsState
import io.github.droidkaigi.confsched.model.AchievementRepository
import io.github.droidkaigi.confsched.model.localAchievementRepository
import kotlinx.coroutines.flow.Flow

sealed interface MainScreenEvent {}

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
