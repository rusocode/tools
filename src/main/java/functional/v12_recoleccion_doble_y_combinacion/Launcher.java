package functional.v12_recoleccion_doble_y_combinacion;

import functional.Book;
import functional.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

public class Launcher {

    public Launcher() {
        List<Book> books = Arrays.asList(
                new Book("345-89", "Sufriendo a Pedro", 2018, Genre.TERROR),
                new Book("923-45", "Los papeles por delante", 1998, Genre.THRILLER),
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY),
                new Book("923-46", "Los papeles por delante", 1998, Genre.THRILLER), // Tiene los mismos datos pero son dos objetos diferentes
                new Book("978-25", "La vida de Baldomero", 2017, Genre.COMEDY)
        );

        /* Optional<Integer> max = books.stream()
                .map(Book::getYearOfPublication)
                .collect(maxBy(Integer::compare));
        Optional<Integer> min = books.stream()
                .map(Book::getYearOfPublication)
                .collect(minBy(Integer::compare));
        if (max.isPresent() && min.isPresent())
            System.out.println("Diferencia en años desde el primer y ultimo libro publicado: " + (max.get() - min.get()));
        else System.out.println("No hay elementos en la lista!"); */

        // A partir de Java 12 se incluyo el metodo teeing() para simplificar la funcion anterior
        books.stream()
                .map(Book::getYearOfPublication)
                // En caso de que la lista este vacia, entonces se devuelve un Optinal vacio
                .filter(year -> year >= 2020)
                .collect(
                        // Este recolector permite combinar los valores que se recolectaron con los dos recolectores anteriores
                        teeing(
                                maxBy(Integer::compare),
                                minBy(Integer::compare),
                                (maxOptional, minOptional) -> maxOptional.map(max -> max - minOptional.get())
                        )
                )
                .ifPresentOrElse(
                        (diferencia -> System.out.println("Diferencia: " + diferencia)),
                        () -> System.out.println("No hay elementos en la lista")
                );

    }

    public static void main(String[] args) {
        new Launcher();
    }

}
