package io.github.droidkaigi.confsched.sessions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.designsystem.component.LoadingText
import io.github.droidkaigi.confsched.designsystem.preview.MultiLanguagePreviews
import io.github.droidkaigi.confsched.designsystem.preview.MultiThemePreviews
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loaded
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loading
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailBottomAppBar
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailScreenTopAppBar
import io.github.droidkaigi.confsched.sessions.section.TimetableItemDetail
import io.github.droidkaigi.confsched.sessions.section.TimetableItemDetailSectionUiState
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect

const val timetableItemDetailScreenRouteItemIdParameterName = "timetableItemId"
const val timetableItemDetailScreenRoute =
    "timetableItemDetail/{$timetableItemDetailScreenRouteItemIdParameterName}"

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
    viewModel: TimetableItemDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = viewModel.userMessageStateHolder,
    )

    TimetableItemDetailScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onBookmarkClick = {
            viewModel.take(TimetableItemDetailEvent.Bookmark(it))
        },
        onLinkClick = onLinkClick,
        onCalendarRegistrationClick = onCalendarRegistrationClick,
        onShareClick = onShareClick,
        onSelectedLanguage = {
            viewModel.take(TimetableItemDetailEvent.SelectDescriptionLanguage(it))
        },
        snackbarHostState = snackbarHostState,
    )
}

sealed class TimetableItemDetailScreenUiState {
    data object Loading : TimetableItemDetailScreenUiState()
    data class Loaded(
        val timetableItem: TimetableItem,
        val timetableItemDetailSectionUiState: TimetableItemDetailSectionUiState,
        val isBookmarked: Boolean,
        val isLangSelectable: Boolean,
        val viewBookmarkListRequestState: ViewBookmarkListRequestState,
        val currentLang: Lang?,
    ) : TimetableItemDetailScreenUiState()
}

sealed class ViewBookmarkListRequestState {
    data object NotRequested : ViewBookmarkListRequestState()
    data object Requested : ViewBookmarkListRequestState()
}

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
                TimetableItemDetailScreenTopAppBar(
                    title = uiState.timetableItem.title,
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
            TimetableItemDetail(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState.timetableItemDetailSectionUiState,
                selectedLanguage = uiState.currentLang,
                onLinkClick = onLinkClick,
                contentPadding = innerPadding,
            )
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
@MultiThemePreviews
@MultiLanguagePreviews
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
                    viewBookmarkListRequestState = ViewBookmarkListRequestState.NotRequested,
                    currentLang = Lang.JAPANESE,
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
