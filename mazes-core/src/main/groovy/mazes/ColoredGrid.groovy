package mazes

import com.stehno.mazes.Cell
import com.stehno.mazes.Grid

import java.awt.Color

class ColoredGrid extends Grid {

    Distances distances
    int maximum

    ColoredGrid(int rows, int cols) {
        super(rows, cols)
    }

    void setDistances(Distances distances) {
        this.distances = distances
        def (farthest, max) = distances.max()
        this.maximum = max
    }

    @Override
    protected Color cellBackgroundColor(Cell cell) {
        int distance = distances[cell] ?: 0
        float intensity = ((maximum - distance) as float) / maximum

        int dark = Math.round(255 * intensity)
        int bright = 128 + Math.round(127 * intensity)

        new Color(dark, bright, dark)
    }
}
