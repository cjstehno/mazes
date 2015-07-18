package mazes

import javax.imageio.ImageIO

class KruskalsWeaveDemo {

    static void main(args) {
        def grid = new PreconfiguredGrid(20,20)
        def state = new KruskalsSate(grid)

        grid.size().times { i->
            int row = 1 + Utils.randInt(grid.rows-2)
            int col = 1 + Utils.randInt(grid.cols -2)
            state.addCrossing(grid.cellAt(row, col))
        }

        Algorithms.kruskals(grid, state)

        ImageIO.write(grid.toImage(inset: 0.2), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}

class SimpleOverCell extends OverCell {

    SimpleOverCell(int row, int col, Grid grid) {
        super(row, col, grid)
    }

    @Override
    def neighbors() {
        def list = []
        if (north) list << north
        if (south) list << south
        if (east) list << east
        if (west) list << west
        list
    }
}

class PreconfiguredGrid extends WeaveGrid {

    PreconfiguredGrid(int rows, int cols) {
        super(rows, cols)
    }

    @Override
    protected prepareGrid() {
        def list = []
        rows.times { r ->
            def columns = []
            cols.times { c ->
                columns << new SimpleOverCell(r, c, this)
            }
            list << columns
        }
        list
    }
}
