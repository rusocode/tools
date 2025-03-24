package com.punkipunk.concurrency;

/**
 * <p>
 * Un lock es un mecanismo de sincronizacion de subprocesos como los bloques sincronizados, excepto que los locks pueden ser mas
 * sofisticados que los bloques sincronizados. Los Locks (y otros mecanismos de sincronizacion mas avanzados) se crean utilizando
 * bloques sincronizados, por lo que no podemos deshacernos por completo de la palabra clave {@code synchronized}.
 * <p>
 * Desde Java 5, el paquete {@code java.util.concurrent.locks} contiene varias implementaciones de lock, por lo que es posible que
 * no tengas que implementar tus propios locks. Pero aun necesitaras saber como usarlos y aun puede resultar util conocer la
 * teoria detras de su implementacion.
 * <h3>Un Lock simple</h3>
 * <p>
 * Comencemos mirando un bloque sincronizado de codigo:
 * <pre>{@code
 * public class Counter {
 *
 *     private int count;
 *
 *     public int inc() {
 *         synchronized (this) {
 *             return ++count;
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe el bloque  {@code synchronized (this)} en el metodo {@code inc()}. Este bloque garantiza que solo un subproceso pueda
 * ejecutar {@code return ++count} a la vez. El codigo en el bloque sincronizado podria haber sido mas avanzado, pero el simple
 * ++count es suficiente para entenderlo.
 * <p>
 * La clase {@code Counter} podria haberse escrito asi, usando un {@code Lock} en lugar de un bloque sincronizado:
 * <pre>{@code
 * public class Counter {
 *
 *     private Lock lock = new Lock();
 *     private int count;
 *
 *     public int inc() {
 *         lock.lock();
 *         int newCount = ++count;
 *         lock.unlock();
 *         return newCount;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * El metodo {@code lock()} bloquea la instancia de Lock para que todos los subprocesos que llaman a lock() se bloqueen hasta que
 * se ejecute {@code unlock()}.
 * <p>
 * Aqui hay una implementacion de Lock simple:
 * <pre>{@code
 * public class Lock {
 *
 *     private boolean isLocked = false;
 *
 *     public synchronized void lock() throws InterruptedException {
 *         while (isLocked) {
 *             wait();
 *         }
 *         isLocked = true;
 *     }
 *
 *     public synchronized void unlock() {
 *         isLocked = false;
 *         notify();
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe el bucle {@code while (isLocked)}, que tambien se denomina "spin lock". Los bloqueos de giro y los metodos
 * {@code wait()} y {@code notify()} se tratan con mas detalle en {@link com.punkipunk.concurrency.signaling.Signaling Signaling}.
 * Si bien {@code isLocked} es true, el hilo que llama a lock() esta estacionado esperando en la llamada de wait(). En caso de que
 * el hilo regrese inesperadamente de la llamada wait() sin haber recibido una llamada notify() (AKA Spurious Wakeup), el hilo
 * vuelve a verificar la condicion isLocked para ver si es seguro continuar o no, en lugar de simplemente asumir que ser
 * despertado significa que es seguro continuar. Si isLocked es falso, el subproceso sale del bucle while(isLocked) y establece
 * isLocked nuevamente en verdadero, para bloquear la instancia de Lock para otros subprocesos que llamen a lock().
 * <p>
 * Cuando el hilo termina con el codigo en la seccion critica (el codigo entre lock() y unlock()), el hilo llama a unlock(). La
 * ejecucion de unlock() establece isLocked nuevamente en falso y notifica (despierta) a uno de los subprocesos que esperan en la
 * llamada wait() en el metodo lock(), si lo hay.
 * <h3>Lock Reentrance</h3>
 * <p>
 * Los bloques sincronizados en Java son reentrantes. Esto significa que si un subproceso ingresa a un bloque de codigo
 * sincronizado y, por lo tanto, toma el lock en el objeto monitor en el que esta sincronizado el bloque, el subproceso puede
 * ingresar en otros bloques de codigo sincronizados en el mismo objeto monitor. Aqui hay un ejemplo:
 * <pre>{@code
 * public class Reentrant {
 *
 *     public synchronized outer() {
 *         inner();
 *     }
 *
 *     public synchronized inner() {
 *         // Do something
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como tanto el {@code outer()} como el {@code inner()} se declaran sincronizados, lo que en Java equivale a un bloque
 * {@code synchronized (this)}. Si un hilo llama a outer() no hay problema en llamar a inner() desde dentro de outer(), ya que
 * ambos metodos (o bloques) estan sincronizados en el mismo objeto monitor ("this"). Si un hilo ya mantiene el lock en un objeto
 * monitor, tiene acceso a todos los bloques sincronizados en el mismo objeto monitor. Esto se llama reentrance. El hilo puede
 * volver a ingresar a cualquier bloque de codigo para el cual ya tenga el lock.
 * <p>
 * Sin embargo, incluso si los bloques sincronizados son reentrantes, la clase Lock mostrada anteriormente no es reentrante. Si
 * reescribimos la clase {@code Reentrant} como se muestra a continuacion, el hilo que llama a outer() se bloqueara dentro de
 * {@code lock.lock()} en el metodo inner().
 * <pre>{@code
 * public class Reentrant2 {
 *
 *     Lock lock = new Lock();
 *
 *     public outer() {
 *         lock.lock();
 *         inner();
 *         lock.unlock();
 *     }
 *
 *     public synchronized inner() {
 *         lock.lock();
 *         // Do something
 *         lock.unlock();
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Un hilo que llama a outer() primero bloqueara la instancia de Lock. Luego llamara a inner(). Dentro del metodo inner() el hilo
 * intentara nuevamente bloquear la instancia de Lock. Esto fallara (lo que significa que el hilo se bloqueara), ya que la
 * instancia de Lock ya estaba bloqueada en el metodo outer().
 * <p>
 * La razon por la que el hilo se bloqueara la segunda vez que llame a lock() sin haber llamado a unlock() en el medio, es
 * evidente cuando observamos la implementacion de lock():
 * <pre>{@code
 * public class Lock {
 *
 *     boolean isLocked = false;
 *
 *     public synchronized void lock() throws InterruptedException {
 *         while (isLocked) {
 *             wait();
 *         }
 *         isLocked = true;
 *     }
 *
 *   ...
 *
 * }
 * }</pre>
 * <p>
 * Es la condicion dentro del bucle while (spin lock) la que determina si un hilo puede salir del metodo lock() o no. Actualmente,
 * la condicion es que isLocked debe ser falso para que esto se permita, independientemente del hilo que lo haya bloqueado.
 * <p>
 * Para que la clase Lock sea reentrante necesitamos hacer un pequeño cambio:
 * <pre>{@code
 * public class Lock {
 *
 *     boolean isLocked = false;
 *     Thread lockedBy = null;
 *     int lockedCount = 0;
 *
 *     public synchronized void lock() throws InterruptedException {
 *         Thread callingThread = Thread.currentThread();
 *         while (isLocked && lockedBy != callingThread) {
 *             wait();
 *         }
 *         isLocked = true;
 *         lockedCount++;
 *         lockedBy = callingThread;
 *     }
 *
 *     public synchronized void unlock() {
 *         if (Thread.curentThread() == this.lockedBy) {
 *             lockedCount--;
 *             if (lockedCount == 0) {
 *                 isLocked = false;
 *                 notify();
 *             }
 *         }
 *     }
 *
 *   ...
 *
 * }
 * }</pre>
 * <p>
 * Observe como el bucle while (spin lock) ahora tambien toma en consideracion el hilo que bloqueo la instancia de Lock. Si el
 * lock esta desbloqueado (isLocked = false) o el hilo que llama es el hilo que bloqueo la instancia de Lock, el bucle while no se
 * ejecutara y el hilo que llama a lock() podra salir del metodo.
 * <p>
 * Ademas, necesitamos contar la cantidad de veces que el mismo hilo ha bloqueado el candado. De lo contrario, una sola llamada a
 * unlock() desbloqueara el bloqueo, incluso si el bloqueo se ha bloqueado varias veces. No queremos que el bloqueo se desbloquee
 * hasta que el hilo que lo bloqueo haya ejecutado la misma cantidad de llamadas de unlock() que de llamadas de lock().
 * <p>
 * La clase Lock ahora es reentrante.
 * <h3>Lock Fairness</h3>
 * <p>
 * Los bloques sincronizados no ofrecen garantias sobre la secuencia en la que se concede acceso a los subprocesos que intentan
 * ingresar a ellos. Por lo tanto, si muchos subprocesos compiten constantemente por el acceso al mismo bloque sincronizado,
 * existe el riesgo de que uno o mas subprocesos nunca obtengan acceso; ese acceso siempre se concede a otros subprocesos. A esto
 * se le llama starvation. Para evitar esto, un {@code Lock} debe ser justo. Dado que las implementaciones de Lock que se muestran
 * en este texto utilizan bloques sincronizados internamente, no garantizan la equidad (fairness).
 * <h3>Llamar a unlock() desde una clausula finally</h3>
 * <p>
 * Cuando se protege una seccion critica con un bloqueo, y la seccion critica puede generar excepciones, es importante llamar al
 * metodo unlock() desde dentro de una clausula finally. Al hacerlo, se asegura de que el bloqueo este desbloqueado para que otros
 * subprocesos puedan bloquearlo. Aqui hay un ejemplo:
 * <pre>{@code
 * lock.lock();
 * try {
 *   // Realizar codigo de seccion critica, que puede generar una excepcion
 * } finally {
 *   lock.unlock();
 * }
 * }</pre>
 * <p>
 * Esta pequeña construccion garantiza que el Lock este desbloqueado en caso de que se produzca una excepcion desde el codigo en
 * la seccion critica. Si no se llamaba a unlock() desde dentro de una clausula finally y se lanzaba una excepcion desde la
 * seccion critica, Lock permaneceria bloqueado para siempre, provocando que todos los subprocesos que llamaran a lock() en esa
 * instancia de Lock se detuvieran indefinidamente.
 * <p>
 * Fuente: <a
 * href="https://jenkov.com/tutorials/java-concurrency/locks.html">https://jenkov.com/tutorials/java-concurrency/locks.html</a>
 */

public class Lock {


}
