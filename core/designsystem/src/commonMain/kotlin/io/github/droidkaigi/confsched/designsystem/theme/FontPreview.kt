package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
internal fun FontPreview() {
    Column(
        Modifier.background(color = Color.White),
    ) {
        val fonts = listOf(
            dotGothic16FontFamily(),
            // Add here when adding fonts.
        )
        fonts.forEach { fontFamily ->
            Text("Display Large", style = appTypography(fontFamily).displayLarge)
            Text("Display Medium", style = appTypography(fontFamily).displayMedium)
            Text("Display Small", style = appTypography(fontFamily).displaySmall)
            Text("Headline Large", style = appTypography(fontFamily).headlineLarge)
            Text("Headline Medium", style = appTypography(fontFamily).headlineMedium)
            Text("Headline Small", style = appTypography(fontFamily).headlineSmall)
            Text("Title Large", style = appTypography(fontFamily).titleLarge)
            Text("Title Medium", style = appTypography(fontFamily).titleMedium)
            Text("Title Small", style = appTypography(fontFamily).titleSmall)
            Text("Label Large", style = appTypography(fontFamily).labelLarge)
            Text("Label Medium", style = appTypography(fontFamily).labelMedium)
            Text("Label Small", style = appTypography(fontFamily).labelSmall)
            Text("Body Large", style = appTypography(fontFamily).bodyLarge)
            Text("Body Medium", style = appTypography(fontFamily).bodyMedium)
            Text("Body Small", style = appTypography(fontFamily).bodySmall)
        }
    }
}
