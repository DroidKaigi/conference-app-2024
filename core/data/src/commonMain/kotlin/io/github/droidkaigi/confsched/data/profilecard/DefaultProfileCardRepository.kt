package io.github.droidkaigi.confsched.data.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardRepository
import io.github.droidkaigi.confsched.model.ProfileImage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import qrcode.QRCode

public class DefaultProfileCardRepository(
    private val profileCardDataStore: ProfileCardDataStore,
    private val ioDispatcher: CoroutineDispatcher,
) : ProfileCardRepository {
    private val profileImageInEditStateFlow = MutableStateFlow<ProfileImage?>(null)
    private val profileImageCandidateStateFlow = MutableStateFlow<ProfileImage?>(null)

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

    @OptIn(ExperimentalResourceApi::class)
    override suspend fun loadQrCodeImageByteArray(
        link: String,
        centerLogoRes: DrawableResource,
    ): ByteArray {
        return withContext(ioDispatcher) {
            val logoImage = getDrawableResourceBytes(
                environment = getSystemResourceEnvironment(),
                resource = centerLogoRes,
            )
            QRCode.ofSquares()
                .withLogo(logoImage, 400, 400)
                .build(link)
                .renderToBytes()
        }
    }

    @Composable
    override fun profileImageInEdit(): ProfileImage? {
        val profileImage by profileImageInEditStateFlow.safeCollectAsRetainedState()
        return profileImage
    }

    override fun setProfileImageInEdit(profileImage: ProfileImage) {
        profileImageInEditStateFlow.value = profileImage
    }

    override fun clearProfileImageInEdit() {
        profileImageInEditStateFlow.value = null
    }

    @Composable
    override fun profileImageCandidate(): ProfileImage? {
        val profileImageCandidate by profileImageCandidateStateFlow.safeCollectAsRetainedState()
        return profileImageCandidate
    }

    override fun setProfileImageCandidate(profileImage: ProfileImage) {
        profileImageCandidateStateFlow.value = profileImage
    }

    override fun clearProfileImageCache() {
        profileImageCandidateStateFlow.value = null
    }
}
