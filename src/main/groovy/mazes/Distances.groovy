package mazes

class Distances {

    private final Cell root
    private final cells = [:]

    Distances(Cell root){
        this.root = root

        cells[root] = 0
    }

    def getAt(Cell cell){
        cells[cell]
    }

    def putAt(Cell cell, int distance){
        cells[cell] = distance
    }

    def cells(){
        cells.keySet()
    }
}
