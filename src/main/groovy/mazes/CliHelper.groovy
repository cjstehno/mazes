package mazes

import javax.imageio.ImageIO

class CliHelper {

    static void generate(args, mazeGenerator) {
        def cli = new CliBuilder()
        cli.r(longOpt: 'rows', args: 1, argName: 'row-count', 'Number of rows to generate (defaults to 6).')
        cli.c(longOpt: 'cols', args: 1, argName: 'col-count', 'Number of columns to generate (defaults to 6).')
        cli.f(longOpt: 'file', args: 1, argName: 'image-file', 'Render as image to specified file (PNG).')
        cli.h(longOpt: 'help', 'Show usage help.')

        def options = cli.parse(args)

        if (options.help) {
            cli.usage()

        } else {
            def maze = mazeGenerator(new Grid((options.r ?: 6) as int, (options.c ?: 6) as int))

            if (options.f) {
                ImageIO.write(maze.toImage(), 'png', new File(options.f))

            } else {
                println maze
            }
        }
    }
}
