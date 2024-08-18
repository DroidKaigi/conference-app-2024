package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.toImageBitmap
import conference_app_2024.feature.profilecard.generated.resources.card_back_blue
import conference_app_2024.feature.profilecard.generated.resources.card_back_green
import conference_app_2024.feature.profilecard.generated.resources.card_back_orange
import conference_app_2024.feature.profilecard.generated.resources.card_back_pink
import conference_app_2024.feature.profilecard.generated.resources.card_back_white
import conference_app_2024.feature.profilecard.generated.resources.card_back_yellow
import conference_app_2024.feature.profilecard.generated.resources.card_front_blue
import conference_app_2024.feature.profilecard.generated.resources.card_front_green
import conference_app_2024.feature.profilecard.generated.resources.card_front_orange
import conference_app_2024.feature.profilecard.generated.resources.card_front_pink
import conference_app_2024.feature.profilecard.generated.resources.card_front_white
import conference_app_2024.feature.profilecard.generated.resources.card_front_yellow
import conference_app_2024.feature.profilecard.generated.resources.icon_qr
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardTheme
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardType
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.profilecard.ProfileCardRes
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import io.ktor.util.decodeBase64Bytes
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val ProfileCardFlipCardTestTag = "ProfileCardFlipCardTestTag"
const val ProfileCardFlipCardFrontTestTag = "ProfileCardFlipCardFrontTestTag"
const val ProfileCardFlipCardBackTestTag = "ProfileCardFlipCardBackTestTag"

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

    Card(
        modifier = modifier
            .testTag(ProfileCardFlipCardTestTag)
            .size(width = 300.dp, height = 380.dp)
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        elevation = CardDefaults.cardElevation(10.dp),
    ) {
        val profileImage = remember { uiState.image.decodeBase64Bytes().toImageBitmap() }
        if (isBack) { // Back
            FlipCardBack(uiState)
        } else { // Front
            FlipCardFront(
                uiState = uiState,
                profileImage = profileImage,
            )
        }
    }
}

@Composable
private fun FlipCardFront(
    uiState: Card,
    profileImage: ImageBitmap,
    modifier: Modifier = Modifier,
) {
    val background = when (uiState.cardType) {
        ProfileCardType.Iguana -> ProfileCardRes.drawable.card_front_green
        ProfileCardType.Hedgehog -> ProfileCardRes.drawable.card_front_orange
        ProfileCardType.Giraffe -> ProfileCardRes.drawable.card_front_yellow
        ProfileCardType.Flamingo -> ProfileCardRes.drawable.card_front_pink
        ProfileCardType.Jellyfish -> ProfileCardRes.drawable.card_front_blue
        ProfileCardType.None -> ProfileCardRes.drawable.card_front_white
    }
    val namePrimaryColor = LocalProfileCardTheme.current.primaryColor
    Box(
        modifier = modifier
            .testTag(ProfileCardFlipCardFrontTestTag)
            .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(103.dp))
            Image(
                bitmap = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(131.dp),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = uiState.occupation,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                maxLines = 1,
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            brush = Brush.verticalGradient(listOf(Color.White, namePrimaryColor)),
                        ),
                    ) {
                        append(uiState.nickname)
                    }
                },
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun FlipCardBack(
    uiState: Card,
    modifier: Modifier = Modifier,
) {
    val background = when (uiState.cardType) {
        ProfileCardType.Iguana -> ProfileCardRes.drawable.card_back_green
        ProfileCardType.Hedgehog -> ProfileCardRes.drawable.card_back_orange
        ProfileCardType.Giraffe -> ProfileCardRes.drawable.card_back_yellow
        ProfileCardType.Flamingo -> ProfileCardRes.drawable.card_back_pink
        ProfileCardType.Jellyfish -> ProfileCardRes.drawable.card_back_blue
        ProfileCardType.None -> ProfileCardRes.drawable.card_back_white
    }
    Box(
        modifier = modifier
            .testTag(ProfileCardFlipCardBackTestTag)
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f
            },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(ProfileCardRes.drawable.icon_qr),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )
    }
}

@Composable
@Preview
fun FlipCardFrontPreview() {
    val uiState = ProfileCard.Exists.fake().let { (nickname, occupation, link, image, cardType) ->
        Card(nickname, occupation, link, image, cardType)
    }
    val profileImage = uiState.image.decodeBase64Bytes().toImageBitmap()

    KaigiTheme {
        Surface(modifier = Modifier.size(300.dp, 380.dp)) {
            ProvideProfileCardTheme(uiState.cardType.name) {
                FlipCardFront(
                    uiState = uiState,
                    profileImage = profileImage,
                )
            }
        }
    }
}

@Composable
@Preview
fun FlipCardBackPreview() {
    val uiState = ProfileCard.Exists.fake().let { (nickname, occupation, link, image, cardType) ->
        Card(nickname, occupation, link, image, cardType)
    }

    KaigiTheme {
        Surface(
            modifier = Modifier
                .size(300.dp, 380.dp)
                .graphicsLayer {
                    rotationY = 180f
                },
        ) {
            FlipCardBack(
                uiState = uiState,
            )
        }
    }
}
