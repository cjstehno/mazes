package mazes.alg

import mazes.CliHelper
import mazes.Grid

import static mazes.Utils.pick
import static mazes.Utils.randBool

class Sidewinder {

    static algorithm = { Grid grid ->
        grid.eachRow { row ->
            def run = []

            row.each { cell ->
                run << cell

                boolean atEastBounds = !cell.east
                boolean atNorthBounds = !cell.north

                boolean shouldCloseOut = atEastBounds || (!atNorthBounds && randBool())

                if (shouldCloseOut) {
                    def member = pick(run)
                    if (member.north) member.link(member.north)

                    run.clear()

                } else {
                    cell.link(cell.east)
                }
            }
        }

        grid
    }

    static void main(args) {
        CliHelper.generate(args, algorithm)
    }
}

