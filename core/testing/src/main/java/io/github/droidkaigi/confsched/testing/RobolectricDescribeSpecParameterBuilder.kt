package io.github.droidkaigi.confsched.testing

inline fun <reified T> describeTests(block: TestCaseTreeBuilder<T>.() -> Unit): List<DescribedTestCase<T>> {
    val builder = TestCaseTreeBuilder<T>()
    builder.block()
    val root = builder.build()
    return generateTestCases(root)
}

suspend fun <T> DescribedTestCase<T>.execute(robot: T) {
    for ((index, step) in steps.withIndex()) {
        println("Executing step: $index ($description)")
        when (step) {
            is TestNode.Run -> step.action(robot)
            is TestNode.Check -> {
                if (step.description == targetCheckDescription) {
                    step.action(robot)
                }
            }

            is TestNode.Describe -> {}
        }
        println("Step executed: $index")
    }
}

sealed class TestNode<T> {
    data class Describe<T>(val description: String, val children: List<TestNode<T>>) : TestNode<T>()
    data class Run<T>(val action: suspend T.() -> Unit) : TestNode<T>()
    data class Check<T>(val description: String, val action: suspend T.() -> Unit) : TestNode<T>()
}

data class DescribedTestCase<T>(
    val description: String,
    val steps: List<TestNode<T>>,
    val targetCheckDescription: String,
) {
    override fun toString(): String = description
}

data class AncestryNode<T>(
    val node: TestNode<T>,
    val childIndex: Int,
)

data class CheckNode<T>(
    val description: String,
    val fullDescription: String,
    val node: TestNode.Check<T>,
    val ancestry: List<AncestryNode<T>>,
)

class TestCaseTreeBuilder<T> {
    private val children = mutableListOf<TestNode<T>>()

    fun describe(description: String, block: TestCaseTreeBuilder<T>.() -> Unit) {
        val builder = TestCaseTreeBuilder<T>()
        builder.block()
        children.add(TestNode.Describe(description, builder.children))
    }

    fun run(action: suspend T.() -> Unit) {
        children.add(TestNode.Run { action() })
    }

    fun check(description: String, action: suspend T.() -> Unit) {
        children.add(TestNode.Check(description) { action() })
    }

    fun build(): TestNode.Describe<T> = TestNode.Describe("", children)
}

fun <T> generateTestCases(root: TestNode.Describe<T>): List<DescribedTestCase<T>> {
    val checkNodes = collectCheckNodes(root)
    return checkNodes.map { createTestCase(it) }
}

/**
 * Collect all check nodes from the test tree
 * it will be O(N)
 */
private fun <T> collectCheckNodes(root: TestNode.Describe<T>): List<CheckNode<T>> {
    val checkNodes = mutableListOf<CheckNode<T>>()

    fun traverse(node: TestNode<T>, parentDescription: String, ancestry: List<AncestryNode<T>>) {
        when (node) {
            is TestNode.Describe -> {
                val currentDescription =
                    if (parentDescription.isEmpty()) node.description else "$parentDescription - ${node.description}"
                node.children.forEachIndexed { index, child ->
                    val currentAncestry = ancestry + AncestryNode(node, index)
                    traverse(child, currentDescription, currentAncestry)
                }
            }

            is TestNode.Check -> {
                val fullDescription = if (parentDescription.isNotBlank()) {
                    "$parentDescription - ${node.description}"
                } else {
                    node.description
                }
                checkNodes.add(CheckNode(node.description, fullDescription, node, ancestry))
            }

            is TestNode.Run -> {}
        }
    }

    traverse(root, "", emptyList())
    return checkNodes
}

/**
 * Create a test case from a check node
 * We only run the steps that are necessary to reach the check node
 * so the time complexity might be O(logN)
 */
private fun <T> createTestCase(checkNode: CheckNode<T>): DescribedTestCase<T> {
    val steps = mutableListOf<TestNode<T>>()

    fun processNode(node: TestNode<T>, ancestry: List<TestNode<T>>, depth: Int) {
        when (node) {
            is TestNode.Describe -> {
                for (child in node.children) {
                    if (depth + 1 < checkNode.ancestry.size && child == checkNode.ancestry[depth + 1].node) {
                        processNode(child, ancestry + node, depth + 1)
                    } else if (child is TestNode.Run) {
                        steps.add(child)
                    } else if (child == checkNode.node) {
                        steps.add(child)
                    }
                }
            }

            is TestNode.Run -> {
                steps.add(node)
            }

            is TestNode.Check -> {
                if (node == checkNode.node) {
                    steps.add(node)
                }
            }
        }
    }

    processNode(checkNode.ancestry.first().node, emptyList(), 0)

    return DescribedTestCase(checkNode.fullDescription, steps, checkNode.description)
}
