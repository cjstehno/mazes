package mazes

import groovy.transform.*

@EqualsAndHashCode(includes = ['row', 'col'])
@ToString(includes = ['row', 'col'])
class Cell {

    final int row, col
    Cell north, south, east, west

    private final links = [:]

    Cell(int row, int col) {
        this.row = row
        this.col = col
    }

    Cell link(cell, bidi = true) {
        links[cell] = true
        if (bidi) cell.link(this, false)
        this
    }

    Cell unlink(cell, bidi = true) {
        links.remove(cell)
        if (bidi) cell.unlink(this, false)
        this
    }

    Set<Cell> links() {
        links.keySet()
    }

    boolean linked(cell) {
        links.containsKey(cell)
    }

    def neighbors() {
        def list = []
        if (north) list << north
        if (south) list << south
        if (east) list << east
        if (west) list << west
        list
    }
}