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

    BufferedImage toImage(int cellSize = 10) {
        int imgW = cellSize * cols + 1
        int imgH = cellSize * rows + 1

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
                    int x1 = cell.col * cellSize
                    int y1 = cell.row * cellSize
                    int x2 = (cell.col + 1) * cellSize
                    int y2 = (cell.row + 1) * cellSize

                    if (mode == 'backgrounds') {
                        gfx.color = cellBackgroundColor(cell)
                        gfx.fillRect(x1, y1, x2, y2)

                    } else {
                        gfx.color = wall

                        if (!cell.north) gfx.drawLine(x1, y1, x2, y1)
                        if (!cell.west) gfx.drawLine(x1, y1, x1, y2)
                        if (!cell.linked(cell.east)) gfx.drawLine(x2, y1, x2, y2)
                        if (!cell.linked(cell.south)) gfx.drawLine(x1, y2, x2, y2)
                    }
                }
            }
        }

        bufferedImage
    }

    protected Color cellBackgroundColor(Cell cell) {
        Color.WHITE
    }

    protected String contentsOf(Cell cell) {
        ' '
    }

    static void main(args){
        def grid = new Grid(25,25)

        Algorithms.recursiveBacktracker(grid)

        println "Deadends (before): ${grid.deadends().size()}"

        grid.braid(0.5f)

        println "Deadends (after): ${grid.deadends().size()}"

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
