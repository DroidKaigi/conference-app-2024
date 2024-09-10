package io.github.droidkaigi.confsched.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import io.github.droidkaigi.confsched.designsystem.theme.RoomTheme

private const val ClickableTextExpandAnimateDurationMillis = 300

@Composable
private fun findResults(
    content: String,
    regex: Regex,
): Sequence<MatchResult> {
    return remember(content) {
        regex.findAll(content)
    }
}

@Composable
private fun getAnnotatedString(
    content: String,
    findUrlResults: Sequence<MatchResult>,
): AnnotatedString {
    return buildAnnotatedString {
        pushStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.inverseSurface,
            ),
        )
        append(content)
        pop()

        var lastIndex = 0
        findUrlResults.forEach { matchResult ->
            val startIndex = content.indexOf(
                string = matchResult.value,
                startIndex = lastIndex,
            )
            val endIndex = startIndex + matchResult.value.length
            addStyle(
                style = SpanStyle(
                    color = RoomTheme.Jellyfish.primaryColor,
                    textDecoration = TextDecoration.Underline,
                ),
                start = startIndex,
                end = endIndex,
            )
            addStringAnnotation(
                tag = matchResult.value,
                annotation = matchResult.value,
                start = startIndex,
                end = endIndex,
            )

            lastIndex = endIndex
        }
    }
}

/**
 * Provides ClickableText with underline for the specified regex.
 * When underlining a string other than a url, please specify the url as well.
 *
 * @param regex Specify a Regex to extract the string for which you want underlined text decoration
 * @param url If the string to be extracted by regex is a string other than a URL, use this one.
 */
@Composable
fun ClickableLinkText(
    style: TextStyle,
    content: String,
    onLinkClick: (url: String) -> Unit,
    regex: Regex,
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    url: String? = null,
    onOverflow: (Boolean) -> Unit = {},
) {
    val findResults = findResults(
        content = content,
        regex = regex,
    )

    val annotatedString = getAnnotatedString(
        content = content,
        findUrlResults = findResults,
    )

    var isOverflowing by remember { mutableStateOf(false) }

    LaunchedEffect(isOverflowing) {
        onOverflow(isOverflowing)
    }

    ClickableText(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(ClickableTextExpandAnimateDurationMillis, easing = EaseInQuart),
            ),
        text = annotatedString,
        style = style,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = { textLayoutResult ->
            isOverflowing = textLayoutResult.hasVisualOverflow
        },
        onClick = { offset ->
            findResults.forEach { matchResult ->
                annotatedString.getStringAnnotations(
                    tag = matchResult.value,
                    start = offset,
                    end = offset,
                ).firstOrNull()?.let {
                    onLinkClick(url ?: matchResult.value)
                }
            }
        },
    )
}
