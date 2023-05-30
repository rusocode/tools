package functional.v11_streams_operaciones_individuales_y_ordenacion;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h2>Tratamiento individual de elementos</h2>
 * En algunas ocasiones necesitamos realizar algun tratamiento sobre cada uno de los elementos del stream. En dicho caso
 * debemos diferenciar entre operaciones terminales y no terminales. Las operaciones terminales NO produciran ningun
 * nuevo stream, mientras que las operaciones intermedias si.
 * <p>
 * La primera de las operaciones terminales para tratar cada uno de los elementos de un stream es un forEach(comsumer),
 * que aplica una determinada accion, recibida en forma de Consumer, a cada uno de los elementos del stream. Este metodo
 * no retorna nada, por lo que se trata de una operacion terminal. Debemos tener en cuenta que si se ejecuta sobre un
 * stream paralelo, NO hay ninguna garantia sobre en que orden se aplica la accion a los elementos.
 * <p>
 * Otro metodo muy parecido es forEachOrderer(consumer), que aplica una determinada accion, recibida en forma de
 * Consumer, a cada uno de los elementos del stream en el orden intrinseco del mismo. Este metodo no retorno nada, por
 * lo que se trata de una operacion terminal. Normalmente se usa encadenado despues de llamar al metodo sorted(), que
 * habra ordenado el stream. La ventaja de este metodo es que se garantiza que la accion se aplica a los elementos en el
 * orden intrinseco del stream, incluso aunque este se trate de un stream paralelo, aunque conlleve un peor rendimiento.
 * <p>
 * Los metodos forEach() y forEachOrdered() permiten realizar por tanto una iteracion interna, en contraposicion con la
 * interacion externa que se realizaria con un bucle for en una coleccion.
 * <p>
 * Tambien tenemos disponible una operacion intermedia con la que tratar individualmente cada uno de los elementos de un
 * stream, sin tener por ello que terminar la cadena de operaciones. Se trata del metodo peek(consumer), que aplica una
 * determinacion accion, recibida en forma de Consumer, a cada uno de los elementos del stream. Este metodo retorna un
 * nuevo stream con los mismos elementos que el original, por lo que se trata de una operacion no terminal.
 *
 * <br><br>
 *
 * <h2>Ordenacion</h2>
 * Algunos streams son ordenados, es decir que sus elementos poseen un determinado orden intrinseco significativo,
 * conocido como encounter order. Por ejemplo, un stream cuya fuente de datos corresponda a una lista creara un stream
 * ordenado, cuyo encounter order sera el orden en el que los elementos estan situados en la lista. Sin embargo, otros
 * streams no son ordenados, en el sentido de que sus elementos no tienen un orden intrinseco significativo. Por ejemplo,
 * un stream cuya fuente de datos sea un conjunto (set) sera un stream sin encounter order, ya que en un conjunto los
 * elementos no tienen un orden preestablecido.
 * <p>
 * El hecho de que un stream sea ordenado o no dependera del tipo de fuente de datos asociada y de las operaciones
 * intermedias anteriores que hayamos realizado mediante las que se ha obtenido el stream.
 * <p>
 * Algunas operaciones trabajan por defecto en base a este encounter order, imponiendo una restriccion acerca del orden
 * en el que los elementos deben ser procesados, como por ejemplo las operaciones intermedias limit() o skip(). Por
 * ejemplo:
 * <pre>{@code
 * Stream.of("Rulo", "Ruso", "Jose")
 *      .limit(2)
 *      .forEach(System.out::println);
 * }</pre>
 * muestra los valores Rulo y Ruso.
 * <p>
 * Sin embargo, existen otras operaciones que no tienen en cuenta el encounter order, como por ejemplo el forEach(), por
 * lo que el resultado de la operacion puede variar entre distintas operaciones (si queremos forzar a que se traten en
 * el orden intrinseco podemos usar en su lugar el operador forEachOrdered()). Por ejemplo:
 * <pre>{@code
 * IntStream.range(1, 8).parallel()
 *      .filter(n -> n % 2 == 0)
 *      .forEach(System.out::println);
 * }</pre>
 * Por otra parte, si queremos obtener un stream ordenado a partir de otro desordenado o a partir de otro stream
 * ordenado por un orden distinto, podemos usar el metodo sorted(), en cuyo caso los elementos del stream deben
 * implementar la interfaz Comparable para determinar el orden en el que deben ser ordenados. Otra posibilidad mejor
 * es simplemente pasar un objeto Comparator como argumento del metodo sorted(comparator). Como resultado de esta
 * operacion obtendremos un nuevo stream ordenado cuyo orden intrinseco correspondera al establecido mediante la
 * operacion sorted(), lo que afectara al procesamiento de los elementos en futuras operaciones que tengan en cuenta
 * dicho orden, como limit() y skip(). Por ejemplo:
 * <pre>{@code
 * Stream.of("Rulo", "Ruso", "Anyi")
 *      .sorted((s1, s2) -> s1.compareTo(s2))
 *      .limit(2);
 *      .forEach(System.out::println);
 * }</pre>
 * muestra los valores Anyi y Rulo, en este orden.
 */

public class Launcher {

    Random random = new Random();

    public Launcher() {
        /* List<Integer> result = Stream.of(2, 4, 6)
                .peek(System.out::println) // Al ser una operacion intermedia me permite seguir encadenando operaciones sobre los streams, esto se conoce como accion colateral
                .collect(Collectors.toList()); */

        /* Stream.of(2, 4, 6).parallel() // Ejecuta los elementos de manera simultanea
                .forEachOrdered(System.out::println); */ // .forEach(System.out::println);

        /* Stream.of(2, 4, 6)
                .limit(2)
                .forEach(System.out::println); */

        /* Stream.of(2, 6, 4)
                .sorted() // Devuelve un Stream con los elementos ordenados
                .forEach(System.out::println); */

        // Utiliza los metodos por defecto de la interfaz Comparator para determinar el orden de los elementos
        Stream.of(2, 6, 4)
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);

    }

    public Stream<Integer> getRandomNumbers(Integer size) {
        return random.ints(size, 0, 10).boxed();
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
