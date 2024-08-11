package io.github.droidkaigi.confsched.data.sponsors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.SponsorsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart

public class DefaultSponsorsRepository(
    private val sponsorsApi: SponsorsApiClient,
) : SponsorsRepository {
    private val sponsorsStateFlow =
        MutableStateFlow<PersistentList<Sponsor>>(persistentListOf())

    override fun getSponsorStream(): Flow<PersistentList<Sponsor>> {
        return sponsorsStateFlow.onStart {
            if (sponsorsStateFlow.value.isEmpty()) {
                refresh()
            }
        }
    }

    override suspend fun refresh() {
        sponsorsStateFlow.value = sponsorsApi
            .sponsors()
            .toPersistentList()
    }

    @Composable
    override fun sponsors(): PersistentList<Sponsor> {
        val sponsors by sponsorsStateFlow.safeCollectAsRetainedState()
        SafeLaunchedEffect(Unit) {
            if (sponsors.isEmpty()) {
                refresh()
            }
        }
        return sponsors
    }
}
