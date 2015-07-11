package mazes

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.image.BufferedImage

class Mask {

    private int rows, cols
    private bits

    Mask(final int rows, final int cols) {
        this.rows = rows
        this.cols = cols
        this.bits = makeBits(rows, cols)
    }

    Mask(final File file) {
        if (file.name.toLowerCase().endsWith('.txt')) {
            loadTextMask(file)

        } else {
            loadImageMask(file)
        }
    }

    int getRows() {
        return rows
    }

    int getCols() {
        return cols
    }

    private void loadTextMask(File file) {
        def lines = file.readLines()

        this.rows = lines.size()
        this.cols = lines[0].trim().length()
        this.bits = makeBits(rows, cols)

        lines.eachWithIndex { String line, int r ->
            line.trim().eachWithIndex { value, int c ->
                setAt(r, c, value != 'X')
            }
        }
    }

    private void loadImageMask(File file){
        BufferedImage image = ImageIO.read(file)

        this.rows = image.height
        this.cols = image.width
        this.bits = makeBits(rows, cols)

        rows.times { r ->
            cols.times { c ->
                setAt(r, c, image.getRGB(c, r) != Color.BLACK.getRGB())
            }
        }
    }

    private static makeBits(int rows, int cols) {
        def list = []

        rows.times { r ->
            def row = []
            cols.times { c ->
                row << true
            }
            list << row
        }

        list
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
