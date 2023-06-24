package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Aqui estamos usando flatMap y flatMap con biFunction.
 * <p>
 * El operador flatMap aplica una funcion a cada elemento del flujo de entrada y luego combina los resultados en un solo 
 * flujo de salida. La funcion que se aplica a cada elemento puede retornar cualquier numero de elementos (incluso cero
 * o mas), y estos elementos se emiten en cualquier orden. Esto significa que los elementos resultantes pueden
 * intercalarse en el flujo de salida en funcion del tiempo que toma procesar cada elemento. En otras palabras, no se 
 * mantiene el orden relativo de los elementos originales.
 */

public class FlatMap {

    public static void main(String[] args) {
        exFlatMap();
        // exFlatMapBiFunction();
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
