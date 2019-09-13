import kotlinx.coroutines.*
import strategies.HeuristicStrategy
import strategies.NeighborStrategy
import strategies.calculateEuclideanHeuristic
import strategies.findDiagonalNeighbors
import util.Failure
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Pathfinder : CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + Dispatchers.Main

    private val grid = mutableMapOf<Int, List<Node>>()

    private var columns: Int = 0
    private var rows: Int = 0

    private lateinit var heuristicStrategy: HeuristicStrategy
    private lateinit var neighborStrategy: NeighborStrategy

    private var start: Node? = null
    private var goal: Node? = null

    operator fun invoke(
        heuristicStrategy: (neighbor: Node, final: Node) -> Double = calculateEuclideanHeuristic,
        neighborStrategy: (grid: Map<Int, List<Node>>, columns: Int, rows: Int) -> Unit = findDiagonalNeighbors,
        columns: Int,
        rows: Int
    ): Pair<Map<Int, List<Node>>, Node?> {
        setup(heuristicStrategy, neighborStrategy, columns, rows)

        var finalNode: Node? = null

        runBlocking {
            this@Pathfinder.neighborStrategy.generateNeighbors(grid, columns, rows)
            setupBiDimensionalMatrix()
            neighborStrategy.invoke(grid, columns, rows)
            finalNode = calculateBestRoute()
        }

        return Pair(grid, finalNode)
    }

    private fun setup(
        heuristicStrategy: (neighbor: Node, final: Node) -> Double,
        neighborStrategy: (grid: Map<Int, List<Node>>, columns: Int, rows: Int) -> Unit,
        columns: Int,
        rows: Int
    ) {
        this.heuristicStrategy = HeuristicStrategy(heuristicStrategy)
        this.neighborStrategy = NeighborStrategy(neighborStrategy)
        this.columns = columns
        this.rows = rows
    }

    private suspend fun setupBiDimensionalMatrix() = withContext(Dispatchers.IO) {
        for (c in 0 until columns) {
            mutableListOf<Node>().also { nodes ->
                for (r in 0 until rows) {
                    nodes.add(Node(c, r))
                }
                grid[c] = nodes
            }
        }
        start = grid[0]?.get(0)
        goal = grid[columns - 1]?.get(rows - 1)
    }

    @Throws(Failure.BestPathNotFound::class, Failure.PreRequisitesMissing::class)
    private suspend fun calculateBestRoute(): Node = withContext(Dispatchers.IO) {
        suspendCoroutine<Node> { continuation ->

            val unsearched: Queue<Node> = PriorityQueue()
            val searched = mutableSetOf<Node>()

            start?.let { startNode ->
                goal?.let { goalNode ->

                    startNode.gScore = 0.0
                    startNode.hScore = heuristicStrategy.calculateDistance(startNode, goalNode)
                    unsearched.add(start)

                    while (unsearched.isNotEmpty()) {

                        val currentNode = unsearched.poll()

                        if (currentNode == goal) {
                            continuation.resume(currentNode)
                            return@suspendCoroutine
                        }

                        val currentNodeDistance = currentNode.gScore
                        currentNode.neighbors.forEach { neighbor ->
                            if (neighbor == goal) {
                                continuation.resume(currentNode)
                                return@suspendCoroutine
                            }

                            val gScore =
                                currentNodeDistance + heuristicStrategy.calculateDistance(currentNode, neighbor)
                            if (!searched.contains(neighbor) && gScore < neighbor.gScore) {
                                neighbor.hScore = heuristicStrategy.calculateDistance(goalNode, neighbor)
                                neighbor.gScore = gScore
                                neighbor.previous = currentNode
                                unsearched.add(neighbor)
                            }
                        }
                        searched.add(currentNode)
                    }

                    continuation.resumeWithException(Failure.BestPathNotFound)

                } ?: continuation.resumeWithException(Failure.PreRequisitesMissing)
            } ?: continuation.resumeWithException(Failure.PreRequisitesMissing)
        }
    }
}