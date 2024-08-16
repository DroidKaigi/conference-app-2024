package io.github.droidkaigi.confsched.testing.utils

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionCollection

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
