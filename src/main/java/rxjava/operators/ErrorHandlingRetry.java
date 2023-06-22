package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ¿Alguna vez has pensado cual es la segunda cosa que hacemos si no podemos manejar el error? Si, lo volvemos a
 * intentar segun muchos factores y el tiempo especificado.
 */

public class ErrorHandlingRetry {

    public static void main(String[] args) {
        // retryWithPredicate();
        // exRetry();
        exRetryUntil();
    }

    /**
     * Este bloque de reintento intenta analizar el error y toma una decision basada en el error, ya sea para reintentar
     * o no segun nuestra logica dentro de ese bloque.
     */
    private static void retryWithPredicate() {
        Observable.error(new IOException("This is an example error"))
                .retry(error -> {
                    if (error instanceof IOException) {
                        System.out.println("retrying");
                        return true;
                    } else return false;
                })
                .subscribe(
                        System.out::println,
                        error -> System.out.println("Subscribed Error: " + error.getMessage()),
                        () -> System.out.println("Completed"));
    }

    /**
     * Este reintento toma el numero e intenta volver a intentar suscribirse y obtener los datos del observable
     * nuevamente.
     */
    private static void exRetry() {
        Observable.error(new Exception("This is an example error"))
                .retry(3)
                .subscribe(
                        System.out::println,
                        error -> System.out.println("Subscribed Error: " + error.getMessage()),
                        () -> System.out.println("Completed"));
    }

    /**
     * retryUntil depende del booleano que pasemos, sigue reintentando hasta que pasemos verdadero segun la logica.
     */
    private static void exRetryUntil() {
        AtomicInteger atomicInteger = new AtomicInteger();
        Observable.error(new Exception("This is an example error"))
                .doOnError(error -> {
                    System.out.println(atomicInteger.get());
                    atomicInteger.getAndIncrement();
                })
                .retryUntil(() -> {
                    System.out.println("Retrying");
                    return atomicInteger.get() >= 3;
                })
                .subscribe(
                        System.out::println,
                        error -> System.out.println("Subscribed Error: " + error.getMessage()),
                        () -> System.out.println("Completed"));
    }

}
