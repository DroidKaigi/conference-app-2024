package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalAnimatedVisibilityScope
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableCategory
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableSessionType
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.sessions.component.EmptySearchResultBody
import io.github.droidkaigi.confsched.sessions.component.SearchFilterUiState
import io.github.droidkaigi.confsched.sessions.component.SearchFilters
import io.github.droidkaigi.confsched.sessions.component.SearchTextFieldAppBar
import io.github.droidkaigi.confsched.sessions.section.SearchList
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import org.jetbrains.compose.ui.tooling.preview.Preview

const val searchScreenRoute = "search"

fun NavGraphBuilder.searchScreens(
    onTimetableItemClick: (TimetableItem) -> Unit,
    onBackClick: () -> Unit,
) {
    composable(searchScreenRoute) {
        CompositionLocalProvider(
            LocalAnimatedVisibilityScope provides this@composable,
        ) {
            SearchScreen(
                onTimetableItemClick = onTimetableItemClick,
                onBackClick = onBackClick,
            )
        }
    }
}

fun NavController.navigateToSearchScreen() {
    navigate(searchScreenRoute) {
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun SearchScreen(
    onTimetableItemClick: (TimetableItem) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    eventFlow: EventFlow<SearchScreenEvent> = rememberEventFlow(),
    uiState: SearchScreenUiState = searchScreenPresenter(eventFlow),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )

    SearchScreen(
        uiState = uiState,
        onTimetableItemClick = onTimetableItemClick,
        onTimetableItemBookmark = { eventFlow.tryEmit(SearchScreenEvent.Bookmark(it)) },
        onSearchWordChanged = { eventFlow.tryEmit(SearchScreenEvent.UpdateSearchWord(it)) },
        onClearSearchWordClick = { eventFlow.tryEmit(SearchScreenEvent.ClearSearchWord) },
        onSelectDay = { eventFlow.tryEmit(SearchScreenEvent.SelectDay(it)) },
        onSelectSessionType = { eventFlow.tryEmit(SearchScreenEvent.SelectSessionType(it)) },
        onSelectCategory = { eventFlow.tryEmit(SearchScreenEvent.SelectCategory(it)) },
        onSelectLanguage = { eventFlow.tryEmit(SearchScreenEvent.SelectLanguage(it)) },
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

sealed interface SearchScreenUiState {
    val searchWord: String
    val searchFilterDayUiState: SearchFilterUiState<DroidKaigi2024Day>
    val searchFilterCategoryUiState: SearchFilterUiState<TimetableCategory>
    val searchFilterSessionTypeUiState: SearchFilterUiState<TimetableSessionType>
    val searchFilterLanguageUiState: SearchFilterUiState<Lang>
    val userMessageStateHolder: UserMessageStateHolder

    data class Empty(
        override val searchWord: String,
        override val searchFilterDayUiState: SearchFilterUiState<DroidKaigi2024Day>,
        override val searchFilterCategoryUiState: SearchFilterUiState<TimetableCategory>,
        override val searchFilterSessionTypeUiState: SearchFilterUiState<TimetableSessionType>,
        override val searchFilterLanguageUiState: SearchFilterUiState<Lang>,
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : SearchScreenUiState

    data class SearchList(
        override val searchWord: String,
        override val searchFilterDayUiState: SearchFilterUiState<DroidKaigi2024Day>,
        override val searchFilterCategoryUiState: SearchFilterUiState<TimetableCategory>,
        override val searchFilterSessionTypeUiState: SearchFilterUiState<TimetableSessionType>,
        override val searchFilterLanguageUiState: SearchFilterUiState<Lang>,
        override val userMessageStateHolder: UserMessageStateHolder,
        val timetableListUiState: TimetableListUiState,
    ) : SearchScreenUiState
}

@Composable
fun SearchScreen(
    uiState: SearchScreenUiState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onTimetableItemBookmark: (TimetableItem) -> Unit,
    onSearchWordChanged: (String) -> Unit,
    onClearSearchWordClick: () -> Unit,
    onSelectDay: (DroidKaigi2024Day) -> Unit,
    onSelectCategory: (TimetableCategory) -> Unit,
    onSelectLanguage: (Lang) -> Unit,
    onSelectSessionType: (TimetableSessionType) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            SearchTextFieldAppBar(
                searchWord = uiState.searchWord,
                onChangeSearchWord = onSearchWordChanged,
                onClickClear = onClearSearchWordClick,
                onClickBack = onBackClick,
            )
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
        ) {
            HorizontalDivider()
            SearchFilters(
                filterDayUiState = uiState.searchFilterDayUiState,
                filterCategoryUiState = uiState.searchFilterCategoryUiState,
                filterSessionTypeUiState = uiState.searchFilterSessionTypeUiState,
                filterLanguageUiState = uiState.searchFilterLanguageUiState,
                onSelectDay = onSelectDay,
                onSelectCategory = onSelectCategory,
                onSelectLanguage = onSelectLanguage,
                onSelectSessionType = onSelectSessionType,
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
            )
            Spacer(Modifier.height(15.dp))
            when (uiState) {
                is SearchScreenUiState.Empty -> EmptySearchResultBody(
                    searchWord = uiState.searchWord,
                    modifier = Modifier.imePadding(),
                )

                is SearchScreenUiState.SearchList -> SearchList(
                    uiState = uiState.timetableListUiState,
                    highlightWord = uiState.searchWord,
                    onBookmarkClick = { timetableItem, _ -> onTimetableItemBookmark(timetableItem) },
                    onTimetableItemClick = onTimetableItemClick,
                    modifier = Modifier.imePadding(),
                    contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding()),
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview_Empty() {
    KaigiTheme {
        SearchScreen(
            uiState = SearchScreenUiState.Empty(
                searchWord = "Input text",
                searchFilterDayUiState = SearchFilterUiState(
                    selectedItems = listOf(),
                    selectableItems = listOf(
                        DroidKaigi2024Day.ConferenceDay1,
                        DroidKaigi2024Day.ConferenceDay2,
                    ),
                    selectedValuesText = "",
                ),
                searchFilterCategoryUiState = SearchFilterUiState(
                    selectedItems = listOf(),
                    selectableItems = TimetableCategory.fakes(),
                    selectedValuesText = "",
                ),
                searchFilterSessionTypeUiState = SearchFilterUiState(
                    selectedItems = listOf(),
                    selectableItems = TimetableSessionType.entries,
                    selectedValuesText = "",
                ),
                searchFilterLanguageUiState = SearchFilterUiState(
                    selectedItems = listOf(),
                    selectableItems = Lang.entries,
                    selectedValuesText = "",
                ),
                userMessageStateHolder = UserMessageStateHolderImpl(),
            ),
            onBackClick = {},
            onClearSearchWordClick = {},
            onSearchWordChanged = {},
            onTimetableItemClick = {},
            onTimetableItemBookmark = {},
            onSelectDay = {},
            onSelectCategory = {},
            onSelectLanguage = {},
            onSelectSessionType = {},
        )
    }
}
