package functional.superfunctions.classes;

import functional.superfunctions.interfaces.Arithmetic;

public class Multiplier implements Arithmetic {

    @Override
    public Integer calculate(Integer value1, Integer value2) {
        return value1 * value2;
    }

}
