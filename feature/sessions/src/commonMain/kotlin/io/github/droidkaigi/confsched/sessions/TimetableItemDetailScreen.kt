package io.github.droidkaigi.confsched.sessions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.sessions.generated.resources.image
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
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalSnackbarHostState
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.TimetableItem.Special
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
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val timetableItemDetailScreenRouteItemIdParameterName = "timetableItemId"
const val TimetableItemDetailBookmarkIconTestTag = "TimetableItemDetailBookmarkIconTestTag"
const val TimetableItemDetailScreenLazyColumnTestTag = "TimetableItemDetailScreenLazyColumnTestTag"
const val TimetableItemDetailMessageRowTestTag = "TimetableItemDetailMessageRowTestTag"
const val TimetableItemDetailMessageRowTextTestTag = "TimetableItemDetailMessageRowTextTestTag"

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
    val snackbarHostState = snackbarHostSate()
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
    val layoutDirection = LocalLayoutDirection.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (uiState is Loaded) {
                ProvideRoomTheme(uiState.roomThemeKey) {
                    TimetableItemDetailTopAppBar(
                        onNavigationIconClick = onNavigationIconClick,
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
        contentWindowInsets = WindowInsets.displayCutout.union(WindowInsets.systemBars),
    ) { innerPadding ->
        val surfaceModifier = if (sharedTransitionScope != null && animatedScope != null) {
            with(sharedTransitionScope) {
                Modifier
                    .fillMaxSize()
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
        }

        Surface(
            modifier = surfaceModifier,
        ) {
            if (uiState is Loaded) {
                ProvideRoomTheme(uiState.roomThemeKey) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = innerPadding.calculateTopPadding(),
                                bottom = innerPadding.calculateBottomPadding(),
                            )
                            .testTag(TimetableItemDetailScreenLazyColumnTestTag),
                    ) {
                        item {
                            TimetableItemDetailHeadline(
                                modifier = Modifier
                                    .padding(
                                        start = innerPadding.calculateStartPadding(layoutDirection),
                                        end = innerPadding.calculateEndPadding(layoutDirection),
                                    ),
                                currentLang = uiState.currentLang,
                                timetableItem = uiState.timetableItem,
                                isLangSelectable = uiState.isLangSelectable,
                                onLanguageSelect = onSelectedLanguage,
                            )
                        }

                        when (uiState.timetableItem) {
                            is Session -> uiState.timetableItem.message
                            is Special -> uiState.timetableItem.message
                        }?.let {
                            item {
                                Row(
                                    modifier = Modifier
                                        .padding(
                                            start = 8.dp,
                                            top = 24.dp,
                                            end = 8.dp,
                                            bottom = 4.dp,
                                        )
                                        .height(IntrinsicSize.Min)
                                        .testTag(TimetableItemDetailMessageRowTestTag),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    Icon(
                                        modifier = Modifier.fillMaxHeight(),
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = stringResource(SessionsRes.string.image),
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                    Text(
                                        modifier = Modifier.testTag(
                                            TimetableItemDetailMessageRowTextTestTag,
                                        ),
                                        text = it.currentLangTitle,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                }
                            }
                        }

                        item {
                            TimetableItemDetailSummaryCard(
                                modifier = Modifier.padding(
                                    start = innerPadding.calculateStartPadding(layoutDirection),
                                    end = innerPadding.calculateEndPadding(layoutDirection),
                                ),
                                timetableItem = uiState.timetableItem,
                            )
                        }

                        item {
                            TimetableItemDetailContent(
                                modifier = Modifier.padding(
                                    start = innerPadding.calculateStartPadding(layoutDirection),
                                    end = innerPadding.calculateEndPadding(layoutDirection),
                                ),
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
fun snackbarHostSate(): SnackbarHostState {
    val state = LocalSnackbarHostState.current
    if (state != null) {
        return state
    }
    return remember { SnackbarHostState() }
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
