package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.PersistentList

interface ContributorsRepository {

    suspend fun refresh()

    @Composable
    fun contributors(): PersistentList<Contributor>
}
