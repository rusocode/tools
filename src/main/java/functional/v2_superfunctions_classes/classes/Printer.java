package functional.v2_superfunctions_classes.classes;

import functional.v2_superfunctions_classes.interfaces.Consumer;

/**
 * Imprime el valor en consola.
 */

public class Printer implements Consumer {

    @Override
    public void consume(Integer value) {
        System.out.println(value);
    }

}
