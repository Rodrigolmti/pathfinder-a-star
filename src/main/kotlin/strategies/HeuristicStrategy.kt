package strategies

import Node
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class HeuristicStrategy(private val strategy: (from: Node, to: Node) -> Double) {
    fun calculateDistance(from: Node, to: Node): Double = strategy.invoke(from, to)
}

val calculateManhattanHeuristic = { from: Node, to: Node ->
    abs(from.row.toDouble() - to.row.toDouble()) + abs(to.column.toDouble() - to.column.toDouble())
}

/* Use this heuristic to calculate best rout without diagonal route */
val calculateNonDiagonalHeuristic = { from: Node, to: Node ->
    max(
        abs(from.row.toDouble() - to.row.toDouble()),
        abs(to.column.toDouble() - to.column.toDouble())
    )
}

/* Use this heuristic to calculate best rout with diagonal route using dijkstra’s algorithm */
val calculateEuclideanHeuristic = { from: Node, to: Node ->
    sqrt(
        (from.row.toDouble() - to.row.toDouble()).pow(2) +
                (from.column.toDouble() - to.column.toDouble()).pow(2)
    )
}

