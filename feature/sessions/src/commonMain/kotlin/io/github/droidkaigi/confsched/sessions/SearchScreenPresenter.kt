package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.Filters
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.TimetableCategory
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItemList
import io.github.droidkaigi.confsched.model.TimetableSessionType
import io.github.droidkaigi.confsched.model.localSessionsRepository
import io.github.droidkaigi.confsched.sessions.SearchScreenEvent.Bookmark
import io.github.droidkaigi.confsched.sessions.SearchScreenEvent.ClearSearchWord
import io.github.droidkaigi.confsched.sessions.SearchScreenEvent.SelectCategory
import io.github.droidkaigi.confsched.sessions.SearchScreenEvent.SelectDay
import io.github.droidkaigi.confsched.sessions.SearchScreenEvent.SelectLanguage
import io.github.droidkaigi.confsched.sessions.SearchScreenEvent.SelectSessionType
import io.github.droidkaigi.confsched.sessions.SearchScreenEvent.UpdateSearchWord
import io.github.droidkaigi.confsched.sessions.component.SearchFilterUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import io.github.takahirom.rin.rememberRetained
import kotlinx.collections.immutable.toPersistentMap

sealed interface SearchScreenEvent {
    data class Bookmark(val timetableItem: TimetableItem) : SearchScreenEvent
    data class UpdateSearchWord(val word: String) : SearchScreenEvent
    data class SelectDay(val day: DroidKaigi2024Day) : SearchScreenEvent
    data class SelectSessionType(val sessionType: TimetableSessionType) : SearchScreenEvent
    data class SelectCategory(val category: TimetableCategory) : SearchScreenEvent
    data class SelectLanguage(val language: Lang) : SearchScreenEvent
    data object ClearSearchWord : SearchScreenEvent
}

@Composable
fun searchScreenPresenter(
    events: EventFlow<SearchScreenEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
): SearchScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    val sessions by rememberUpdatedState(sessionsRepository.timetable())

    var searchWord by rememberRetained { mutableStateOf("") }
    val selectedDays = rememberRetained { mutableStateListOf<DroidKaigi2024Day>() }
    val selectedSessionTypes = rememberRetained { mutableStateListOf<TimetableSessionType>() }
    val selectedCategories = rememberRetained { mutableStateListOf<TimetableCategory>() }
    val selectedLanguages = rememberRetained { mutableStateListOf<Lang>() }
    val filters by remember {
        derivedStateOf {
            Filters(
                searchWord = searchWord,
                days = selectedDays,
                categories = selectedCategories,
                sessionTypes = selectedSessionTypes,
                languages = selectedLanguages,
            )
        }
    }
    val filteredSessions by rememberUpdatedState(
        if (filters.isEmpty()) TimetableItemList() else sessions.filtered(filters).timetableItems,
    )

    val searchFilterDayUiState: SearchFilterUiState<DroidKaigi2024Day> by rememberUpdatedState(
        SearchFilterUiState(
            selectedItems = selectedDays,
            selectableItems = sessions.days,
            selectedValuesText = selectedDays.joinToString { it.monthAndDay() },
        ),
    )

    val searchFilterCategoryUiState: SearchFilterUiState<TimetableCategory> by rememberUpdatedState(
        SearchFilterUiState(
            selectedItems = selectedCategories,
            selectableItems = sessions.categories,
            selectedValuesText = selectedCategories.joinToString { it.title.currentLangTitle },
        ),
    )

    val searchFilterSessionTypeUiState: SearchFilterUiState<TimetableSessionType> by rememberUpdatedState(
        SearchFilterUiState(
            selectedItems = selectedSessionTypes,
            selectableItems = sessions.sessionTypes,
            selectedValuesText = selectedSessionTypes.joinToString { it.label.currentLangTitle },
        ),
    )

    val searchFilterLanguageUiState: SearchFilterUiState<Lang> by rememberUpdatedState(
        SearchFilterUiState(
            selectedItems = selectedLanguages,
            selectableItems = sessions.languages.map { it.toLang() },
            selectedValuesText = selectedLanguages.joinToString { it.tagName },
        ),
    )

    EventEffect(events) { event ->
        when (event) {
            is Bookmark -> {
                sessionsRepository.toggleBookmark(event.timetableItem.id)
            }

            is UpdateSearchWord -> {
                searchWord = event.word
            }

            is ClearSearchWord -> {
                searchWord = ""
            }

            is SelectDay -> {
                if (selectedDays.contains(event.day)) {
                    selectedDays.remove(event.day)
                } else {
                    selectedDays.add(event.day)
                }
            }

            is SelectCategory -> {
                if (selectedCategories.contains(event.category)) {
                    selectedCategories.remove(event.category)
                } else {
                    selectedCategories.add(event.category)
                }
            }

            is SelectSessionType -> {
                if (selectedSessionTypes.contains(event.sessionType)) {
                    selectedSessionTypes.remove(event.sessionType)
                } else {
                    selectedSessionTypes.add(event.sessionType)
                }
            }

            is SelectLanguage -> {
                if (selectedLanguages.contains(event.language)) {
                    selectedLanguages.remove(event.language)
                } else {
                    selectedLanguages.add(event.language)
                }
            }
        }
    }

    when {
        filters.isNotEmpty() && filteredSessions.isEmpty() -> {
            SearchScreenUiState.Empty(
                searchWord = searchWord,
                searchFilterDayUiState = searchFilterDayUiState,
                searchFilterCategoryUiState = searchFilterCategoryUiState,
                searchFilterSessionTypeUiState = searchFilterSessionTypeUiState,
                searchFilterLanguageUiState = searchFilterLanguageUiState,
                userMessageStateHolder = userMessageStateHolder,
            )
        }

        else -> {
            SearchScreenUiState.SearchList(
                searchWord = searchWord,
                searchFilterDayUiState = searchFilterDayUiState,
                searchFilterCategoryUiState = searchFilterCategoryUiState,
                searchFilterSessionTypeUiState = searchFilterSessionTypeUiState,
                searchFilterLanguageUiState = searchFilterLanguageUiState,
                userMessageStateHolder = userMessageStateHolder,
                timetableListUiState = TimetableListUiState(
                    timetableItemMap = filteredSessions.groupBy {
                        TimetableListUiState.TimeSlot(
                            startTimeString = it.startsTimeString,
                            endTimeString = it.endsTimeString,
                        )
                    }.mapValues { entries ->
                        entries.value.sortedWith(
                            compareBy({ it.day?.name.orEmpty() }, { it.startsTimeString }),
                        )
                    }.toPersistentMap(),
                    timetable = sessions,
                ),
            )
        }
    }
}
