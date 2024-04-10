package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.collections.immutable.PersistentList

interface ContributorsRepository {

    suspend fun refresh()

    @Composable
    fun contributors(): PersistentList<Contributor>
}

@Composable
fun localContributorsRepository(): ContributorsRepository {
    return LocalRepositories.current[ContributorsRepository::class] as ContributorsRepository
}
