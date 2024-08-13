package io.github.droidkaigi.confsched.profilecard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.profilecard.generated.resources.icon_qr
import io.github.droidkaigi.confsched.model.ProfileCardTheme.BLUE
import io.github.droidkaigi.confsched.model.ProfileCardTheme.Default
import io.github.droidkaigi.confsched.model.ProfileCardTheme.ORANGE
import io.github.droidkaigi.confsched.model.ProfileCardTheme.PINK
import io.github.droidkaigi.confsched.model.ProfileCardTheme.WHITE
import io.github.droidkaigi.confsched.model.ProfileCardTheme.YELLOW
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun FlipCard(
    uiState: ProfileCardUiState.Card,
    modifier: Modifier = Modifier,
    isCreated: Boolean = false
) {
    var isFlipped by remember { mutableStateOf(false) }
    var initialRotation by remember { mutableStateOf(0f) }
    val rotation = animateFloatAsState(
        targetValue = if (isFlipped) 180f else initialRotation,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )
    val targetRotation = animateFloatAsState(
        targetValue = 30f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    ).value
    val targetRotation2 = animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    ).value

    LaunchedEffect(Unit) {
        if (isCreated) {
            initialRotation = targetRotation
            delay(400)
            initialRotation = targetRotation2
        }
    }

    Card(
        modifier = modifier
            .size(width = 300.dp, height = 380.dp)
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            },
        colors = CardDefaults.cardColors(
            containerColor = when (uiState.theme) {
                Default -> Color(0xFFC0FF8E)
                ORANGE -> Color(0xFFFFBB69)
                YELLOW -> Color(0xFFFFFA77)
                PINK -> Color(0xFFFFA0C9)
                BLUE -> Color(0xFF93E5FF)
                WHITE -> Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        if (isFlipped) { // Back
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
                    painter = rememberAsyncImagePainter(uiState.imageUri ?: ""),
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
