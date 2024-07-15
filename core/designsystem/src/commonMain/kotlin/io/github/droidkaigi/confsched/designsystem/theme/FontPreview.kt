package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
private fun FontPreview() {
    Column(
        Modifier.background(color = Color.White),
    ) {
        Text("Display Large", style = AppTypography.displayLarge)
        Text("Display Medium", style = AppTypography.displayMedium)
        Text("Display Small", style = AppTypography.displaySmall)
        Text("Headline Large", style = AppTypography.headlineLarge)
        Text("Headline Medium", style = AppTypography.headlineMedium)
        Text("Headline Small", style = AppTypography.headlineSmall)
        Text("Title Large", style = AppTypography.titleLarge)
        Text("Title Medium", style = AppTypography.titleMedium)
        Text("Title Small", style = AppTypography.titleSmall)
        Text("Label Large", style = AppTypography.labelLarge)
        Text("Label Medium", style = AppTypography.labelMedium)
        Text("Label Small", style = AppTypography.labelSmall)
        Text("Body Large", style = AppTypography.bodyLarge)
        Text("Body Medium", style = AppTypography.bodyMedium)
        Text("Body Small", style = AppTypography.bodySmall)
    }
}
