package mazes

class UnderCell extends Cell {

    UnderCell(OverCell overCell) {
        super(overCell.row, overCell.col)

        if (overCell.hasHorizontalPassage()) {
            this.north = overCell.north
            overCell.north.south = this

            this.south = overCell.south
            overCell.south.north = this

            link(north)
            link(south)

        } else {
            this.east = overCell.east
            overCell.east.west = this

            this.west = overCell.west
            overCell.west.east = this

            link(east)
            link(west)
        }
    }

    boolean hasHorizontalPassage() {
        east || west
    }

    boolean hasVerticalPassage() {
        north || south
    }
}
