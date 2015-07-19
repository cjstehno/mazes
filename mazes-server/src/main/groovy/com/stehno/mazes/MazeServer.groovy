package com.stehno.mazes

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
@ComponentScan(['com.stehno.mazes.controller', 'com.stehno.mazes.repository'])
class MazeServer {

    static void main(final String[] args) {
        new SpringApplicationBuilder(MazeServer).run(args)
    }
}
