package com.stehno.mazes

import spock.lang.Specification

class GridSpec extends Specification {

    def 'basic properties'(){
        when:
        def grid = new Grid(4,5)

        then:
        grid.rows == 4
        grid.cols == 5
    }
}
