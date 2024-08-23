package io.github.droidkaigi.confsched.data.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardRepository

public class DefaultProfileCardRepository(
    private val profileCardDataStore: ProfileCardDataStore,
) : ProfileCardRepository {
    @Composable
    override fun profileCard(): ProfileCard {
        val profileCard by remember {
            profileCardDataStore.get()
        }.safeCollectAsRetainedState(ProfileCard.Loading)

        return profileCard
    }

    override suspend fun save(profileCard: ProfileCard.Exists) {
        profileCardDataStore.save(profileCard)
    }
}
