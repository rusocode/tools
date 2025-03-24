package com.punkipunk.rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Aqui mostramos la diferencia entre los operadores map y flatMap.
 */

public class MapVsFlatMap {

    public static void main(String[] args) {
        // exMap();
        // exFlatMap();
        exMapWithObservable();
    }

    /**
     * Utiliza el operador map para cambiar la emision intermedia antes de que llegue a su suscriptor.
     */
    private static void exMap() {
        Observable<String> observable = Observable.just("foo", "bard", "james");
        observable
                .map(item -> item.toUpperCase())
                .subscribe(System.out::println);
    }

    /**
     * Utiliza flatMap para fusionar los observables devueltos en funcion de la emision.
     */
    private static void exFlatMap() {
        Observable<String> observable = Observable.just("foo", "bar", "jam");
        observable.flatMap(item -> Observable.fromArray(item.split("")))
                .subscribe(System.out::println);
    }

    /**
     * Utiliza el operador map para convertir la secuencia intermedia en Observables y, por razones obvias, los
     * suscriptores obtienen el objeto Observable en su lugar.
     */
    private static void exMapWithObservable() {
        Observable<String> observable = Observable.just("foo", "bar", "jam");
        observable.map(item -> Observable.fromArray(item.split(""))).subscribe(System.out::println);
    }

}
