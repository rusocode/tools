package com.punkipunk.functional.v7_interfaces_funcionales_estandar.v6_lambdas;

import java.util.Random;

/**
 * En este caso no hay referencias a las interfaces funcionales porque las expresiones lambdas no son literales de tipo
 * funcion. Esto quiere decir que no estan asociados especificamente con una determinada interfaz funcional. Por lo
 * tanto se puede usar la lambda como valor de cualquier parametro o variable que sea de un tipo correspondiente a una
 * interfaz funcional cuyo unico metodo abstracto tenga una unica firma compatible con la firma de la lambda.
 */

public class Launcher {

    public Launcher() {
        Random random = new Random();
        Integer total = Flujo.get(10, () -> random.nextInt(10))
                .filter(value -> value % 2 == 0)
                .transform(value -> value * value)
                .actuar(value -> System.out.println(value))
                .calculate(0, ((value1, value2) -> value1 + value2));
        System.out.println("Suma: " + total);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
