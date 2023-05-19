package functional.v4_superfunctions_generic;

import functional.v4_superfunctions_generic.interfaces.*;

import java.util.List;
import java.util.Random;

/**
 * Las interfaces genericas permiten recibir y retornar tipo genericos.
 */

public class Launcher {

    public Launcher() {
        List<Integer> numbers = Superfunctions.get(10, new Getter<>() {
            final Random random = new Random();

            @Override
            public Integer get() {
                return random.nextInt(10);
            }
        });
        System.out.println(numbers);

        List<Integer> filtereds = Superfunctions.filter(numbers, new Predicate<>() {
            @Override
            public boolean test(Integer value) {
                return value % 2 == 0;
            }
        });
        System.out.println(filtereds);

        /* Ahora la funcion transform es generica ya que recibe por parametro una lista de un determinado tipo T y
         * devuelve una lista transformada que recibe un determinado tipo T (por ejemplo un Integer) y retorna un
         * determinado tipo R (por ejemplo un String). */
        List<Integer> transformed = Superfunctions.transform(filtereds, new OperadorUnario<>() {
            @Override
            public Integer transform(Integer value) {
                return value * value;
            }
        });
        System.out.println(transformed);

        // Pasa de Integer a String utilizando la interfaz Transformer
        List<String> convertidosEnCadena = Superfunctions.transform(filtereds, new Transformer<Integer, String>() {
            @Override
            public String transform(Integer value) {
                return "Valor: " + value;
            }
        });
        System.out.println(convertidosEnCadena);

        Superfunctions.consume(transformed, new Consumer<>() {
            @Override
            public void consume(Integer value) {
                System.out.println(value);
            }
        });

        Integer total = Superfunctions.calculate(transformed, 0, new OperadorBianario<>() {
            @Override
            public Integer calculate(Integer value1, Integer value2) {
                return value1 + value2;
            }
        });
        System.out.println("Suma: " + total);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
