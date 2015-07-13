package mazes

class Utils {

    private static final Random rand = new Random()

    static <T> T pick(Collection<T> list) {
        list ? list[rand.nextInt(list.size())] : null
    }

    static int randInt(int max) {
        rand.nextInt(max)
    }

    static float randFloat(){
        rand.nextFloat()
    }

    static boolean randBool() {
        rand.nextBoolean()
    }

    static boolean even(Number n){
        n % 2 == 0
    }

    static boolean odd( Number n){
        n % 2 != 0
    }
}
