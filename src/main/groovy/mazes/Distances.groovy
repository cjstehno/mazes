package mazes

class Distances {

    private final Cell root
    private final cells = [:]

    Distances(Cell root) {
        this.root = root
        cells[root] = 0
    }

    def getAt(Cell cell) {
        cells[cell]
    }

    def putAt(Cell cell, int distance) {
        cells[cell] = distance
    }

    def cells() {
        cells.keySet()
    }

    def pathTo(Cell goal) {
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
}
