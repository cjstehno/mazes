package mazes

import spock.lang.Specification

class GridSpec extends Specification {

    def 'simple: 4x4'(){
        when:
        def grid = new Grid(4,4)
        def cells = grid.cells

        then:
        grid.rows == 4
        grid.cols == 4
        grid.size() == 16
        cells.size() == 16

        assertCell cells[0], 0, 0, null, cells[4], cells[1], null
        assertCell cells[1], 0, 1, null, cells[5], cells[2], cells[0]
        assertCell cells[2], 0, 2, null, cells[6], cells[3], cells[1]
        assertCell cells[3], 0, 3, null, cells[7], null, cells[2]

        assertCell cells[4], 1, 0, cells[0], cells[8], cells[5], null
        assertCell cells[5], 1, 1, cells[1], cells[9], cells[6], cells[4]
        assertCell cells[6], 1, 2, cells[2], cells[10], cells[7], cells[5]
        assertCell cells[7], 1, 3, cells[3], cells[11], null, cells[6]

        assertCell cells[8], 2, 0, cells[4], cells[12], cells[9], null
        assertCell cells[9], 2, 1, cells[5], cells[13], cells[10], cells[8]
        assertCell cells[10], 2, 2, cells[6], cells[14], cells[11], cells[9]
        assertCell cells[11], 2, 3, cells[7], cells[15], null, cells[10]

        assertCell cells[12], 3, 0, cells[8], null, cells[13], null
        assertCell cells[13], 3, 1, cells[9], null, cells[14], cells[12]
        assertCell cells[14], 3, 2, cells[10], null, cells[15], cells[13]
        assertCell cells[15], 3, 3, cells[11], null, null, cells[14]
    }

    private static void assertCell(cell, row, col, n, s, e, w){
        assert cell.row == row && cell.col == col
        assert cell.north == n
        assert cell.south == s
        assert cell.east == e
        assert cell.west == w
    }
}
