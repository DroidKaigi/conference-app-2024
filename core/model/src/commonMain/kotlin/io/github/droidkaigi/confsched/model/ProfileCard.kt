package io.github.droidkaigi.confsched.model

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

sealed interface ProfileCard {
    data object Loading : ProfileCard

    data object DoesNotExists : ProfileCard

    data class Exists(
        val nickname: String,
        val occupation: String?,
        val link: String?,
        val image: String?,
        val theme: ProfileCardTheme,
    ) : ProfileCard
}

data class ImageData internal constructor(
    val image: String,
    val imageBase64: ByteArray,
) {
    constructor(image: String) : this(image, image.decodeBase64Bytes())
    constructor(imageBase64: ByteArray) : this(imageBase64.toBase64(), imageBase64)

    private val imageHash: Int = imageBase64.contentHashCode()

    override fun equals(other: Any?): Boolean {
        return this === other
            || other is ImageData && imageHash == other.imageHash
    }

    override fun hashCode(): Int {
        return imageHash
    }

    companion object {
        @OptIn(ExperimentalEncodingApi::class)
        private fun ByteArray.toBase64(): String = Base64.encode(this)

        @OptIn(ExperimentalEncodingApi::class)
        private fun String.decodeBase64Bytes(): ByteArray = Base64.decode(this)
    }
}

enum class ProfileCardTheme {
    Iguana,
    Hedgehog,
    Giraffe,
    Flamingo,
    Jellyfish,
    None,
}
