package com.stehno.mazes

import spock.lang.Specification

class CellSpec extends Specification {

    def 'basic properties'(){
        when:
        def cell = new Cell(3,5)

        then:
        cell.row == 3
        cell.col == 5
        !cell.links()
        !cell.neighbors()
    }

    def 'links: bi-directional'(){
        setup:
        def cellA = new Cell(3,5)
        def cellB = new Cell(1,7)

        when:
        cellA.link(cellB)

        then:
        cellA.links().contains(cellB)
        cellB.links().contains(cellA)
        cellA.linked(cellB)
        cellB.linked(cellA)

        when:
        cellA.unlink(cellB)

        then:
        !cellA.links().contains(cellB)
        !cellB.links().contains(cellA)
        !cellA.linked(cellB)
        !cellB.linked(cellA)
    }

    def 'links: uni-directional'(){
        setup:
        def cellA = new Cell(3,5)
        def cellB = new Cell(1,7)

        when:
        cellA.link(cellB, false)

        then:
        cellA.links().contains(cellB)
        !cellB.links().contains(cellA)
        cellA.linked(cellB)
        !cellB.linked(cellA)

        when:
        cellA.unlink(cellB)

        then:
        !cellA.links().contains(cellB)
        !cellA.linked(cellB)
        !cellB.linked(cellA)
    }

    def 'neighbors'(){
        setup:
        def cellA = new Cell(3,5)
        def cellB = new Cell(1,7)
        def cellC = new Cell(8,6)

        cellA.north = cellB
        cellA.east = cellC

        when:
        def neighbors = cellA.neighbors()

        then:
        neighbors.size() == 2
        neighbors.containsAll([cellB, cellC])
    }
}
