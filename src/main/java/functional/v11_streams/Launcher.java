package functional.v11_streams;

import java.util.List;
import java.util.Random;
import java.util.stream.*;

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
 * (tuberia). Es decir que es como una tuberia por la que van pasando los datos. Por lo tanto un pipeline tiene los
 * siguientes elementos en el siguiente orden:
 * <ol>
 * <li>Una funcion generadora del stream.
 * <li>Cero o mas operaciones intermedias.
 * <li>Una operacion terminal.
 * </ol>
 * Debemos tener en cuenta que cada operacion intermedia del pipeline genera un nuevo stream resultante de aplicar la
 * operacion indicada al stream anterior a la cadena. Por ejemplo, si tenemos el siguiente pipeline:
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
 * <li>etc
 * </ul>
 *
 * <h2>Filtrado</h2>
 * Otra de las operaciones intermedias que se pueden realizar sobre un stream es el filtrado de sus elementos, es decir,
 * la generacion de un nuevo stream que solo contega algunos de los elementos del stream original. Para eso, Java nos
 * proporciona distintos metodos como distinct(), que retorna un nuevo stream con los elementos del stream original,
 * excepto aquellos que estuvieran repetidos. Por ejemplo:
 * <pre>{@code
 * Stream.of(1, 3, 2, 3, 1)
 *      .distinct()
 *      .forEach(System.out::println);
 * }</pre>
 * muestra los valores 1, 3 y 2, en este orden.
 * <p>
 * Otro de los elementos de filtrado es el metodo limit(), que retorna un nuevo stream con tan solo n elementos de stream
 * original, atendiendo al orden intrinseco del mismo.
 * <pre>{@code
 * Stream.of(1, 3, 2, 3, 1)
 *      .limit(2)
 *      .forEach(System.out::println);
 * }</pre>
 * muestra los valores 1 y 3, en este orden.
 * <p>
 * El metodo de filtrado mas generico es el metodo filter(predicate), que retorna un nuevo stream que solo incorpora los
 * elementos del stream original que cumplan el predicado recibido (aquellos para los que la funcion suministrada retorne
 * true). Por ejemplo:
 * <pre>{@code
 * Stream.of(1, 3, 8, 5, 2)
 *      .filter(n -> n < 4)
 *      .forEach(System.out::println);
 * }</pre>
 * muestra los valores 1, 3 y 2, en este orden.
 * <p>
 * Java 9 introdujo dos nuevos metodos para saltarse metodos de un stream. El primero de ellos es dropWhile(predicate)
 * que retorna un nuevo stream obtenido a partir de recorrer desde el principio el stream original, comprobando para
 * cada uno de ellos si cumple el predicado o no. El primer elemento incluido en el stream es aquel que no cumpla el
 * predicado y a partir de ese momento se incluye el resto de elementos, independientemente de si cumplen el predicado
 * o no. Por ejemplo:
 * <pre>{@code
 * Stream.of(2, 4, 6, 8, 9, 10, 12)
 * .dropWhile(n -> n % 2 == 0)
 * .forEach(System.out::println);
 * }</pre>
 * muestra 9, 10 y 12, en este orden. Hasta que alguno no cumple con el predicado, entonces incorpora los demas elementos.
 * El metodo takeWhile() hace lo mismo pero al reves.
 *
 * <br><br>
 *
 * <h2>Map (transformacion)</h2>
 * En primer lugar tenemos una serie de metodos que permiten obtener un stream de un tipo primitivo a partir de uno que
 * no lo sea. Entre ellos tenemos mapToDouble(toDoubleFunction), que retorna un DoubleStream correspondiente de aplicar
 * cada elemento del stream original la funcion de conversion a double recibida.
 * <p>
 * Si queremos realizar la conversion inversa, es decir, pasar por ejemplo un IntStream a un Stream<Integer> podemos
 * aplicar sobre el IntStream el metodo boxed() que retorna un stream similar al anterior pero en el que el tipo de los
 * elementos correspondientes al tipo boxed del tipo primitivo correspondiente al stream original.
 * <p>
 * El metodo principal de transformacion es el metodo map(mapFunction), que retorna un nuevo stream obtenido a partir
 * de aplicar la funcion de transformacion mapFunction indicada a cada uno de los elementos del stream original. El
 * tipo del stream resultante correspondera al tipo de retorno de la funcion de transformacion, que puede ser distinto
 * al tipo del stream original. El stream resultante contendra tantos elementos como el stream original. Por ejemplo:
 * <pre>{@code
 * Stream.of(1, 2, 3)
 *      .map(n -> "Valor " + n)
 *      .forEach(System.out::println);
 * }</pre>
 * muestra Valor 1, Valor 2 y Valor 3, en este orden.
 * <p>
 * En algunas ocasiones, tenemos una funcion de transformacion que retorna un Stream. El problema de aplicar esta funcion
 * de transformacion con el metodo map(mapFunction) es que el stream resultante seria un Stream<Stream<Tipo>>. En estos
 * casos seria mas optimo obtener un unico Stream<Tipo> que contuviera concatenados todos los elementos de todos los
 * substreams. A este proceso se lo conoce como aplanado (flat). Si queremos que se realice el aplanado de substreams
 * deberemos usar el metodo flatMap(funcion_transformacion). Por ejemplo:
 */

