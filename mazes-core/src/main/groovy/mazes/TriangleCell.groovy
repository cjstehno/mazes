package mazes

class TriangleCell extends Cell {

    TriangleCell(int row, int col) {
        super(row, col)
    }

    boolean upright() {
        Utils.even(row + col)
    }

    @Override
    def neighbors() {
        def list = []
        if (west) list << west
        if (east) list << east
        if (!upright() && north) list << north
        if (upright() && south) list << south
        list
    }
}
