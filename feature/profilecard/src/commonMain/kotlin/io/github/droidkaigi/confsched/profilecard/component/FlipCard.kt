package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.profilecard.generated.resources.icon_qr
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardScreenTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardScreenTheme
import io.github.droidkaigi.confsched.profilecard.ProfileCardRes
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun FlipCard(
    uiState: Card,
    modifier: Modifier = Modifier,
    isCreated: Boolean = false,
) {
    var isFlipped by remember { mutableStateOf(false) }
    var isCreated by rememberSaveable { mutableStateOf(isCreated) }
    var initialRotation by remember { mutableStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else initialRotation,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )
    val isBack by remember { derivedStateOf { rotation > 90f } }
    val targetRotation by animateFloatAsState(
        targetValue = 30f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )
    val targetRotation2 by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )

    LaunchedEffect(Unit) {
        if (isCreated) {
            initialRotation = targetRotation
            delay(400)
            initialRotation = targetRotation2
            isCreated = false
        }
    }

    ProvideProfileCardScreenTheme(uiState.theme.toString()) {
        Card(
            modifier = modifier
                .size(width = 300.dp, height = 380.dp)
                .clickable { isFlipped = !isFlipped }
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                },
            colors = CardDefaults.cardColors(containerColor = LocalProfileCardScreenTheme.current.containerColor),
            elevation = CardDefaults.cardElevation(10.dp),
        ) {
            if (isBack) { // Back
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(ProfileCardRes.drawable.icon_qr),
                        contentDescription = null,
                        modifier = Modifier.size(160.dp),
                    )
                }
            } else { // Front
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.image ?: ""),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(120.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = uiState.occupation ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = uiState.nickname,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}
