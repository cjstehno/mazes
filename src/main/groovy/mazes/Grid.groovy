package mazes

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
import java.util.List

import static java.awt.image.BufferedImage.TYPE_INT_ARGB
import static mazes.Utils.*

class Grid {

    final int rows, cols

    private grid

    Grid(final int rows, final int cols, boolean initialize = true) {
        this.rows = rows
        this.cols = cols
        if (initialize) init()
    }

    protected void init() {
        this.grid = prepareGrid()
        configureCells()
    }

    protected prepareGrid() {
        def list = []
        rows.times { r ->
            def columns = []
            cols.times { c ->
                columns << new Cell(r, c)
            }
            list << columns
        }
        list
    }

    protected void configureCells() {
        eachCell { cell ->
            if (cell) {
                int row = cell.row
                int col = cell.col

                cell.north = cellAt(row - 1, col)
                cell.south = cellAt(row + 1, col)
                cell.west = cellAt(row, col - 1)
                cell.east = cellAt(row, col + 1)
            }
        }
    }

    Cell cellAt(int row, int col) {
        if ((0..<rows).containsWithinBounds(row) && (0..<cols).containsWithinBounds(col)) {
            _grid(row, col)
        } else {
            null
        }
    }

    protected Cell _grid(int row, int col) {
        grid[row][col]
    }

    Cell randomCell() {
        cellAt(randInt(rows), randInt(cols))
    }

    int size() {
        rows * cols
    }

    protected rowAt(int row) {
        grid[row]
    }

    void eachRow(Closure closure) {
        grid.each(closure)
    }

    void eachCell(Closure closure) {
        eachRow { row ->
            row.each(closure)
        }
    }

    List<Cell> cells() {
        grid.flatten()
    }

    def deadends() {
        cells().findAll { cell -> cell.links().size() == 1 }
    }

    def braid(float p = 1.0) {
        def ends = deadends()
        Collections.shuffle(ends)
        ends.each { cell ->
            if (cell.links().size() == 1 && randFloat() > p) {

                def neighbors = cell.neighbors().findAll { n -> !cell.linked(n) }

                def best = neighbors.findAll { Cell n -> n.links().size() == 1 }
                best = best ?: neighbors

                cell.link(pick(best))
            }
        }
    }

    String toString() {
        def output = new StringBuilder('+' + '---+' * cols + '\n')

        eachRow { r ->
            def top = new StringBuilder('|')
            def bottom = new StringBuilder('+')

            r.each { c ->
                if (!c) c = new Cell(-1, -1)

                def eastBound = (c.linked(c.east) ? ' ' : '|')
                top.append(" ${contentsOf(c)} ").append(eastBound)

                def southBound = (c.linked(c.south) ? '   ' : '---')
                bottom.append(southBound).append('+')
            }

            output.append(top).append('\n')
            output.append(bottom).append('\n')
        }

        output
    }

    // cellSize, inset
    BufferedImage toImage(Map options = [:]) {
        def (cellSize, inset) = imageOptions(options)

        int imgW = cellSize * cols + 1
        int imgH = cellSize * rows + 1
        inset = (cellSize * inset) as int

        def wall = Color.BLACK

        BufferedImage bufferedImage = new BufferedImage(imgW, imgH, TYPE_INT_ARGB)
        def gfx = bufferedImage.createGraphics()

        // fill background
        gfx.color = Color.WHITE
        gfx.fillRect(0, 0, imgW, imgH)

        gfx.color = wall

        ['backgrounds', 'walls'].each { mode ->
            eachCell { cell ->
                if (cell) {
                    int x = cell.col * cellSize
                    int y = cell.row * cellSize

                    if (inset > 0) {
                        toImageWithInset(gfx, cell, mode, cellSize, x, y, inset)
                    } else {
                        toImageWithoutInset(gfx, cell, mode, cellSize, x, y)
                    }


                }
            }
        }

        bufferedImage
    }

    protected imageOptions(Map opts = [:]) {
        def map = [cellSize: 10, inset: 0] + opts
        [map.cellSize, map.inset]
    }

    private void toImageWithoutInset(Graphics2D gfx, Cell cell, String mode, int cellSize, int x, int y) {
        int x1 = x
        int y1 = y
        int x2 = x1 + cellSize
        int y2 = y1 + cellSize

        if (mode == 'backgrounds') {
            gfx.color = cellBackgroundColor(cell)
            gfx.fillRect(x, y, x2, y2)

        } else {
            gfx.color = Color.BLACK

            if (!cell.north) gfx.drawLine(x1, y1, x2, y1)
            if (!cell.west) gfx.drawLine(x1, y1, x1, y2)
            if (!cell.linked(cell.east)) gfx.drawLine(x2, y1, x2, y2)
            if (!cell.linked(cell.south)) gfx.drawLine(x1, y2, x2, y2)
        }
    }

    private void toImageWithInset(Graphics2D gfx, Cell cell, String mode, int cellSize, int x, int y, int inset) {
        def (x1, x2, x3, x4, y1, y2, y3, y4) = cellCoordinatesWithInset(x, y, cellSize, inset)

        if (mode == 'backgrounds') {
            // ...
        } else {
            gfx.color = Color.BLACK

            if (cell.linked(cell.north)) {
                gfx.drawLine(x2, y1, x2, y2)
                gfx.drawLine(x3, y1, x3, y2)

            } else {
                gfx.drawLine(x2, y2, x3, y2)
            }

            if (cell.linked(cell.south)) {
                gfx.drawLine(x2, y3, x2, y4)
                gfx.drawLine(x3, y3, x3, y4)

            } else {
                gfx.drawLine(x2, y3, x3, y3)
            }

            if (cell.linked(cell.west)) {
                gfx.drawLine(x1, y2, x2, y2)
                gfx.drawLine(x1, y3, x2, y3)

            } else {
                gfx.drawLine(x2, y2, x2, y3)
            }

            if (cell.linked(cell.east)) {
                gfx.drawLine(x3, y2, x4, y2)
                gfx.drawLine(x3, y3, x4, y3)

            } else {
                gfx.drawLine(x3, y2, x3, y3)
            }
        }
    }

    private cellCoordinatesWithInset(int x, int y, int cellSize, int inset) {
        int x1 = x
        int x2 = x1 + inset
        int x4 = x + cellSize
        int x3 = x4 - inset

        int y1 = y
        int y2 = y1 + inset
        int y4 = y + cellSize
        int y3 = y4 - inset

        [x1, x2, x3, x4, y1, y2, y3, y4]
    }

    protected Color cellBackgroundColor(Cell cell) {
        Color.WHITE
    }

    protected String contentsOf(Cell cell) {
        ' '
    }

    static void main(args) {
        def grid = new Grid(25, 25)

        Algorithms.recursiveBacktracker(grid)

        println "Deadends (before): ${grid.deadends().size()}"

        grid.braid(0.5f)

        println "Deadends (after): ${grid.deadends().size()}"

        ImageIO.write(grid.toImage(inset:0.1), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
