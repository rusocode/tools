package concurrency;

/**
 * <h3>Como se produce el bloqueo del monitor anidado</h3>
 * <p>
 * El bloqueo del monitor anidado (nested monitor lockout) es un problema similar al deadlock. Un bloqueo de monitor anidado
 * ocurre asi:
 * <pre>{@code
 *
 * Thread 1 se sincroniza en A
 * Thread 1 se sincroniza en B (mientras esta sincronizado en A)
 * Thread 1 decide esperar una señal de otro hilo antes de continuar
 * Thread 1 llama a B.wait() liberando asi el bloqueo en B, pero no en A
 *
 * Thread 2 necesita bloquear tanto A como B (en esa secuencia) para enviar la señal al hilo 1
 * Thread 2 no puede bloquear A ya que el hilo 1 aun mantiene el bloqueo en A
 * Thread 2 permanece bloqueado indefinidamente esperando que el hilo 1 libere el bloqueo en A
 *
 * Thread 1 permanece bloqueado indefinidamente esperando la señal del hilo 2, por lo que nunca se libera el bloqueo en A, que
 * debe liberarse para que el hilo 2 pueda enviar la señal al hilo 1, etc.
 *
 * }</pre>
 * <p>
 * Esto puede parecer una situacion bastante teorica, pero observe la ingenua implementacion de {@code Lock} a continuacion:
 * <pre>{@code
 * // Implementacion de bloqueo con problema de bloqueo de monitor anidado
 * public class Lock {
 *
 *     protected MonitorObject monitorObject = new MonitorObject();
 *     protected boolean isLocked;
 *
 *     // Thread 1
 *     public void lock() throws InterruptedException {
 *         synchronized (this) { // A
 *             while (isLocked) {
 *                 synchronized (monitorObject) { // B
 *                     monitorObject.wait(); // Libera el bloqueo en B pero no en A
 *                 }
 *             }
 *             isLocked = true;
 *         }
 *     }
 *
 *     // Thread 2
 *     public void unlock() {
 *         // No puede bloquear A ya que el hilo 1 aun mantiene el bloqueo en A, por lo
 *         // tanto permanece bloqueado indefinidamenete esperando que el hilo 1 libere el
 *         // bloqueo en A.
 *         synchronized (this) { // A
 *             isLocked = false;
 *             synchronized (monitorObject) { // B
 *                 monitorObject.notify();
 *             }
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * El metodo {@code lock()} primero sincroniza en "this" y luego en {@code monitorObject}. Si {@code isLocked} es falso, no hay
 * problema, pero si es verdadero, el hilo se queda esperando en {@code monitorObject.wait()}. El problema es que esta llamada
 * solo libera el monitor de monitorObject, no el de "this". Cuando otro hilo intenta desbloquear llamando a {@code unlock()}, se
 * bloquea al intentar entrar en el bloque {@code synchronized (this)} de unlock(). Esto crea una situacion de interbloqueo donde
 * el hilo esperando en lock() necesita que se ejecute unlock() para salir, pero ningun hilo puede ejecutar unlock() hasta que el
 * primero salga del bloque sincronizado externo. Como resultado, cualquier hilo que llame a lock() o unlock() quedara bloqueado
 * indefinidamente. Esta situacion se conoce como bloqueo anidado de monitores.
 * <h3>Un ejemplo mas realista</h3>
 * <p>
 * Puede afirmar que nunca implementaria un bloqueo como el que se mostro anteriormente. Que no llamarias a {@code wait()} y
 * {@code notify()} en un objeto de monitor interno, sino mas bien en el this probablemente sea cierto. Pero hay situaciones en
 * las que pueden surgir diseños como el anterior. Por ejemplo, si implementara fairness en un Lock. Al hacerlo, desea que cada
 * subproceso llame a wait() en cada uno de sus propios objetos de cola, de modo que pueda notificar a los subprocesos uno a la
 * vez.
 * <p>
 * Mire esta ingenua implementacion de un bloqueo justo:
 * <pre>{@code
 * // Implementacion de Fair Lock con problema de bloqueo de monitor anidado
 * public class FairLock {
 *
 *     private boolean isLocked;
 *     private Thread lockingThread;
 *     private List<QueueObject> waitingThreads = new ArrayList<>();
 *
 *     public void lock() throws InterruptedException {
 *         QueueObject queueObject = new QueueObject();
 *
 *         synchronized (this) {
 *             waitingThreads.add(queueObject);
 *             while (isLocked || waitingThreads.get(0) != queueObject) {
 *                 synchronized (queueObject) {
 *                     try {
 *                         queueObject.wait();
 *                     } catch (InterruptedException e) {
 *                         waitingThreads.remove(queueObject);
 *                         throw e;
 *                     }
 *                 }
 *             }
 *             waitingThreads.remove(queueObject);
 *             isLocked = true;
 *             lockingThread = Thread.currentThread();
 *         }
 *     }
 *
 *     public synchronized void unlock() {
 *         if (this.lockingThread != Thread.currentThread()) {
 *             throw new IllegalMonitorStateException("Calling thread has not locked this lock");
 *         }
 *         isLocked = false;
 *         lockingThread = null;
 *         if (waitingThreads.size() > 0) {
 *             QueueObject queueObject = waitingThreads.get(0);
 *             synchronized (queueObject) {
 *                 queueObject.notify();
 *             }
 *         }
 *     }
 *
 * }
 * }</pre>
 * <pre>{@code
 * public class QueueObject{}
 * }</pre>
 * <p>
 * El metodo lock() llama a queueObject.wait() desde dentro de dos bloques sincronizados, uno en "this" y otro anidado en la
 * variable local queueObject. Cuando un hilo llama a queueObject.wait(), libera el bloqueo en la instancia de QueueObject, pero
 * no el asociado con "this". Ademas, el metodo unlock() esta declarado como sincronizado, lo que equivale a un bloque
 * synchronized (this). Esto significa que si un hilo esta esperando dentro de lock(), el objeto monitor asociado con "this"
 * estara bloqueado por el hilo en espera. Todos los hilos que llamen a unlock() quedaran bloqueados indefinidamente, esperando
 * que el hilo en espera libere el bloqueo en "this". Sin embargo, esto nunca ocurrira, ya que solo puede suceder si un hilo logra
 * enviar una señal al hilo en espera, lo cual solo puede hacerse ejecutando el metodo unlock(). Por lo tanto, esta implementacion
 * de FairLock podria conducir a un bloqueo anidado de monitores. Se sugiere una mejor implementacion en
 * {@link StarvationFairness}.
 * <h3>Nested Monitor Lockout vs Deadlock</h3>
 * <p>
 * El resultado del nested monitor lockout y deadlock es practicamente el mismo: los subprocesos involucrados terminan bloqueados
 * para siempre esperando el uno al otro.
 * <p>
 * Sin embargo, las dos situaciones no son iguales. Como se explica en {@link concurrency.deadlock.Deadlock Deadlock}, se produce
 * un interbloqueo cuando dos subprocesos obtienen bloqueos en diferente orden. El subproceso 1 bloquea A, espera a B. El
 * subproceso 2 ha bloqueado a B y ahora espera a A. Como se explica en
 * {@link concurrency.deadlock.DeadlockPrevention DeadlockPrevention}, los interbloqueos se pueden evitar bloqueando siempre los
 * bloqueos en el mismo orden (Lock Ordering). Sin embargo, un bloqueo de monitor anidado se produce exactamente cuando dos
 * subprocesos toman los bloqueos en el mismo orden. El subproceso 1 bloquea A y B, luego libera B y espera una señal del
 * subproceso 2. El subproceso 2 necesita tanto A como B para enviar la señal al subproceso 1. Entonces, un hilo esta esperando
 * una señal y otro, que se libere un bloqueo.
 * <p>
 * La diferencia se resume aqui:
 * <pre>{@code
 * En un deadlock, dos subprocesos esperan que el otro libere los bloqueos.
 *
 * En el nested monitor lockout, el subproceso 1 mantiene el bloqueo en A y espera una señal del subproceso 2. El subproceso 2
 * necesita el bloqueo A para enviar la señal al subproceso 1.
 * }</pre>
 * <p>
 * Links: <a href="https://jenkov.com/tutorials/java-concurrency/nested-monitor-lockout.html">Nested Monitor Lockout</a>
 */

public class NestedMonitorLockout {

}