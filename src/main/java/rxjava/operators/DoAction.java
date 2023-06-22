package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Estos no son mas que operadores, pero los llamamos operadores de "do" o "action" respectivamente.
 */

public class DoAction {

    public static void main(String[] args) {
        // exDoOnSubscribe();
        exDoOnNext();
        // exDoOnComplete();
    }

    /**
     * doOnSubscribe obtendra el disposable tan pronto como suscribamos el observable especifico.
     */
    private static void exDoOnSubscribe() {
        Observable.just(1, 2, 3, 4, 5)
                .doOnSubscribe(disposable -> System.out.println("doOnSubscribe: Subscribed"))
                .subscribe(System.out::println);
    }

    /**
     * doOnNext obtendra el elemento justo antes de que llegue al final de onNext.
     */
    private static void exDoOnNext() {
        Observable.just(1, 2, 3, 4, 5)
                .doOnNext(item -> System.out.println("doOnNext: " + ++item))
                .subscribe(System.out::println);
    }

    /**
     * doOnComplete se anulara justo antes de llegar a la parte inferior de onComplete.
     */
    private static void exDoOnComplete() {
        Observable.just(1, 2, 3, 4, 5)
                .doOnComplete(() -> System.out.println("doOnComplete: Completed"))
                .subscribe(System.out::println, System.out::print, () -> System.out.println("Completed"));
    }


}
