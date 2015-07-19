package mazes

import com.stehno.mazes.Cell
import com.stehno.mazes.Grid
import com.stehno.mazes.RandomUtils

import javax.imageio.ImageIO

import static com.stehno.mazes.RandomUtils.*

class Algorithms {

    static binaryTree = { Grid grid ->
        grid.eachCell { cell ->
            def neighbors = []
            if (cell.north) neighbors << cell.north
            if (cell.east) neighbors << cell.east

            def neighbor = rand(neighbors)
            if (neighbor) cell.link(neighbor)
        }

        grid
    }

    static sidewinder = { Grid grid ->
        grid.eachRow { row ->
            def run = []

            row.each { cell ->
                run << cell

                boolean atEastBounds = !cell.east
                boolean atNorthBounds = !cell.north

                boolean shouldCloseOut = atEastBounds || (!atNorthBounds && randBool())

                if (shouldCloseOut) {
                    def member = rand(run)
                    if (member.north) member.link(member.north)

                    run.clear()

                } else {
                    cell.link(cell.east)
                }
            }
        }

        grid
    }

    static aldousBroder = { Grid grid ->
        Cell cell = grid.randomCell()
        int unvisited = grid.size() - 1

        while (unvisited) {
            Cell neighbor = rand(cell.neighbors())
            if (neighbor.links().isEmpty()) {
                cell.link(neighbor)
                unvisited--
            }

            cell = neighbor
        }

        grid
    }

    static wilsons = { Grid grid ->
        def unvisited = []
        grid.eachCell { cell -> unvisited << cell }

        def first = rand(unvisited)
        unvisited.remove(first)

        while (unvisited) {
            Cell cell = rand(unvisited)
            def path = [cell]

            while (unvisited.contains(cell)) {
                cell = rand(cell.neighbors())
                def position = path.indexOf(cell)
                if (position > 0) {
                    path = path[0..position]
                } else {
                    path << cell
                }
            }

            0.upto(path.size() - 2) { index ->
                path[index].link(path[index + 1])
                unvisited.remove(path[index])
            }
        }

        grid
    }

    static huntAndKill = { Grid grid ->
        Cell current = grid.randomCell()

        while (current) {
            def unvisitedNeighbors = current.neighbors().findAll { n -> n.links().isEmpty() }
            if (unvisitedNeighbors) {
                def neighbor = rand(unvisitedNeighbors)
                current.link(neighbor)
                current = neighbor

            } else {
                current = null

                for (cell in grid.cells()) {
                    def visitedNeighbors = cell.neighbors().findAll { n -> n.links() }
                    if (cell.links().isEmpty() && visitedNeighbors) {
                        current = cell
                        current.link(rand(visitedNeighbors))
                        break
                    }
                }
            }
        }

        grid
    }

    static recursiveBacktracker = { Grid grid, startAt = grid.randomCell() ->
        def stack = []
        stack.push startAt

        while (stack) {
            def current = stack.last()
            def neighbors = current.neighbors().findAll { n -> n.links().isEmpty() }
            if (neighbors.isEmpty()) {
                stack.pop()

            } else {
                def neighbor = rand(neighbors)
                current.link(neighbor)
                stack.push neighbor
            }
        }

        grid
    }

    static kruskals = { Grid grid, KruskalsSate state = new KruskalsSate(grid) ->
        def newList = new ArrayList(state.neighbors)
        Collections.shuffle(newList)

        def neighbors = newList

        while (neighbors) {
            def leftRight = neighbors.pop()
            if (state.canMerge(leftRight[0], leftRight[1])) {
                state.merge(leftRight[0], leftRight[1])
            }
        }

        grid
    }

    static simplifiedPrims = { Grid grid, startAt = grid.randomCell() ->
        def active = []
        active.push(startAt)

        while (active) {
            def cell = rand(active)
            def availableNeighbors = cell.neighbors().findAll { n -> !n.links() }

            if (availableNeighbors) {
                def neighbor = rand(availableNeighbors)
                cell.link(neighbor)
                active.push(neighbor)
            } else {
                active.remove(cell)
            }
        }

        grid
    }

