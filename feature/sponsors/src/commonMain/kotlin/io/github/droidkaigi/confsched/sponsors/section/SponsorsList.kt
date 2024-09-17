package io.github.droidkaigi.confsched.sponsors.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.sponsors.generated.resources.gold_sponsor
import conference_app_2024.feature.sponsors.generated.resources.platinum_sponsor
import conference_app_2024.feature.sponsors.generated.resources.supporters
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.droidkaigiui.plus
import io.github.droidkaigi.confsched.model.Plan.GOLD
import io.github.droidkaigi.confsched.model.Plan.PLATINUM
import io.github.droidkaigi.confsched.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.sponsors.SponsorsByPlanUiState
import io.github.droidkaigi.confsched.sponsors.SponsorsListUiState
import io.github.droidkaigi.confsched.sponsors.SponsorsRes
import io.github.droidkaigi.confsched.sponsors.component.SponsorHeader
import io.github.droidkaigi.confsched.sponsors.component.SponsorItem
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val SponsorsListLazyVerticalGridTestTag = "SponsorsListLazyVerticalGridTestTag"
const val SponsorsListSponsorHeaderTestTagPrefix = "SponsorsListSponsorHeaderTestTag:"
const val SponsorsListSponsorItemTestTagPrefix = "SponsorsListSponsorItemTestTag:"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SponsorsList(
    uiState: SponsorsListUiState,
    onSponsorsItemClick: (url: String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .let {
                if (scrollBehavior != null) {
                    it.nestedScroll(scrollBehavior.nestedScrollConnection)
                } else {
                    it
                }
            }
            .testTag(SponsorsListLazyVerticalGridTestTag),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 48.dp,
        ).plus(contentPadding),
    ) {
        sponsorsByPlanSection(
            headerStringResource = SponsorsRes.string.platinum_sponsor,
            sponsorsByPlanUiState = uiState.platinumSponsorsUiState,
            onSponsorsItemClick = onSponsorsItemClick,
            contentPadding = contentPadding,
            sponsorItemSpan = { GridItemSpan(maxLineSpan) },
            sponsorItemHeight = 110.dp,
        )

        sponsorsByPlanSection(
            headerStringResource = SponsorsRes.string.gold_sponsor,
            sponsorsByPlanUiState = uiState.goldSponsorsUiState,
            onSponsorsItemClick = onSponsorsItemClick,
            contentPadding = contentPadding,
            sponsorItemSpan = { GridItemSpan(3) },
            sponsorItemHeight = 77.dp,
        )

        sponsorsByPlanSection(
            headerStringResource = SponsorsRes.string.supporters,
            sponsorsByPlanUiState = uiState.supportersUiState,
            onSponsorsItemClick = onSponsorsItemClick,
            contentPadding = contentPadding,
            sponsorItemSpan = { GridItemSpan(2) },
            sponsorItemHeight = 77.dp,
            isLastSection = true,
        )
    }
}

private fun LazyGridScope.sponsorsByPlanSection(
    headerStringResource: StringResource,
    sponsorsByPlanUiState: SponsorsByPlanUiState,
    contentPadding: PaddingValues,
    sponsorItemSpan: LazyGridItemSpanScope.() -> GridItemSpan,
    sponsorItemHeight: Dp,
    onSponsorsItemClick: (url: String) -> Unit,
    isLastSection: Boolean = false,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        val headerText = stringResource(headerStringResource)
        SponsorHeader(
            text = headerText,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(SponsorsListSponsorHeaderTestTagPrefix.plus(headerText)),
        )
    }
    when (sponsorsByPlanUiState) {
        is SponsorsByPlanUiState.Exists -> {
            items(
                items = sponsorsByPlanUiState.sponsors,
                span = { sponsorItemSpan() },
            ) { sponsor ->
                SponsorItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sponsorItemHeight)
                        .testTag(SponsorsListSponsorItemTestTagPrefix.plus(sponsor.name)),
                    sponsor = sponsor,
                    onSponsorsItemClick = onSponsorsItemClick,
                )
            }
        }

        is SponsorsByPlanUiState.Loading -> {
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(contentPadding).fillMaxWidth(),
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
    if (isLastSection.not()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SponsorsListPreview() {
    KaigiTheme {
        Surface {
            SponsorsList(
                uiState = SponsorsListUiState(
                    platinumSponsorsUiState = SponsorsByPlanUiState.Exists(
                        userMessageStateHolder = UserMessageStateHolderImpl(),
                        sponsors = Sponsor.fakes().filter { it.plan == PLATINUM }
                            .toPersistentList(),
                    ),
                    goldSponsorsUiState = SponsorsByPlanUiState.Exists(
                        userMessageStateHolder = UserMessageStateHolderImpl(),
                        sponsors = Sponsor.fakes().filter { it.plan == GOLD }.toPersistentList(),
                    ),
                    supportersUiState = SponsorsByPlanUiState.Exists(
                        userMessageStateHolder = UserMessageStateHolderImpl(),
                        sponsors = Sponsor.fakes().filter { it.plan == SUPPORTER }
                            .toPersistentList(),
                    ),
                ),
                onSponsorsItemClick = {},
                scrollBehavior = null,
            )
        }
    }
}
