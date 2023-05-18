package functional.superfunctions.classes;

import functional.superfunctions.interfaces.Transformer;

/**
 * Calcula el cubo del valor.
 */

public class Cubed implements Transformer {

    @Override
    public Integer transform(Integer value) {
        return value * value * value;
    }

}
