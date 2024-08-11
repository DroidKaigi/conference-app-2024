package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class FixedAccentColors(
    val primaryFixed: Color,
    val onPrimaryFixed: Color,
    val secondaryFixed: Color,
    val onSecondaryFixed: Color,
    val tertiaryFixed: Color,
    val onTertiaryFixed: Color,
    val primaryFixedDim: Color,
    val secondaryFixedDim: Color,
    val tertiaryFixedDim: Color,
)

@Suppress("CompositionLocalAllowlist")
internal val LocalFixedAccentColors = compositionLocalOf {
    FixedAccentColors(
        primaryFixed = darkScheme.primaryContainer,
        onPrimaryFixed = darkScheme.onPrimaryContainer,
        secondaryFixed = darkScheme.secondaryContainer,
        onSecondaryFixed = darkScheme.onSecondaryContainer,
        tertiaryFixed = darkScheme.tertiaryContainer,
        onTertiaryFixed = darkScheme.onTertiaryContainer,
        primaryFixedDim = darkScheme.primary,
        secondaryFixedDim = darkScheme.secondary,
        tertiaryFixedDim = darkScheme.tertiary,
    )
}

@Suppress("UnusedReceiverParameter")
val ColorScheme.primaryFixed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.primaryFixed

@Suppress("UnusedReceiverParameter")
val ColorScheme.onPrimaryFixed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.onPrimaryFixed

@Suppress("UnusedReceiverParameter")
val ColorScheme.secondaryFixed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.secondaryFixed

@Suppress("UnusedReceiverParameter")
val ColorScheme.onSecondaryFixed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.onSecondaryFixed

@Suppress("UnusedReceiverParameter")
val ColorScheme.tertiaryFixed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.tertiaryFixed

@Suppress("UnusedReceiverParameter")
val ColorScheme.onTertiaryFixed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.onTertiaryFixed

@Suppress("UnusedReceiverParameter")
val ColorScheme.primaryFixedDim: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.primaryFixedDim

@Suppress("UnusedReceiverParameter")
val ColorScheme.secondaryFixedDim: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.secondaryFixedDim

@Suppress("UnusedReceiverParameter")
val ColorScheme.tertiaryFixedDim: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalFixedAccentColors.current.tertiaryFixedDim
