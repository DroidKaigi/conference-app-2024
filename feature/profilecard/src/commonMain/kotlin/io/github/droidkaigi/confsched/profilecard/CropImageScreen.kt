package io.github.droidkaigi.confsched.profilecard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.preat.peekaboo.image.picker.toImageBitmap
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.model.ProfileImage

const val cropImageScreenRoute = "cropImage"

fun NavGraphBuilder.cropImageScreens(
    onNavigationIconClick: () -> Unit,
) {
    composable(
        cropImageScreenRoute,
    ) {
        CropImageScreen(
            onNavigationIconClick = dropUnlessResumed(block = onNavigationIconClick),
        )
    }
}

internal data class CropImageScreenState(
    val profileImage: ProfileImage?,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CropImageScreen(
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    eventEmitter: EventFlow<CropImageScreenEvent> = rememberEventFlow(),
    uiState: CropImageScreenState = cropImageScreenPresenter(eventEmitter),
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Crop Image")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigationIconClick,
                    ) {
                        Icon(
                            imageVector = Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            uiState.profileImage?.let {
                Image(
                    bitmap = it.bytes.toImageBitmap(),
                    contentDescription = null,
                )
            }
        }
    }
}
