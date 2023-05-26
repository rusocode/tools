package functional.v11_streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
 * <p>
 * Otra caracteristica importantisima del pipeline es que es perezoso (lazy), lo que quiere decir que <b>las operaciones
 * intermedias solo son ejecutadas cuando las requiere la operacion terminal que se este ejecutando</b>. Esto quiere
 * decir que en el ejemplo anterior, al ejecutar el metodo forEach() este solicita los elementos del stream al metodo
 * filter(), que a su vez solicita los elementos del stream al metodo estatico range().
 *
 * <br><br>
 *
 * <h2>Creacion de un stream a partir de una fuente de datos</h2>
 * Java permite muchas maneras de crear un stream, depèndiendo de la fuente de datos origen deseada. Algunas de estas
 * fuentes son:
 * <ul>
 * <li>Coleccion
 * <li>Conjunto predeterminado de elementos
 * <li>Funcion suministradora de objetos (interfaz funcional Supplier)
 * <li>Un valor inicial y una funcion que obtiene el siguiente elemento a partir del anterior
 * <li>Generador de numeros aleatorios
 * <li>Metodos estaticos de la clase IntStream
 * <li>Metodo chars() de String
 * </ul>
 */

public class Launcher {

    Random random = new Random();

    public Launcher() {
        // names.forEach(System.out::println); // Bucle implicito

        // List<String> names = new ArrayList<>(List.of("Manolo", "Pedro", "Rulo"));

        // 1. Funcion generadora del stream (crea un nuevo stream que tiene como fuente de datos esa lista de nombres)
        // List<String> result = names.stream()
        // 2. 0 o mas operaciones intermedias
        //  .limit(1) // Limita los elementos de esta secuencia, truncados para que no superen la longitud maxima
        // .filter(name -> name.contains("P")) // filter es una operacion intermedia porque retorna un stream de String
        // 3. Operacion terminal
        // .forEach(System.out::println); // La operacion terminal consiste en consumir (imprimir por consola) cada uno de los elementos producidos por el paso anterior del stream

        // Las operaciones terminales se caracterizan por que NO retornan un stream, pero hay algunas que si lo hacen...
        //  .collect(Collectors.toList()); // Esta operacion terminal recolecta los datos intermedios del stream en una lista
        /* Otra caracteristica importante de las operaciones terminales es que despues de ejecucion, el stream
         * original NO puede ser usado de nuevo. */
        // System.out.println(result);

        // Conjunto predeterminado de elementos
       /* List<String> result = Stream.of("Manolo", "Pedro", "Rulo") // Stream finito
                .collect(Collectors.toList());
        System.out.println(result); */

        // Funcion suministradora de objetos (interfaz funcional Supplier)
        /* List<Integer> result = Stream.generate(() -> {
                    int next = random.nextInt(10);
                    System.out.printf("Se ha generado el %d\n", next);
                    return next;
                })
                .limit(3) /* Ahora limita el numero de elementos con el que esta trabajando. Esta es la demostracion de
                 * que el stream funciona de manera perezosa por que se limita a 3 veces por que el limit llama a la
                 * funcion del Supplier solo tres veces.
                .collect(Collectors.toList());
        System.out.println(result); */

        // Un valor inicial y una funcion que obtiene el siguiente elemento a partir del anterior
        /* List<Integer> result = Stream.iterate(1, value -> value * 5)
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(result); */

        // Ahora en Java 9 le agregaron al metodo iterate un Predicado
        /* List<Integer> result = Stream.iterate(1, value -> value < 1000, value -> value * 5) // La condicion del segundo parametro actua como limit
                .collect(Collectors.toList());
        System.out.println(result); */

        // Generador de numeros aleatorios
        /* List<Integer> result = random.ints(5, 0, 10)
                .boxed() // Devuelve un Stream que consta de los elementos de este flujo, cada uno encuadrado en un Integer
                .collect(Collectors.toList());
        System.out.println(result); */

        // Metodos estaticos de la clase IntStream
       /*  List<Integer> result = IntStream.range(0, 10)
                .boxed().collect(Collectors.toList());
        System.out.println(result); */

        // Metodo chars() de String
        List<Integer> result = "Rulo".chars()
                .boxed().collect(Collectors.toList());
        System.out.println(result);

    }

    private int randomInt() {
        return random.nextInt(10);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
