package functional.superfunctions.classes;

import functional.superfunctions.interfaces.Predicate;

/**
 * Testea si el valor es impar.
 */

public class Odd implements Predicate {

    @Override
    public boolean test(Integer value) {
        return value % 2 != 0;
    }
}
