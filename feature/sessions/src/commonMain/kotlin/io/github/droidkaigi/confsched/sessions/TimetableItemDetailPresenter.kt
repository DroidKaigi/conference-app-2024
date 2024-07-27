package io.github.droidkaigi.confsched.sessions

import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItemId
import io.github.droidkaigi.confsched.model.TimetableSessionType.NORMAL
import io.github.droidkaigi.confsched.model.localSessionsRepository
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailEvent.Bookmark
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailEvent.SelectDescriptionLanguage
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailEvent.ViewBookmarkListRequestCompleted
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loaded
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenUiState.Loading
import io.github.droidkaigi.confsched.sessions.strings.TimetableItemDetailStrings.BookmarkedSuccessfully
import io.github.droidkaigi.confsched.sessions.strings.TimetableItemDetailStrings.ViewBookmarkList
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import io.github.droidkaigi.confsched.ui.rememberNavigationArgument
import kotlinx.coroutines.flow.SharedFlow

sealed interface TimetableItemDetailEvent {
    data class Bookmark(val timetableItem: TimetableItem) : TimetableItemDetailEvent
    data class SelectDescriptionLanguage(val language: Lang) : TimetableItemDetailEvent
    data object ViewBookmarkListRequestCompleted : TimetableItemDetailEvent
}

@Composable
fun timetableItemDetailPresenter(
    events: SharedFlow<TimetableItemDetailEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
    timetableItemId: String = rememberNavigationArgument(
        key = timetableItemDetailScreenRouteItemIdParameterName,
        initialValue = "",
    ),
): TimetableItemDetailScreenUiState = providePresenterDefaults<TimetableItemDetailScreenUiState> { userMessageStateHolder ->
    val timetableItemStateWithBookmark by rememberUpdatedState(
        sessionsRepository
            .timetableItemWithBookmark(TimetableItemId(timetableItemId)),
    )
    var selectedDescriptionLanguage by remember { mutableStateOf<Lang?>(null) }

    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Bookmark -> {
                    val timetableItemWithBookmark = timetableItemStateWithBookmark
                    val timetableItem =
                        timetableItemWithBookmark?.first ?: return@collect
                    sessionsRepository.toggleBookmark(timetableItem.id)
                    val oldBookmarked = timetableItemWithBookmark.second
                    if (!oldBookmarked) {
                        userMessageStateHolder.showMessage(
                            message = BookmarkedSuccessfully.asString(),
                            actionLabel = ViewBookmarkList.asString(),
                            duration = Short,
                        )
                    }
                }

                is ViewBookmarkListRequestCompleted -> {
                }

                is SelectDescriptionLanguage -> {
                    selectedDescriptionLanguage = event.language
                }
            }
        }
    }
    SafeLaunchedEffect(timetableItemStateWithBookmark?.first) {
        val timetableItem = timetableItemStateWithBookmark?.first ?: return@SafeLaunchedEffect
        if (selectedDescriptionLanguage == null) {
            selectedDescriptionLanguage = Lang.valueOf(timetableItem.language.langOfSpeaker)
        }
    }
    val timetableItemStateWithBookmarkValue = timetableItemStateWithBookmark
        ?: return@providePresenterDefaults Loading(userMessageStateHolder)
    val (timetableItem, bookmarked) = timetableItemStateWithBookmarkValue
    Loaded(
        timetableItem = timetableItem,
        timetableItemDetailSectionUiState = TimetableItemDetailSectionUiState(timetableItem),
        isBookmarked = bookmarked,
        isLangSelectable = timetableItem.sessionType == NORMAL,
        currentLang = selectedDescriptionLanguage,
        roomThemeKey = timetableItem.room.getThemeKey(),
        userMessageStateHolder = userMessageStateHolder,
    )
}
