package mazes.alg

import mazes.Grid
import mazes.Mask

import javax.imageio.ImageIO

class SimpleMask {

    static final main(args) {
//        def mask = new Mask(new File('/media/cjstehno/Storage/projects/mazes/src/main/resources/mask.txt'))
        def mask = new Mask(new File('/media/cjstehno/Storage/projects/mazes/src/main/resources/mask.png'))

        def grid = new Grid(mask)
        RecursiveBacktracker.algorithm(grid)

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
