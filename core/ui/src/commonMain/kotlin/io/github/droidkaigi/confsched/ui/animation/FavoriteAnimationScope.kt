package io.github.droidkaigi.confsched.ui.animation

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

@Suppress("CompositionLocalAllowlist")
internal val LocalFavoriteAnimationScope: ProvidableCompositionLocal<FavoriteAnimationScope> =
    staticCompositionLocalOf {
        error("No FavoriteAnimationScope provided")
    }

internal fun FavoriteAnimationScope(
    clock: Clock,
    coroutineScope: CoroutineScope,
    isEnabled: Boolean,
): FavoriteAnimationScope = FavoriteAnimationScopeImpl(
    clock = clock,
    coroutineScope = coroutineScope,
    isEnabled = isEnabled,
)

sealed interface FavoriteAnimationScope {
    val animations: List<FavoriteAnimationState>
    fun setAnimationFramePosition(position: Offset)
    fun setTargetPosition(position: Offset)
    fun startAnimation(position: Offset)
}

private class FavoriteAnimationScopeImpl(
    private val clock: Clock,
    private val coroutineScope: CoroutineScope,
    private val isEnabled: Boolean,
) : FavoriteAnimationScope {

    private val specMutex = Mutex()
    private val observeAnimationMutex = Mutex()

    private var animationFramePosition: Offset = Offset.Zero
    private var targetPosition: Offset = Offset.Zero

    private val animationSpecs = mutableListOf<FavoriteAnimationSpec>()

    private var _animations = mutableStateOf(emptyList<FavoriteAnimationState>())
    override val animations: List<FavoriteAnimationState> by _animations

    override fun setAnimationFramePosition(position: Offset) {
        animationFramePosition = position
    }

    override fun setTargetPosition(position: Offset) {
        targetPosition = position
    }

    override fun startAnimation(position: Offset) {
        if (!isEnabled) {
            return
        }
        coroutineScope.launch {
            addAnimationSpec(startPosition = position)
            observeAnimation()
        }
    }

    private suspend fun addAnimationSpec(startPosition: Offset) {
        specMutex.withLock {
            animationSpecs.add(
                FavoriteAnimationSpec(
                    startTime = clock.now().toEpochMilliseconds(),
                    startPosition = startPosition,
                    targetPosition = targetPosition,
                )
            )
        }
    }

    private suspend fun observeAnimation() {
        observeAnimationMutex.withLock {

            var isObserving = specMutex.withLock { animationSpecs.isNotEmpty() }
            while (isObserving) {
                specMutex.withLock {
                    val now = clock.now().toEpochMilliseconds()
                    animationSpecs.retainAll { spec ->
                        spec.endTime > now
                    }
                    _animations.value = animationSpecs.map { spec ->
                        FavoriteAnimationState(
                            offset = spec.calculateCurrentOffset(now) - animationFramePosition,
                        )
                    }
                    isObserving = animationSpecs.isNotEmpty()
                }
                delay(5)
            }
        }
    }
}

data class FavoriteAnimationState(
    val offset: Offset,
)

data class FavoriteAnimationSpec(
    val startTime: Long,
    val startPosition: Offset,
    val targetPosition: Offset,
) {
    companion object {
        private const val GRAPHICAL_ACCELERATION_PIXELS = 9.8f
    }

    private val targetXFromStart: Float = targetPosition.x - startPosition.x
    private val targetYFromStart: Float = targetPosition.y - startPosition.y

    // 自由落下時に目標地点に到達するまでの時間
    private val durationMillis: Long =
        sqrt((2 * targetYFromStart).absoluteValue / GRAPHICAL_ACCELERATION_PIXELS).toLong() * 1000

    val endTime = startTime + durationMillis

    fun calculateCurrentOffset(currentTime: Long): Offset {
        if (currentTime > endTime) {
            return targetPosition
        }
        val elapsedTime = currentTime - startTime
        val progress = elapsedTime / durationMillis.toFloat()
        return Offset(
            x = startPosition.x + targetXFromStart * progress,
            y = startPosition.y + (0.5 * GRAPHICAL_ACCELERATION_PIXELS * (elapsedTime.toFloat() / 1000).pow(
                2
            )).toFloat(),
        )
    }

    private fun Float.pow(exponent: Int): Float {
        return toDouble().pow(exponent).toFloat()
    }
}
