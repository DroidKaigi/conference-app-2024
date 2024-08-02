package io.github.droidkaigi.confsched.sponsors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.Plan.GOLD
import io.github.droidkaigi.confsched.model.Plan.PLATINUM
import io.github.droidkaigi.confsched.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.SponsorsRepository
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.model.localSponsorsRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow

sealed interface SponsorsScreenEvent

@Composable
fun sponsorsScreenPresenter(
    events: Flow<SponsorsScreenEvent>,
    sponsorsRepository: SponsorsRepository = localSponsorsRepository(),
): SponsorsScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    val sponsors by rememberUpdatedState(sponsorsRepository.sponsors())
    SafeLaunchedEffect(Unit) {
        events.collect {}
    }
    SponsorsScreenUiState(
        sponsorsListUiState = SponsorsListUiState(
            platinumSponsors = Sponsor.fakes().filter { it.plan == PLATINUM }.toPersistentList(),
            goldSponsors = Sponsor.fakes().filter { it.plan == GOLD }.toPersistentList(),
            supporters = Sponsor.fakes().filter { it.plan == SUPPORTER }.toPersistentList(),
        ),
        userMessageStateHolder = userMessageStateHolder,
    )
}
