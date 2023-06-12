package rx;

import io.reactivex.rxjava3.core.Observable;

/**
 * Crea un Observable usando Observable.range().
 * <p>
 * Tambien es posible crear un Observable usando defer(). Este metodo delega la generacion de elementos justo antes de
 * que llegue a onNext() para que todos los observadores obtengan una nueva instancia y se mantengan actualizados con
 * el nuevo valor.
 * <p>
 * El metodo defer() puede consumir un ObservableSource completo, me refiero a otro Observable (que puede tener su
 * propio estado) y fromCallable() consume un solo valor (generico <T>).
 * <p>
 * Y tambien fromCallable() pasa el error al Observable, para que pueda manejar el error en su bloque onError con gracia.
 * Pero en el caso de defer (diferir), no se ocupa del error, debe manejar la excepcion usted mismo.
 */

public class ReactiveLoop {

    private static final int start = 5;
    private static int count = 2;

    public static void main(String[] args) {
        // Observable<Integer> observable = Observable.range(0, 10);
        // observable.subscribe(System.out::println);

        // Imprime desde start hasta (start + count - 1)
        /* Observable<Integer> observable = Observable.range(start, count);
        observable.subscribe(System.out::println); */

        // Defer
        Observable<Integer> observable = Observable.defer(() -> {
            System.out.println("New Observable is created with start = " + start + " and count = " + count);
            return Observable.range(start, count);
        });
        observable.subscribe(item -> System.out.println("Observer 1: " + item));
        count = 3;
        observable.subscribe(item -> System.out.println("Observer 2: " + item));
    }

}
