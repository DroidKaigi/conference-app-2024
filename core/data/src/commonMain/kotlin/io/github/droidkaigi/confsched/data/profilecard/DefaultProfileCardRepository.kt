package io.github.droidkaigi.confsched.data.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardRepository

internal class DefaultProfileCardRepository(
    private val profileCardDataStore: ProfileCardDataStore,
) : ProfileCardRepository {
    @Composable
    override fun profileCard(): ProfileCard? {
        val profileCard by remember {
            profileCardDataStore.get()
        }.safeCollectAsRetainedState(null)

        return profileCard
    }

    override suspend fun save(profileCard: ProfileCard) {
        profileCardDataStore.save(profileCard)
    }
}
