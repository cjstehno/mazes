package mazes.alg

import mazes.DistanceGrid

/**
 * FIXME: document me
 */
class Dijkstra {

    static void main(args){
        def grid = new DistanceGrid(6,6)
        BinaryTree.algorithm(grid)
        grid.calculateDistances()
        println grid

        println 'Path from NW corner to SW corner'

        grid.distances = grid.distances.pathTo(grid.cellAt(grid.rows -1, 0))

        println grid
    }
}
