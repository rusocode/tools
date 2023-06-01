package functional.v12_collect;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h2>Collect (reduccion mutable)</h2>
 * La operacion de recoleccion (collect) es una operacion terminal que permite crear una estructura de
 * datos con los resultados del procesamiento de datos asociado a un stream. La operacion de
 * recoleccion tambien recibe el nombre de operacion de reduccion mutable. Para llevarla a cabo
 * usaremos el metodo collect() de Stream al que deberemos pasarle un objeto de una clase que
 * implemente la interfaz Collector.
 * <p>
 * Aunque podemos crear nuestras propias clases que implementen dicha interfaz, en la mayoria de las
 * ocasiones podremos utilizar alguno de los recolectores estandar proporcionados por Java a traves de
 * la clase auxiliar Collectors, que contiene metodos estaticos que retornan objetos Collector
 * correspondientes a los recolectores mas habituales. Todos estos metodos estan diseñados para
 * funcionar de manera optima incluso con streams paralelos. Estudiemos los mas habituales.
 * <p>
 * Para los ejemplos que veremos a continuacion supongamos que tenemos la siguiente lista de libros:
 * <pre>{@code
 * List<Book> books = Arrays.asList(
 *      new Book("345-89", "Sufriendo a Pedro", 2018, Genre.TERROR),
 *      new Book("923-45", "Los papeles por delante", 1998, Genre.THRILLER),
 *      new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY),
 *      new Book("923-45", "Los papeles por delante", 1998, Genre.THRILLER),
 *      new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY)
 * );
 * }</pre>
 *
 * <br>
 *
 * <h3>Recolectores a estructuras de datos clasicas</h3>
 * En primer lugar tendremos un conjunto de metodos estaticos de la clase Collectors que nos permiten recolectar los
 * elementos de un stream y almacenarlos en una estructura de datos. Asi tendremos:
 * <p>
 * {@code toList()}: Retorna una lista con los elementos del stream.
 * <p>
 * {@code toUnmodifiableList()}: Retorna una lista inmutable de los elementos del stream en el orden en que son
 * producidos (encounter order).
 * <pre>{@code
 * List<Book> twentyCenturyBooks = books.stream()
 *      .filter(book -> book.getYearOfPublication() < 2000)
 *      .collect(Collectors.toList());
 * }</pre>
 * {@code toSet()}: Retorna un set (conjunto) con los elementos del stream. Si se hay elementos duplicados en el stream,
 * son ignorados.
 * <p>
 * {@code toCollection(collectionSupplier)}: El problema de toList() y toSet() es que no podemos especificar la
 * implementacion concreta que queremos que se use. Por ejemplo, no podemos indicar que se use un LinkedList, en el caso
 * de toList(), o un TreeSet, en el caso de toSet(). Para solucionar este problema, la clase Collectors posee un metodo
 * toCollection(collectionSupplier), que recibe un supplier que retorna la estructura de datos concreta en la que
 * queremos que se recolecte el stream.
 * <pre>{@code
 * Set<Book> twentyCenturyBooks = books.stream()
 *      .filter(book -> book.getYearOfPublication() < 2000)
 *      .collect(Collectors.toCollection(TreeSets::new));
 * }</pre>
 * {@code toMap(keyMapperFunction, valueMapperFunction)}: Retorna un map obtenido a partir de aplicar cada elemento del
 * stream por un lado la funcion de obtencion de la clave keyMapperFunction, de manera que su valor de retorno sera
 * usado como clave del elemento en el mapa resultante, y por otro lado la funcion de obtencion de valor
 * valueMapperFunction, de manera que su valor de retorno sera usado como valor del elemento en el mapa resultante. Se
 * espera que la funcion keyMapperFunction no pueda retornar el mismo valor para dos elementos distintos del stream, ya
 * que el mapa resultante no puede tener claves repetidas, por lo que se lanzara una excepcion IllegalStateException en
 * ese caso.
 * <p>
 * Una excepcion importante es la recoleccion de los elementos de un stream hacia un array, que no se realizara a traves
 * de ningln recolector ni del metodo collect(), sino directamente a traves del metodo toArray() de la clase Stream, que
 * retorna un Object[], es decir un array de elementos de la clase Object, debido a que los arrays no usan genericos.
 */

public class Launcher {

