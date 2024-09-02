package io.github.droidkaigi.confsched.designsystem.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import kotlin.math.ceil

/**
 * A [Text] component that automatically downsizes its font size to fit its content,
 * with the upper bound being defined by the font size of its [style].
 */
@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = when (textAlign) {
            TextAlign.Left, TextAlign.Start -> Alignment.CenterStart
            TextAlign.Center -> Alignment.Center
            TextAlign.Right, TextAlign.End -> Alignment.CenterEnd
            TextAlign.Justify -> Alignment.Center
            else -> Alignment.CenterStart
        },
    ) {
        Text(
            text = text,
            color = color,
            fontSize = calculateFontSize(text, style, color, textAlign, maxLines),
            style = style,
            maxLines = maxLines,
            textAlign = textAlign,
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.calculateFontSize(
    text: String,
    style: TextStyle,
    color: Color,
    textAlign: TextAlign,
    maxLines: Int,
): TextUnit = with(LocalDensity.current) {
    // Upper bound of the font size, will decrease in the `while` loop below
    var targetFontSize = style.fontSize

    // Calculate the text layout using the current `targetFontSize`
    val calculateParagraph = @Composable {
        val finalStyle = style.merge(
            TextStyle(
                color = color,
                fontSize = targetFontSize,
                textAlign = textAlign,
            ),
        )

        Paragraph(
            text = text,
            style = finalStyle,
            maxLines = maxLines,
            constraints = Constraints(maxWidth = ceil(maxWidth.toPx()).toInt()),
            density = this,
            fontFamilyResolver = LocalFontFamilyResolver.current,
        )
    }

    var paragraph = calculateParagraph()

    // Keep decreasing the font size until the text fits in the box without exceeding the max lines
    while (paragraph.didExceedMaxLines || maxHeight < paragraph.height.toDp() || maxWidth < paragraph.minIntrinsicWidth.toDp()) {
        targetFontSize *= 0.95
        paragraph = calculateParagraph()
    }

    targetFontSize
}
