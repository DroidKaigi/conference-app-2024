package io.github.droidkaigi.confsched.testing.utils

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.text.TextLayoutResult
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

fun hasTestTag(
    testTag: String,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
): SemanticsMatcher {
    return SemanticsMatcher(
        "TestTag ${if (substring) "contains" else "is"} '$testTag' (ignoreCase: $ignoreCase)",
    ) { node ->
        val nodeTestTag: String? = node.config.getOrNull(SemanticsProperties.TestTag)
        when {
            nodeTestTag == null -> false
            substring -> nodeTestTag.contains(testTag, ignoreCase)
            else -> nodeTestTag.equals(testTag, ignoreCase)
        }
    }
}

fun SemanticsNodeInteractionCollection.assertCountAtLeast(
    minimumExpectedSize: Int,
): SemanticsNodeInteractionCollection {
    val errorOnFail = "Failed to assert minimum count of nodes."
    val matchedNodes = fetchSemanticsNodes(
        atLeastOneRootRequired = minimumExpectedSize > 0,
        errorOnFail,
    )
    if (matchedNodes.size < minimumExpectedSize) {
        throw AssertionError(
            buildErrorMessageForMinimumCountMismatch(
                errorMessage = errorOnFail,
                foundNodes = matchedNodes,
                minimumExpectedCount = minimumExpectedSize,
            ),
        )
    }
    return this
}

fun SemanticsNodeInteraction.assertTextDoesNotContain(
    substring: String,
) {
    fetchSemanticsNode()
        .let { node ->
            val textProperty = node
                .config
                .getOrNull(
                    key = SemanticsProperties.Text,
                )
            val textContent = textProperty
                ?.joinToString(
                    separator = " ",
                ) { it.text }
            assertFalse(
                "Node text contains unexpected substring: $substring",
                textContent?.contains(substring) ?: false,
            )
        }
}

fun SemanticsNodeInteraction.assertLineCount(expectedCount: Int) {
    fetchSemanticsNode()
        .let { node ->
            val results = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)
                ?.action
                ?.invoke(results)
            val result = results.firstOrNull()

            assertTrue(
                "Node has unexpected line count (expected $expectedCount, but was ${result?.lineCount})",
                result?.lineCount == expectedCount,
            )
        }
}

fun <T> SemanticsNodeInteraction.assertSemanticsProperty(
    key: SemanticsPropertyKey<T>,
    condition: (T?) -> Boolean,
) {
    fetchSemanticsNode()
        .let { node ->
            val actual = node
                .config
                .getOrNull(key)

            assertTrue(
                "Node has unexpected value for semantics property $key: $actual",
                condition(actual),
            )
        }
}

private fun buildErrorMessageForMinimumCountMismatch(
    errorMessage: String,
    foundNodes: List<SemanticsNode>,
    minimumExpectedCount: Int,
): String {
    return buildString {
        appendLine(errorMessage)
        appendLine("Expected at least: $minimumExpectedCount")
        appendLine("Found: ${foundNodes.size}")
        appendLine("Matched nodes:")
        foundNodes.forEachIndexed { index, node ->
            appendLine("$index: $node")
        }
    }
}
