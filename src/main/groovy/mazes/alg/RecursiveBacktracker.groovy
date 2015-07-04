package mazes.alg

import mazes.CliHelper
import mazes.Grid

import static mazes.Utils.pick

class RecursiveBacktracker {

    static algorithm = { Grid grid, startAt = grid.randomCell() ->
        def stack = []
        stack.push startAt

        while (stack) {
            def current = stack.last()
            def neighbors = current.neighbors().findAll { n -> n.links().isEmpty() }
            if (neighbors.isEmpty()) {
                stack.pop()

            } else {
                def neighbor = pick(neighbors)
                current.link(neighbor)
                stack.push neighbor
            }
        }

        grid
    }

    static void main(args) {
        CliHelper.generate(args, algorithm)
    }
}
