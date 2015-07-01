package mazes

import static mazes.Utils.pick

class BinaryTree {

    static Grid on(Grid grid) {
        grid.eachCell { cell ->
            def neighbors = []
            if (cell.north) neighbors << cell.north
            if (cell.east) neighbors << cell.east

            def neighbor = pick(neighbors)
            if (neighbor) cell.link(neighbor)
        }

        grid
    }

    static void main(args) {
        println BinaryTree.on(new Grid(6, 7))
    }
}

