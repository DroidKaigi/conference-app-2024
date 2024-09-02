package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.toImageBitmap
import conference_app_2024.feature.profilecard.generated.resources.card_front_blue
import conference_app_2024.feature.profilecard.generated.resources.card_front_green
import conference_app_2024.feature.profilecard.generated.resources.card_front_orange
import conference_app_2024.feature.profilecard.generated.resources.card_front_pink
import conference_app_2024.feature.profilecard.generated.resources.card_front_white
import conference_app_2024.feature.profilecard.generated.resources.card_front_yellow
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardTheme
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardType
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.profilecard.ProfileCardRes
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import io.github.droidkaigi.confsched.profilecard.decodeBase64Bytes
import io.github.droidkaigi.confsched.profilecard.toCardUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val ProfileCardFlipCardFrontTestTag = "ProfileCardFlipCardFrontTestTag"

@Composable
internal fun FlipCardFront(
    uiState: Card,
    profileImagePainter: Painter,
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
                painter = profileImagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
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
@Preview
fun FlipCardFrontPreview() {
    val uiState = ProfileCard.Exists.fake().toCardUiState()!!
    val painter = BitmapPainter(uiState.image.decodeBase64Bytes().toImageBitmap())

    KaigiTheme {
        Surface(modifier = Modifier.size(300.dp, 380.dp)) {
            ProvideProfileCardTheme(uiState.cardType.name) {
                FlipCardFront(
                    uiState = uiState,
                    profileImagePainter = painter,
                )
            }
        }
    }
}
