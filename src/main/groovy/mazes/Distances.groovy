package mazes

class Distances {

    private final cells = [:]

    Distances(Cell root){
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
