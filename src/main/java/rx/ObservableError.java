package rx;

import io.reactivex.rxjava3.core.Observable;

/**
 * Crea un Observable usando Observable.error().
 * <p>
 * Este metodo de fabrica solo proporciona una excepcion a sus Observadores a traves de onError(). Esto es especialmente
 * util si no queremos lanzar una excepcion directamente, para que podamos manejarlo con gracia en onError().
 */

public class ObservableError {

    public static void main(String[] args) {
        // throwException();
        throwExceptionUsingCallable();
    }

    /**
     * Este metodo usa Observable.error() para pasar una nueva instancia de excepcion directamente. Entonces sus
     * observadores obtienen la misma instancia de excepcion cada vez.
     */
    private static void throwException() {
        Observable observable = Observable.error(new Exception("An Exception"));
        observable.subscribe(System.out::println, error -> System.out.println("Error 1: " + error.hashCode()));
        observable.subscribe(System.out::println, error -> System.out.println("Error 2: " + error.hashCode()));
    }

    /**
     * Este metodo usa Observable.error() para pasar una nueva instancia de Callable que toma una excepcion como tipo de
     * devolucion a traves de lambda. Entonces, sus observadores obtienen una nueva instancia de excepcion en onError()
     * cada vez.
     */
    private static void throwExceptionUsingCallable() {
        Observable observable = Observable.error(() -> {
            // Imprime este mensaje para mostrar que se crea una nueva instancia antes de publicar el error a sus observadores
            System.out.println("New Exception Created");
            return new Exception("An Exception");
        });
        observable.subscribe(System.out::println, error -> System.out.println("Error 1: " + error.hashCode()));
        observable.subscribe(System.out::println, error -> System.out.println("Error 2: " + error.hashCode()));
    }

}
