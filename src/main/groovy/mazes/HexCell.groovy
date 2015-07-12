package mazes

class HexCell extends Cell {

    Cell northeast, northwest, southeast, southwest

    HexCell(int row, int col) {
        super(row, col)
    }

    @Override
    def neighbors() {
        def list = []
        if (northwest) list << northwest
        if (north) list << north
        if (northeast) list << northeast
        if (southwest) list << southwest
        if (south) list << south
        if (southeast) list << southeast
        list
    }
}