    static truePrims = { Grid grid, startAt = grid.randomCell() ->
        def active = []
        active.push(startAt)

        def costs = [:]
        grid.eachCell { cell ->
            costs[cell] = randInt(100)
        }

        while (active) {
            def cell = active.min { a, b -> costs[a] <=> costs[b] }
            def availableNeighbors = cell.neighbors().findAll { n -> !n.links() }

            if (availableNeighbors) {
                def neighbor = availableNeighbors.min { a, b -> costs[a] <=> costs[b] }
                cell.link(neighbor)
                active.push(neighbor)
            } else {
                active.remove(cell)
            }
        }

        grid
    }

    static growingTree = { Grid grid, startAt = grid.randomCell(), Closure closure ->
        def active = []
        active.push(startAt)

        while (active) {
            def cell = closure(active)
            def availableNeighbors = cell.neighbors().findAll { n -> !n.links() }

            if (availableNeighbors) {
                def neighbor = rand(availableNeighbors)
                cell.link(neighbor)
                active.push(neighbor)
            } else {
                active.remove(cell)
            }
        }

        grid
    }

    static recursiveDivision = { Grid grid ->
        grid.eachCell { cell ->
            cell.neighbors().each { n -> cell.link(n, false) }
        }

        divide(grid, 0, 0, grid.rows, grid.cols)
    }

    static divide(Grid grid, int row, int col, int height, int width) {
                                      // this is the part that adds the rooms
        if (height <= 1 || width <= 1 || height < 5 && width < 5 && randInt(4) == 0) return

        if (height > width) {
            divideHorizontally(grid, row, col, height, width)
        } else {
            divideVertically(grid, row, col, height, width)
        }
    }

    static divideHorizontally(Grid grid, int row, int col, int height, int width) {
        def divideSouthOf = randInt(height - 1)
        def passageAt = randInt(width)

        width.times { x ->
            if (passageAt == x) return

            def cell = grid.cellAt(row + divideSouthOf, col + x)
            cell.unlink(cell.south)
        }

        divide(grid, row, col, divideSouthOf + 1, width)
        divide(grid, row + divideSouthOf + 1, col, height - divideSouthOf - 1, width)
    }

    static divideVertically(Grid grid, int row, int col, int height, int width) {
        def divideEastOf = randInt(width - 1)
        def passageAt = randInt(height)

        height.times { y ->
            if (passageAt == y) return

            def cell = grid.cellAt(row + y, col + divideEastOf)
            cell.unlink(cell.east)
        }

        divide(grid, row, col, height, divideEastOf + 1)
        divide(grid, row, col + divideEastOf + 1, height, width - divideEastOf - 1)
    }

    static void main(args) {
        def grid = new Grid(25, 25)

        Algorithms.recursiveDivision(grid)

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}

class KruskalsSate {

    final neighbors = []

    private final Grid grid
    private final setForCell = [:]
    private final cellsInSet = [:]

    KruskalsSate(Grid grid) {
        this.grid = grid

        grid.eachCell { Cell cell ->
            def setSize = setForCell.size()

            setForCell[cell] = setSize
            cellsInSet[setSize] = [cell]

            if (cell.south) neighbors << [cell, cell.south]
            if (cell.east) neighbors << [cell, cell.east]
        }
    }

    boolean canMerge(left, rigth) {
        setForCell[left] != setForCell[rigth]
    }

    void merge(Cell left, Cell right) {
        left.link(right)

        def winner = setForCell[left]
        def loser = setForCell[right]
        def losers = new ArrayList<>(cellsInSet[loser] ?: [right])

        losers.each { cell ->
            if (cellsInSet[winner]) {
                cellsInSet[winner] << cell
            } else {
                cellsInSet[winner] = [cell]
            }
            setForCell[cell] = winner
        }

        cellsInSet.remove(loser)
    }

    boolean addCrossing(Cell cell) {
        if (cell.links() || !canMerge(cell.east, cell.west) || !canMerge(cell.north, cell.south)) {
            return false
        }

        neighbors.removeAll { n -> n[0] == cell || n[1] == cell }

        if (RandomUtils.randBool()) {
            merge(cell.west, cell)
            merge(cell, cell.east)

            grid.tunnelUnder(cell)
            merge(cell.north, cell.north.south)
            merge(cell.south, cell.south.north)

        } else {
            merge(cell.north, cell)
            merge(cell, cell.south)

            grid.tunnelUnder(cell)
            merge(cell.west, cell.west.east)
            merge(cell.east, cell.east.west)
        }

        true
    }
}
