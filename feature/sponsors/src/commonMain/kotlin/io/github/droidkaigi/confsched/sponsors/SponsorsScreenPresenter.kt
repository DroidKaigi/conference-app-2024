package io.github.droidkaigi.confsched.sponsors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.Plan.GOLD
import io.github.droidkaigi.confsched.model.Plan.PLATINUM
import io.github.droidkaigi.confsched.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.SponsorsRepository
import io.github.droidkaigi.confsched.model.localSponsorsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

sealed interface SponsorsScreenEvent

@Composable
fun sponsorsScreenPresenter(
    events: EventFlow<SponsorsScreenEvent>,
    sponsorsRepository: SponsorsRepository = localSponsorsRepository(),
): SponsorsScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    val sponsors by rememberUpdatedState(sponsorsRepository.sponsors())
    val sponsorListUiState by rememberUpdatedState(
        sponsorList(
            userMessageStateHolder = userMessageStateHolder,
            sponsors = sponsors,
        ),
    )
    EventEffect(events) { event ->
    }
    SponsorsScreenUiState(
        sponsorsListUiState = sponsorListUiState,
        userMessageStateHolder = userMessageStateHolder,
    )
}

@Composable
private fun sponsorList(
    sponsors: PersistentList<Sponsor>,
    userMessageStateHolder: UserMessageStateHolder,
): SponsorsListUiState {
    val platinumSponsors = sponsors.filter { it.plan == PLATINUM }.toPersistentList()
    val goldSponsors = sponsors.filter { it.plan == GOLD }.toPersistentList()
    val supporters = sponsors.filter { it.plan == SUPPORTER }.toPersistentList()

    val platinumSponsorsUiState = if (platinumSponsors.isNotEmpty()) {
        SponsorsByPlanUiState.Exists(
            userMessageStateHolder = userMessageStateHolder,
            sponsors = platinumSponsors,
        )
    } else {
        SponsorsByPlanUiState.Loading(
            userMessageStateHolder = userMessageStateHolder,
        )
    }

    val goldSponsorsUiState = if (goldSponsors.isNotEmpty()) {
        SponsorsByPlanUiState.Exists(
            userMessageStateHolder = userMessageStateHolder,
            sponsors = goldSponsors,
        )
    } else {
        SponsorsByPlanUiState.Loading(
            userMessageStateHolder = userMessageStateHolder,
        )
    }

    val supportersUiState = if (supporters.isNotEmpty()) {
        SponsorsByPlanUiState.Exists(
            userMessageStateHolder = userMessageStateHolder,
            sponsors = supporters,
        )
    } else {
        SponsorsByPlanUiState.Loading(
            userMessageStateHolder = userMessageStateHolder,
        )
    }

    return SponsorsListUiState(
        platinumSponsorsUiState = platinumSponsorsUiState,
        goldSponsorsUiState = goldSponsorsUiState,
        supportersUiState = supportersUiState,
    )
}
