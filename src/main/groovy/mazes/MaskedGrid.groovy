package mazes

class MaskedGrid extends Grid {

    final Mask mask

    MaskedGrid(final Mask mask) {
        super(mask.rows, mask.cols, false)
        this.mask = mask
        init()
    }

    @Override
    protected prepareGrid() {
        def list = []
        rows.times { r ->
            def columns = []
            cols.times { c ->
                if (mask.getAt(r, c)){
                    columns << new Cell(r, c)
                } else {
                    columns << null
                }
            }
            list << columns
        }
        list
    }

    @Override
    Cell randomCell() {
        def (row, col) = mask.randomLocation()
        cellAt(row, col)
    }

    @Override
    int size() {
        mask.count()
    }
}
