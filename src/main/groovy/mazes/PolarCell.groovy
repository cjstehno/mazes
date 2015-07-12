package mazes

class PolarCell extends Cell {

    Cell cw, ccw, inward
    final List<Cell> outward = []

    PolarCell(int row, int col) {
        super(row, col)
    }

    @Override
    def neighbors() {
        def list = []
        if(cw) list << cw
        if(ccw) list << ccw
        if(inward) list << inward
        list.addAll(outward)
        list
    }
}
