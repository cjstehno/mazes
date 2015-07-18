package mazes

import javax.imageio.ImageIO

import static mazes.Utils.pick
import static mazes.Utils.randBool

class Algorithms {

    static binaryTree = { Grid grid ->
        grid.eachCell { cell ->
            def neighbors = []
            if (cell.north) neighbors << cell.north
            if (cell.east) neighbors << cell.east

            def neighbor = pick(neighbors)
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
                    def member = pick(run)
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
            Cell neighbor = pick(cell.neighbors())
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

        def first = pick(unvisited)
        unvisited.remove(first)

        while (unvisited) {
            Cell cell = pick(unvisited)
            def path = [cell]

            while (unvisited.contains(cell)) {
                cell = pick(cell.neighbors())
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
                def neighbor = pick(unvisitedNeighbors)
                current.link(neighbor)
                current = neighbor

            } else {
                current = null

                for (cell in grid.cells()) {
                    def visitedNeighbors = cell.neighbors().findAll { n -> n.links() }
                    if (cell.links().isEmpty() && visitedNeighbors) {
                        current = cell
                        current.link(pick(visitedNeighbors))
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
                def neighbor = pick(neighbors)
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

    static void main(args) {
        def grid = new Grid(25, 25)
        //        Algorithms.recursiveBacktracker(grid)

        Algorithms.kruskals(grid)

        //        def start = grid.cellAt(grid.rows / 2 as int, grid.cols / 2 as int)
        //        grid.distances = start.distances()

        println grid
        //        println "Deadends: ${grid.deadends().size()}"

        ImageIO.write(grid.toImage(inset: 0.1), 'png', new File("${System.getProperty('user.home')}/maze.png"))
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

        if (Utils.randBool()) {
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