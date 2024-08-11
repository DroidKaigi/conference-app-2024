package io.github.droidkaigi.confsched.staff

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Staff
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.staff.component.StaffItem
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.ui.handleOnClickIfNotNavigating
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.ui.tooling.preview.Preview

const val staffScreenRoute = "staff"
const val StaffScreenTestTag = "StaffScreenTestTag"

fun NavGraphBuilder.staffScreens(
    onNavigationIconClick: () -> Unit,
    onStaffItemClick: (url: String) -> Unit,
) {
    composable(staffScreenRoute) {
        val lifecycleOwner = LocalLifecycleOwner.current

        StaffScreen(
            onNavigationIconClick = {
                handleOnClickIfNotNavigating(
                    lifecycleOwner,
                    onNavigationIconClick,
                )
            },
            onStaffItemClick = onStaffItemClick,
        )
    }
}

data class StaffUiState(
    val staff: PersistentList<Staff>,
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun StaffScreen(
    onNavigationIconClick: () -> Unit,
    onStaffItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val eventEmitter = rememberEventEmitter<StaffScreenEvent>()
    val uiState = staffScreenPresenter(events = eventEmitter)

    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    StaffScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigationIconClick,
        onStaffItemClick = onStaffItemClick,
        modifier = modifier,
        isTopAppBarHidden = isTopAppBarHidden,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(
    uiState: StaffUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    isTopAppBarHidden: Boolean,
    modifier: Modifier = Modifier,
    onStaffItemClick: (url: String) -> Unit,
) {
    val density = LocalDensity.current.density
    val scrollBehavior =
        if (!isTopAppBarHidden) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        } else {
            null
        }
    var navigationIconWidthDp by remember { mutableStateOf(0f) }
    val isCenterTitle = remember(scrollBehavior?.state?.collapsedFraction) {
        scrollBehavior?.let {
            // Hide title position because it doesn't look smooth if it is displayed when collapsed
            when (scrollBehavior.state.collapsedFraction) {
                in 0.7f..1.0f -> true
                in 0.0f..0.5f -> false
                else -> null // Don't display while on the move.
            }
        }
    }
    Scaffold(
        modifier = modifier.testTag(StaffScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (!isTopAppBarHidden) {
                LargeTopAppBar(
                    title = {
                        AnimatedVisibility(
                            visible = isCenterTitle != null,
                            enter = fadeIn(),
                            // No animation required as it is erased with alpha
                            exit = ExitTransition.None,
                        ) {
                            Text(
                                text = "Staff",
                                modifier = Modifier.then(
                                    when (isCenterTitle) {
                                        true -> {
                                            Modifier
                                                .padding(end = navigationIconWidthDp.dp)
                                                .fillMaxWidth()
                                        }

                                        false -> Modifier
                                        null -> Modifier.alpha(0f)
                                    },
                                ),
                                textAlign = TextAlign.Center,
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier
                                .onGloballyPositioned {
                                    navigationIconWidthDp = it.size.width / density
                                },
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
        LazyColumn(
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
        ) {
            items(uiState.staff) { staff ->
                StaffItem(
                    staff = staff,
                    onStaffItemClick = onStaffItemClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
@Preview
fun StaffScreenPreview() {
    KaigiTheme {
        Surface {
            StaffScreen(
                uiState = StaffUiState(
                    staff = Staff.fakes(),
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                snackbarHostState = SnackbarHostState(),
                onStaffItemClick = {},
                onBackClick = {},
                isTopAppBarHidden = false,
            )
        }
    }
}
