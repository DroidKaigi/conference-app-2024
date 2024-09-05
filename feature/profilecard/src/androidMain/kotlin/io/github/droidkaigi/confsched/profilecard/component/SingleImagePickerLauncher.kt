package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.runtime.Composable
import com.preat.peekaboo.image.picker.ImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import kotlinx.coroutines.CoroutineScope

@Composable
internal actual fun rememberCroppingImagePickerLauncher(
    onCropImage: (ByteArray) -> Unit,
    onSelectedImage: (ByteArray) -> Unit,
    scope: CoroutineScope,
): ImagePickerLauncher {
    return rememberSingleImagePickerLauncher(scope) {
        val imageBitmap = it.toImageBitmap()

        if (imageBitmap.height != imageBitmap.width) {
            onCropImage(it)
        } else {
            onSelectedImage(it)
        }
    }
}
