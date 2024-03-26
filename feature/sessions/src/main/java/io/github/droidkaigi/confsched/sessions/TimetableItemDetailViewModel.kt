package io.github.droidkaigi.confsched.sessions

import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItemId
import io.github.droidkaigi.confsched.model.TimetableSessionType.NORMAL
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailEvent.Bookmark
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailEvent.SelectDescriptionLanguage
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailEvent.ViewBookmarkListRequestCompleted
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loaded
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loading
import io.github.droidkaigi.confsched.sessions.ViewBookmarkListRequestState.NotRequested
import io.github.droidkaigi.confsched.sessions.ViewBookmarkListRequestState.Requested
import io.github.droidkaigi.confsched.sessions.section.TimetableItemDetailSectionUiState
import io.github.droidkaigi.confsched.sessions.strings.TimetableItemDetailStrings.BookmarkedSuccessfully
import io.github.droidkaigi.confsched.sessions.strings.TimetableItemDetailStrings.ViewBookmarkList
import io.github.droidkaigi.confsched.ui.ComposeViewModel
import io.github.droidkaigi.confsched.ui.KmpViewModelLifecycle
import io.github.droidkaigi.confsched.ui.UserMessageResult.ActionPerformed
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed interface TimetableItemDetailEvent {
    data class Bookmark(val timetableItem: TimetableItem) : TimetableItemDetailEvent
    data class SelectDescriptionLanguage(val language: Lang) : TimetableItemDetailEvent
    data object ViewBookmarkListRequestCompleted : TimetableItemDetailEvent
}

@HiltViewModel
class TimetableItemDetailViewModel @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    val userMessageStateHolder: UserMessageStateHolder,
    private val savedStateHandle: SavedStateHandle,
    private val viewModelLifecycle: KmpViewModelLifecycle,
) : ViewModel(),
    ComposeViewModel<TimetableItemDetailEvent, TimetableItemDetailScreenUiState> by ComposeViewModel(
        viewModelLifecycle = viewModelLifecycle,
        userMessageStateHolder = userMessageStateHolder,
        content = { events ->
            timetableItemDetailViewModel(
                events = events,
                eventEmitter = { take(it) },
                userMessageStateHolder = userMessageStateHolder,
                sessionsRepository = sessionsRepository,
                sessionIdFlow = savedStateHandle.getStateFlow(
                    timetableItemDetailScreenRouteItemIdParameterName,
                    "",
                ),
            )
        },
    )

@Composable
fun timetableItemDetailViewModel(
    events: Flow<TimetableItemDetailEvent>,
    eventEmitter: (TimetableItemDetailEvent) -> Unit,
    sessionsRepository: SessionsRepository,
    userMessageStateHolder: UserMessageStateHolder,
    sessionIdFlow: StateFlow<String>,
): TimetableItemDetailScreenUiState {
    val timetableItemId by remember {
        sessionIdFlow
    }.collectAsState()
    val timetableItemStateWithBookmark by rememberUpdatedState(
        sessionsRepository
            .timetableItemWithBookmark(TimetableItemId(timetableItemId)),
    )
    var viewBookmarkListRequest by remember {
        mutableStateOf<ViewBookmarkListRequestState>(
            NotRequested,
        )
    }
    var selectedDescriptionLanguage by remember { mutableStateOf<Lang?>(null) }

    LaunchedEffect(timetableItemStateWithBookmark?.first) {
        val timetableItem = timetableItemStateWithBookmark?.first
        val sessionDefaultLang = timetableItem?.language
        if (sessionDefaultLang != null) {
            eventEmitter(
                SelectDescriptionLanguage(
                    Lang.valueOf(
                        sessionDefaultLang.langOfSpeaker,
                    ),
                ),
            )
        }
    }
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Bookmark -> {
                    val timetableItemWithBookmark = timetableItemStateWithBookmark
                    val timetableItem =
                        timetableItemWithBookmark?.first ?: return@collect
                    sessionsRepository.toggleBookmark(timetableItem.id)
                    val oldBookmarked = timetableItemWithBookmark.second
                    if (!oldBookmarked) {
                        val result = userMessageStateHolder.showMessage(
                            message = BookmarkedSuccessfully.asString(),
                            actionLabel = ViewBookmarkList.asString(),
                            duration = Short,
                        )
                        if (result == ActionPerformed) {
                            viewBookmarkListRequest = Requested
                        }
                    }
                }

                is ViewBookmarkListRequestCompleted -> {
                    viewBookmarkListRequest = NotRequested
                }

                is SelectDescriptionLanguage -> {
                    selectedDescriptionLanguage = event.language
                }
            }
        }
    }
    val timetableItemStateWithBookmarkValue = timetableItemStateWithBookmark
        ?: return Loading
    val (timetableItem, bookmarked) = timetableItemStateWithBookmarkValue
    return Loaded(
        timetableItem = timetableItem,
        timetableItemDetailSectionUiState = TimetableItemDetailSectionUiState(timetableItem),
        isBookmarked = bookmarked,
        isLangSelectable = timetableItem.sessionType == NORMAL,
        viewBookmarkListRequestState = viewBookmarkListRequest,
        currentLang = selectedDescriptionLanguage,
    )
}
