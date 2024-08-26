package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import conference_app_2024.core.droidkaigiui.generated.resources.connection_failed
import io.github.droidkaigi.confsched.model.AppError
import io.github.droidkaigi.confsched.model.AppError.InternetConnectionException
import org.jetbrains.compose.resources.stringResource
import kotlin.reflect.KClass

/**
 * If you want to display composeResource error messages instead of error messages from the API, define them here
 */
@Composable
internal fun composeResourceErrorMessages(): List<ComposeResourceErrorMessage> = listOf(
    ComposeResourceErrorMessage(
        InternetConnectionException::class,
        stringResource(DroidKaigiUiRes.string.connection_failed),
    ),
)

internal data class ComposeResourceErrorMessage(
    val appErrorClass: KClass<out AppError>,
    val message: String,
)
