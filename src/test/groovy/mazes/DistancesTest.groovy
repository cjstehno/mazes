package mazes

import spock.lang.Specification

class DistancesTest extends Specification {

    def 'simple: get'(){
        when:
        def root = new Cell(0,0)
        def distances = new Distances(root)

        then:
        distances[root] == 0
    }

    def 'simple: put'(){
        when:
        def root = new Cell(0,0)
        def distances = new Distances(root)

        def cell = new Cell(0,1)
        distances[cell] = 1

        then:
        distances[root] == 0
        distances[cell] == 1
    }
}
