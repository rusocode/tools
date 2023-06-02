package functional.v12_metodo_stream_de_optional;

import functional.Book;
import functional.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Launcher {

    public Launcher() {
        List<String> isbns = List.of("345-89", "978-25", "no");
        List<Book> result = isbns.stream()
                .map(this::findBookByIsbn)
                /* Crea un stream que contenga el valor que este presente en el Optional de esos tres streams de book y
                 * los convierte en un solo stream de book en el que incorpora cada uno de los elementos de esos tres
                 * streams de book. Esto permite de una manera mas corta realizar la misma operacion que el metodo
                 * .filter() y .map(). */
                .flatMap(Optional::stream)
                // Filtra los libros con valores presentes (es decir, filtra el Optional que no este vacio)
                // .filter(Optional::isPresent)
                // Transforma el stream de Optional a un stream de Book con los valores presentes
                // .map(Optional::get)
                .collect(Collectors.toList());
        System.out.println(result);
    }

    /**
     * Busca el primer libro con el isbn especificado.
     *
     * @param isbn isbn del libro.
     * @return un Optional con el Book que tiene el isbn recibido si es que existe o un Optional vacio si el isbn no
     * existe.
     */
    private Optional<Book> findBookByIsbn(String isbn) {
        List<Book> books = Arrays.asList(
                new Book("345-89", "Sufriendo a Pedro", 2018, Genre.TERROR),
                new Book("923-45", "Los papeles por delante", 1998, Genre.THRILLER),
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY),
                new Book("923-46", "Los papeles por delante", 1998, Genre.THRILLER), // Tiene los mismos datos pero son dos objetos diferentes
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY)
        );
        return books.stream().filter(book -> book.getISBN().equals(isbn)).findFirst();
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
