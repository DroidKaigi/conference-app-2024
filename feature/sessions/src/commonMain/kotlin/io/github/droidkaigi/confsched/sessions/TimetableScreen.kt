package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.preview.MultiThemePreviews
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.DroidKaigi2023Day
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
import io.github.droidkaigi.confsched.ui.getScreenSizeInfo
import kotlinx.collections.immutable.toPersistentMap

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

private val timetableTopBackgroundLight = Color(0xFFF6FFD3)
private val timetableTopBackgroundDark = Color(0xFF2D4625)

@Composable
@ReadOnlyComposable
private fun timetableTopBackground() = if (!isSystemInDarkTheme()) {
    timetableTopBackgroundLight
} else {
    timetableTopBackgroundDark
}

private val timetableTopGradientLight = Color(0xFFA9E5FF)
private val timetableTopGradientDark = Color(0xFF050D10)

@Composable
@ReadOnlyComposable
private fun timetableTopGradient() = if (!isSystemInDarkTheme()) {
    timetableTopGradientLight
} else {
    timetableTopGradientDark
}

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
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val gradientEndRatio =
        if (getScreenSizeInfo().isPort) {
            0.2f
        } else {
            0.5f
        }
    val timetableTopGradient = timetableTopGradient()
    val bottomPaddingPx = with(density) { contentPadding.calculateBottomPadding().toPx() }
    Scaffold(
        modifier = modifier
            .testTag(TimetableScreenTestTag)
            .background(timetableTopBackground())
            .drawWithCache {
                onDrawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to timetableTopGradient,
                            gradientEndRatio to Color.Transparent,
                        ),
                        size = Size(
                            size.width,
                            size.height - bottomPaddingPx,
                        ),
                    )
                }
            },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
//            TimetableTopArea(
//                timetableUiType = uiState.timetableUiType,
//                onTimetableUiChangeClick = onTimetableUiChangeClick,
//            )
            Row {
                Text(text = "UiType: ${uiState.timetableUiType}")
                Button(
                    modifier = Modifier.testTag(TimetableUiTypeChangeButtonTestTag),
                    onClick = { onTimetableUiChangeClick() },
                ) {
                    Text("Change UiType!")
                }
            }
        },
        contentWindowInsets = WindowInsets(
            left = contentPadding.calculateLeftPadding(layoutDirection),
            top = contentPadding.calculateTopPadding(),
            right = contentPadding.calculateRightPadding(layoutDirection),
            bottom = contentPadding.calculateBottomPadding(),
        ),
        containerColor = Color.Transparent,
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
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

@MultiThemePreviews
@Composable
fun PreviewTimetableScreenDark() {
    CompositionLocalProvider(LocalClock provides FakeClock) {
        KaigiTheme {
            TimetableScreen(
                uiState = TimetableScreenUiState(
                    contentUiState = TimetableSheetUiState.ListTimetable(
                        mapOf(
                            DroidKaigi2023Day.Day1 to TimetableListUiState(
                                mapOf<String, List<TimetableItem>>().toPersistentMap(),
                                Timetable(),
                            ),
                            DroidKaigi2023Day.Day2 to TimetableListUiState(
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
