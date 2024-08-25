package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.coroutines.CoroutineDispatcher

interface ProfileCardRepository {
    val ioDispatcher: CoroutineDispatcher

    @Composable
    fun profileCard(): ProfileCard
    suspend fun save(profileCard: ProfileCard.Exists)
}

@Composable
fun localProfileCardRepository(): ProfileCardRepository {
    return LocalRepositories.current[ProfileCardRepository::class] as ProfileCardRepository
}
