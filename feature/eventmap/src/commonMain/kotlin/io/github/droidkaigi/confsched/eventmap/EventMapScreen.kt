package io.github.droidkaigi.confsched.eventmap

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.eventmap.component.EventMapItem
import io.github.droidkaigi.confsched.eventmap.component.EventMapTab
import io.github.droidkaigi.confsched.model.EventMapEvent
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.handleOnClickIfNotNavigating
import io.github.droidkaigi.confsched.ui.rememberUserMessageStateHolder
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

const val eventMapScreenRoute = "eventMap"
const val EventMapScreenTestTag = "EventMapScreenTestTag"

fun NavGraphBuilder.eventMapScreens(
    onNavigationIconClick: () -> Unit,
    onEventMapItemClick: (url: String) -> Unit,
) {
    composable(eventMapScreenRoute) {
        val lifecycleOwner = LocalLifecycleOwner.current
        EventMapScreen(
            onNavigationIconClick = {
                handleOnClickIfNotNavigating(
                    lifecycleOwner,
                    onNavigationIconClick,
                )
            },
            onEventMapItemClick = onEventMapItemClick,
        )
    }
}

fun NavController.navigateEventMapScreen() {
    navigate(eventMapScreenRoute) {
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
    onNavigationIconClick: () -> Unit,
    onEventMapItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
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
        isTopAppBarHidden = isTopAppBarHidden,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigationIconClick,
        onEventMapItemClick = onEventMapItemClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    uiState: EventMapUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onEventMapItemClick: (url: String) -> Unit,
    isTopAppBarHidden: Boolean,
    modifier: Modifier = Modifier,
) {
    Logger.d { "EventMapScreen: $uiState" }
    val scrollBehavior =
        if (!isTopAppBarHidden) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        } else {
            null
        }
    Scaffold(
        modifier = modifier.testTag(EventMapScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (scrollBehavior != null) {
                LargeTopAppBar(
                    title = {
                        Text(text = "イベントマップ")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                        ) {
                            Icon(
                                imageVector = Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { padding ->
        EventMap(
            eventMapEvents = uiState.eventMap,
            onEventMapItemClick = onEventMapItemClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .let {
                    if (scrollBehavior != null) {
                        it.nestedScroll(scrollBehavior.nestedScrollConnection)
                    } else {
                        it
                    }
                },
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
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        item {
            EventMapTab()
            Spacer(Modifier.height(26.dp))
        }
        itemsIndexed(eventMapEvents) { index, eventMapEvent ->
            EventMapItem(
                eventMapEvent = eventMapEvent,
                onClick = onEventMapItemClick,
                modifier = Modifier.fillMaxWidth(),
            )
            if (eventMapEvents.lastIndex != index) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
@Preview
fun PreviewEventMapScreen() {
    EventMapScreen(
        uiState = EventMapUiState(persistentListOf(), rememberUserMessageStateHolder()),
        snackbarHostState = SnackbarHostState(),
        isTopAppBarHidden = false,
        onBackClick = {},
        onEventMapItemClick = {},
    )
}
