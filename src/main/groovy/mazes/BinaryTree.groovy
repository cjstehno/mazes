package mazes

import javax.imageio.ImageIO

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
        def maze = BinaryTree.on(new Grid(10,10))
        def image = maze.toImage()
        ImageIO.write(image, 'png', new File('/home/cjstehno/Desktop/maze.png'))
    }
}

