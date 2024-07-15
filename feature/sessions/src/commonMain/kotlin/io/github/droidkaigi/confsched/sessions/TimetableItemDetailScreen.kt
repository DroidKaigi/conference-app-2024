package io.github.droidkaigi.confsched.sessions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.component.LoadingText
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loaded
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loading
import io.github.droidkaigi.confsched.sessions.component.TimeTableItemDetailContent
import io.github.droidkaigi.confsched.sessions.component.TimeTableItemDetailHeadline
import io.github.droidkaigi.confsched.sessions.component.TimeTableItemDetailSummaryCard
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailBottomAppBar
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailTopAppBar
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import org.jetbrains.compose.ui.tooling.preview.Preview

const val timetableItemDetailScreenRouteItemIdParameterName = "timetableItemId"
const val timetableItemDetailScreenRoute =
    "timetableItemDetail/{$timetableItemDetailScreenRouteItemIdParameterName}"
const val TimetableItemDetailBookmarkIconTestTag = "TimetableItemDetailBookmarkIconTestTag"

fun NavGraphBuilder.sessionScreens(
    onNavigationIconClick: () -> Unit,
    onLinkClick: (url: String) -> Unit,
    onCalendarRegistrationClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
) {
    composable(timetableItemDetailScreenRoute) {
        TimetableItemDetailScreen(
            onNavigationIconClick = onNavigationIconClick,
            onLinkClick = onLinkClick,
            onCalendarRegistrationClick = onCalendarRegistrationClick,
            onShareClick = onShareClick,
        )
    }
}

fun NavController.navigateToTimetableItemDetailScreen(
    timetableItem: TimetableItem,
) {
    navigate(
        timetableItemDetailScreenRoute.replace(
            "{$timetableItemDetailScreenRouteItemIdParameterName}",
            timetableItem.id.value,
        ),
    )
}

@Composable
fun TimetableItemDetailScreen(
    onNavigationIconClick: () -> Unit,
    onLinkClick: (url: String) -> Unit,
    onCalendarRegistrationClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    eventEmitter: EventEmitter<TimetableItemDetailEvent> = rememberEventEmitter(),
    uiState: TimetableItemDetailScreenUiState = timetableItemDetailPresenter(
        events = eventEmitter,
    ),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )

    TimetableItemDetailScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onBookmarkClick = {
            eventEmitter.tryEmit(TimetableItemDetailEvent.Bookmark(it))
        },
        onLinkClick = onLinkClick,
        onCalendarRegistrationClick = onCalendarRegistrationClick,
        onShareClick = onShareClick,
        onSelectedLanguage = {
            eventEmitter.tryEmit(TimetableItemDetailEvent.SelectDescriptionLanguage(it))
        },
        snackbarHostState = snackbarHostState,
    )
}

sealed interface TimetableItemDetailScreenUiState {
    data class Loading(
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : TimetableItemDetailScreenUiState
    data class Loaded(
        val timetableItem: TimetableItem,
        val timetableItemDetailSectionUiState: TimetableItemDetailSectionUiState,
        val isBookmarked: Boolean,
        val isLangSelectable: Boolean,
        val currentLang: Lang?,
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : TimetableItemDetailScreenUiState

    val userMessageStateHolder: UserMessageStateHolder
}

data class TimetableItemDetailSectionUiState(
    val timetableItem: TimetableItem,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimetableItemDetailScreen(
    uiState: TimetableItemDetailScreenUiState,
    onNavigationIconClick: () -> Unit,
    onBookmarkClick: (TimetableItem) -> Unit,
    onLinkClick: (url: String) -> Unit,
    onCalendarRegistrationClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onSelectedLanguage: (Lang) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (uiState is Loaded) {
                TimetableItemDetailTopAppBar(
                    isLangSelectable = uiState.isLangSelectable,
                    onNavigationIconClick = onNavigationIconClick,
                    onSelectedLanguage = onSelectedLanguage,
                    scrollBehavior = scrollBehavior,
                )
            }
        },
        bottomBar = {
            if (uiState is Loaded) {
                TimetableItemDetailBottomAppBar(
                    timetableItem = uiState.timetableItem,
                    isBookmarked = uiState.isBookmarked,
                    onBookmarkClick = onBookmarkClick,
                    onCalendarRegistrationClick = onCalendarRegistrationClick,
                    onShareClick = onShareClick,
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (uiState is Loaded) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
            ) {
                item {
                    TimeTableItemDetailHeadline(
                        timetableItem = uiState.timetableItem,
                    )
                }

                item {
                    TimeTableItemDetailSummaryCard(
                        timetableItem = uiState.timetableItem,
                    )
                }

                item {
                    TimeTableItemDetailContent(
                        timetableItem = uiState.timetableItem,
                        currentLang = uiState.currentLang,
                        onLinkClick = onLinkClick,
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = (uiState is Loading),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LoadingText(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            )
        }
    }
}

@Composable
@Preview
fun TimetableItemDetailScreenPreview() {
    var isBookMarked by remember { mutableStateOf(false) }
    val fakeSession = Session.fake()

    KaigiTheme {
        Surface {
            TimetableItemDetailScreen(
                uiState = Loaded(
                    timetableItem = fakeSession,
                    timetableItemDetailSectionUiState = TimetableItemDetailSectionUiState(
                        fakeSession,
                    ),
                    isBookmarked = isBookMarked,
                    isLangSelectable = true,
                    currentLang = Lang.JAPANESE,
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                onNavigationIconClick = {},
                onBookmarkClick = {
                    isBookMarked = !isBookMarked
                },
                onLinkClick = {},
                onCalendarRegistrationClick = {},
                onShareClick = {},
                onSelectedLanguage = {},
                snackbarHostState = SnackbarHostState(),
            )
        }
    }
}
