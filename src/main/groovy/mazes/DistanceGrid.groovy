package mazes

/**
 * FIXME: document me
 */
class DistanceGrid extends Grid {

    private Distances distances

    DistanceGrid(int rows, int cols) {
        super(rows, cols)
    }

    @Override
    protected String contentsOf(Cell cell) {
        if (distances && distances[cell]) {
            return Integer.toString(distances[cell], 36)
        } else {
            return super.contentsOf(cell)
        }
    }
}
