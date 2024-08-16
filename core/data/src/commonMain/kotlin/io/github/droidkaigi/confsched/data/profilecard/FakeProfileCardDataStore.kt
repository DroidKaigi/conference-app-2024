package io.github.droidkaigi.confsched.data.profilecard

import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCard.Exists
import io.github.droidkaigi.confsched.model.fake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public class FakeProfileCardDataStore : ProfileCardDataStore {

    public sealed class Status : ProfileCardDataStore {
        public data object AllNotEntered : Status() {
            override suspend fun save(profileCard: Exists) {
                TODO("Not yet implemented")
            }

            override fun get(): Flow<ProfileCard> {
                return flowOf(
                    ProfileCard.DoesNotExists,
                )
            }
        }

        public data object NoInputOtherThanImage : Status() {
            override suspend fun save(profileCard: Exists) {
                TODO("Not yet implemented")
            }

            override fun get(): Flow<ProfileCard> {
                return flowOf(
                    Exists.fake().copy(
                        nickname = "",
                        occupation = "",
                        link = "",
                    ),
                )
            }
        }
    }

    private var status: Status = Status.AllNotEntered

    public fun setup(status: Status) {
        this.status = status
    }

    override suspend fun save(profileCard: Exists) {
        this.status.save(profileCard)
    }

    override fun get(): Flow<ProfileCard> {
        return this.status.get()
    }
}
