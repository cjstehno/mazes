package mazes.alg

import mazes.Cell
import mazes.CliHelper
import mazes.Grid

import static mazes.Utils.pick

class Wilsons {

    static algorithm = { Grid grid ->
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

    static void main(args) {
        CliHelper.generate(args, algorithm)
    }
}
