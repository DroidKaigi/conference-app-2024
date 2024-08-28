package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun PhotoPickerButton(
    onSelectedImage: (ByteArray) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val imagePicker = rememberSingleImagePickerLauncher(onResult = onSelectedImage)

    OutlinedButton(
        onClick = imagePicker::launch,
        modifier = modifier,
        contentPadding = ButtonDefaults.TextButtonWithIconContentPadding,
    ) {
        content.invoke()
    }
}

@Composable
private fun rememberSingleImagePickerLauncher(
    scope: CoroutineScope = rememberCoroutineScope(),
    onResult: (ByteArray) -> Unit,
) = rememberImagePickerLauncher(
    selectionMode = SelectionMode.Single,
    scope = scope,
    onResult = { byteArrays ->
        byteArrays.firstOrNull()?.let {
            onResult(it)
        }
    },
)
