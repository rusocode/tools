package com.punkipunk.functional.v12_recolectar_stream_y_aplicar_funcion;

import com.punkipunk.functional.Book;
import com.punkipunk.functional.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

        /* Crea un grupo de libros en base al año de publicacion y agrega a ese grupo los titulos de cada uno utilizando
         * el mapping. El metodo mapping primero realiza la transformacion y despues se recolecta. */
        /* Map<Integer, List<String>> result = books.stream()
                .collect(groupingBy(Book::getYearOfPublication, mapping(Book::getTitle, toList())));
        System.out.println(result); */

        /* Esta forma primero recolecta y despues se le aplica la funcion finalizadora. La diferencia con mapping es
         * que este primero transforma con la funcion que le indico y despues recolecta, y collectingAndThen recolecta
         * primero con el downstream que le indique y despues ejecuta la funcion de transformacion. */
        // String result = books.stream().collect(collectingAndThen(counting(), valor -> valor + " books"));

        // En el caso de querer especificar con cadenas (texto) la cantidad de libros de cada titulo...
        Map<Integer, String> result = books.stream()
                .collect(groupingBy(Book::getYearOfPublication, collectingAndThen(counting(), valor -> valor + " books")));
        System.out.println(result);
        // {2017=2 books, 2018=1 books, 1998=2 books}{2017=2 books, 2018=1 books, 1998=2 books}

    }

    public static void main(String[] args) {
        new Launcher();
    }

}
