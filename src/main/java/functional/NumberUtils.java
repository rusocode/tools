package functional;

/**
 * Clase de prueba para hacer referencia a los metodos estaticos con el method reference.
 */

public class NumberUtils {

    private NumberUtils() {
    }

    public static boolean isPrime(int value) {
        for (int i = 2; i < value; i++)
            if (value % i == 0) return false;
        return true;
    }

    public static int squaring(int value) {
        return value * value;
    }


}
