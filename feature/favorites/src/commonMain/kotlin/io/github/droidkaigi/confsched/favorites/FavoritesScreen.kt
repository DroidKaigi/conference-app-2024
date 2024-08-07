package io.github.droidkaigi.confsched.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.ui.component.TimetableItemCard
import io.github.droidkaigi.confsched.ui.component.TimetableItemTag
import io.github.droidkaigi.confsched.ui.handleOnClickIfNotNavigating
import io.github.droidkaigi.confsched.ui.icon
import org.jetbrains.compose.ui.tooling.preview.Preview

const val favoritesScreenRoute = "favorites"
const val FavoritesScreenTestTag = "FavoritesScreenTestTag"

fun NavGraphBuilder.favoritesScreens(
    onNavigationIconClick: () -> Unit,
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
        )
    }
}

fun NavController.navigateFavoritesScreen() {
    navigate(favoritesScreenRoute) {
        popUpTo(route = checkNotNull(graph.findStartDestination().route)) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true    }
}

sealed interface FavoritesSheetUiState {
    val allFilterSelected: Boolean
    val workDayFilterSelected: Boolean
    val day1FilterSelected: Boolean
    val day2FilterSelected: Boolean

    data class FavoriteListUiState(
        override val allFilterSelected: Boolean,
        override val workDayFilterSelected: Boolean,
        override val day1FilterSelected: Boolean,
        override val day2FilterSelected: Boolean,
        val timeTable: Timetable,
    ): FavoritesSheetUiState

    data class Empty(
        override val allFilterSelected: Boolean,
        override val workDayFilterSelected: Boolean,
        override val day1FilterSelected: Boolean,
        override val day2FilterSelected: Boolean,
    ): FavoritesSheetUiState
}

data class FavoritesScreenUiState(
    val favoritesSheetUiState: FavoritesSheetUiState,
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun FavoritesScreen(
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        modifier = modifier,
        isTopAppBarHidden = isTopAppBarHidden,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: FavoritesScreenUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    isTopAppBarHidden: Boolean,
    modifier: Modifier = Modifier,
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
                        Text(text = "Favorite")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                        ) {
                            Icon(
                                imageVector = Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = uiState.favoritesSheetUiState.allFilterSelected,
                    onClick = {},
                    label = { Text("すべて") },
                    leadingIcon = {
                        if (uiState.favoritesSheetUiState.allFilterSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                    },
                )
                FilterChip(
                    selected = uiState.favoritesSheetUiState.workDayFilterSelected,
                    onClick = {},
                    label = { Text("9/11") },
                    leadingIcon = {
                        if (uiState.favoritesSheetUiState.workDayFilterSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                    },
                )
                FilterChip(
                    selected = uiState.favoritesSheetUiState.day1FilterSelected,
                    onClick = {},
                    label = { Text("9/12") },
                    leadingIcon = {
                        if (uiState.favoritesSheetUiState.day1FilterSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                    },
                )
                FilterChip(
                    selected = uiState.favoritesSheetUiState.day2FilterSelected,
                    onClick = {},
                    label = { Text("9/13") },
                    leadingIcon = {
                        if (uiState.favoritesSheetUiState.day2FilterSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                    },
                )
            }

            when (uiState.favoritesSheetUiState) {
                is FavoritesSheetUiState.Empty -> {
                    EmptyView()
                }

                is FavoritesSheetUiState.FavoriteListUiState -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.favoritesSheetUiState.timeTable.timetableItems) { timetableItem ->
                            TimetableItemCard(
                                modifier = Modifier.fillMaxWidth(),
                                isBookmarked = true,
                                tags = {
                                    TimetableItemTag(
                                        tagText = timetableItem.room.name.currentLangTitle,
                                        icon = timetableItem.room.icon,
                                        tagColor = LocalRoomTheme.current.primaryColor,
                                        modifier = Modifier.background(LocalRoomTheme.current.containerColor),
                                    )
                                    Spacer(modifier = Modifier.padding(3.dp))
                                    timetableItem.language.labels.forEach { label ->
                                        TimetableItemTag(
                                            tagText = label,
                                            tagColor = MaterialTheme.colorScheme.outline
                                        )
                                        Spacer(modifier = Modifier.padding(3.dp))
                                    }
                                    timetableItem.day?.let {
                                        TimetableItemTag(
                                            tagText = "9/${it.dayOfMonth}",
                                            tagColor = MaterialTheme.colorScheme.outline,
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                },
                                timetableItem = timetableItem,
                                onTimetableItemClick = {},
                                onBookmarkClick = { timetableItem, isBookmarked -> },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(24.dp),
                )
                .size(84.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.Green,
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "登録されたセッションがありません",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "気になるセッションをお気に入り登録しましょう",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
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
                        workDayFilterSelected = true,
                        day1FilterSelected = true,
                        day2FilterSelected = false,
                        timeTable = Timetable.fake(),
                    ),
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                snackbarHostState = SnackbarHostState(),
                onBackClick = {},
                isTopAppBarHidden = false,
            )
        }
    }
}
