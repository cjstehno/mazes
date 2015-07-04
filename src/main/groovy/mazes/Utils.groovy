package mazes

/**
 * FIXME: document me
 */
class Utils {

    private static final Random rand = new Random()

    static <T> T pick(Collection<T> list) {
        list ? list[rand.nextInt(list.size())] : null
    }

    static boolean randBool(){
        rand.nextBoolean()
    }
}
