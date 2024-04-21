package io.github.droidkaigi.confsched.data.contributors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.compose.safeCollectAsState
import io.github.droidkaigi.confsched.model.Contributor
import io.github.droidkaigi.confsched.model.ContributorsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow

public class DefaultContributorsRepository(
    private val contributorsApi: ContributorsApiClient,
) : ContributorsRepository {
    private val contributorsStateFlow =
        MutableStateFlow<PersistentList<Contributor>>(persistentListOf())

    @Composable
    override fun contributors(): PersistentList<Contributor> {
        val contributors by contributorsStateFlow.safeCollectAsState()
        SafeLaunchedEffect(Unit) {
            if (contributors.isEmpty()) {
                refresh()
            }
        }
        return contributors
    }

    override suspend fun refresh() {
        contributorsStateFlow.value = contributorsApi
            .contributors()
            .toPersistentList()
    }
}
