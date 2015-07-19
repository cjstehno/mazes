package com.stehno.mazes

class RandomUtils {

    private static final Random rng = new Random()

    static <T> T rand(Collection<T> list) {
        list ? list[rng.nextInt(list.size())] : null
    }

    static int randInt(int max) {
        rng.nextInt(max)
    }

    static float randFloat(){
        rng.nextFloat()
    }

    static boolean randBool() {
        rng.nextBoolean()
    }
}
