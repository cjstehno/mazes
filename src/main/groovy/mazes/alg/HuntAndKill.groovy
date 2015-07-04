package mazes.alg

import mazes.Cell
import mazes.CliHelper
import mazes.Grid

import static mazes.Utils.pick

class HuntAndKill {

    static algorithm = { Grid grid ->
        Cell current = grid.randomCell()

        while (current) {
            def unvisitedNeighbors = current.neighbors().findAll { n -> n.links().isEmpty() }
            if (unvisitedNeighbors) {
                def neighbor = pick(unvisitedNeighbors)
                current.link(neighbor)
                current = neighbor

            } else {
                current = null

                for( cell in grid.cells) {
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

    static void main(args) {
        CliHelper.generate(args, algorithm)
    }
}
