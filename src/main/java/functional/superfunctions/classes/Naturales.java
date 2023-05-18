package functional.superfunctions.classes;

import functional.superfunctions.interfaces.Getter;

/**
 * Obtiene un numero natural.
 */

public class Naturales implements Getter {

    private static int next;

    @Override
    public Integer get() {
        return next++;
    }

}
