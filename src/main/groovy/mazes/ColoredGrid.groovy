package mazes

import java.awt.*

class ColoredGrid extends DistanceGrid {

    private Cell farthest
    private int maximum

    ColoredGrid(int rows, int cols) {
        super(rows, cols)
    }

    void calculateMax(){
        def (farthest, maximum) = distances.max()
        this.farthest = farthest
        this.maximum = maximum
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
