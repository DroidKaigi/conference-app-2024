package io.github.droidkaigi.confsched.profilecard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.roundToIntRect
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.preat.peekaboo.image.picker.toImageBitmap
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.model.ProfileImage
import io.github.droidkaigi.confsched.profilecard.component.ImageCropAreaSelector
import kotlin.math.max

const val cropImageScreenRoute = "cropImage"

const val CropImageScreenTestTag = "CropImageScreenTestTag"

fun NavGraphBuilder.cropImageScreens(
    onNavigationIconClick: () -> Unit,
    onBackWithConfirm: () -> Unit,
) {
    composable(
        cropImageScreenRoute,
    ) {
        CropImageScreen(
            onNavigationIconClick = dropUnlessResumed(block = onNavigationIconClick),
            onBackWithConfirm = onBackWithConfirm,
        )
    }
}

internal sealed interface CropImageScreenState {
    data object Init : CropImageScreenState

    data class Select(
        val profileImage: ProfileImage,
        val isProcessing: Boolean,
    ) : CropImageScreenState

    data class Confirm(
        val profileImage: ProfileImage,
        val shouldBack: Boolean,
    ) : CropImageScreenState
}

@Composable
fun CropImageScreen(
    onNavigationIconClick: () -> Unit,
    onBackWithConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CropImageScreen(
        onNavigationIconClick = onNavigationIconClick,
        onBackWithConfirm = onBackWithConfirm,
        modifier = modifier,
        eventFlow = rememberEventFlow(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CropImageScreen(
    onNavigationIconClick: () -> Unit,
    onBackWithConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    eventFlow: EventFlow<CropImageScreenEvent> = rememberEventFlow(),
    uiState: CropImageScreenState = cropImageScreenPresenter(eventFlow),
) {
    LaunchedEffect(uiState is CropImageScreenState.Confirm && uiState.shouldBack) {
        if (uiState is CropImageScreenState.Confirm && uiState.shouldBack) {
            onBackWithConfirm()
        }
    }

    Scaffold(
        modifier = modifier
            .testTag(CropImageScreenTestTag)
            .fillMaxSize(),
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
        when (uiState) {
            is CropImageScreenState.Init -> Unit
            is CropImageScreenState.Select -> {
                Box(
                    modifier = Modifier.padding(contentPadding),
                ) {
                    SelectScreen(
                        profileImage = uiState.profileImage,
                        isCropButtonEnabled = !uiState.isProcessing,
                        onCrop = { cropRect ->
                            eventFlow.tryEmit(
                                CropImageScreenEvent.Crop(
                                    rect = cropRect,
                                ),
                            )
                        },
                    )

                    if (uiState.isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }

            is CropImageScreenState.Confirm -> {
                ConfirmScreen(
                    profileImage = uiState.profileImage,
                    onConfirm = {
                        eventFlow.tryEmit(CropImageScreenEvent.Confirm)
                    },
                    onCancel = {
                        eventFlow.tryEmit(CropImageScreenEvent.Cancel)
                    },
                    modifier = Modifier.padding(contentPadding),
                )
            }
        }
    }
}

@Composable
private fun SelectScreen(
    profileImage: ProfileImage,
    isCropButtonEnabled: Boolean,
    onCrop: (cropRect: IntRect) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cropRectRatio = 0.6f
    var transformCalculator: TransformCalculator? by remember { mutableStateOf(null) }
    var scale: Float by remember { mutableFloatStateOf(1f) }
    var offset: Offset by remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            val density = LocalDensity.current
            val calculator = remember(profileImage, maxWidth, maxHeight, density) {
                val imageSize = profileImage.size.toSize()
                val layoutSize = with(density) {
                    Size(
                        width = maxWidth.toPx(),
                        height = maxHeight.toPx(),
                    )
                }

                TransformCalculator(
                    cropRectRatio = cropRectRatio,
                    layoutSize = layoutSize,
                    imageSize = imageSize,
                ).also { calculator ->
                    scale = max(calculator.minScale, 1f)
                    transformCalculator = calculator
                }
            }

            ImageCropAreaSelector(
                profileImage = profileImage,
                transformCalculator = calculator,
                scale = { scale },
                offset = { offset },
                onTransform = { newScale, newOffset ->
                    scale = newScale
                    offset = newOffset
                },
            )
        }

        Button(
            onClick = {
                val calculator = requireNotNull(transformCalculator)
                val cropRect = calculator.calculateCropRectInImage(
                    scale = scale,
                    offset = offset,
                )
                onCrop(cropRect.roundToIntRect())
            },
            enabled = isCropButtonEnabled,
        ) {
            Text("Crop")
        }
    }
}

internal class TransformCalculator(
    cropRectRatio: Float,
    layoutSize: Size,
    private val imageSize: Size,
) {
    private val imageLayoutRatio: Float =
        if (imageSize.width / imageSize.height >= layoutSize.width / layoutSize.height) {
            layoutSize.width / imageSize.width
        } else {
            layoutSize.height / imageSize.height
        }

    val cropRectLength: Float = if (layoutSize.width <= layoutSize.height) {
        layoutSize.width * cropRectRatio
    } else {
        layoutSize.height * cropRectRatio
    }

    val minScale: Float = if (imageSize.width <= imageSize.height) {
        cropRectLength / (imageSize.width * imageLayoutRatio)
    } else {
        cropRectLength / (imageSize.height * imageLayoutRatio)
    }

    fun calculateNext(
        oldScale: Float,
        oldOffset: Offset,
        zoom: Float,
        pan: Offset,
    ): Pair<Float, Offset> {
        val tempOffset = oldOffset - pan / oldScale
        val newScale = (oldScale * zoom).coerceAtLeast(minScale)

        val minOffsetX =
            (cropRectLength / newScale - imageSize.width * imageLayoutRatio) / 2
        val minOffsetY =
            (cropRectLength / newScale - imageSize.height * imageLayoutRatio) / 2

        val newOffset = Offset(
            x = tempOffset.x.coerceIn(minOffsetX, -minOffsetX),
            y = tempOffset.y.coerceIn(minOffsetY, -minOffsetY),
        )

        return newScale to newOffset
    }

    fun calculateCropRectInImage(
        scale: Float,
        offset: Offset,
    ): Rect {
        val cropRect = Rect(
            offset = offset * scale - Offset(cropRectLength, cropRectLength) / 2f,
            size = Size(cropRectLength, cropRectLength),
        )
        return Rect(
            offset = cropRect.topLeft / (scale * imageLayoutRatio) + imageSize.toRect().center,
            size = cropRect.size / (scale * imageLayoutRatio),
        )
    }
}

@Composable
private fun ConfirmScreen(
    profileImage: ProfileImage,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = profileImage.bytes.toImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Button(
                onClick = onConfirm,
            ) {
                Text("Confirm")
            }
            Button(
                onClick = onCancel,
            ) {
                Text("Cancel")
            }
        }
    }
}
