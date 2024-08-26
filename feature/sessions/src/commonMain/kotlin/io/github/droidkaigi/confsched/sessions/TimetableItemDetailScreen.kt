package io.github.droidkaigi.confsched.sessions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.designsystem.component.LoadingText
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalAnimatedVisibilityScope
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalSharedTransitionScope
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.TimetableItemId
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loaded
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loading
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailBottomAppBar
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailContent
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailHeadline
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailSummaryCard
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailTopAppBar
import io.github.droidkaigi.confsched.sessions.navigation.TimetableItemDetailDestination
import org.jetbrains.compose.ui.tooling.preview.Preview

const val timetableItemDetailScreenRouteItemIdParameterName = "timetableItemId"
const val TimetableItemDetailBookmarkIconTestTag = "TimetableItemDetailBookmarkIconTestTag"
const val TimetableItemDetailScreenLazyColumnTestTag = "TimetableItemDetailScreenLazyColumnTestTag"

fun NavGraphBuilder.sessionScreens(
    onNavigationIconClick: () -> Unit,
    onLinkClick: (url: String) -> Unit,
    onCalendarRegistrationClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onFavoriteListClick: () -> Unit,
) {
    composable<TimetableItemDetailDestination> {
        CompositionLocalProvider(
            LocalAnimatedVisibilityScope provides this@composable,
        ) {
            TimetableItemDetailScreen(
                onNavigationIconClick = onNavigationIconClick,
                onLinkClick = onLinkClick,
                onCalendarRegistrationClick = onCalendarRegistrationClick,
                onShareClick = onShareClick,
                onFavoriteListClick = onFavoriteListClick,
            )
        }
    }
}

fun NavController.navigateToTimetableItemDetailScreen(
    timetableItem: TimetableItem,
) {
    navigate(TimetableItemDetailDestination(timetableItem.id.value))
}

@Composable
fun TimetableItemDetailScreen(
    onNavigationIconClick: () -> Unit,
    onLinkClick: (url: String) -> Unit,
    onCalendarRegistrationClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onFavoriteListClick: () -> Unit,
    eventFlow: EventFlow<TimetableItemDetailEvent> = rememberEventFlow(),
    uiState: TimetableItemDetailScreenUiState = timetableItemDetailPresenter(
        events = eventFlow,
    ),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )

    LaunchedEffect(uiState is Loaded && uiState.shouldGoToFavoriteList) {
        if (uiState is Loaded && uiState.shouldGoToFavoriteList) {
            eventFlow.tryEmit(TimetableItemDetailEvent.FavoriteListNavigated)
            onFavoriteListClick()
        }
    }

    TimetableItemDetailScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onBookmarkClick = {
            eventFlow.tryEmit(TimetableItemDetailEvent.Bookmark(it))
        },
        onLinkClick = onLinkClick,
        onCalendarRegistrationClick = onCalendarRegistrationClick,
        onShareClick = onShareClick,
        onSelectedLanguage = {
            eventFlow.tryEmit(TimetableItemDetailEvent.SelectDescriptionLanguage(it))
        },
        snackbarHostState = snackbarHostState,
    )
}

sealed interface TimetableItemDetailScreenUiState {
    data class Loading(
        override val timetableItemId: TimetableItemId,
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : TimetableItemDetailScreenUiState

    data class Loaded(
        val timetableItem: TimetableItem,
        val timetableItemDetailSectionUiState: TimetableItemDetailSectionUiState,
        val isBookmarked: Boolean,
        val isLangSelectable: Boolean,
        val currentLang: Lang?,
        val roomThemeKey: String,
        val shouldGoToFavoriteList: Boolean,
        override val timetableItemId: TimetableItemId,
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : TimetableItemDetailScreenUiState

    val timetableItemId: TimetableItemId
    val userMessageStateHolder: UserMessageStateHolder
}

data class TimetableItemDetailSectionUiState(
    val timetableItem: TimetableItem,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
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
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedScope = LocalAnimatedVisibilityScope.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (uiState is Loaded) {
                ProvideRoomTheme(uiState.roomThemeKey) {
                    TimetableItemDetailTopAppBar(
                        isLangSelectable = uiState.isLangSelectable,
                        onNavigationIconClick = onNavigationIconClick,
                        onSelectedLanguage = onSelectedLanguage,
                        scrollBehavior = scrollBehavior,
                    )
                }
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
        val surfaceModifier = if (sharedTransitionScope != null && animatedScope != null) {
            with(sharedTransitionScope) {
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = timetableDetailSharedContentStateKey(timetableItemId = uiState.timetableItemId),
                        ),
                        animatedVisibilityScope = animatedScope,
                    )
            }
        } else {
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        }

        Surface(
            modifier = surfaceModifier,
        ) {
            if (uiState is Loaded) {
                ProvideRoomTheme(uiState.roomThemeKey) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(TimetableItemDetailScreenLazyColumnTestTag),
                    ) {
                        item {
                            TimetableItemDetailHeadline(
                                currentLang = uiState.currentLang,
                                timetableItem = uiState.timetableItem,
                            )
                        }

                        item {
                            TimetableItemDetailSummaryCard(
                                timetableItem = uiState.timetableItem,
                            )
                        }

                        item {
                            TimetableItemDetailContent(
                                timetableItem = uiState.timetableItem,
                                currentLang = uiState.currentLang,
                                onLinkClick = onLinkClick,
                            )
                        }
                    }
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
                    roomThemeKey = "iguana",
                    timetableItemId = fakeSession.id,
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                    shouldGoToFavoriteList = false,
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

internal fun timetableDetailSharedContentStateKey(timetableItemId: TimetableItemId) =
    "timetable-item-${timetableItemId.value}"
