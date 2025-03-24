package com.punkipunk.functional.v2_superfunctions_classes.classes;

import com.punkipunk.functional.v2_superfunctions_classes.interfaces.Getter;

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
