package com.punkipunk.rxjava;

import io.reactivex.rxjava3.core.Observable;

/**
 * Crea un Observable usando fromCallable(). Este metodo delega la generacion de elementos justo antes de que llegue a
 * onNext() para que no arroje una excepcion. Y en su lugar, puede recibir una devolucion de llamada en su onError().
 */

public class FromCallable {

    public static void main(String[] args) {
        Observable<Integer> observable = Observable.fromCallable(() -> {
            System.out.println("Calling Method");
            return getNumber();
        });
        observable.subscribe(System.out::println,
                error -> System.out.println("An Exception Occurred" + error.getLocalizedMessage()));
    }

    /**
     * Este metodo devuelve una expresion que es un int.
     *
     * @return una expresion ficticia (int).
     */
    private static int getNumber() {
        System.out.println("Generating Value");
        return 1 / 0;
    }

}
