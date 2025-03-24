package com.punkipunk.rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * El operador Delay es muy util para retrasar la emision de cualquier observable a sus suscriptores.
 */

public class Delay {

    public static void main(String[] args) {
        // delay();
        delayError();
    }

    /**
     * Se uso el operador 'delay' para agregar un retraso antes de la emision de inicio Observable.
     * Nota: 'delay' no retrasa cada emision, sino que retrasa el inicio de la emision.
     */
    private static void delay() {
        Observable.just(1, 2, 3, 4, 5)
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);
        pause(5000);
    }

    /**
     * El operador 'delay' no agrega ningun retraso antes de emitir un error. Esto significa que el error se emite
     * inmediatamente a sus suscriptores de forma predeterminada. Para retrasar la emision del error necesitamos pasar
     * el parametro delayError como true.
     */
    private static void delayError() {
        Observable.error(new Exception("Error"))
                .delay(3, TimeUnit.SECONDS, true)
                .subscribe(System.out::println,
                        error -> System.out.println(error.getLocalizedMessage()),
                        () -> System.out.println("Completed"));
        pause(5000);
    }

    private static void pause(int duration) {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
