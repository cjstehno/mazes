package mazes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes = ['row', 'col'])
@ToString(includes = ['row', 'col'])
class Cell {

    final int row, col
    Cell north, south, east, west

    private final links = [:]

    Cell(final int row, final int col) {
        this.row = row
        this.col = col
    }

    Cell link(Cell cell, boolean bidi = true) {
        links[cell] = true
        if (bidi) cell.link(this, false)
        this
    }

    Cell unlink(Cell cell, boolean bidi = true) {
        links.remove(cell)
        if (bidi) cell.unlink(this, false)
        this
    }

    Set<Cell> links() {
        links.keySet()
    }

    boolean linked(Cell cell) {
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

    def distances() {
        def distances = new Distances(this)
        def frontier = [this]

        while (frontier) {
            def newFrontier = []
            frontier.each { cell ->
                cell.links().each { linked ->
                    if (distances[linked] == null) {
                        distances[linked] = distances[cell] + 1
                        newFrontier << linked
                    }
                }
            }

            frontier = newFrontier
        }

        distances
    }
}
