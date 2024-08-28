package io.github.droidkaigi.confsched.sponsors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
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
        sponsorList(sponsors = sponsors),
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
): SponsorsListUiState {
    return SponsorsListUiState(
        platinumSponsors = sponsors.filter { it.plan == PLATINUM }.toPersistentList(),
        goldSponsors = sponsors.filter { it.plan == GOLD }.toPersistentList(),
        supporters = sponsors.filter { it.plan == SUPPORTER }.toPersistentList(),
    )
}
