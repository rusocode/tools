package functional.v8_method_reference;

import functional.NumberUtils;

import java.util.Random;

/**
 * Una referencia a metodo (<i>method reference</i>) no es mas que una expresion que denota (representa) a un metodo, de
 * una forma parecida a los punteros a funcion de C/C++.
 * <p>
 * ¿Entonces se recomienda usar lambdas o method reference?
 * <br>
 * Segun libros y desarrolladores importantes se recomienda usar el method reference siempre que sea posible.
 */

public class Launcher {

    Random random = new Random();

    public Launcher() {
        Integer total = Flujo.get(10, this::randomInt) // () -> randomInt()
                /* El method reference solo se aplica en el caso de que el valor que recibe la lambda es el que se le
                 * pasa a la funcion que es llamada en el cuerpo de la lambda. Es decir que la lambda que se esta
                 * pasando, se pasa en forma de referencia de metodo. */
                .filter(NumberUtils::isPrime) // Es equivalente a la expresion lambda: value -> NumberUtils.isPrime(value)
                .transform(NumberUtils::squaring) // Referencia a metodo estatico
                .actuar(System.out::println) // value -> System.out.println(value) / Referencia a metodo de instancia
                .calculate(0, (Integer::sum)); // (value1, value2) -> Integer.sum(value1, value2)
        System.out.println("Suma: " + total);
    }

    private int randomInt() {
        return random.nextInt(10);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
