package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import co.touchlab.kermit.Logger
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.abs

@Composable
internal fun rememberFlingBehavior(): FlingBehavior {
    val flingSpec = rememberSplineBasedDecay<Float>()
    return remember {
        NestedScrollFlingBehavior(flingSpec)
    }
}

private class NestedScrollFlingBehavior(
    private val flingDecay: DecayAnimationSpec<Float>,
) : FlingBehavior {
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        return if (abs(initialVelocity) > 1f) {
            var velocityLeft = initialVelocity
            var lastValue = 0f
            val animationState = AnimationState(
                initialValue = 0f,
                initialVelocity = initialVelocity,
            )
            try {
                animationState.animateDecay(flingDecay) {
                    val delta = value - lastValue
                    val consumed = scrollBy(delta)
                    lastValue = value
                    velocityLeft = this.velocity
                    if (abs(delta - consumed) > 0.5f) {
                        this.cancelAnimation()
                    }
                }
            } catch (e: CancellationException) {
                Logger.d { "NestedScrollFlingBehavior.performFling CancellationException $e" }
                velocityLeft = animationState.velocity
            }
            velocityLeft
        } else {
            initialVelocity
        }
    }
}
