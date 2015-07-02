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
    }
}
