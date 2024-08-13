package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories

interface ProfileCardRepository {
    @Composable
    fun profileCard(): ProfileCard?
    suspend fun save(profileCard: ProfileCard)
}

@Composable
fun localProfileCardRepository(): ProfileCardRepository {
    return LocalRepositories.current[ProfileCardRepository::class] as ProfileCardRepository
}
