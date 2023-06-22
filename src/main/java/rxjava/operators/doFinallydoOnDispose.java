package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * ¿Cual es la diferencia entre doFinally y doOnDipose? ¿Y por que doOnDispose no funciona en algunos casos?
 */

public class doFinallydoOnDispose {

    public static void main(String[] args) {
        // exDoFinally();
        exDoOnDispose();
    }

    /**
     * doFinally funciona despues de que se completa el observable o onComplete.
     */
    private static void exDoFinally() {
        Observable.just(1, 2, 3, 4, 5)
                .doFinally(() -> System.out.println("doFinally: Completed"))
                .subscribe(System.out::println);
    }

    /**
     * doOnDispose solo funciona si podemos eliminar el observable explicitamente antes de onComplete o onError.
     */
    private static void exDoOnDispose() {
        Observable.just(1, 2, 3, 4, 5)
                .doOnDispose(() -> System.out.println("onDispose: Disposed"))
                .doOnSubscribe(disposable -> disposable.dispose())
                .subscribe(System.out::println);
    }

}
