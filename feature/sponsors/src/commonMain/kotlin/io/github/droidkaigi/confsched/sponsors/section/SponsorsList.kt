package io.github.droidkaigi.confsched.sponsors.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.sponsors.generated.resources.gold_sponsor
import conference_app_2024.feature.sponsors.generated.resources.platinum_sponsor
import conference_app_2024.feature.sponsors.generated.resources.supporters
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Plan.GOLD
import io.github.droidkaigi.confsched.model.Plan.PLATINUM
import io.github.droidkaigi.confsched.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.sponsors.SponsorsListUiState
import io.github.droidkaigi.confsched.sponsors.SponsorsRes
import io.github.droidkaigi.confsched.sponsors.component.SponsorHeader
import io.github.droidkaigi.confsched.sponsors.component.SponsorItem
import kotlinx.collections.immutable.toPersistentList
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
            bottom = 48.dp + contentPadding.calculateBottomPadding(),
        ),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            SponsorHeader(
                text = stringResource(SponsorsRes.string.platinum_sponsor),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(
                        SponsorsListSponsorHeaderTestTagPrefix
                            .plus(stringResource(SponsorsRes.string.platinum_sponsor)),
                    ),
            )
        }
        items(
            items = uiState.platinumSponsors,
            span = { GridItemSpan(maxLineSpan) },
        ) { sponsor ->
            SponsorItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag(SponsorsListSponsorItemTestTagPrefix.plus(sponsor.name)),
                sponsor = sponsor,
                onSponsorsItemClick = onSponsorsItemClick,
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            SponsorHeader(
                text = stringResource(SponsorsRes.string.gold_sponsor),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(
                        SponsorsListSponsorHeaderTestTagPrefix
                            .plus(stringResource(SponsorsRes.string.gold_sponsor)),
                    ),
            )
        }
        items(
            items = uiState.goldSponsors,
            span = { GridItemSpan(3) },
        ) { sponsor ->
            SponsorItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(77.dp)
                    .testTag(SponsorsListSponsorItemTestTagPrefix.plus(sponsor.name)),
                sponsor = sponsor,
                onSponsorsItemClick = onSponsorsItemClick,
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            SponsorHeader(
                text = stringResource(SponsorsRes.string.supporters),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(
                        SponsorsListSponsorHeaderTestTagPrefix
                            .plus(stringResource(SponsorsRes.string.supporters)),
                    ),
            )
        }
        items(
            items = uiState.supporters,
            span = { GridItemSpan(2) },
        ) { sponsor ->
            SponsorItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(77.dp)
                    .testTag(SponsorsListSponsorItemTestTagPrefix.plus(sponsor.name)),
                sponsor = sponsor,
                onSponsorsItemClick = onSponsorsItemClick,
            )
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
                    platinumSponsors = Sponsor.fakes().filter { it.plan == PLATINUM }.toPersistentList(),
                    goldSponsors = Sponsor.fakes().filter { it.plan == GOLD }.toPersistentList(),
                    supporters = Sponsor.fakes().filter { it.plan == SUPPORTER }.toPersistentList(),
                ),
                onSponsorsItemClick = {},
                scrollBehavior = null,
            )
        }
    }
}
