package com.punkipunk.rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

import java.util.Comparator;

/**
 * Utiliza el operador sorted para ordenar la emision en funcion de diferentes criterios.
 */

public class Sorted {

    public static void main(String[] args) {
        useSorted();
        useSortedWithOwnComparator();
        useSortedOnNonComparator();
    }

    /**
     * Utiliza el operador sorted para ordenar la emision.
     */
    private static void useSorted() {
        Observable.just(3, 5, 2, 4, 1)
                .sorted()
                .subscribe(System.out::println);
    }

    /**
     * Utiliza el operador sorted junto con la funcion reverseOrder para ordenar e invertir la emision.
     */
    private static void useSortedWithOwnComparator() {
        Observable.just(3, 5, 2, 4, 1)
                .sorted(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }

    /**
     * Utiliza el operador sorted junto con la funcion comparingInt para ordenar la emision en funcion de su longitud.
     */
    private static void useSortedOnNonComparator() {
        Observable.just("foo", "john", "bar")
                .sorted(Comparator.comparingInt(String::length)) // (first, second) -> Integer.compare(first.length(), second.length())
                .subscribe(System.out::println);
    }

}
