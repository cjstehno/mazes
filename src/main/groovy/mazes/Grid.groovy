package mazes

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
}