package mazes

import java.awt.*
import java.awt.image.BufferedImage

import static java.awt.image.BufferedImage.TYPE_INT_ARGB

class Grid {

    final int rows, cols

    private final cells = []

    Grid(int rows, int cols) {
        this.rows = rows
        this.cols = cols

        prepareGrid()
        configureCells()
    }

    private void prepareGrid() {
        rows.times { r ->
            cols.times { c ->
                cells << new Cell(r, c)
            }
        }
    }

    private void configureCells() {
        eachCell { cell ->
            int row = cell.row
            int col = cell.col

            cell.north = cellAt(row - 1, col)
            cell.south = cellAt(row + 1, col)
            cell.west = cellAt(row, col - 1)
            cell.east = cellAt(row, col + 1)
        }
    }

    Cell cellAt(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return null

        cells[row * cols + col]
    }

    int size() {
        rows * cols
    }

    void eachRow(Closure closure) {
        rows.times { r ->
            closure(cells[(r * cols)..(r * cols + cols - 1)])
        }
    }

    void eachCell(Closure closure) {
        cells.each(closure)
    }

    // FIXME: the renderers should be external formatters

    String toString() {
        def output = new StringBuilder('+' + '---+' * cols + '\n')

        eachRow { r ->
            def top = new StringBuilder('|')
            def bottom = new StringBuilder('+')

            r.each { c ->
                if (!c) c = new Cell(-1, -1)

                def eastBound = (c.linked(c.east) ? ' ' : '|')
                top.append('   ').append(eastBound)

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

        def background = Color.WHITE
        def wall = Color.BLACK

        BufferedImage bufferedImage = new BufferedImage(imgW, imgH, TYPE_INT_ARGB)
        def gfx = bufferedImage.createGraphics()

        // fill background
        gfx.setColor(background)
        gfx.fillRect(0, 0, imgW, imgH)

        gfx.setColor(wall)

        eachCell { cell ->
            int x1 = cell.col * cellSize
            int y1 = cell.row * cellSize
            int x2 = (cell.col + 1) * cellSize
            int y2 = (cell.row + 1) * cellSize

            if (!cell.north) gfx.drawLine(x1, y1, x2, y1)

            if (!cell.west) gfx.drawLine(x1, y1, x1, y2)

            if (!cell.linked(cell.east)) gfx.drawLine(x2, y1, x2, y2)

            if (!cell.linked(cell.south)) gfx.drawLine(x1, y2, x2, y2)
        }

        bufferedImage
    }
}