package mazes.alg

import mazes.Cell
import mazes.CliHelper
import mazes.Grid

import static mazes.Utils.pick

class AldousBroder {

    static algorithm = { Grid grid ->
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

    static void main(args) {
        CliHelper.generate(args, algorithm)
    }
}
