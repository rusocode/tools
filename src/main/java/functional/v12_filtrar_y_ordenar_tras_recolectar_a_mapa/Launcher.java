package functional.v12_filtrar_y_ordenar_tras_recolectar_a_mapa;

import functional.Book;
import functional.Genre;

import java.util.*;

import static java.util.stream.Collectors.*;

public class Launcher {

    public Launcher() {
        List<Book> books = Arrays.asList(
                new Book("345-89", "Sufriendo a Pedro", 2018, Genre.TERROR),
                new Book("923-45", "Los papeles por delante", 1998, Genre.THRILLER),
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY),
                new Book("923-46", "Los papeles por delante", 1998, Genre.THRILLER),
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY)
        );

        /* Recolecta los grupos de libros en base a su genero con el downstream especificado. Luego convierte esa lista
         * de map en un stream de set para poder filtrar los grupos con mas de 1 libro. Ahora recolecta transformando
         * en un mapa los libros. En otras palabras, se filtraron las entradas del mapa despues de haber recolectado, y
         * despues filtrar se volvio a recolectar pero hacia un mapa. */
        Map<Genre, Long> result = books.stream()
                .collect(groupingBy(Book::getGenre, counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                /* Ordena en orden inverso los valores del mapa utilizando el metodo estatico de Entry, pero la funcion
                 * toMap le da igual el orden de elementos, por lo tanto es necesario pasar como tercer y cuarto
                 * argumento al metodo toMap una lambda y la estructura de datos LinkedHashMap. El LinkedHashMap si
                 * preserva el orden del stream. */
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (entry1, entry2) -> entry1, LinkedHashMap::new));
        System.out.println(result);

    }

    public static void main(String[] args) {
        new Launcher();
    }

}
