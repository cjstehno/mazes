package mazes.alg

import mazes.ColoredGrid

import javax.imageio.ImageIO

class Coloring {

    static void main(args) {
        def grid = new ColoredGrid(25, 25)
        //        BinaryTree.algorithm(grid)
        Wilsons.algorithm(grid)
        grid.calculateDistances(grid.cellAt((grid.rows / 2) as int, (grid.cols / 2) as int))
        grid.calculateMax()

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
