package mazes

import static java.lang.Integer.toString

class DistanceGrid extends Grid {

    Distances distances

    DistanceGrid(int rows, int cols) {
        super(rows, cols)
    }

    @Override
    protected String contentsOf(Cell cell) {
        if (distances && distances[cell] != null) {
            return toString(distances[cell], 36)
        } else {
            super.contentsOf(cell)
        }
    }
}
