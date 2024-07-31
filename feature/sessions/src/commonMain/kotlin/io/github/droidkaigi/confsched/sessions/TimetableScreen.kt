package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grid3x3
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.sessions.generated.resources.timetable
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableUiType
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableSheet
import io.github.droidkaigi.confsched.sessions.section.TimetableSheetUiState
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.ui.compositionlocal.FakeClock
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalClock
import kotlinx.collections.immutable.toPersistentMap
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val timetableScreenRoute = "timetable"
const val TimetableListItemBookmarkIconTestTag = "TimetableListItemBookmarkIcon"
const val TimetableListItemTestTag = "TimetableListItem"
const val TimetableUiTypeChangeButtonTestTag = "TimetableUiTypeChangeButton"
fun NavGraphBuilder.nestedSessionScreens(
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    composable(timetableScreenRoute) {
        TimetableScreen(
            onTimetableItemClick = onTimetableItemClick,
            contentPadding = contentPadding,
            modifier = modifier,
        )
    }
}

fun NavController.navigateTimetableScreen() {
    navigate(timetableScreenRoute) {
        popUpTo(route = checkNotNull(graph.findStartDestination().route)) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

const val TimetableScreenTestTag = "TimetableScreen"

@Composable
fun TimetableScreen(
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    eventEmitter: EventEmitter<TimetableScreenEvent> = rememberEventEmitter<TimetableScreenEvent>(),
    uiState: TimetableScreenUiState = timetableScreenPresenter(
        events = eventEmitter,
    ),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    TimetableScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onTimetableItemClick = onTimetableItemClick,
        onBookmarkClick = { item, bookmarked ->
            eventEmitter.tryEmit(TimetableScreenEvent.Bookmark(item, bookmarked))
        },
        onTimetableUiChangeClick = {
            eventEmitter.tryEmit(TimetableScreenEvent.UiTypeChange)
        },
        contentPadding = contentPadding,
        modifier = modifier,
    )
}

data class TimetableScreenUiState(
    val contentUiState: TimetableSheetUiState,
    val timetableUiType: TimetableUiType,
    val userMessageStateHolder: UserMessageStateHolder,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimetableScreen(
    uiState: TimetableScreenUiState,
    snackbarHostState: SnackbarHostState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    onTimetableUiChangeClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val layoutDirection = LocalLayoutDirection.current
    Scaffold(
        modifier = modifier
            .testTag(TimetableScreenTestTag)
            .background(Color.Black),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(SessionsRes.string.timetable),
                            fontSize = 24.sp,
                            lineHeight = 32.sp,
                            fontWeight = FontWeight.W400,
                            modifier = Modifier.weight(1F),
                        )
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).clickable {
                            },
                        )
                        Icon(
                            imageVector = Icons.Default.Grid3x3,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).clickable {
                                onTimetableUiChangeClick()
                            }.testTag(TimetableUiTypeChangeButtonTestTag),
                        )
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(
            left = contentPadding.calculateLeftPadding(layoutDirection),
            top = contentPadding.calculateTopPadding(),
            right = contentPadding.calculateRightPadding(layoutDirection),
            bottom = contentPadding.calculateBottomPadding(),
        ),
        containerColor = Color.Transparent,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()).fillMaxWidth(),
        ) {
            TimetableSheet(
                modifier = Modifier
                    .fillMaxSize(),
                onTimetableItemClick = onTimetableItemClick,
                uiState = uiState.contentUiState,
                onFavoriteClick = onBookmarkClick,
                contentPadding = PaddingValues(
                    bottom = innerPadding.calculateBottomPadding(),
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection),
                ),
            )
        }
    }
}

@Preview
@Composable
fun PreviewTimetableScreenDark() {
    CompositionLocalProvider(LocalClock provides FakeClock) {
        KaigiTheme {
            TimetableScreen(
                uiState = TimetableScreenUiState(
                    contentUiState = TimetableSheetUiState.ListTimetable(
                        mapOf(
                            DroidKaigi2024Day.Workday to TimetableListUiState(
                                mapOf<String, List<TimetableItem>>().toPersistentMap(),
                                Timetable(),
                            ),
                            DroidKaigi2024Day.ConferenceDay1 to TimetableListUiState(
                                mapOf<String, List<TimetableItem>>().toPersistentMap(),
                                Timetable(),
                            ),
                        ),
                    ),
                    timetableUiType = TimetableUiType.Grid,
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                snackbarHostState = SnackbarHostState(),
                onTimetableItemClick = {},
                onBookmarkClick = { _, _ -> },
                onTimetableUiChangeClick = {},
                modifier = Modifier.statusBarsPadding(),
            )
        }
    }
}
