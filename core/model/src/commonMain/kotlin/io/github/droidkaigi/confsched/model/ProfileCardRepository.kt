package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import org.jetbrains.compose.resources.DrawableResource

interface ProfileCardRepository {
    @Composable
    fun profileCard(): ProfileCard
    suspend fun save(profileCard: ProfileCard.Exists)
    suspend fun loadQrCodeImageByteArray(link: String, centerLogoRes: DrawableResource): ByteArray
}

@Composable
fun localProfileCardRepository(): ProfileCardRepository {
    return LocalRepositories.current[ProfileCardRepository::class] as ProfileCardRepository
}
