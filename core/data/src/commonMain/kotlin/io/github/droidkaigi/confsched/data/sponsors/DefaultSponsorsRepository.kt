package io.github.droidkaigi.confsched.data.sponsors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.SponsorsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
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
            .catch {
                // SKIE doesn't support throwing exceptions from Flow.
                // For more information, please refer to https://github.com/touchlab/SKIE/discussions/19 .
                Logger.e("Failed to refresh in getSponsorStream()", it)
                emit(sponsorsStateFlow.value)
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
