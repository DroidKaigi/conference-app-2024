package io.github.droidkaigi.confsched.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.droidkaigi.confsched.compose.safeCollectAsState
import io.github.droidkaigi.confsched.model.AchievementRepository
import io.github.droidkaigi.confsched.model.localAchievementRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface MainScreenEvent {}

@Composable
fun mainScreenPresenter(
    events: Flow<MainScreenEvent>,
    achievementRepository: AchievementRepository = localAchievementRepository(),
): MainScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    val isAchievementsEnabled: Boolean by achievementRepository
        .getAchievementEnabledStream()
        .safeCollectAsState(
            initial = false
        )
    MainScreenUiState(
        isAchievementsEnabled = isAchievementsEnabled,
        userMessageStateHolder = userMessageStateHolder,
    )
}
