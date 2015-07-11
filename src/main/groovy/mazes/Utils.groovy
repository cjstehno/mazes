package mazes

class Utils {

    private static final Random rand = new Random()

    static <T> T pick(Collection<T> list) {
        list ? list[rand.nextInt(list.size())] : null
    }

    static int randInt(int max) {
        rand.nextInt(max)
    }

    static boolean randBool() {
        rand.nextBoolean()
    }
}
