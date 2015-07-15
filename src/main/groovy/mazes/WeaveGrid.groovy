package mazes

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

class WeaveGrid extends Grid {

    private final underCells = []

    WeaveGrid(int rows, int cols) {
        super(rows, cols)
    }

    @Override
    protected prepareGrid() {
        def list = []
        rows.times { r ->
            def columns = []
            cols.times { c ->
                columns << new OverCell(r, c, this)
            }
            list << columns
        }
        list
    }

    void tunnelUnder(OverCell overCell) {
        def underCell = new UnderCell(overCell)
        underCells.push(underCell)
    }

    @Override
    void eachCell(Closure closure) {
        super.eachCell(closure)

        underCells.each(closure)
    }

    @Override
    BufferedImage toImage(Map options = [:]) {
        def (cellSize, inset) = imageOptions(options)

        super.toImage(cellSize: cellSize, inset: inset ?: 0.1)
    }

    @Override
    protected void toImageWithInset(Graphics2D gfx, Cell cell, String mode, int cellSize, int x, int y, int inset) {
        if (cell instanceof OverCell) {
            super.toImageWithInset(gfx, cell, mode, cellSize, x, y, inset)
        } else {
            def (x1, x2, x3, x4, y1, y2, y3, y4) = cellCoordinatesWithInset(x, y, cellSize, inset)

            gfx.color = Color.BLACK

            if( cell.hasVerticalPassage()){
                gfx.drawLine(x2, y1, x2, y2)
                gfx.drawLine(x3, y1, x3, y2)
                gfx.drawLine(x2, y3, x2, y4)
                gfx.drawLine(x3, y3, x3, y4)
            } else {
                gfx.drawLine(x1, y2, x2, y2)
                gfx.drawLine(x1, y3, x2, y3)
                gfx.drawLine(x3, y2, x4, y2)
                gfx.drawLine(x3, y3, x4, y3)
            }
        }
    }

    static void main(args) {
        def grid = new WeaveGrid(25, 25)
        Algorithms.recursiveBacktracker(grid)

        grid.braid(0.5f)

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
