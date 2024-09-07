package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

interface ContributorsRepository {

    @Throws(AppError::class, CancellationException::class)
    suspend fun refresh()

    @Composable
    fun contributors(): PersistentList<Contributor>

    fun getContributorStream(): Flow<PersistentList<Contributor>>
}

@Composable
fun localContributorsRepository(): ContributorsRepository {
    return LocalRepositories.current[ContributorsRepository::class] as ContributorsRepository
}
