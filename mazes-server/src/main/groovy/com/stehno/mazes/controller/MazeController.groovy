package com.stehno.mazes.controller

import com.stehno.mazes.Grid
import com.stehno.mazes.repository.ImageRepository
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import mazes.Algorithms
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.imageio.ImageIO

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

@RestController @RequestMapping('/maze') @Slf4j
class MazeController {

    // FIXME: unit testing

    @Autowired private ImageRepository imageRepository

    @RequestMapping(method = POST)
    void retrieveMaze(@RequestBody MazeDefinition maze) {
        log.info 'Requested: {}', maze

        // FIXME: need visualization support

        Grid grid = generateGrid(maze.rows, maze.cols, maze.braiding, maze.algorithm)

        def data = new ByteArrayOutputStream()
        data.withStream { outstr ->
            ImageIO.write(grid.toImage(cellSize: maze.passageWidth), 'png', outstr)
        }

        imageRepository.store(data.toByteArray())
    }

    // TODO: add visualizations then move this into service class
    Grid generateGrid(int rows, int cols, int braidingPercent, Algorithm algorithm) {
        Grid grid = new Grid(rows, cols)

        algorithm.fn(grid)

        if (braidingPercent) {
            grid.braid((braidingPercent / 100) as float)
        }

        grid
    }

    @RequestMapping(value = 'image', method = GET)
    byte[] image() {
        imageRepository.retrieve() ?: MazeController.getResource('/blankmaze.png').bytes
    }
}

@EqualsAndHashCode @ToString(includeNames = true, includePackage = false)
class MazeDefinition {
    int rows
    int cols
    int passageWidth
    int braiding
    PathVisualization visualization
    Algorithm algorithm
}

enum PathVisualization {
    NONE, FLOODING, SHORTEST, LONGEST
}

enum Algorithm {
    BINARY_TREE(Algorithms.binaryTree),
    SIDEWINDER(Algorithms.sidewinder),
    ALDOUS_BRODER(Algorithms.aldousBroder),
    WILSONS(Algorithms.wilsons),
    HUNT_AND_KILL(Algorithms.huntAndKill),
    RECURSIVE_BACKTRACKER(Algorithms.recursiveBacktracker),
    KRUSKALS(Algorithms.kruskals),
    SIMPLIFIED_PRIMS(Algorithms.simplifiedPrims),
    TRUE_PRIMS(Algorithms.truePrims),
    RECURSIVE_DIVISION(Algorithms.recursiveDivision),
    RECURSIVE_DIVISION_WITH_ROOMS(Algorithms.recursiveDivision) // FIXME: need to make this distinction

    final Closure fn

    private Algorithm(Closure fn) {
        this.fn = fn
    }
}