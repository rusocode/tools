package com.punkipunk.rxjava;

import io.reactivex.rxjava3.core.Observable;

import static java.lang.Thread.sleep;

/**
 * Crea un Observable usando dos metodos de fabrica, es decir, Observable.empty() y Observable.never().
 */

public class EmptyNever {

    public static void main(String[] args) {
        // createObservableUsingEmpty();
        createObservableUsingNever();
    }

    /**
     * Crea un Observable usando el metodo de fabrica empty() que no emite ningun elemento a onNext() y solo se completa
     * de inmediato. Entonces, recibimos la devolucion de llamada en onComplete().
     */
    private static void createObservableUsingEmpty() {
        Observable observable = Observable.empty();
        observable.subscribe(System.out::println, System.out::println, () -> System.out.println("Completed"));
    }

    /**
     * Crea un Observable usando el metodo de fabrica never() que no emite ningun elemento y nunca se completa. Por lo
     * tanto, sus observadores siguen esperando hasta que se ejecuta el hilo. Observable.never() se utiliza
     * principalmente con fines de prueba.
     */
    private static void createObservableUsingNever() {
        Observable observable = Observable.never();
        observable.subscribe(System.out::println, System.out::println, () -> System.out.println("Completed"));
        // Pausa el hilo principal con la esperanza de que imprima algo
        pause(3000);
    }

    /**
     * Duerme el subproceso principal durante una duracion especifica.
     *
     * @param duration duracion del sueño en milisegundos.
     */
    private static void pause(int duration) {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
