package io.github.droidkaigi.confsched.staff

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
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
    val scrollBehavior =
        if (!isTopAppBarHidden) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        } else {
            null
        }
    Scaffold(
        modifier = modifier.testTag(StaffScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (scrollBehavior != null) {
                LargeTopAppBar(
                    title = {
                        Text(text = "Staff")
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
