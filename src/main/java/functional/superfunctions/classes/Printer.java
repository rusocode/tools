package functional.superfunctions.classes;

import functional.superfunctions.interfaces.Consumer;

/**
 * Imprime el valor en consola.
 */

public class Printer implements Consumer {

    @Override
    public void consume(Integer value) {
        System.out.println(value);
    }

}
