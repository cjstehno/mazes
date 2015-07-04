package mazes.alg

import mazes.ColoredGrid

import javax.imageio.ImageIO

/**
 * Created by cjstehno on 7/4/15.
 */
class Coloring {

    static void main(args){
        def grid = new ColoredGrid(25,25)
//        BinaryTree.algorithm(grid)
        Sidewinder.algorithm(grid)
        grid.calculateDistances()
        grid.calculateMax()

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
