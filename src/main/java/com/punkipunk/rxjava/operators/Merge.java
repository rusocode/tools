package com.punkipunk.rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Este video muestra el trabajo de merge con Observables e iterables, tambien muestra el merge con matriz y el operador
 * mergeWith.
 */

public class Merge {

    public static void main(String[] args) {
        // exMerge();
        // exMergeArray();
        // exMergeIterable();
        // exMergeWith();
        exMergeInfinite();
    }

    /**
     * Utiliza la funcion estatica merge para fusionar Observables. Esta funcion puede tomar como maximo 4 Observables.
     */
    private static void exMerge() {
        Observable<Integer> oneToFive = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> sixToTen = Observable.just(6, 7, 8, 9, 10);

        Observable.merge(sixToTen, oneToFive).subscribe(System.out::println);
    }

    /**
     * Utiliza la funcion estatica mergeArray para fusionar Observables ilimitados, ya que requiere vararg.
     */
    private static void exMergeArray() {
        Observable<Integer> oneToFive = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> sixToTen = Observable.just(6, 7, 8, 9, 10);
        Observable<Integer> elevenToFifteen = Observable.just(11, 12, 13, 14, 15);
        Observable<Integer> sixteenToTwenty = Observable.just(16, 17, 18, 19, 20);
        Observable<Integer> twentyOneToTwentyFive = Observable.just(21, 22, 23, 24, 25);

        Observable.mergeArray(oneToFive, sixToTen, elevenToFifteen, sixteenToTwenty, twentyOneToTwentyFive)
                .subscribe(System.out::println);

    }

    /**
     * Utiliza la funcion estatica merge para fusionar una lista de Observables.
     */
    private static void exMergeIterable() {
        Observable<Integer> oneToFive = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> sixToTen = Observable.just(6, 7, 8, 9, 10);
        Observable<Integer> elevenToFifteen = Observable.just(11, 12, 13, 14, 15);
        Observable<Integer> sixteenToTwenty = Observable.just(16, 17, 18, 19, 20);
        Observable<Integer> twentyOneToTwentyFive = Observable.just(21, 22, 23, 24, 25);
        List<Observable<Integer>> observableList =
                Arrays.asList(oneToFive, sixToTen, elevenToFifteen, sixteenToTwenty, twentyOneToTwentyFive);

        Observable.merge(observableList).subscribe(System.out::println);

    }

    /**
     * Todos los Observables tienen la funcion mergeWith para fusionarlo facilmente con otro Observable. No podemos
     * fusionarnos con mas de un Observable en este caso.
     */
    private static void exMergeWith() {
        Observable<Integer> oneToFive = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> sixToTen = Observable.just(6, 7, 8, 9, 10);

        oneToFive.mergeWith(sixToTen).subscribe(System.out::println);
    }

    /**
     * Muestra una implementacion de la funcion merge con infinitos Observables. Como intervalo emite datos como tiempo
     * dado.
     */
    private static void exMergeInfinite() {
        Observable<String> infinite1 = Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .map(item -> "From infinite1: " + item);
        Observable<String> infinite2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(item -> "From infinite2: " + item);
        infinite1.mergeWith(infinite2).subscribe(System.out::println);
        pause(10000);
    }

    private static void pause(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
