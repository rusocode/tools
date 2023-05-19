package functional.superfunctions.classes;

import functional.superfunctions.interfaces.Getter;

import java.util.Random;

/**
 * Obtiene un numero aleatorio.
 */

public class Aleatorio implements Getter {

    private final Random random = new Random();

    @Override
    public Integer get() {
        return random.nextInt(10);
    }
}
