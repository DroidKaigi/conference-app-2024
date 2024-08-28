package io.github.droidkaigi.confsched.testing

inline fun <reified T> describeBehaviors(
    name: String,
    block: TestCaseTreeBuilder<T>.() -> Unit,
): List<DescribedBehavior<T>> {
    val builder = TestCaseTreeBuilder<T>()
    builder.block()
    val root = builder.build(name = name)
    return generateBehaviors(root)
}

suspend fun <T> DescribedBehavior<T>.execute(robot: T) {
    for ((index, step) in steps.withIndex()) {
        println("Executing step: $index ($description)")
        when (step) {
            is TestNode.Run -> step.action(robot)
            is TestNode.ItShould -> {
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
    data class ItShould<T>(val description: String, val action: suspend T.() -> Unit) :
        TestNode<T>()
}

data class DescribedBehavior<T>(
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
    val node: TestNode.ItShould<T>,
    val ancestry: List<AncestryNode<T>>,
)

class TestCaseTreeBuilder<T> {
    private val children = mutableListOf<TestNode<T>>()

    fun describe(description: String, block: TestCaseTreeBuilder<T>.() -> Unit) {
        val builder = TestCaseTreeBuilder<T>()
        builder.block()
        children.add(TestNode.Describe(description, builder.children))
    }

    fun doIt(action: suspend T.() -> Unit) {
        children.add(TestNode.Run { action() })
    }

    fun itShould(description: String, action: suspend T.() -> Unit) {
        children.add(TestNode.ItShould(description) { action() })
    }

    fun build(name: String): TestNode.Describe<T> = TestNode.Describe(name, children)
}

fun <T> generateBehaviors(root: TestNode.Describe<T>): List<DescribedBehavior<T>> {
    val checkNodes = collectCheckNodes(root)
    return checkNodes.map { createTestCase(it) }
}

/**
 * Collect all check nodes from the test tree
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

            is TestNode.ItShould -> {
                val fullDescription = if (parentDescription.isNotBlank()) {
                    "$parentDescription - it should ${node.description}"
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
 */
private fun <T> createTestCase(checkNode: CheckNode<T>): DescribedBehavior<T> {
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

            is TestNode.ItShould -> {
                if (node == checkNode.node) {
                    steps.add(node)
                }
            }
        }
    }

    processNode(checkNode.ancestry.first().node, emptyList(), 0)

    return DescribedBehavior(checkNode.fullDescription, steps, checkNode.description)
}
