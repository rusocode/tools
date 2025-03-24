package com.punkipunk.rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Mostramos como el operador concat, que se parece mucho mas al operador merge, y el operador concatMap, que es como el
 * operador flatMap, son diferentes entre si.
 * <p>
 * El operador concatMap tambien aplica una funcion a cada elemento del flujo de entrada, pero en lugar de emitir los
 * resultados en cualquier orden, los emite en el mismo orden en que se recibieron. Esto significa que se mantiene el
 * orden relativo de los elementos originales. Sin embargo, si la funcion aplicada a cada elemento es costosa en
 * terminos de tiempo de procesamiento, puede retrasar la emision de los elementos subsiguientes, lo que puede afectar
 * el rendimiento genera.
 */

public class Concat {

    public static void main(String[] args) {
        // exConcat();
        // exConcatWith();
        exConcatMap();
    }

    private static void exConcat() {
        Observable<Integer> observable1 = Observable.just(1, 2, 3, 4, 5);
        Observable<Integer> observable2 = Observable.just(6, 7, 8, 9, 10);
        Observable.concat(observable1, observable2).subscribe(System.out::println);
    }

    /**
     * A diferencia del operador merge, concat espera a que termine el primer observador. Es decir que ejecuta el flujo
     * de manera ordenada.
     */
    private static void exConcatWith() {
        Observable<String> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .map(item -> "Observable1: " + item); // Imprime 5 items cada 1 segundo
        Observable<String> observable2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(item -> "Observable2: " + item); // Imprime items infinitos cada 300 ms
        observable1.concatWith(observable2).subscribe(System.out::println);
        pause(10000); // Pausa el hilo principal por 10s
    }

    /**
     * La principal diferencia entre flatMap y concatMap radica en como manejan el orden de emision de los eventos
     * resultantes.
     */
    private static void exConcatMap() {
        Observable.just("foo", "bar", "jam")
                .concatMap(item -> Observable.fromArray(item.split("")))
                .subscribe(System.out::println);
    }

    private static void pause(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
