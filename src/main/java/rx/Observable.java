package rx;

import java.util.Arrays;
import java.util.List;

/**
 * En programacion reactiva, un <b><i>Observable</i></b> es un concepto fundamental que representa una secuencia de
 * eventos o valores que pueden ser observados. Es parte de la biblioteca de programacion reactiva, como RxJava, RxJS,
 * Reactor, entre otros.
 * <p>
 * Un Observable emite elementos de manera asincrona a medida que ocurren, y otros componentes pueden suscribirse a el
 * para recibir estos elementos y reaccionar en consecuencia. Los elementos emitidos pueden ser cualquier tipo de datos,
 * como numeros, objetos, eventos, etc.
 * <p>
 * El patron de diseño Observable sigue el patron de diseño de "observador-observable" (tambien conocido como patron de
 * diseño de "publicador-suscriptor"). Esto significa que los componentes que estan interesados en los eventos emitidos
 * por el Observable se suscriben a el y reciben notificaciones cada vez que se emite un nuevo elemento.
 * <p>
 * El flujo de eventos en un Observable puede tener las siguientes caracteristicas:
 * <ol>
 * <li>Emision de elementos: El Observable emite elementos uno tras otro en orden.
 * <li>Emision de error: El Observable puede emitir un error en cualquier momento, interrumpiendo el flujo normal de
 * eventos.
 * <li>Finalizacion: El Observable puede finalizar su secuencia de emision cuando ha completado su trabajo.
 * </ol>
 * La programacion reactiva se basa en la composicion de operadores que permiten transformar, combinar y manipular los
 * datos emitidos por los Observables de manera declarativa y eficiente. Esto facilita el manejo de flujos de datos
 * asincronos y complejos, permitiendo una programacion mas reactiva y facil de mantener.
 * <p>
 * En este ejemplo se usan tres maneras muy comunes e importantes de crear Observables.
 */

public class Observable {

    public static void main(String[] args) {
        // createObservableWithJust();
        // createObservableFromIterable();
        createObservableUsingCreate();
    }

    /**
     * Crea un entero observable usando el metodo estatico de Observable just(). Este metodo convierte los elementos
     * proporcionados en un Observable. Asi mas tarde podemos suscribirnos y obtener los elementos uno por uno para
     * tomar medidas.
     */
    private static void createObservableWithJust() {
        io.reactivex.rxjava3.core.Observable<Integer> observable = io.reactivex.rxjava3.core.Observable.just(1, 2, 3, 4, 5);
        observable.subscribe(System.out::println);
    }

    /**
     * Crea un entero observable usando el metodo estatico de Observable fromIterable(). Este metodo convierte cualquier
     * cosa que extienda o implemente iterable, en un observable. Asi mas tarde podemos suscribirnos y obtener los
     * elementos uno por uno para tomar medidas.
     */
    private static void createObservableFromIterable() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        io.reactivex.rxjava3.core.Observable<Integer> observable = io.reactivex.rxjava3.core.Observable.fromIterable(list);
        observable.subscribe(System.out::println);
    }

    /**
     * Crea un entero observable usando el metodo estatico create() de Observable. El emisor aqui es una interfaz. Al
     * llamar a su metodo onNext(), onComplete() y onError(), podemos enviar eventos a nuestros suscriptores. Para que
     * nuestros suscriptores puedan suscribirse y obtener el articulo uno por uno para tomar medidas.
     */
    private static void createObservableUsingCreate() {
        io.reactivex.rxjava3.core.Observable<Integer> observable = io.reactivex.rxjava3.core.Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onNext(4);
            emitter.onNext(5);
            emitter.onNext(null);
            emitter.onComplete();
        });
        observable.subscribe(System.out::println,
                error -> System.out.println("There was error: " + error.getLocalizedMessage()),
                () -> System.out.println("Completed"));
    }
}
