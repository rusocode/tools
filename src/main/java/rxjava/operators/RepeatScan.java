package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * En este episodio, hemos cubierto los operadores repetidos que basicamente lo ayudan a repetir las emisiones. Y un
 * operador muy interesante llamado operador de escaneo.
 */

public class RepeatScan {

    public static void main(String[] args) {
        useRepeat();
        useScan();
        useScanWithInitialValue();
    }

    /**
     * Especifica cuantas veces se repetira la emision.
     */
    private static void useRepeat() {
        Observable.just(1, 2, 3, 4, 5)
                .repeat(3)
                .subscribe(System.out::println);
    }

    /**
     * Utiliza un operador de escaneo para imprimir la suma del elemento emitido anteriormente y el actual que se va a
     * emitir.
     */
    private static void useScan() {
        Observable.just(1, 2, 3, 4, 5)
                .scan(Integer::sum) // (accumulator, next) -> accumulator + next
                .subscribe(System.out::println);
    }

    /**
     * Utiliza el operador de escaneo que imprime la suma del elemento emitido anteriormente y el actual que se va a
     * emitir, pero tambien tiene en cuenta la emision inicial al especificar un valor inicial.
     */
    private static void useScanWithInitialValue() {
        Observable.just(1, 2, 3, 4, 5)
                .scan(10, Integer::sum)
                .subscribe(System.out::println);
    }

}
