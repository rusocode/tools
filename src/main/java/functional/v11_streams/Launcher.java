package functional.v11_streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Un <i>stream</i> (flujo) en Java es una secuencia de elementos que se pueden procesar (mapear, filtrar, transformar,
 * reducir y recolectar), de forma secuencial o paralela, mediante una cadena de operaciones especificas a traves de
 * expresiones lambda. Los streams permiten optimizar la forma de procesar grandes colecciones de datos.
 * <p>
 * A partir de Java 8 se incorpora la interfaz Stream que contiene toda la funcionalidad de la programacion funcional.
 * Esta interfaz proporciona metodos de factoria en la mayoria de las interfaces y clases de colecciones. Por lo tanto
 * se puede omitir la clase Flujo y usar las funciones de la interfaz Stream. Cada una de estas funciones retorna un
 * Stream de un determinado elemento.
 * <p>
 * La interfaz Stream<T> representa un flujo de elementos de tipo T, aunque tambien se definen interfaces concretas para
 * los tipos primitivos, como IntStream, LongStream, etc.
 * <p>
 * Un stream es una abstraccion que representa un flujo de datos, pero NO una estructura de datos, ya que los elementos
 * NO son almacenados en el stream, sino tan solo procesados por el. De hecho, NO se puede acceder individualmente a un
 * determinado elemento del stream, sino que se define la fuente de datos origen del stream y la secuencia de
 * operaciones que se deben aplicar sobre sus elementos, especificadas de forma funcional mediante expresiones lambda.
 * <p>
 * Mas aun, la fuente de datos origen del stream NO se ve efectada por las operaciones realizadas a partir de el dentro
 * del stream. Por ejemplo, si se filtran algunos elementos de datos del stream, NO se aliminan realmente de la fuente
 * de datos origen, simplemente se omiten a partir de ese momento y ya no se tienen en cuenta en la siguiente operacion
 * incluida en la secuencia de operaciones del stream. Por tanto, los datos con los que trabajamos no se ven afectados
 * por el stream.
 * <p>
 * Los streams solo gestionan datos transitorios en memoria, lo que implica que si la aplicacion falla dichos datos se
 * perderan.
 * <p>
 * Un stream puede ser finito, es decir, tener un numero finito de elementos, o infinito, si genera un numero
 * infinito de elementos. Algunas operaciones permiten restringir el numero de elementos procesados, como limit()
 * o findFirst().
 *
 * <br><br>
 *
 * <h2>Pipeline</h2>
 * Resulta que los stream funcionan con un encadenado de operaciones. Ese encadenado es lo que se llama Pipeline
 * (tuberia). Es decir que es una tuberia por la que van pasando los datos. Por tanto un pipeline tiene los siguientes
 * elementos en el siguiente orden:
 * <ol>
 * <li>Una funcion generadora del stream.
 * <li>Cero o mas operaciones intermedias.
 * <li>Una operacion terminal.
 * </ol>
 * Debemos tener en cuenta que cada operacion intermedia del pipeline genera un nuevo stream resultante de aplicar la
 * operacion indicada al stream anterior a la cadena. Por ejemplo, si tenemos el siguiente pipelina:
 * <pre>{@code
 * IntStream.range(1, 8)
 *      .filter(n -> n % 2 == 0)
 *      .forEach(System.out::println);
 * }</pre>
 * El metodo estatico range() retorna un stream que es usado por el metodo filter() que retorna un nuevo stream que es
 * usado por el metodo forEach().
 */

public class Launcher {

    Random random = new Random();

    public Launcher() {
        // names.forEach(System.out::println); // Bucle implicito

        List<String> names = new ArrayList<>(List.of("Manolo", "Pedro", "Rulo"));

        // 1. Funcion generadora del stream (crea un nuevo stream que tiene como fuente de datos esa lista de nombres)
        names.stream()
                // 2. 0 o mas operaciones intermedias
                .filter(name -> name.contains("P")) // filter es una operacion intermedia porque retorna un stream de String
                // 3. Operacion terminal
                .forEach(System.out::println); // La operacion terminal consiste en consumir (imprimir por consola) cada uno de los elementos producidos por el paso anterior del stream
        /* Flujo.get(10, this::randomInt)
                .filter(value -> value >= 0)
                .sort(Integer::compareTo)
                .transform(NumberUtils::squaring)
                .transform(Descripcion::new)
                .actuar(System.out::println)
                .transform(Descripcion::getValue)
                .max(Integer::compare)
                .ifPresentOrElse(value -> System.out.println("Maximo: " + value.doubleValue()),
                        () -> System.out.println("No hay maximo porque el flujo esta vacio!")); */
    }

    private int randomInt() {
        return random.nextInt(10);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
