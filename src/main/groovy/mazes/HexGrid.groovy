package mazes

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

import static java.awt.image.BufferedImage.TYPE_INT_ARGB
import static mazes.Utils.even
import static mazes.Utils.odd

class HexGrid extends Grid {

    HexGrid(int rows, int cols) {
        super(rows, cols)
    }

    @Override
    protected prepareGrid() {
        def list = []
        rows.times { r ->
            def columns = []
            cols.times { c ->
                columns << new HexCell(r, c)
            }
            list << columns
        }
        list
    }

    @Override
    protected void configureCells() {
        eachCell { HexCell cell ->
            int row = cell.row
            int col = cell.col

            int northDiagonal
            int southDiagonal

            if (even(col)) {
                northDiagonal = row - 1
                southDiagonal = row
            } else {
                northDiagonal = row
                southDiagonal = row + 1
            }

            cell.northwest = cellAt(northDiagonal, col - 1)
            cell.north = cellAt(row - 1, col)
            cell.northeast = cellAt(northDiagonal, col + 1)
            cell.southwest = cellAt(southDiagonal, col - 1)
            cell.south = cellAt(row + 1, col)
            cell.southeast = cellAt(southDiagonal, col + 1)
        }
    }

    @Override
    String toString() {
        "HexGrid(rows=$rows, cols=$cols)"
    }

    @Override
    BufferedImage toImage(Map options = [:]) {
        def (cellSize, inset) = imageOptions(options)

        def aSize = cellSize / 2.0
        def bSize = cellSize * Math.sqrt(3) / 2.0
        def width = cellSize * 2
        def height = bSize * 2

        int imgWidth = (3 * aSize * cols + aSize + 0.5) as int
        int imgHeight = (height * rows + bSize + 0.5) as int

        BufferedImage bufferedImage = new BufferedImage(imgWidth+1, imgHeight+1, TYPE_INT_ARGB)
        def gfx = bufferedImage.createGraphics()

        // fill background
        gfx.color = Color.WHITE
        gfx.fillRect(0, 0, imgWidth, imgHeight)

        gfx.color = Color.BLACK

        ['backgrounds', 'walls'].each { mode ->
            eachCell { cell ->
                def cx = cellSize + 3 * cell.col * aSize
                def cy = bSize + cell.row * height
                if (odd(cell.col)) cy += bSize

                int xFw = (cx - cellSize) as int
                int xNw = (cx - aSize) as int
                int xNe = (cx + aSize) as int
                int xFe = (cx + cellSize) as int

                int yN = (cy - bSize) as int
                int yM = cy as int
                int yS = (cy + bSize) as int

                if (mode == 'backgrounds') {
                    Color color = cellBackgroundColor(cell)
                    if (color) {
                        gfx.color = color
                        gfx.fillPolygon(
                            [xFw, xNw, xNe, xFe, xNe, xNw] as int[],
                            [yM, yN, yN, yM, yS, yS] as int[],
                            6
                        )
                    }

                } else {
                    gfx.color = Color.BLACK
                    if (!cell.southwest) gfx.drawLine(xFw, yM, xNw, yS)
                    if (!cell.northwest) gfx.drawLine(xFw, yM, xNw, yN)
                    if (!cell.north) gfx.drawLine(xNw, yN, xNe, yN)
                    if (!cell.linked(cell.northeast)) gfx.drawLine(xNe, yN, xFe, yM)
                    if (!cell.linked(cell.southeast)) gfx.drawLine(xFe, yM, xNe, yS)
                    if (!cell.linked(cell.south)) gfx.drawLine(xNe, yS, xNw, yS)
                }
            }
        }

        bufferedImage
    }

    static void main(args){
        def grid = new HexGrid(10,10)

        Algorithms.recursiveBacktracker(grid)

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
