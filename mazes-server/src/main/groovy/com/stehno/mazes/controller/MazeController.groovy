package com.stehno.mazes.controller

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

@RestController @RequestMapping('/maze') @Slf4j
class MazeController {

    @RequestMapping(method = POST)
    void retrieveMaze(@RequestBody MazeRequest mazeRequest) {
        // OK response means image should be reloaded
        // run algorithm, generate image and store in session
        log.info 'Requested: {}', mazeRequest
    }

    @RequestMapping(value = 'image', method = GET)
    OutputStream image() {
        // since the storage is a single session based image, this just shows teh image in session
        // The insets will be hard-coded per algorithm to give the best views
    }
}

@EqualsAndHashCode @ToString(includeNames = true)
class MazeRequest {
    int rows
    int cols
    int braiding
    PathVisualization visualization
    Algorithm algorithm
}

enum PathVisualization {
    NONE, FLOODING, SHORTEST, LONGEST
}

enum Algorithm {
    BINARY_TREE,
    SIDEWINDER,
    ALDOUS_BRODER,
    WILSONS,
    HUNT_AND_KILL,
    RECURSIVE_BACKTRACKER,
    KRUSKALS,
    SIMPLIFIED_PRIMS,
    TRUE_PRIMS,
    RECURSIVE_DIVISION,
    RECURSIVE_DIVISION_WITH_ROOMS
}