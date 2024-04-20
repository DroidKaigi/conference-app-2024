package io.github.droidkaigi.confsched.sessions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.component.LoadingText
import io.github.droidkaigi.confsched.designsystem.preview.MultiLanguagePreviews
import io.github.droidkaigi.confsched.designsystem.preview.MultiThemePreviews
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.Lang.JAPANESE
import io.github.droidkaigi.confsched.model.MultiLangText
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loaded
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loading
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl

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
                LargeTopAppBar(title = {
                    Row {
                        Button(onClick = { onNavigationIconClick() }) {
                            Text(
                                text = "Back",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(
                            modifier = Modifier.weight(1F),
                            text = uiState.timetableItem.title.currentLangTitle,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (uiState.isLangSelectable) {
                            Button(onClick = { onSelectedLanguage(JAPANESE) }) {
                                Text(
                                    text = "日本語",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Button(onClick = { onSelectedLanguage(Lang.ENGLISH) }) {
                                Text(
                                    text = "English",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                })
            }
        },
        bottomBar = {
            if (uiState is Loaded) {
                Column {
                    Button(
                        modifier = Modifier.testTag(TimetableItemDetailBookmarkIconTestTag),
                        onClick = { onBookmarkClick(uiState.timetableItem) }) {
                        Text(text = "Bookmark: ${uiState.isBookmarked}")
                    }
                    Button(

                        onClick = { onCalendarRegistrationClick(uiState.timetableItem) }) {
                        Text(text = "Calendar")
                    }
                    Button(onClick = { onShareClick(uiState.timetableItem) }) {
                        Text(text = "Share")
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (uiState is Loaded) {
            Column(
                Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            ) {
                val currentLang = uiState.currentLang ?: Lang.ENGLISH
                fun MultiLangText.getByLang(lang: Lang): String {
                    return if (lang == JAPANESE) {
                        jaTitle
                    } else {
                        enTitle
                    }
                }
                Text(
                    text = when (val item = uiState.timetableItem) {
                        is TimetableItem.Session -> item.description.getByLang(currentLang)
                        is TimetableItem.Special -> item.description.getByLang(currentLang)
                    }
                )
                Button(onClick = { onLinkClick(uiState.timetableItem.url) }) {
                    Text(text = "Link")
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
