package io.github.droidkaigi.confsched.model

import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize

data class ProfileImage(
    val bytes: ByteArray,
) {
    val size: IntSize
        get() = bytes.imageSize

    fun crop(rect: IntRect): ProfileImage {
        return copy(
            bytes = bytes.crop(rect),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ProfileImage

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}

expect val ByteArray.imageSize: IntSize
expect fun ByteArray.crop(rect: IntRect): ByteArray
