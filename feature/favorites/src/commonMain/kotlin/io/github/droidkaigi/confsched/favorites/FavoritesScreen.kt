package io.github.droidkaigi.confsched.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.favorites.generated.resources.content_description_back
import conference_app_2024.feature.favorites.generated.resources.favorite
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.favorites.section.FavoriteSheet
import io.github.droidkaigi.confsched.favorites.section.FavoritesSheetUiState
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.ui.handleOnClickIfNotNavigating
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val favoritesScreenRoute = "favorites"
const val FavoritesScreenTestTag = "FavoritesScreenTestTag"

fun NavGraphBuilder.favoritesScreens(
    onNavigationIconClick: () -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    contentPadding: PaddingValues,
) {
    composable(favoritesScreenRoute) {
        val lifecycleOwner = LocalLifecycleOwner.current

        FavoritesScreen(
            onNavigationIconClick = {
                handleOnClickIfNotNavigating(
                    lifecycleOwner,
                    onNavigationIconClick,
                )
            },
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
    onNavigationIconClick: () -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    isTopAppBarHidden: Boolean = false,
    eventEmitter: EventEmitter<FavoritesScreenEvent> = rememberEventEmitter(),
    uiState: FavoritesScreenUiState = favoritesScreenPresenter(events = eventEmitter),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = UserMessageStateHolderImpl(),
    )
    FavoritesScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigationIconClick,
        onTimetableItemClick = onTimetableItemClick,
        onAllFilterChipClick = {
            eventEmitter.tryEmit(FavoritesScreenEvent.AllFilter)
        },
        onDay1FilterChipClick = {
            eventEmitter.tryEmit(FavoritesScreenEvent.Day1Filter)
        },
        onDay2FilterChipClick = {
            eventEmitter.tryEmit(FavoritesScreenEvent.Day2Filter)
        },
        onBookmarkClick = { timetableItem ->
            eventEmitter.tryEmit(FavoritesScreenEvent.Bookmark(timetableItem))
        },
        isTopAppBarHidden = isTopAppBarHidden,
        contentPadding = contentPadding,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: FavoritesScreenUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onAllFilterChipClick: () -> Unit,
    onDay1FilterChipClick: () -> Unit,
    onDay2FilterChipClick: () -> Unit,
    onBookmarkClick: (TimetableItem) -> Unit,
    isTopAppBarHidden: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val scrollBehavior =
        if (!isTopAppBarHidden) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        } else {
            null
        }
    Scaffold(
        modifier = modifier
            .testTag(FavoritesScreenTestTag)
            .let {
                if (scrollBehavior != null) {
                    it.nestedScroll(scrollBehavior.nestedScrollConnection)
                } else {
                    it
                }
            },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (scrollBehavior != null) {
                LargeTopAppBar(
                    title = {
                        Text(text = stringResource(FavoritesRes.string.favorite))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                        ) {
                            Icon(
                                imageVector = Filled.ArrowBack,
                                contentDescription = stringResource(FavoritesRes.string.content_description_back),
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { padding ->
        FavoriteSheet(
            uiState = uiState.favoritesSheetUiState,
            onTimetableItemClick = onTimetableItemClick,
            onAllFilterChipClick = onAllFilterChipClick,
            onDay1FilterChipClick = onDay1FilterChipClick,
            onDay2FilterChipClick = onDay2FilterChipClick,
            onBookmarkClick = onBookmarkClick,
            contentPadding = contentPadding,
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
            ),
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
                        timeTable = Timetable.fake(),
                    ),
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                snackbarHostState = SnackbarHostState(),
                onBackClick = {},
                onTimetableItemClick = {},
                onAllFilterChipClick = {},
                onDay1FilterChipClick = {},
                onDay2FilterChipClick = {},
                onBookmarkClick = {},
                isTopAppBarHidden = false,
            )
        }
    }
}
