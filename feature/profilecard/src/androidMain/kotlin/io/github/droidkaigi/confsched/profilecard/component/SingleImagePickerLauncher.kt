package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.runtime.Composable
import com.preat.peekaboo.image.picker.ImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import kotlinx.coroutines.CoroutineScope

@Composable
internal actual fun rememberCroppingImagePickerLauncher(
    scope: CoroutineScope,
    onCropImage: (ByteArray) -> Unit,
    onSelectedImage: (ByteArray) -> Unit,
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
