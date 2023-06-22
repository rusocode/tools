package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Aqui estamos usando flatMap y flatMap con biFunction.
 */

public class FlatMap {

    public static void main(String[] args) {
        // exFlatMap();
        exFlatMapBiFunction();
    }

    /**
     * Esto usa flatMap para devolver los Observables deseados al flujo descendente en funcion de las emisiones
     * ascendentes que obtiene en la funcion.
     */
    private static void exFlatMap() {
        Observable<String> observable = Observable.just("foo", "bar", "jam");
        observable.flatMap((string) -> {
            if (string.equals("bar")) return Observable.empty();
            return Observable.fromArray(string.split(""));
        }).subscribe(System.out::println);
    }

    /**
     * Esto es similar al flatMap normal, excepto la biFuncion que fusiona la emision upstream con las emisiones del
     * flatMap que devuelven los Observables de la funcion.
     */
    private static void exFlatMapBiFunction() {
        Observable<String> observable = Observable.just("foo", "bar", "jam");
        observable.flatMap(string -> Observable.fromArray(string.split("")),
                        (actual, second) -> actual + " " + second)
                .subscribe(System.out::println);
    }

}
