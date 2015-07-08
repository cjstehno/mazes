package mazes
import java.awt.*
import java.awt.image.BufferedImage

import static java.awt.image.BufferedImage.TYPE_INT_ARGB

class Grid {

    protected final cells = []
    private Mask mask

    Grid(int rows, int cols) {
        this(new Mask(rows, cols))
    }

    Grid(Mask mask) {
        this.mask = mask

        prepareGrid()
        configureCells()
    }

    protected void prepareGrid() {
        mask.rows.times { r ->
            mask.cols.times { c ->
                if (mask.getAt(r, c)) {
                    cells << new Cell(r, c)
                } else {
                    cells << null
                }
            }
        }
    }

    private void configureCells() {
        eachCell { cell ->
            if( cell ){
                int row = cell.row
                int col = cell.col

                cell.north = cellAt(row - 1, col)
                cell.south = cellAt(row + 1, col)
                cell.west = cellAt(row, col - 1)
                cell.east = cellAt(row, col + 1)
            }
        }
    }

    def deadends() {
        cells.findAll { cell -> cell.links().size() == 1 }
    }

    Cell randomCell() {
        def (row, col) = mask.randomLocation()
        cellAt(row, col)
    }

    Cell cellAt(int row, int col) {
        if (row < 0 || row >= mask.rows || col < 0 || col >= mask.cols) return null

        cells[row * mask.cols + col]
    }

    int size() {
        mask.count()
    }

    void eachRow(Closure closure) {
        mask.rows.times { r ->
            closure(cells[(r * mask.cols)..(r * mask.cols + mask.cols - 1)])
        }
    }

    void eachCell(Closure closure) {
        cells.each(closure)
    }

    String toString() {
        def output = new StringBuilder('+' + '---+' * mask.cols + '\n')

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

    protected String contentsOf(Cell cell) {
        ' '
    }

    BufferedImage toImage(int cellSize = 10) {
        int imgW = cellSize * mask.cols + 1
        int imgH = cellSize * mask.rows + 1

        def wall = Color.BLACK

        BufferedImage bufferedImage = new BufferedImage(imgW, imgH, TYPE_INT_ARGB)
        def gfx = bufferedImage.createGraphics()

        // fill background
        gfx.color = Color.WHITE
        gfx.fillRect(0, 0, imgW, imgH)

        gfx.color = wall

        ['backgrounds', 'walls'].each { mode ->
            eachCell { cell ->
                if( cell ){
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
                } else {
                    // masked
                }
            }
        }

        bufferedImage
    }

    protected Color cellBackgroundColor(Cell cell) {
        Color.WHITE
    }
}