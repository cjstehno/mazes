package mazes.alg
import mazes.DistanceGrid

class LongestPath {

    static void main(args){
        def grid = new DistanceGrid(6,6)
//        BinaryTree.algorithm(grid)
        Sidewinder.algorithm(grid)
        grid.calculateDistances()

        def (goal, dist) = grid.distances.max()

        println "The longest path is ($dist steps):"

        grid.distances = grid.distances.pathTo(goal)

        println grid
    }
}
