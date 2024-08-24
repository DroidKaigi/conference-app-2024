package io.github.droidkaigi.confsched.favorites

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.favorites.generated.resources.favorite
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.favorites.section.FavoriteSheet
import io.github.droidkaigi.confsched.favorites.section.FavoritesSheetUiState
import io.github.droidkaigi.confsched.favorites.section.FavoritesSheetUiState.FavoriteListUiState.TimeSlot
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.fake
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val favoritesScreenRoute = "favorites"
const val FavoritesScreenTestTag = "FavoritesScreenTestTag"

fun NavGraphBuilder.favoritesScreens(
    onTimetableItemClick: (TimetableItem) -> Unit,
    contentPadding: PaddingValues,
) {
    composable(favoritesScreenRoute) {
        FavoritesScreen(
            onTimetableItemClick = onTimetableItemClick,
            contentPadding = contentPadding,
        )
    }
}

fun NavController.navigateFavoritesScreen() {
    navigate(favoritesScreenRoute) {
        popUpTo(route = checkNotNull(graph.findStartDestination().route)) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

data class FavoritesScreenUiState(
    val favoritesSheetUiState: FavoritesSheetUiState,
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun FavoritesScreen(
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    eventFlow: EventFlow<FavoritesScreenEvent> = rememberEventFlow(),
    uiState: FavoritesScreenUiState = favoritesScreenPresenter(events = eventFlow),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = UserMessageStateHolderImpl(),
    )
    FavoritesScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onTimetableItemClick = onTimetableItemClick,
        onAllFilterChipClick = {
            eventFlow.tryEmit(FavoritesScreenEvent.AllFilter)
        },
        onDay1FilterChipClick = {
            eventFlow.tryEmit(FavoritesScreenEvent.Day1Filter)
        },
        onDay2FilterChipClick = {
            eventFlow.tryEmit(FavoritesScreenEvent.Day2Filter)
        },
        onBookmarkClick = { timetableItem ->
            eventFlow.tryEmit(FavoritesScreenEvent.Bookmark(timetableItem))
        },
        contentPadding = contentPadding,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreen(
    uiState: FavoritesScreenUiState,
    snackbarHostState: SnackbarHostState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onAllFilterChipClick: () -> Unit,
    onDay1FilterChipClick: () -> Unit,
    onDay2FilterChipClick: () -> Unit,
    onBookmarkClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val fraction = if (scrollBehavior.state.overlappedFraction > 0.01f) 1f else 0f

    val filterBackgroundColor by animateColorAsState(
        targetValue = lerp(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceContainer,
            FastOutLinearInEasing.transform(fraction),
        ),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
    )

    Scaffold(
        modifier = modifier
            .testTag(FavoritesScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(FavoritesRes.string.favorite),
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        },
    ) { padding ->
        FavoriteSheet(
            uiState = uiState.favoritesSheetUiState,
            filterBackgroundColor = filterBackgroundColor,
            onTimetableItemClick = onTimetableItemClick,
            onAllFilterChipClick = onAllFilterChipClick,
            onDay1FilterChipClick = onDay1FilterChipClick,
            onDay2FilterChipClick = onDay2FilterChipClick,
            onBookmarkClick = onBookmarkClick,
            contentPadding = contentPadding,
            scrollBehavior = scrollBehavior,
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
            ).nestedScroll(scrollBehavior.nestedScrollConnection),
        )
    }
}

@Composable
@Preview
fun FavoritesScreenPreview() {
    KaigiTheme {
        Surface {
            FavoritesScreen(
                uiState = FavoritesScreenUiState(
                    favoritesSheetUiState = FavoritesSheetUiState.FavoriteListUiState(
                        allFilterSelected = false,
                        currentDayFilter = persistentListOf(DroidKaigi2024Day.ConferenceDay1),
                        timetableItemMap = persistentMapOf(
                            TimeSlot(
                                startTimeString = "10:00",
                                endTimeString = "11:00",
                            ) to listOf(
                                Session.fake(),
                            ),
                        ),
                    ),
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                snackbarHostState = SnackbarHostState(),
                onTimetableItemClick = {},
                onAllFilterChipClick = {},
                onDay1FilterChipClick = {},
                onDay2FilterChipClick = {},
                onBookmarkClick = {},
            )
        }
    }
}
