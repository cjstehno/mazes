package mazes

import spock.lang.Specification

class CellSpec extends Specification {

    def 'simple'(){
        when:
        def cell = new Cell(1,2)

        then:
        cell.row == 1
        cell.col == 2
    }
}
