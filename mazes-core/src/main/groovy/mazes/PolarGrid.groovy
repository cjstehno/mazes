package mazes

import javax.imageio.ImageIO
import java.awt.*
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

import static java.awt.image.BufferedImage.TYPE_INT_ARGB
import static mazes.Utils.randInt

class PolarGrid extends Grid {

    PolarGrid(int rows, int cols = 1) {
        super(rows, cols)
    }

    @Override
    protected prepareGrid() {
        def rowsList = []

        def rowHeight = 1.0 / rows
        rowsList[0] = [new PolarCell(0, 0)]

        (1..rows).each { int row ->
            def radius = (row as float) / rows
            def circumference = 2 * Math.PI * radius

            int previousCount = rowsList[row - 1].size()
            def estimatedCellWidth = circumference / previousCount
            def ratio = Math.round(estimatedCellWidth / rowHeight)

            int cells = previousCount * ratio
            rowsList[row] = (0..<cells).collect { col -> new PolarCell(row, col) }
        }

        rowsList
    }

    @Override
    protected void configureCells() {
        eachCell { cell ->
            int row = cell.row
            int col = cell.col

            if (row > 0) {
                cell.cw = cellAt(row, col + 1)
                cell.ccw = cellAt(row, col - 1)

                def ratio = rowAt(row).size() / rowAt(row - 1).size()
                PolarCell parent = cellAt(row - 1, (col / ratio) as int)
                if (parent) parent.outward << cell
                cell.inward = parent
            }
        }
    }

    @Override
    Cell randomCell() {
        int row = randInt(rows)
        cellAt(row, randInt(rowAt(row).size()))
    }

    @Override
    Cell cellAt(int row, int col) {
        if( (0..<rows).containsWithinBounds(row) ){
            _grid(row, col % rowAt(row).size())
        } else {
            null
        }
    }

    @Override
    BufferedImage toImage(Map options = [:]) {
        def (cellSize, inset) = imageOptions(options)

        int imgSize = 2 * rows * cellSize

        def center = imgSize / 2
        def imgW = imgSize + 1
        def imgH = imgSize + 1

        BufferedImage bufferedImage = new BufferedImage(imgW, imgH, TYPE_INT_ARGB)
        Graphics2D gfx = bufferedImage.createGraphics()

        // fill background
        gfx.color = Color.WHITE
        gfx.fillRect(0, 0, imgW, imgH)

        gfx.color = Color.BLACK

        eachCell { PolarCell cell ->
            if (cell.row > 0 && cell.row < rows) {
                def theta = 2 * Math.PI / rowAt(cell.row).size()
                def innerRadius = cell.row * cellSize
                def outerRadius = (cell.row + 1) * cellSize
                def thetaCCW = cell.col * theta
                def thetaCW = (cell.col + 1) * theta

                def ax = center + (innerRadius * Math.cos(thetaCCW)) as int
                def ay = center + (innerRadius * Math.sin(thetaCCW)) as int
                def bx = center + (outerRadius * Math.cos(thetaCCW)) as int
                def by = center + (outerRadius * Math.sin(thetaCCW)) as int
                def cx = center + (innerRadius * Math.cos(thetaCW)) as int
                def cy = center + (innerRadius * Math.sin(thetaCW)) as int
                def dx = center + (outerRadius * Math.cos(thetaCW)) as int
                def dy = center + (outerRadius * Math.sin(thetaCW)) as int

                if (!cell.linked(cell.inward)) gfx.drawLine(ax, ay, cx, cy)
                if (!cell.linked(cell.cw)) gfx.drawLine(cx, cy, dx, dy)
            }
        }

        gfx.draw(new Ellipse2D.Double(0, 0, imgSize, imgSize))

        bufferedImage
    }

    @Override
    String toString() {
        "PolarGrid(rows=$rows)"
    }

    static void main(args) {
        def grid = new PolarGrid(25)

        Algorithms.recursiveBacktracker(grid)

        ImageIO.write(grid.toImage(), 'png', new File("${System.getProperty('user.home')}/maze.png"))
    }
}
