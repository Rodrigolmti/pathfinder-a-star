data class Node(
    val column: Int,
    val row: Int
) : Comparable<Node> {

    var gScore: Double = Double.POSITIVE_INFINITY
    var hScore: Double = Double.POSITIVE_INFINITY
    val neighbors = mutableListOf<Node>()
    var previous: Node? = null

    private var heuristic: Double = 0.0

    override fun compareTo(other: Node): Int {
        return (heuristic + gScore).let { calc ->
            when {
                calc == other.heuristic + other.gScore -> 0
                calc < other.heuristic + other.gScore -> -1
                else -> 1
            }
        }
    }

    override fun toString(): String {
        return "Coluna: $column linha: $row"
    }
}

fun Node.traceForwardBestPath(): MutableList<Node> {
    val bestRoute = mutableListOf<Node>().apply { add(this@traceForwardBestPath) }
    var node: Node? = this.previous
    while (node != null) {
        bestRoute.add(0, node)
        node = node.previous
    }
    return bestRoute
}