package mazes

class Mask {

    final int rows, cols
    private bits

    Mask(final int rows, final int cols) {
        this.rows = rows
        this.cols = cols

        populateBits(rows, cols)
    }

    private void populateBits() {
        def list = []
        rows.times { r ->
            def row = []
            cols.times { c ->
                row << true
            }
            list << row
        }

        this.bits = list
    }

    Mask(File file){
        def lines = file.readLines()

        this.rows = lines.size()
        this.cols = lines[0].trim().length()
        populateBits()

        lines.eachWithIndex { String line, int r->
            line.trim().eachWithIndex{ value, int c->
                setAt(r, c, value != 'X')
            }
        }
    }

    boolean getAt(int row, int col) {
        if ((0..<rows).containsWithinBounds(row) && (0..<cols).containsWithinBounds(col)) {
            return bits[row][col]
        } else {
            return false
        }
    }

    void setAt(int row, int col, boolean isOn) {
        bits[row][col] = isOn
    }

    int count() {
        int count = 0

        rows.times { row ->
            cols.times { col ->
                if (bits[row][col]) count++
            }
        }

        count
    }

    def randomLocation() {
        while (true) {
            int row = Utils.randInt(rows)
            int col = Utils.randInt(cols)

            if (bits[row][col]) {
                return [row, col]
            }
        }
    }
}
