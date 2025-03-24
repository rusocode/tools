package com.punkipunk.rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Aqui hemos usado el operador take y skip y algunas variantes de ellos para mostrar como funcionan y como skip no es
 * mas que lo contrario de take.
 */

public class SuppressingOperators {

    public static void main(String[] args) {
        // takeOperator();
        // takeWithInterval();
        // takeWhileOperator();
        // skipOperator();
        skipWhileOperator();
    }

    /**
     * Emite solo los primeros 2 elementos y luego se completa.
     */
    private static void takeOperator() {
        Observable.just(1, 2, 3, 4, 5)
                .take(2)
                .subscribe(System.out::println, System.out::println, () -> System.out.println("Completed"));
    }

    /**
     * Emite elementos solo para el intervalo de tiempo especificado.
     */
    private static void takeWithInterval() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .subscribe(System.out::println, System.out::println, () -> System.out.println("Completed"));
        pause(5000);
    }

    /**
     * Este takeWhile() es como una combinacion de filter y take, la unica diferencia es que el filter pasa por todos
     * los elementos para verificar si la logica es verdadera, mientras que takeWhile() sigue emitiendo solo parte de la
     * logica verdadera y se completa una vez que obtiene la logica como falsa.
     */
    private static void takeWhileOperator() {
        Observable.just(1, 2, 3, 4, 5, 1, 2, 3, 4, 5)
                .takeWhile(item -> item <= 3)
                .subscribe(System.out::println);
    }

    /**
     * Salta los primeros valores y emite los restantes.
     */
    private static void skipOperator() {
        Observable.just(1, 2, 3, 4, 5)
                .skip(2)
                .subscribe(System.out::println);
    }

    /**
     * skipWhile() es pequeño, es como una combinacion de filter y skip, la unica diferencia es que filter pasa por
     * todos los elementos para verificar si la logica es verdadera, mientras que skipWhile() sigue saltando elementos
     * solo si alguna logica es verdadera y una vez que la logica es falsa, emite los elementos restantes sin comprobar.
     */
    private static void skipWhileOperator() {
        Observable.just(1, 2, 3, 4, 5, 1, 2, 3, 4, 5)
                .skipWhile(item -> item <= 3)
                .subscribe(System.out::println);
    }

    private static void pause(int duration) {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
