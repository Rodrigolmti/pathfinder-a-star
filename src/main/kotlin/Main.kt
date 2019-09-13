import util.Failure

const val columns = 100
const val rows = 100

fun main() {
    try {

        val (grid, finalNode) = Pathfinder()(
            columns = columns,
            rows = rows
        )

        finalNode?.let {
            val bestRoute = it.traceForwardBestPath().also { route ->
                grid[columns - 1]?.get(rows - 1)?.let { node -> route.add(node) }
            }
            printResult(grid, bestRoute)
        }

    } catch (error: Failure) {
        print("Something went wrong! :(")
    }
}

private fun printResult(grid: Map<Int, List<Node>>, path: List<Node>) {
    for (c in 0 until columns) {
        for (r in 0 until rows) {
            val node = grid[c]?.get(r)
            when {
                node == path.first() -> print("[\uD83D\uDCCD] ")
                node == path.last() -> print("[✅] ")
                path.contains(node) -> print("[\uD83D\uDE90] ")
                else -> print("[⬛] ")
            }
        }
        println("")
    }
}
