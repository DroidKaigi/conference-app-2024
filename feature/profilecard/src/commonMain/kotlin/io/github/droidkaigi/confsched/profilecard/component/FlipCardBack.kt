package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.toImageBitmap
import conference_app_2024.feature.profilecard.generated.resources.card_back_blue
import conference_app_2024.feature.profilecard.generated.resources.card_back_green
import conference_app_2024.feature.profilecard.generated.resources.card_back_orange
import conference_app_2024.feature.profilecard.generated.resources.card_back_pink
import conference_app_2024.feature.profilecard.generated.resources.card_back_white
import conference_app_2024.feature.profilecard.generated.resources.card_back_yellow
import conference_app_2024.feature.profilecard.generated.resources.qrcode
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardType.Flamingo
import io.github.droidkaigi.confsched.model.ProfileCardType.Giraffe
import io.github.droidkaigi.confsched.model.ProfileCardType.Hedgehog
import io.github.droidkaigi.confsched.model.ProfileCardType.Iguana
import io.github.droidkaigi.confsched.model.ProfileCardType.Jellyfish
import io.github.droidkaigi.confsched.model.ProfileCardType.None
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.profilecard.ProfileCardRes
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import io.github.droidkaigi.confsched.profilecard.toCardUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import qrcode.QRCode

const val ProfileCardFlipCardBackTestTag = "ProfileCardFlipCardBackTestTag"

@Composable
internal fun FlipCardBack(
    uiState: Card,
    painter: Painter,
    modifier: Modifier = Modifier,
) {
    val background = when (uiState.cardType) {
        Iguana -> ProfileCardRes.drawable.card_back_green
        Hedgehog -> ProfileCardRes.drawable.card_back_orange
        Giraffe -> ProfileCardRes.drawable.card_back_yellow
        Flamingo -> ProfileCardRes.drawable.card_back_pink
        Jellyfish -> ProfileCardRes.drawable.card_back_blue
        None -> ProfileCardRes.drawable.card_back_white
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
            painter = painter,
            contentDescription = stringResource(ProfileCardRes.string.qrcode),
            modifier = Modifier.size(160.dp),
        )
    }
}

@Composable
@Preview
fun FlipCardBackPreview() {
    val uiState = ProfileCard.Exists.fake().toCardUiState()!!
    val painter =
        BitmapPainter(QRCode.ofSquares().build(uiState.link).renderToBytes().toImageBitmap())

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
                painter = painter,
            )
        }
    }
}
