package mazes

class SimpleMask {

    static void main(args){
        def mask = new Mask(5,5)

        mask.setAt(0,0, false)
        mask.setAt(2,2, false)
        mask.setAt(4,4, false)

        def grid = new MaskedGrid(mask)
        Algorithms.recursiveBacktracker(grid)

        println grid
    }
}
