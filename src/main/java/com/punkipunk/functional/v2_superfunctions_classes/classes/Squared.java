package com.punkipunk.functional.v2_superfunctions_classes.classes;

import com.punkipunk.functional.v2_superfunctions_classes.interfaces.Transformer;

/**
 * Calcula el cuadrado del valor.
 */

public class Squared implements Transformer {

    @Override
    public Integer transform(Integer value) {
        return value * value;
    }

}