    public Launcher() {

        // El metodo asList() devuelve una lista de tamaño fijo respaldada por la matriz especificada
        List<Book> books = Arrays.asList(
                new Book("345-89", "Sufriendo a Pedro", 2018, Genre.TERROR),
                new Book("923-45", "Los papeles por delante", 1998, Genre.THRILLER),
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY),
                new Book("923-45", "Los papeles por delante", 1998, Genre.THRILLER), // Tiene los mismos datos pero son dos objetos diferentes
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY)
        );

        /* Filtra los libros menores al año de publicacion 2000 y los recolecta en una lista de tipo generica, en este
         * caso de tipo Book. El metodo collect recibe por parametro la lista de libros utilizando el metodo estatico de
         * la clase de utilidad Collectors toList() que retorna una lista con los elementos del stream. Set no permite
         * elementos repetidos. */
        /* Set<Book> result = books.stream() // Convierte la lista de libros en un Stream
                .filter(book -> book.getYearOfPublication() < 2000)

                // .collect(Collectors.toUnmodifiableList()); // o toList(), cambiar el tipo de Book a List

                // .collect(Collectors.toUnmodifiableSet()); // toSet()

                .collect(Collectors.toCollection(TreeSet::new)); // Recolecta hacia un TreeSet
        System.out.println(result); */

        // Untiliza un Map para recolectar los libros utilizando el ISBN como clave y el propio libro como valor
        /* Map<String, Book> result = books.stream() // Convierte la lista de libros en un Stream
                .filter(book -> book.getYearOfPublication() < 2000)
                .distinct() // Ignora los libros con el ISBN duplicado parae evitar un IllegalStateException en la coleccion de tipo Map

                //.collect(Collectors.toMap(Book::getISBN, Function.identity())); // El metodo identity reemplaza la lambda que recibe y retorna una funcion que siempre devuelve su argumento de entrada: book -> book

                // Como segunda alternativa al metodo distinct() es utilizar un operador binario como tercer argumento para el metodo toMap()
                .collect(Collectors.toMap(Book::getISBN, Function.identity(),
                        (book1, book2) -> new Book(book1.getISBN(), book1.getTitle() + " + " + book2.getTitle(), book1.getYearOfPublication(), book1.getGenre()), TreeMap::new));
        System.out.println(result); */

        // Crea un array de Book utilizando el metodo toArray()
        /* Book[] result = books.stream()
                .filter(book -> book.getYearOfPublication() > 2000)
                .distinct()
                .toArray(Book[]::new);
        Arrays.stream(result).forEach(System.out::println); */

        /* String result = books.stream()
                .distinct()
                // Transforma el stream de Book en un stream de String
                .map(Book::getTitle)
                // Recolecta hacia una cadena con el metodo joining()
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(result); */

        /* String result = books.stream()
                .distinct()
                // La funcion mapping une las dos instrucciones .map y .collect en una linea
                .collect(Collectors.mapping(Book::getTitle, Collectors.joining(", ", "[", "]")));
        System.out.println(result); */

        // Este recolector agrupa los elementos dependiendo de la clave especificada (año de publicacion) en una lista de Book
        /* Map<Integer, List<Book>> result = books.stream()
                .collect(Collectors.groupingBy(book -> book.getYearOfPublication()));
        System.out.println(result); */

        // Crea los grupos en base al año de publicacion y luego a cada elemento del grupo hace un mapeo obteniendo solo el titulo
        /* Map<Integer, String> result = books.stream()
                .collect(Collectors.groupingBy(Book::getYearOfPublication, TreeMap::new, Collectors.mapping(Book::getTitle, Collectors.joining(", "))));
        System.out.println(result); */

        // Recolector de reduccion con la funcion de conteo para la cantidad de libros agrupados en cada año
        /* Map<Integer, Long> result = books.stream()
                .collect(Collectors.groupingBy(Book::getYearOfPublication, Collectors.counting()));
        System.out.println(result); */

        // El metodo counting() se puede utilizar como un metodo de recoleccion simple que devuelve sola la cantidad
        /* Long result = books.stream()
                .filter(book -> book.getYearOfPublication() > 2000)
                .count(); // collect(Collectors.counting())
        System.out.println(result); */

        // Suma los años para cada genero (agrupacion)
        /* Map<Genre, Integer> result = books.stream()
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.summingInt(Book::getYearOfPublication)));
        System.out.println(result); */

        // Devuelve un Optional como valor ya que el metodo minBy puede llegar a devolver una lista vacia
        /* Map<Genre, Optional<Book>> result = books.stream()
                .collect(Collectors.groupingBy(Book::getGenre,
                        Collectors.minBy(Comparator.comparing(Book::getYearOfPublication))));
        System.out.println(result); */

        // Obtiene un resumen de los libros (min, max, suma, count)
        /* Map<Genre, IntSummaryStatistics> result = books.stream()
                .collect(Collectors.groupingBy(Book::getGenre,
                        Collectors.summarizingInt(Book::getYearOfPublication)));
        System.out.println(result); */

        /* Divide el stream en grupos dependiendo de la condicion especificada en el predicate. El metodo partitioningBy()
         * tiene una segunda firma que recibe un downstreamCollector de reduccion. */
        /* Map<Boolean, Long> result = books.stream()
                // .collect(Collectors.partitioningBy(book -> book.getYearOfPublication() < 2000));

                .collect(Collectors.partitioningBy(book -> book.getYearOfPublication() < 2000, Collectors.counting()));
        System.out.println(result); */

        // Recoleccion de un stream con filtrado
        // Map<Genre, Long> result = books.stream()
        // Agrupa los libros dependiendo del genero que son mayores al año de publicacion 2000
        // .filter(book -> book.getYearOfPublication() >= 2000)
        // .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));
        /* Para ahorrar el metodo filter(), la funcion groupingBy() acepta como segundo parametro un Predicado
         * para filtrar los libros usando el metodo filtering(). Ademas esta segunda opcion de la agrupacion
         * filtrada, incluye los libros que no cumplen con la condicion (le asigna 0). Esto se debe porque
         * primero se agrupa y despues se filtran. */
        //  .collect(Collectors.groupingBy(Book::getGenre,
        // Collectors.filtering(book -> book.getYearOfPublication() >= 2000, Collectors.counting())));
        // System.out.println(result);

        // Esta forma reemplaza el metodo filter() y count() separados de la clase Stream
        Long result = books.stream().collect(Collectors.filtering(book -> book.getYearOfPublication() >= 2000, Collectors.counting()));
        System.out.println(result);

    }

    public static void main(String[] args) {
        new Launcher();
    }

}
