package com.punkipunk.rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

import java.io.IOException;

/**
 * Aqui tenemos 4 operadores de manejo de errores para mostrar algunas tecnicas para manejar errores en RxJava.
 */

public class ErrorHandling {

    public static void main(String[] args) {
        exDoOnError();
        // exOnErrorResumeNext();
        // exOnErrorReturn();
        // exOnErrorReturnItem();
    }

    /**
     * El error va primero a doOnError en la cadena, por lo que podemos manejarlo.
     */
    private static void exDoOnError() {
        Observable.error(new Exception("This is an example error"))
                .doOnError(error -> System.out.println("Error: " + error.getMessage()))
                .subscribe(
                        System.out::println,
                        error -> System.out.println("Subscribed Error: " + error.getMessage()),
                        () -> System.out.println("Completed"));
    }

    /**
     * Cada vez que se encuentra un error en la cadena, va a onErrorResumeNext. Como esto toma otro Observable, el
     * suscriptor cambia a ese Observable para omitir el error.
     */
    private static void exOnErrorResumeNext() {
        Observable.error(new Exception("This is an example error"))
                // .onErrorResumeNext(Observable.just(1, 2, 3, 4, 5))
                .subscribe(
                        System.out::println,
                        error -> System.out.println("Subscribed Error: " + error.getMessage()),
                        () -> System.out.println("Completed"));
    }

    /**
     * Podemos devolver valores especificos basados en el tipo de error. Como cuando recibimos el error, va a
     * onErrorReturn.
     */
    private static void exOnErrorReturn() {
        Observable.error(new Exception("This is an example error"))
                .onErrorReturn(error -> {
                    if (error instanceof IOException) return 0;
                    else return 1; // throw new Exception("This is an exception");
                })
                .subscribe(
                        System.out::println,
                        error -> System.out.println("Subscribed Error: " + error.getMessage()), // En caso de que lance un error
                        () -> System.out.println("Completed")); // En caso de que la cadena se complete sin errores
    }

    /**
     * Podemos pasar una alternativa para el suscriptor debajo de la cadena. Cada vez que encuentra un error, le da esa
     * alternativa especifica.
     */
    private static void exOnErrorReturnItem() {
        Observable.error(new IOException("This is an example error"))
                .onErrorReturnItem(1)
                .subscribe(
                        System.out::println,
                        error -> System.out.println("Subscribed Error: " + error.getMessage()),
                        () -> System.out.println("Completed"));
    }

}
