package com.punkipunk.functional.v2_superfunctions_classes.classes;

import com.punkipunk.functional.v2_superfunctions_classes.interfaces.Predicate;

/**
 * Testea si el valor es par.
 */

public class Even implements Predicate {

    @Override
    public boolean test(Integer value) {
        return value % 2 == 0;
    }
}
