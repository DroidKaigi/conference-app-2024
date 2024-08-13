package io.github.droidkaigi.confsched.eventmap

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import co.touchlab.kermit.Logger
import conference_app_2024.feature.eventmap.generated.resources.eventmap
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.eventmap.component.EventMapItem
import io.github.droidkaigi.confsched.eventmap.component.EventMapTab
import io.github.droidkaigi.confsched.eventmap.navigation.EventMapDestination
import io.github.droidkaigi.confsched.model.EventMapEvent
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.ui.rememberUserMessageStateHolder
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val eventMapScreenRoute = "eventMap"
const val EventMapScreenTestTag = "EventMapScreenTestTag"
const val EventMapLazyColumnTestTag = "EventMapLazyColumnTestTag"
const val EventMapItemTestTag = "EventMapItemTestTag:"

fun NavGraphBuilder.eventMapScreens(
    onEventMapItemClick: (url: String) -> Unit,
) {
    composable<EventMapDestination> {
        EventMapScreen(
            onEventMapItemClick = onEventMapItemClick,
        )
    }
}

fun NavController.navigateEventMapScreen() {
    navigate(EventMapDestination) {
        popUpTo(route = checkNotNull(graph.findStartDestination().route)) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

data class EventMapUiState(
    val eventMap: PersistentList<EventMapEvent>,
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun EventMapScreen(
    onEventMapItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val eventEmitter = rememberEventEmitter<EventMapScreenEvent>()
    val uiState = eventMapScreenPresenter(
        events = eventEmitter,
    )

    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    EventMapScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEventMapItemClick = onEventMapItemClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    uiState: EventMapUiState,
    snackbarHostState: SnackbarHostState,
    onEventMapItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Logger.d { "EventMapScreen: $uiState" }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.testTag(EventMapScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(EventMapRes.string.eventmap),
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        EventMap(
            eventMapEvents = uiState.eventMap,
            onEventMapItemClick = onEventMapItemClick,
            modifier = Modifier.fillMaxSize().padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        )
    }
}

@Composable
private fun EventMap(
    eventMapEvents: PersistentList<EventMapEvent>,
    onEventMapItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .testTag(EventMapLazyColumnTestTag),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            EventMapTab()
            Spacer(Modifier.height(26.dp))
        }
        itemsIndexed(eventMapEvents) { index, eventMapEvent ->
            EventMapItem(
                eventMapEvent = eventMapEvent,
                onClick = onEventMapItemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(EventMapItemTestTag.plus(eventMapEvent.roomName.enTitle)),
            )
            if (eventMapEvents.lastIndex != index) {
                Spacer(Modifier.height(24.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Spacer(Modifier.height(24.dp))
            }
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
@Preview
fun PreviewEventMapScreen() {
    EventMapScreen(
        uiState = EventMapUiState(persistentListOf(), rememberUserMessageStateHolder()),
        snackbarHostState = SnackbarHostState(),
        onEventMapItemClick = {},
    )
}
