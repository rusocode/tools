package functional.v12_ordenacion_con_comparadores_avanzados;

import functional.Book;
import functional.Genre;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Launcher {

    public Launcher() {

        List<Book> books = Arrays.asList(
                new Book("345-89", "Sufriendo a Pedro", 2018, Genre.TERROR),
                new Book("923-45", "Los papeles por delante", 1998, Genre.THRILLER),
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY),
                new Book("923-46", "Los papeles por delante", 1998, Genre.THRILLER), // Tiene los mismos datos pero son dos objetos diferentes
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY)
        );

        books.stream()
                // Ordena los libros en base a la comparacion especificada del metodo compareTo en la clase Book (titulo)
                // .sorted(Comparator.naturalOrder())
                // .sorted(Comparator.reverseOrder()) // Ordena por titulo pero al revez

                // Para especificar el tipo de orden se utiliza el segundo parametro
                // .sorted(Comparator.comparing(Book::getTitle, Comparator.reverseOrder()))

                // Ordena los libros en base al año de publicacion usando el metodo comparingInt() de la interfaz funcional Comparator
                // .sorted(Comparator.comparingInt(Book::getYearOfPublication)) // (book1, book2) -> book1.getYearOfPublication() - book2.getYearOfPublication(): Esta forma no es recomendada ya que hay maneras mas simples de comparar

                // Pimero compara por nombre siguiendo el orden natural y si son igual, los compara por el año de publicacion siguiendo el orden natural
                // .sorted(Comparator.comparing(Book::getTitle).thenComparing(Book::getYearOfPublication /*, Comparator.reverseOrder()*/))
                // .forEach(System.out::println);

                // En caso que tengan el mismo titulo y año, los compara por isbn con thenComparing() que actua como un else
                /* .sorted(Comparator.comparing(Book::getTitle)
                        .thenComparing(Book::getYearOfPublication)
                        .thenComparing(Book::getISBN))
                .forEach(System.out::println); */

                // Ordena los libros y obtiene el primero despues del ordenamiento utilizando un Optional en caso de que no haya un primer libro
                /* .sorted(Comparator.comparing(Book::getTitle, Comparator.reverseOrder())
                        .thenComparing(Book::getYearOfPublication, Comparator.reverseOrder())
                        .thenComparing(Book::getISBN, Comparator.reverseOrder())
                )
                // .filter(book -> book.getYearOfPublication() >= 2020) // No hay libros mayores a 2020 por lo tanto retorna un Optional de String sin ningun valor presente
                .findFirst()
                // Si el Optional tiene un valor presente obtiene el titulo del libro
                .map(Book::getTitle)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No hay un primer libro")
                ); */

                /* Retorna un nuevo Optional de Book con la diferencia de que si se cumple el predicado del filtrado, el
                 * filter va a retornar un Optional con el mismo libro que tenia el Optional sobre el que se a ejecutado.
                 * En caso contrario, devuelve un Optional vacio. */
                .sorted(Comparator.comparing(Book::getTitle, Comparator.reverseOrder())
                        .thenComparing(Book::getYearOfPublication, Comparator.reverseOrder())
                        .thenComparing(Book::getISBN, Comparator.reverseOrder())
                )
                .filter(book -> book.getYearOfPublication() >= 2020)
                .findFirst()
                .filter(book -> book.getGenre() == Genre.COMEDY)
                .map(Book::getTitle)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No hay un primer libro")
                );

    }

    public static void main(String[] args) {
        new Launcher();
    }

}
