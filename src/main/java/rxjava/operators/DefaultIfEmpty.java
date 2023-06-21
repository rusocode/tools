package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * ¿Alguna vez se encontro en una situacion en la que el Observable no pudo emitir ningun elemento relacionado con el
 * filter de cualquier otro operador en la cadena Observable y aun desea proporcionar a sus Observadores un valor
 * predeterminado o incluso cambiar el Observable en el medio si emite vacio? Entonces, defaultIfEmpty y switchIfEmpty
 * seran el operador RxJava que lo ayudara a hacerlo muy facilmente.
 */

public class DefaultIfEmpty {

    public static void main(String[] args) {
        useDefaultIfEmpty();
        useSwitchIfEmpty();
    }

    /**
     * Emite al menos un valor predeterminado si la emision se vacia.
     */
    private static void useDefaultIfEmpty() {
        Observable.just(1, 2, 3, 4, 5)
                .filter(item -> item > 10)
                .defaultIfEmpty(100)
                .subscribe(System.out::println);
    }

    /**
     * Esto cambiara a alguna fuente observable alternativa si la emision se vacia.
     */
    private static void useSwitchIfEmpty() {
        Observable.just(1, 2, 3, 4, 5)
                .filter(item -> item > 10)
                .switchIfEmpty(Observable.just(6, 7, 8, 9, 10))
                .subscribe(System.out::println);
    }
}
