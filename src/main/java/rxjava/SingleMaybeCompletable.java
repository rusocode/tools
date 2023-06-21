package rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * En el contexto de RxJava, "Single", "Maybe" y "Completable" son tipos de observables que representan diferentes
 * situaciones y resultados en la programacion reactiva. Aqui tienes una descripcion de cada uno:
 * <ol>
 * <li>Single: Un Single emite un solo valor o un error. Puede ser util cuando se espera un unico resultado, como una
 * respuesta de una solicitud de red. Un Single puede emitir un exito con el valor esperado o un error si algo sale mal.
 * Solo emite un evento en total, ya sea un exito o un error.
 * <li>Maybe: Un Maybe es similar a un Single, pero tambien tiene la posibilidad de no emitir ningun valor en absoluto.
 * Puede emitir un unico valor exitoso, un error o no emitir nada (llamado evento "Completo vacio"). Puede ser util
 * cuando se espera un resultado opcional.
 * <li>Completable: Un Completable representa una accion que se ejecuta sin emitir ningun valor. Puede utilizarse para
 * representar operaciones que no devuelven un resultado, como guardar datos en una base de datos o realizar una
 * actualizacion en segundo plano. Un Completable solo puede emitir un evento de "Completo" para indicar que la accion
 * se ha completado correctamente, o un error si algo sale mal.
 * </ol>
 * Estos tres tipos de observables ofrecen flexibilidad para adaptarse a diferentes situaciones en la programacion
 * reactiva, permitiendo un manejo mas preciso de los resultados y errores.
 */

public class SingleMaybeCompletable {

    public static void main(String[] args) {
        createSingle();
        // createMaybe();
        // createCompletable();
    }

    /**
     * Crea un Single y emite datos a su observador solo una vez.
     */
    private static void createSingle() {
        Single.just("Hello World").subscribe(System.out::println);
    }

    /**
     * Crea un Maybe y puede o no emitir datos a sus observadores.
     * <p>
     * Maybe.empty() se ha llamado aqui y este metodo de fabrica no emite, solo se completa.
     */
    private static void createMaybe() {
        Maybe.empty().subscribe(new MaybeObserver<>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Object o) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * Crea un Completable.
     * <p>
     * Aqui se ha utilizado el metodo de fabrica Completable.fromSingle(), que toma un Single. Pero no emite ningun
     * elemento a sus observadores.
     * <p>
     * Porque CompletableObserver no tiene ningun metodo onNext(). Y su trabajo se limita a que sus observadores sepan
     * que algo se ha completado. Es posible que estes usando esto en algun momento solo para probar algunas cosas. De
     * lo contrario, esto no se usa con mucha frecuencia en la produccion.
     */
    private static void createCompletable() {
        Completable.fromSingle(Single.just("Hello World")).subscribe(() -> System.out.println("Done"));
    }

}
