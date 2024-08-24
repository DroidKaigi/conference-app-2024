package io.github.droidkaigi.confsched.droidkaigiui.animation

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
    staticCompositionLocalOf { DefaultFavoriteAnimationScope }

internal fun FavoriteAnimationScope(
    clock: Clock,
    coroutineScope: CoroutineScope,
    direction: FavoriteAnimationDirection,
): FavoriteAnimationScope = FavoriteAnimationScopeImpl(
    clock = clock,
    coroutineScope = coroutineScope,
    direction = direction,
)

sealed interface FavoriteAnimationScope {
    val animations: List<FavoriteAnimationState>
    fun setAnimationFramePosition(position: Offset)
    fun setTargetPosition(position: Offset)
    fun startAnimation(position: Offset)
}

private data object DefaultFavoriteAnimationScope : FavoriteAnimationScope {
    override val animations: List<FavoriteAnimationState> = emptyList()
    override fun setAnimationFramePosition(position: Offset) {
        // Do nothing
    }

    override fun setTargetPosition(position: Offset) {
        // Do nothing
    }

    override fun startAnimation(position: Offset) {
        // Do nothing
    }
}

private class FavoriteAnimationScopeImpl(
    private val clock: Clock,
    private val coroutineScope: CoroutineScope,
    private val direction: FavoriteAnimationDirection,
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
                    direction = direction,
                ),
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
    val direction: FavoriteAnimationDirection,
) {
    private val targetXFromStart: Float = targetPosition.x - startPosition.x
    private val targetYFromStart: Float = targetPosition.y - startPosition.y

    // Time to reach the target point during animation
    private val durationMillis: Long = when (direction) {
        FavoriteAnimationDirection.Vertical -> targetYFromStart
        FavoriteAnimationDirection.Horizontal -> targetXFromStart
    }.let {
        sqrt((2 * it).absoluteValue / GRAPHICAL_ACCELERATION).toLong() * 1000
    }
    private val durationMillisWithTimesSpeed = durationMillis / TIMES_SPEED

    val endTime = startTime + durationMillisWithTimesSpeed

    fun calculateCurrentOffset(currentTime: Long): Offset {
        if (currentTime > endTime) {
            return targetPosition
        }
        val elapsedTimeMillis = currentTime - startTime
        val elapsedTime = elapsedTimeMillis.toFloat() / (1000 / TIMES_SPEED)
        val progress = elapsedTimeMillis / durationMillisWithTimesSpeed.toFloat()

        return when (direction) {
            FavoriteAnimationDirection.Vertical -> {
                val x = targetXFromStart * progress
                val y = (0.5 * GRAPHICAL_ACCELERATION * elapsedTime.pow(2)).toFloat().adjustYSign()
                Offset(
                    x = startPosition.x + x,
                    y = startPosition.y + y,
                )
            }

            FavoriteAnimationDirection.Horizontal -> {
                val x = (0.5 * GRAPHICAL_ACCELERATION * (durationMillis / 1000 - elapsedTime).pow(2)).toFloat()
                val y = targetYFromStart * progress
                Offset(
                    x = targetPosition.x + x,
                    y = startPosition.y + y,
                )
            }
        }
    }

    private fun Float.pow(exponent: Int): Float {
        return toDouble().pow(exponent).toFloat()
    }

    private fun Float.adjustYSign(): Float {
        return if (targetYFromStart < 0) {
            -this
        } else {
            this
        }
    }

    companion object {
        private const val GRAPHICAL_ACCELERATION = 9.8f
        private const val TIMES_SPEED = 20
    }
}

enum class FavoriteAnimationDirection {
    Vertical,
    Horizontal,
}
