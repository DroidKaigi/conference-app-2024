package io.github.droidkaigi.confsched.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
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

data class FavoritesScreenUiState(
    val timetable: Timetable,
)

@Composable
fun FavoritesScreen(
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = UserMessageStateHolderImpl(),
    )
    FavoritesScreen(
        uiState = FavoritesScreenUiState(timetable = Timetable.fake()),
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
            var selectedFilterIndex by remember { mutableStateOf(0) }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = selectedFilterIndex == 0,
                    onClick = { selectedFilterIndex = 0 },
                    label = { Text("すべて") },
                    leadingIcon = {
                        if (selectedFilterIndex == 0) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                    },
                )
                FilterChip(
                    selected = selectedFilterIndex == 1,
                    onClick = { selectedFilterIndex = 1 },
                    label = { Text("9/11") },
                    leadingIcon = {
                        if (selectedFilterIndex == 1) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                    },
                )
                FilterChip(
                    selected = selectedFilterIndex == 2,
                    onClick = { selectedFilterIndex = 2 },
                    label = { Text("9/12") },
                    leadingIcon = {
                        if (selectedFilterIndex == 2) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                    },
                )
                FilterChip(
                    selected = selectedFilterIndex == 3,
                    onClick = { selectedFilterIndex = 3 },
                    label = { Text("9/13") },
                    leadingIcon = {
                        if (selectedFilterIndex == 3) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                    },
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.timetable.timetableItems) { timetableItem ->
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
                                TimetableItemTag(tagText = label, tagColor = MaterialTheme.colorScheme.outline)
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
                        onBookmarkClick = { timetableItem, isBookmarked ->  },
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun FavoritesScreenPreview() {
    KaigiTheme {
        Surface {
            FavoritesScreen(
                uiState = FavoritesScreenUiState(timetable = Timetable.fake()),
                snackbarHostState = SnackbarHostState(),
                onBackClick = {},
                isTopAppBarHidden = false,
            )
        }
    }
}
