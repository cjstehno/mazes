package com.stehno.mazes.controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController @RequestMapping('/maze')
class MazeController {

    @RequestMapping(method = GET)
    Map retrieveMaze(){
        [alpha:1, bravo:2]
    }
}
