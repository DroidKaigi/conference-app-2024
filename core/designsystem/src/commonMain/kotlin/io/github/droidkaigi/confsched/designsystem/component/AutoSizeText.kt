package io.github.droidkaigi.confsched.designsystem.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
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
        modifier = modifier.semantics(mergeDescendants = true) {},
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
            fontSize = rememberFontSize(text, style, color, textAlign, maxLines),
            style = style,
            maxLines = maxLines,
            textAlign = textAlign,
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.rememberFontSize(
    text: String,
    style: TextStyle,
    color: Color,
    textAlign: TextAlign,
    maxLines: Int,
): TextUnit {
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current

    return remember(text, style.fontSize, textAlign, maxLines, density, maxWidth, maxHeight) {
        // Helper function to calculate if a given text size
        // would cause an overflow when placed into this BoxWithConstraints
        val hasOverflowWhenPlaced: TextUnit.() -> Boolean = {
            val finalStyle = style.merge(
                TextStyle(
                    color = color,
                    fontSize = this,
                    textAlign = textAlign,
                ),
            )

            with(density) {
                Paragraph(
                    text = text,
                    style = finalStyle,
                    maxLines = maxLines,
                    constraints = Constraints(maxWidth = ceil(maxWidth.toPx()).toInt()),
                    density = this,
                    fontFamilyResolver = fontFamilyResolver,
                ).run {
                    didExceedMaxLines || maxHeight < height.toDp() || maxWidth < minIntrinsicWidth.toDp()
                }
            }
        }

        // If the original text size fits already without overflowing,
        // then there is no need to do anything
        if (!style.fontSize.hasOverflowWhenPlaced()) {
            return@remember style.fontSize
        }

        // Otherwise, find the biggest font size that still fits using binary search
        var lo = 1
        var hi = style.fontSize.value.toInt()
        val type = style.fontSize.type

        while (lo <= hi) {
            val mid = lo + (hi - lo) / 2
            if (mid.asTextUnit(type).hasOverflowWhenPlaced()) {
                hi = mid - 1
            } else {
                lo = mid + 1
            }
        }

        // After the binary search, the right pointer is the largest size
        // that still works without overflowing the box
        hi.asTextUnit(type)
    }
}

private fun Int.asTextUnit(type: TextUnitType) = when (type) {
    TextUnitType.Sp -> this.sp
    TextUnitType.Em -> this.em
    TextUnitType.Unspecified -> TextUnit.Unspecified
    else -> error("Invalid TextUnitType: $type")
}
