package rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import static java.lang.Thread.sleep;

/**
 * En programacion reactiva, los terminos <b><i>Cold Observable</i></b>, <b><i>Hot Observable</i></b> y <b><i>Connectable Observable</i></b>
 * se refieren a diferentes comportamientos de los Observables en cuanto a la emision de eventos y la suscripcion de
 * Observers.
 * <ol>
 * <li>Cold Observable: Un Cold Observable es aquel que inicia la emision de elementos solo cuando un Observer se
 * suscribe a el. Cada vez que un Observer se suscribe a un Cold Observable, se crea una nueva instancia de la secuencia
 * de eventos para ese Observer en particular. Esto significa que cada suscriptor recibe una secuencia independiente de
 * eventos. El flujo de eventos no se comparte entre los Observers. Es como ver una pelicula desde el principio para
 * cada espectador individualmente.
 * <li>Hot Observable: Un Hot Observable, por otro lado, emite eventos independientemente de si hay Observers suscritos
 * o no. La emision de eventos ocurre incluso antes de que los Observers se suscriban. Cuando un Observer se suscribe a
 * un Hot Observable, comienza a recibir eventos desde el punto en que se encuentra el Observable en ese momento. Los
 * Observers que se suscriben mas tarde se pierden los eventos anteriores y solo reciben los nuevos eventos emitidos. Es
 * como una transmision en vivo donde los espectadores pueden unirse en cualquier momento y solo ven lo que se esta
 * transmitiendo actualmente.
 * <li>Connectable Observable: Un Connectable Observable es un tipo especial de Observable que permite controlar
 * explicitamente cuando comienza la emision de eventos. Inicialmente, un Connectable Observable no emite eventos
 * incluso si hay Observers suscritos. Para que comience la emision, se debe llamar al metodo {@code connect()} en el
 * Connectable Observable. Una vez que se llama a connect(), el Connectable Observable comienza a emitir eventos a todos
 * los Observers suscritos simultaneamente. Esto es util para sincronizar y coordinar la emision de eventos en casos en
 * los que se requiere que todos los Observers comiencen a recibir eventos al mismo tiempo.
 * </ol>
 * En resumen, la diferencia principal entre estos tipos de Observables radica en cuando se inicia la emision de eventos
 * y si los Observers reciben la secuencia completa de eventos o solo los eventos emitidos despues de su suscripcion.
 */

public class ColdHotConnectable {

    public static void main(String[] args) {
        // createColdObservable();
        createHotAndConnectableObservable();
    }

    /**
     * Crea un Cold Observable usando Observable.just(). Porque todos y cada uno de onNext() obtienen su emision por
     * separado.
     */
    private static void createColdObservable() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable.subscribe(item -> System.out.println("Observer 1: " + item));
        pause(3000);
        observable.subscribe(item -> System.out.println("Observer 2: " + item));
    }

    /**
     * Crea un Hot Observable. En el momento en que llama al metodo publish() en Observable.just() convierte el
     * Observable en un Connectable Observable. Connectable Observable NO inicia su emision justo despues de suscribirse.
     * En el momento en que llama al metodo connect(), comienza la emision. Cualquier observador que se suscriba despues
     * de llamar a connect() pierde las emisiones.
     */
    private static void createHotAndConnectableObservable() {
        ConnectableObservable<Integer> observable = Observable.just(1, 2, 3, 4, 5).publish();
        observable.subscribe(item -> System.out.println("Observer 1: " + item));
        observable.connect();
        observable.subscribe(item -> System.out.println("Observer 2: " + item)); // Emision perdida
    }

    /**
     * Duerme el subproceso principal durante una duracion especifica.
     *
     * @param duration duracion del sueño en milisegundos.
     */
    private static void pause(int duration) {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
