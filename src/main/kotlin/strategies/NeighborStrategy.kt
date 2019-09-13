package strategies

import Node

class NeighborStrategy(private val strategy: (grid: Map<Int, List<Node>>, columns: Int, rows: Int) -> Unit) {
    fun generateNeighbors(grid: Map<Int, List<Node>>, columns: Int, rows: Int) = strategy.invoke(grid, columns, rows)
}

val findDiagonalNeighbors = { grid: Map<Int, List<Node>>, columns: Int, rows: Int ->
    for (column in 0 until columns) {
        for (row in 0 until rows) {
            grid[column]?.get(row)?.let { node ->
                generateNonDiagonalNeighbors(columns, rows, column, row, grid, node)
                generateDiagonalNeighbors(columns, rows, column, row, grid, node)
            }
        }
    }
}

val findNonDiagonalNeighbors = { grid: Map<Int, List<Node>>, columns: Int, rows: Int ->
    for (column in 0 until columns) {
        for (row in 0 until rows) {
            grid[column]?.get(row)?.let { node ->
                generateNonDiagonalNeighbors(columns, rows, column, row, grid, node)
            }
        }
    }
}

private fun generateNonDiagonalNeighbors(
    columns: Int,
    rows: Int,
    column: Int,
    row: Int,
    grid: Map<Int, List<Node>>,
    node: Node
) {
    if (isGivenNodeValid(columns, rows, column - 1, row)) {
        grid[column - 1]?.get(row)?.let { node.neighbors.add(it) }
    }
    if (isGivenNodeValid(columns, rows, column + 1, row)) {
        grid[column + 1]?.get(row)?.let { node.neighbors.add(it) }
    }
    if (isGivenNodeValid(columns, rows, column, row + 1)) {
        grid[column]?.get(row + 1)?.let { node.neighbors.add(it) }
    }
    if (isGivenNodeValid(columns, rows, column, row - 1)) {
        grid[column]?.get(row - 1)?.let { node.neighbors.add(it) }
    }
}

private fun generateDiagonalNeighbors(
    columns: Int,
    rows: Int,
    column: Int,
    row: Int,
    grid: Map<Int, List<Node>>,
    node: Node
) {
    if (isGivenNodeValid(columns, rows, column - 1, row + 1)) {
        grid[column - 1]?.get(row + 1)?.let { node.neighbors.add(it) }
    }
    if (isGivenNodeValid(columns, rows, column - 1, row - 1)) {
        grid[column - 1]?.get(row - 1)?.let { node.neighbors.add(it) }
    }
    if (isGivenNodeValid(columns, rows, column + 1, row + 1)) {
        grid[column + 1]?.get(row + 1)?.let { node.neighbors.add(it) }
    }
    if (isGivenNodeValid(columns, rows, column + 1, row - 1)) {
        grid[column + 1]?.get(row - 1)?.let { node.neighbors.add(it) }
    }
}

private fun isGivenNodeValid(
    columns: Int,
    rows: Int,
    row: Int,
    column: Int
) = (row >= 0) && (row < rows) && (column >= 0) && (column < columns)

