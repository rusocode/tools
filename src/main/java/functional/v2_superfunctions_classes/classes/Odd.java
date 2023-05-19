package functional.v2_superfunctions_classes.classes;

import functional.v2_superfunctions_classes.interfaces.Predicate;

/**
 * Testea si el valor es impar.
 */

public class Odd implements Predicate {

    @Override
    public boolean test(Integer value) {
        return value % 2 != 0;
    }
}
