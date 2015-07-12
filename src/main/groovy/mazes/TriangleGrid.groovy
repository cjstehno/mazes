package mazes

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

import static java.awt.image.BufferedImage.TYPE_INT_ARGB

class TriangleGrid extends Grid {

    TriangleGrid(int rows, int cols) {
        super(rows, cols)
    }

    @Override
    protected prepareGrid() {
        def list = []
        rows.times { r ->
            def columns = []
            cols.times { c ->
                columns << new TriangleCell(r, c)
            }
            list << columns
        }
        list
    }

    @Override
    protected void configureCells() {
        eachCell { cell ->
            int row = cell.row
            int col = cell.col

            cell.west = cellAt(row, col - 1)
            cell.east = cellAt(row, col + 1)

            if (cell.upright()) {
                cell.south = cellAt(row + 1, col)
            } else {
                cell.north = cellAt(row - 1, col)
            }
        }
    }

    @Override
    String toString() {
        "TriangleGrid(rows=$rows, cols=$cols)"
    }

    @Override
    BufferedImage toImage(int size = 16) {
        def halfWidth = size / 2.0
        def height = size * Math.sqrt(3) / 2.0
        def halfHeight = height / 2.0

        int imgWidth = (size * (cols + 1) / 2.0) as int
        int imgHeight = (height * rows) as int

        BufferedImage bufferedImage = new BufferedImage(imgWidth + 1, imgHeight + 1, TYPE_INT_ARGB)
        def gfx = bufferedImage.createGraphics()

        // fill background
        gfx.color = Color.WHITE
        gfx.fillRect(0, 0, imgWidth, imgHeight)

        gfx.color = Color.BLACK

        ['backgrounds', 'walls'].each { mode ->
            eachCell { cell ->
                def cx = halfWidth + cell.col * halfWidth
                def cy = halfHeight + cell.row * height

                int westX = (cx - halfWidth) as int
                int midX = cx as int
                int eastX = (cx + halfWidth) as int

                int apexY
                int baseY

                if (cell.upright()) {
                    apexY = (cy - halfHeight) as int
                    baseY = (cy + halfHeight) as int
                } else {
                    apexY = (cy + halfHeight) as int
                    baseY = (cy - halfHeight) as int
                }

                if (mode == 'backgrounds') {
                    Color color = cellBackgroundColor(cell)
                    if (color) {
                        gfx.color = color
                        gfx.fillPolygon(
                            [westX, midX, eastX] as int[],
                            [baseY, apexY, baseY] as int[],
                            3
                        )
                    }
                } else {
                    gfx.color = Color.BLACK
                    if (!cell.west) {
                        gfx.drawLine(westX, baseY, midX, apexY)
                    }

                    if (!cell.linked(cell.east)) {
                        gfx.drawLine(eastX, baseY, midX, apexY)
                    }

                    boolean noSouth = cell.upright() && !cell.south
                    boolean notLinked = !cell.upright() && !cell.linked(cell.north)

                    if (noSouth || notLinked) {
                        gfx.drawLine(eastX, baseY, westX, baseY)
                    }
                }
            }
        }

        bufferedImage
    }

    static void main(args) {
        def grid = new TriangleGrid(10, 17)

        Algorithms.recursiveBacktracker(grid)

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
