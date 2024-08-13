package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.preat.peekaboo.image.picker.toImageBitmap
import io.github.droidkaigi.confsched.profilecard.rememberSingleImagePickerLauncher

@Composable
internal fun PhotoPickerButton(
    onSelectedImage: (ByteArray) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var isOpenDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberSingleImagePickerLauncher {
        with(it.toImageBitmap()) {
            if (height != width) {
                // If the image is not square, throw an error.
                // Ideally, we would like to crop the image, but this is currently on hold due to the difficulty of implementing it by KMP.
                isOpenDialog = true
            } else {
                onSelectedImage(it)
            }
        }
    }

    if (isOpenDialog) {
        AlertDialog(
            onDismissRequest = { isOpenDialog = false },
            title = { Text("Error") },
            text = { Text("The image is not square.") },
            confirmButton = {
                Button(
                    onClick = { isOpenDialog = false },
                ) {
                    Text("OK")
                }
            },
        )
    }

    Button(
        onClick = {
            imagePicker.launch()
        },
        modifier = modifier
    ) {
        content()
    }
}
