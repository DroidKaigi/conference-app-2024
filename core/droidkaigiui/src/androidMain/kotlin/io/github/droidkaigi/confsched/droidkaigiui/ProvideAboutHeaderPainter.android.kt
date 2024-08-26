package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import conference_app_2024.core.droidkaigiui.generated.resources.about_header_title
import io.github.droidkaigi.confsched.core.droidkaigiui.R
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
actual fun provideAboutHeaderTitlePainter(enableAnimation: Boolean): Painter {
    return if (enableAnimation) {
        var animationPlayed by remember { mutableStateOf(false) }

        val painter = rememberAnimatedVectorPainter(
            animatedImageVector = AnimatedImageVector.animatedVectorResource(id = R.drawable.anim_header_title),
            atEnd = animationPlayed,
        )

        LaunchedEffect(Unit) {
            animationPlayed = true
        }

        painter
    } else {
        painterResource(DroidKaigiUiRes.drawable.about_header_title)
    }
}
