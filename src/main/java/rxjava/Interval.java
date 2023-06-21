package rxjava;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Crea un Observable usando interval(). Este metodo de fabrica es muy diferente en comparacion con los anteriores. Como
 * funciona en un hilo de calculo y emite en funcion de un intervalo especifico.
 */

public class Interval {

    public static void main(String[] args) {
        Observable observable = Observable.interval(1, TimeUnit.SECONDS);
        observable.subscribe(item -> System.out.println("Observer 1: " + item));
        pause(2000);
        observable.subscribe(item -> System.out.println("Observer 2: " + item));
        pause(3000);
    }

    private static void pause(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
