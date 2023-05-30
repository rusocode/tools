package functional.v3_superfunctions_inline_classes;

import functional.v3_superfunctions_inline_classes.interfaces.*;

import java.util.List;
import java.util.Random;

/**
 * Las clases inline permiten crear objetos a traves de una clase anonima. La ventaja de esto es que no es necesario
 * crear clases para cada funcion.
 */

public class Launcher {

    public Launcher() {
        List<Integer> numbers = Superfunctions.get(10, new Getter() {
            final Random random = new Random();

            @Override
            public Integer get() {
                return random.nextInt(10);
            }
        });
        System.out.println(numbers);

        List<Integer> filtereds = Superfunctions.filter(numbers, new Predicate() {
            @Override
            public boolean test(Integer value) {
                return value % 2 == 0;
            }
        });
        System.out.println(filtereds);

        List<Integer> transformed = Superfunctions.transform(filtereds, new Transformer() {
            @Override
            public Integer transform(Integer value) {
                return value * value;
            }
        });
        System.out.println(transformed);

        Superfunctions.consume(transformed, new Consumer() {
            @Override
            public void consume(Integer value) {
                System.out.println(value);
            }
        });

        System.out.println("Suma: " + Superfunctions.calculate(transformed, 0, new Arithmetic() {
            @Override
            public Integer calculate(Integer value1, Integer value2) {
                return value1 + value2;
            }
        }));
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
