package functional.superfunctions.classes;

import functional.superfunctions.interfaces.Transformer;

/**
 * Calcula el cuadrado del valor.
 */

public class Squared implements Transformer {

    @Override
    public Integer transform(Integer value) {
        return value * value;
    }

}
