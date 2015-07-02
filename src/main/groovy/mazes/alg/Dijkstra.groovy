package mazes.alg

import mazes.DistanceGrid

/**
 * FIXME: document me
 */
class Dijkstra {

    static void main(args){
        def grid = new DistanceGrid(6,6)
        BinaryTree.algorithm(grid)

        def start = grid.cellAt(0,0)
        def distances = start.distances()

        grid.distances = distances

        println grid
    }
}
