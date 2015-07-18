package mazes

class OverCell extends Cell {

    private final Grid grid

    OverCell(int row, int col, Grid grid) {
        super(row, col)
        this.grid = grid
    }

    @Override
    def neighbors() {
        def list = super.neighbors()

        if (canTunnelNorth()) list << north.north
        if (canTunnelSouth()) list << south.south
        if (canTunnelEast()) list << east.east
        if (canTunnelWest()) list << west.west

        list
    }

    @Override
    Cell link(Cell cell, boolean bidi = true) {
        def neighbor = null

        if (north && north == cell.south) {
            neighbor = north
        } else if (south && south == cell.north) {
            neighbor = south
        } else if (east && east == cell.west) {
            neighbor = east
        } else if (west && west == cell.east) {
            neighbor = west
        }

        if (neighbor) {
            grid.tunnelUnder(neighbor)
        } else {
            super.link(cell, bidi)
        }
    }

    boolean canTunnelNorth() {
        north && north.north && north.hasHorizontalPassage()
    }

    boolean canTunnelSouth() {
        south && south.south && south.hasHorizontalPassage()
    }

    boolean canTunnelEast() {
        east && east.east && east.hasVerticalPassage()
    }

    boolean canTunnelWest() {
        west && west.west && west.hasVerticalPassage()
    }

    boolean hasHorizontalPassage() {
        linked(east) && linked(west) && !linked(north) && !linked(south)
    }

    boolean hasVerticalPassage() {
        linked(north) && linked(south) && !linked(east) && !linked(west)
    }
}


