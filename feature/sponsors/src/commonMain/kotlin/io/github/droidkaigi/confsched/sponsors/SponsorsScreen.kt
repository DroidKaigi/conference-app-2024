package io.github.droidkaigi.confsched.sponsors

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Plan.GOLD
import io.github.droidkaigi.confsched.model.Plan.PLATINUM
import io.github.droidkaigi.confsched.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.ui.handleOnClickIfNotNavigating
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.ui.tooling.preview.Preview

const val sponsorsScreenRoute = "sponsors"
const val SponsorsScreenTestTag = "SponsorsScreenTestTag"

fun NavGraphBuilder.sponsorsScreens(
    onNavigationIconClick: () -> Unit,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    composable(sponsorsScreenRoute) {
        val lifecycleOwner = LocalLifecycleOwner.current

        SponsorsScreen(
            onNavigationIconClick = {
                handleOnClickIfNotNavigating(
                    lifecycleOwner,
                    onNavigationIconClick,
                )
            },
            onSponsorsItemClick = onSponsorsItemClick,
        )
    }
}

data class SponsorsScreenUiState(
    val sponsorListUiState: SponsorListUiState,
    val userMessageStateHolder: UserMessageStateHolder,
)

data class SponsorListUiState(
    val platinumSponsors: PersistentList<Sponsor>,
    val goldSponsors: PersistentList<Sponsor>,
    val supporters: PersistentList<Sponsor>,
)

@Composable
fun SponsorsScreen(
    onNavigationIconClick: () -> Unit,
    onSponsorsItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val uiState = SponsorsScreenUiState(
        sponsorListUiState = SponsorListUiState(
            platinumSponsors = Sponsor.fakes().filter { it.plan == PLATINUM }.toPersistentList(),
            goldSponsors = Sponsor.fakes().filter { it.plan == GOLD }.toPersistentList(),
            supporters = Sponsor.fakes().filter { it.plan == SUPPORTER }.toPersistentList(),
        ),
        userMessageStateHolder = UserMessageStateHolderImpl(),
    )

    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    SponsorsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigationIconClick,
        onSponsorsItemClick = onSponsorsItemClick,
        modifier = modifier,
        isTopAppBarHidden = isTopAppBarHidden,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SponsorsScreen(
    uiState: SponsorsScreenUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    isTopAppBarHidden: Boolean,
    modifier: Modifier = Modifier,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    val scrollBehavior =
        if (!isTopAppBarHidden) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        } else {
            null
        }
    Scaffold(
        modifier = modifier.testTag(SponsorsScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (scrollBehavior != null) {
                LargeTopAppBar(
                    title = {
                        Text(text = "Sponsors")
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
                .let {
                    if (scrollBehavior != null) {
                        it.nestedScroll(scrollBehavior.nestedScrollConnection)
                    } else {
                        it
                    }
                },
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "PLATINUM SPONSORS",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                )
            }
            items(
                items = uiState.sponsorListUiState.platinumSponsors,
                span = { GridItemSpan(maxLineSpan) },
            ) { sponsor ->
                SponsorItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    sponsor = sponsor,
                    onSponsorsItemClick = onSponsorsItemClick,
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "GOLD SPONSORS",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                )
            }
            items(
                items = uiState.sponsorListUiState.goldSponsors,
                span = { GridItemSpan(3) },
            ) { sponsor ->
                SponsorItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(77.dp),
                    sponsor = sponsor,
                    onSponsorsItemClick = onSponsorsItemClick,
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "SUPPORTERS",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                )
            }
            items(
                items = uiState.sponsorListUiState.platinumSponsors,
                span = { GridItemSpan(2) },
            ) { sponsor ->
                SponsorItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(77.dp),
                    sponsor = sponsor,
                    onSponsorsItemClick = onSponsorsItemClick,
                )
            }
        }
    }
}

@Composable
fun SponsorItem(
    modifier: Modifier,
    sponsor: Sponsor,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    Card(
        modifier = modifier.clickable { onSponsorsItemClick(sponsor.link) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
        ),
    ) {
        Image(
            painter = rememberAsyncImagePainter(sponsor.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp,
                ),
        )
    }
}

@Composable
@Preview
fun SponsorsScreenPreview() {
    KaigiTheme {
        Surface {
            SponsorsScreen(
                uiState = SponsorsScreenUiState(
                    sponsorListUiState = SponsorListUiState(
                        platinumSponsors = Sponsor.fakes().filter { it.plan == PLATINUM }.toPersistentList(),
                        goldSponsors = Sponsor.fakes().filter { it.plan == GOLD }.toPersistentList(),
                        supporters = Sponsor.fakes().filter { it.plan == SUPPORTER }.toPersistentList(),
                    ),
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                snackbarHostState = SnackbarHostState(),
                onSponsorsItemClick = {},
                onBackClick = {},
                isTopAppBarHidden = false,
            )
        }
    }
}
