package mazes.alg

import mazes.CliHelper
import mazes.Grid

import static mazes.Utils.pick

class BinaryTree {

    static algorithm = {Grid grid->
        grid.eachCell { cell ->
            def neighbors = []
            if (cell.north) neighbors << cell.north
            if (cell.east) neighbors << cell.east

            def neighbor = pick(neighbors)
            if (neighbor) cell.link(neighbor)
        }

        grid
    }

    static void main(args){
        CliHelper.generate(args, algorithm)
    }
}

