package mazes

import com.stehno.mazes.Cell

class Distances {

    private final Cell root
    private final cells = [:]

    Distances(Cell root) {
        this.root = root
        cells[root] = 0
    }

    def getAt(Cell cell){
        cells[cell]
    }

    void putAt(Cell cell, int dist){
        cells[cell] = dist
    }

    Set<Cell> cells(){
        cells.keySet()
    }

    def pathTo(Cell goal){
        def current = goal

        def breadcrumbs = new Distances(root)
        breadcrumbs[current] = cells[current]

        while (current != root) {
            for (neighbor in current.links()) {
                if (cells[neighbor] < cells[current]) {
                    breadcrumbs[neighbor] = cells[neighbor]
                    current = neighbor
                    break
                }
            }
        }

        breadcrumbs
    }

    def max(){
        int maxDistance = 0
        Cell maxCell = root

        cells.each { cell, dist->
            if( dist > maxDistance ){
                maxCell = cell
                maxDistance = dist
            }
        }

        [maxCell, maxDistance]
    }
}