public class Launcher {

    Random random = new Random();

    public Launcher() {
        // names.forEach(System.out::println); // Bucle implicito

        // List<String> names = new ArrayList<>(List.of("Manolo", "Pedro", "Rulo"));

        // 1. Funcion generadora del stream (crea un nuevo stream que tiene como fuente de datos la lista de nombres)
        // List<String> result = names.stream()
        // 2. 0 o mas operaciones intermedias
        //  .limit(1) // Limita los elementos de esta secuencia, truncados para que no superen la longitud maxima
        // .filter(name -> name.contains("P")) // filter es una operacion intermedia porque retorna un stream de String
        // 3. Operacion terminal
        // .forEach(System.out::println); // La operacion terminal consiste en consumir (imprimir por consola) cada uno de los elementos producidos por el paso anterior del stream

        // Las operaciones terminales se caracterizan por que NO retornan un stream, pero hay algunas que si lo hacen...
        // .collect(Collectors.toList()); // Esta operacion terminal recolecta los datos intermedios del stream en una lista
        /* Otra caracteristica importante de las operaciones terminales es que despues de ejecucion, el stream original
         * NO puede ser usado de nuevo. */
        // System.out.println(result);

        // Creacion de un stream a partir de una fuente de datos...
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
                 * que el stream funciona de manera perezosa por que se limita a 3 llamadas de la funcion de la interfaz
                 Supplier.
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
       /* List<Integer> result = "Rulo".chars()
                .boxed().collect(Collectors.toList());
        System.out.println(result); */

        // Map (transformacion)
        /* List<Integer> result = IntStream.range(0, 10)
                .boxed()
                .map(integer -> integer * 2)
                // .map(integer -> "Valor: " + integer * 2)
                .collect(Collectors.toList());
        System.out.println(result); */

        /* El flatMap solo se va a utilizar cuando el retorno de la lambda no sea de un tipo normal sino que sea un
         * Stream de un tipo. Esta funcion aplana ("recolecta") todo los streams que se han producido para cada uno de
         * los valores del stream original y los aplana en un unico elemento stream. Ese unico stream es el que retorna
         * el flatMap. */
        Stream.of(2, 4, 6)
                .flatMap(this::getRandomNumbers) // Ya no retorna un Stream<Stream<Tipo>>
                .forEach(System.out::println);
        // .forEach(integerStream -> integerStream.forEach(System.out::println));

    }

    public Stream<Integer> getRandomNumbers(Integer size) {
        return random.ints(size, 0, 10).boxed(); // Conversion inversa de IntStream a un Stream<Integer>
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
