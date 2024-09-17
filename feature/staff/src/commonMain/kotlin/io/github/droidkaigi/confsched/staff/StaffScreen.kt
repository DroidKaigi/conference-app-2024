package io.github.droidkaigi.confsched.staff

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.staff.generated.resources.staff_title
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedMediumTopAppBar
import io.github.droidkaigi.confsched.model.Staff
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.staff.component.StaffItem
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val staffScreenRoute = "staff"
const val StaffScreenTestTag = "StaffScreenTestTag"
const val StaffScreenLazyColumnTestTag = "StaffScreenLazyColumnTestTag"
const val StaffItemTestTagPrefix = "StaffItemTestTag:"

fun NavGraphBuilder.staffScreens(
    onNavigationIconClick: () -> Unit,
    onStaffItemClick: (url: String) -> Unit,
) {
    composable(staffScreenRoute) {
        StaffScreen(
            onNavigationIconClick = onNavigationIconClick,
            onStaffItemClick = onStaffItemClick,
        )
    }
}

sealed interface StaffUiState {
    val userMessageStateHolder: UserMessageStateHolder

    data class Loading(
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : StaffUiState

    data class Exists(
        override val userMessageStateHolder: UserMessageStateHolder,
        val staff: PersistentList<Staff>,
    ) : StaffUiState
}

@Composable
fun StaffScreen(
    onNavigationIconClick: () -> Unit,
    onStaffItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val eventFlow = rememberEventFlow<StaffScreenEvent>()
    val uiState = staffScreenPresenter(events = eventFlow)

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
    val layoutDirection = LocalLayoutDirection.current
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
            if (!isTopAppBarHidden) {
                AnimatedMediumTopAppBar(
                    title = stringResource(StaffRes.string.staff_title),
                    onBackClick = onBackClick,
                    scrollBehavior = scrollBehavior,
                    navIconContentDescription = "Back",
                )
            }
        },
        contentWindowInsets = WindowInsets.displayCutout.union(WindowInsets.systemBars),
    ) { padding ->
        when (uiState) {
            is StaffUiState.Exists -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding())
                        .let {
                            if (scrollBehavior != null) {
                                it.nestedScroll(scrollBehavior.nestedScrollConnection)
                            } else {
                                it
                            }
                        }
                        .testTag(StaffScreenLazyColumnTestTag),
                    contentPadding = PaddingValues(
                        start = padding.calculateStartPadding(layoutDirection),
                        end = padding.calculateEndPadding(layoutDirection),
                        bottom = 40.dp + padding.calculateBottomPadding(),
                    ),
                ) {
                    items(uiState.staff) { staff ->
                        StaffItem(
                            staff = staff,
                            onStaffItemClick = onStaffItemClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(StaffItemTestTagPrefix.plus(staff.id)),
                        )
                    }
                }
            }
            is StaffUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(padding).fillMaxSize(),
                ) {
                    CircularProgressIndicator()
                }
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
                uiState = StaffUiState.Exists(
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
