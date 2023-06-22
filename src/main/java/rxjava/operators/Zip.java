package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

/**
 * Aqui estamos usando el operador zip, zipWith y zipIterable para fusionar la emision en la funcion del Zipper.
 */

public class Zip {

    public static void main(String[] args) {
        exZip();
        exZipWith();
        exZipIterable();
    }

    /**
     * Utiliza el operador Zip para obtener el flujo en la funcion del Zipper.
     */
    private static void exZip() {
        Observable<Integer> oneToFive = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> sixToTen = Observable.range(6, 5);
        Observable<Integer> elevenToFifteen = Observable.fromIterable(Arrays.asList(11, 12, 13, 14, 15));

        Observable.zip(oneToFive, sixToTen, elevenToFifteen, (a, b, c) -> a + b + c)
                .subscribe(System.out::println);

    }

    /**
     * Utiliza el operador ZipWith en el Observable para comprimir facilmente un Observable con otro.
     */
    private static void exZipWith() {
        Observable<Integer> oneToFive = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> sixToTen = Observable.fromIterable(Arrays.asList(6, 7, 8, 9, 10));

        oneToFive.zipWith(sixToTen, Integer::sum)
                .subscribe(System.out::println);
    }

    /**
     * Utiliza el operador zip que toma la Lista de Observables y proporciona la emision comprimida en una matriz.
     */
    private static void exZipIterable() {
        Observable<Integer> oneToFive = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> sixToTen = Observable.just(6, 7, 8, 9, 10);
        Observable<Integer> elevenToFifteen = Observable.just(11, 12, 13, 14, 15);

        List<Observable<Integer>> observables = Arrays.asList(
                oneToFive, sixToTen, elevenToFifteen
        );

        Observable.zip(observables, Arrays::toString, true, 1)
                .subscribe(System.out::println);
    }

}
