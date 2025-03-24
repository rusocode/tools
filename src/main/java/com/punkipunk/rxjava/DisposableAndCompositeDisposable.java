package com.punkipunk.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.ResourceObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * ¿Sabes que cuando se subscribe a un Observable, se crea automaticamente una transmision para que pueda manejar la
 * emision? Y esto puede causar fugas de memoria y consumir los recursos de nuestro sistema. Es por eso que puede ver
 * una advertencia en IntelliJ cada vez que se suscribe, diciendo "el resultado de subscribe() ha sido ignorado".
 * Nuestro trabajo es manejar con gracia esas suscripciones cuando hayamos terminado.
 */

public class DisposableAndCompositeDisposable {

    public static void main(String[] args) {
        // handleDisposable();
        // handleDisposableInObserver();
        // handleDisposableOutsideObserver();
        compositeDisposable();
    }

    /**
     * Guarda el disposable devuelto de subscribe(), y lo desecha despues de 3000 milisegundos y pausa el hilo por 3000
     * milisegundos mas para verificar si emite o no.
     */
    private static void handleDisposable() {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
        Disposable disposable = observable.subscribe(System.out::println);
        pause(3000);
        disposable.dispose();
        pause(3000);
    }

    /**
     * Cuando pasamos un Observer al metodo subscribe(), devuelve void. Por lo tanto, debemos obtener el Desechable del
     * metodo anulado onSubscribe, para que podamos manejarlo en cualquier lugar y en cualquier momento.
     */
    private static void handleDisposableInObserver() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        Observer<Integer> observer = new Observer<>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                if (integer == 3) disposable.dispose();
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        };
        observable.subscribe(observer);
    }

    /**
     * Utiliza ResourceObserver para desechar fuera de la suscripcion con el metodo subscribeWith(). Este devuelve el
     * Observer que pasamos cuando ResourceObserver implementa Disposable, por lo que podemos tratarlo como si fuera un
     * Disposable.
     */
    private static void handleDisposableOutsideObserver() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        ResourceObserver<Integer> observer = new ResourceObserver<>() {

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Disposable d = observable.subscribeWith(observer);
    }

    /**
     * Usa CompositeDisposable y su metodo add para agregar Disposables al conjunto de Disposables llamando a dispose en
     * CompositeDisposable en lugar de desechar a todos y cada uno. Incluso podemos usar el metodo delete para eliminar
     * cualquier disposable del conjunto de CompositeDisposable.
     */
    private static void compositeDisposable() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
        Disposable disposable1 = observable.subscribe(item -> System.out.println("Observer 1: " + item));
        Disposable disposable2 = observable.subscribe(item -> System.out.println("Observer 2: " + item));
        compositeDisposable.addAll(disposable1, disposable2);
        pause(3000);
        compositeDisposable.delete(disposable1);
        compositeDisposable.dispose();
        pause(3000);
    }

    private static void pause(int duration) {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
