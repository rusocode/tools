package com.punkipunk.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * En programacion reactiva, un <b><i>Observer</i></b> es un componente que se suscribe a un Observable para recibir
 * notificaciones de eventos o valores emitidos por el Observable. El Observer representa el "observador" en el patron
 * de diseño "observador-observable" o "publicador-suscriptor".
 * <p>
 * Cuando un Observer se suscribe a un Observable, establece una conexion entre ambos. A medida que el Observable emite
 * elementos, el Observer recibe las notificaciones y puede reaccionar a ellas en consecuencia. El Observer define una
 * serie de metodos que pueden implementarse para manejar diferentes tipos de eventos:
 * <ol>
 * <li>onNext: Este metodo se invoca cuando el Observable emite un nuevo elemento. El Observer puede manejar este evento
 * realizando alguna accion en funcion del valor recibido.
 * <li>onError: Si el Observable encuentra un error durante su ejecucion, notifica al Observer llamando a este metodo.
 * Aqui, el Observer puede manejar el error de acuerdo con la logica de la aplicacion.
 * <li>onComplete: Cuando el Observable completa su secuencia de emision de elementos, se llama a este metodo en el
 * Observer. El Observer puede llevar a cabo alguna accion de limpieza o finalizacion en este punto.
 * </ol>
 * El Observer puede suscribirse y cancelar la suscripcion a un Observable en cualquier momento. Al cancelar la
 * suscripcion, se interrumpe la conexion entre el Observer y el Observable, y el Observer ya no recibira mas eventos.
 * <p>
 * La programacion reactiva fomenta el uso de Observers para manejar y procesar flujos de eventos asincronos de manera
 * declarativa y eficiente. Permite una forma mas sencilla de trabajar con flujos de datos y eventos en comparacion con
 * enfoques tradicionales basados en callbacks o manejo explicito de hilos.
 */

public class Observer {

    public static void main(String[] args) {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);

        io.reactivex.rxjava3.core.Observer<Integer> observer = new io.reactivex.rxjava3.core.Observer<>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        };

        observable.subscribe(observer);
    }
}
