package mazes

import javax.imageio.ImageIO

class AsciiMask {

    static void main(args){
//        def file = new File(AsciiMask.getResource('/mask.txt').toURI())
        def file = new File(AsciiMask.getResource('/mask.png').toURI())
        def mask = new Mask(file)

        def grid = new MaskedGrid(mask)
        Algorithms.recursiveBacktracker(grid)

//        println grid

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
