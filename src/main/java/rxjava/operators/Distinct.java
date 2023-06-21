package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Aqui hemos usado los distinct y los distinctUntilChanged y sus dos sobrecargas.
 */

public class Distinct {

    public static void main(String[] args) {
        // distinctOperator();
        // distinctWithKeySelector();
        // distinctUntilChangedOperator();
        distinctUntilChangedWithKeySelector();
    }

    /**
     * Obtiene la emision unica.
     */
    private static void distinctOperator() {
        Observable.just(1, 1, 2, 2, 3, 3, 4, 5, 1, 2)
                .distinct()
                .subscribe(System.out::println);
    }

    /**
     * Obtiene la emision basado en la propiedad del elemento para distinguir los elementos duplicados.
     */
    private static void distinctWithKeySelector() {
        Observable.just("foo", "fool", "super", "foss", "foil")
                .distinct(String::length)
                .subscribe(System.out::println);
    }

    /**
     * Evita elementos duplicados consecutivos uno tras otro.
     */
    private static void distinctUntilChangedOperator() {
        Observable.just(1, 1, 2, 2, 3, 3, 4, 5, 1, 2)
                .distinctUntilChanged()
                .subscribe(System.out::println);
    }

    /**
     * Obtiene la emision basado en la propiedad del elemento para distinguir los elementos duplicados consecutivos.
     */
    private static void distinctUntilChangedWithKeySelector() {
        Observable.just("foo", "fool", "super", "foss", "foil")
                .distinctUntilChanged(String::length)
                .subscribe(System.out::println);
    }

}
