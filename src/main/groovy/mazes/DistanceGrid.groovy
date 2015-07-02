package mazes

/**
 * FIXME: document me
 */
class DistanceGrid extends Grid {

    private Distances distances

    DistanceGrid(int rows, int cols) {
        super(rows, cols)
    }

    @Override
    protected String contentsOf(Cell cell) {
        if (distances && distances[cell] != null) {
            return Integer.toString(distances[cell], 36)
        } else {
            return super.contentsOf(cell)
        }
    }

    void calculateDistances(){
        def root = cellAt(0,0)
        distances = new Distances(root)
        def frontier = [root]

        while(frontier){
            def newFrontier = []

            frontier.each { cell->
                cell.links().each { linked->
                    if( distances[linked] == null ){
                        distances[linked] = distances[cell] + 1
                        newFrontier << linked
                    }
                }
            }

            frontier.clear()
            frontier.addAll(newFrontier)
        }
    }
}
