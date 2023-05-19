package functional.v2_superfunctions_classes.classes;

import functional.v2_superfunctions_classes.interfaces.Arithmetic;

public class Adder implements Arithmetic {

    @Override
    public Integer calculate(Integer value1, Integer value2) {
        return value1 + value2;
    }

}
