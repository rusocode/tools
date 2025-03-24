package com.punkipunk.concurrency;

/**
 * <h3>¿Que son las Slipped Conditions?</h3>
 * <p>
 * Slipped Conditions (condiciones deslizadas) significa que, desde el momento en que un subproceso verifica una determinada
 * condicion hasta que actua sobre ella, otro subproceso ha cambiado la condicion, de modo que es erroneo que actue el primer
 * subproceso. Aqui hay un ejemplo simple:
 * <pre>{@code
 * public class Lock {
 *
 *     private boolean isLocked = true;
 *
 *     public void lock() {
 *         synchronized (this) {
 *             while (isLocked) {
 *                 try {
 *                     this.wait();
 *                 } catch(InterruptedException e) {
 *                     // Do nothing, keep waiting
 *                 }
 *             }
 *         }
 *         synchronized (this) {
 *             isLocked = true;
 *         }
 *     }
 *
 *     public synchronized void unlock() {
 *         isLocked = false;
 *         this.notify();
 *     }
 *
 * }
 * }</pre>
 * <p>
 * El ejemplo muestra una clase {@code Lock} con metodos {@code lock()} y {@code unlock()}. El metodo lock() tiene dos bloques
 * sincronizados: uno para esperar hasta que isLocked sea falso, y otro para establecerlo como verdadero. Si dos hilos llaman a
 * lock() simultaneamente cuando isLocked es falso, ambos podrian ver la condicion como falsa y establecerla como verdadera,
 * resultando en un comportamiento incorrecto. Para evitar esto, la verificacion y modificacion de la condicion deben realizarse
 * atomicamente por el mismo hilo. La solucion propuesta es mover la linea que establece isLocked como verdadero al primer bloque
 * sincronizado, justo despues del bucle while:
 * <pre>{@code
 * public class Lock {
 *
 *     private boolean isLocked = true;
 *
 *     public void lock() {
 *         synchronized (this) {
 *             while (isLocked) {
 *                 try {
 *                     this.wait();
 *                 } catch(InterruptedException e) {
 *                     // Do nothing, keep waiting
 *                 }
 *             }
 *             isLocked = true;
 *         }
 *     }
 *
 *     public synchronized void unlock() {
 *         isLocked = false;
 *         this.notify();
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Ahora la prueba y configuracion de la condicion isLocked se realiza de forma atomica desde dentro del mismo bloque
 * sincronizado.
 * <h3>Un ejemplo mas realista</h3>
 * <p>
 * Puede argumentar con razon que nunca implementaria un Lock como la primera implementacion que se muestra en este texto y, por
 * lo tanto, afirmar que las condiciones deslizadas son un problema mas bien teorico. Pero el primer ejemplo se mantuvo bastante
 * simple para transmitir mejor la nocion de condiciones de deslizamiento.
 * <p>
 * Un ejemplo mas realista seria durante la implementacion de un Fair Lock, como se analiza en el la clase
 * {@link StarvationFairness}. Si observamos la implementacion ingenua del {@link NestedMonitorLockout} e intentamos eliminar el
 * problema de bloqueo del monitor anidado, es facil llegar a una implementacion que sufre condiciones de error.
 * <p>
 * Observe como {@code synchronized(queueObject)} con su llamada {@code queueObject.wait()} esta anidado dentro del bloque
 * {@code synchronized(this)}, lo que resulta en el problema de bloqueo del monitor anidado. Para evitar este problema, el bloque
 * synchronized(queueObject) debe moverse fuera del bloque synchronized(this). Asi es como podria verse:
 * <pre>{@code
 * // Implementacion de Fair Lock con problema de slipped conditions
 * public class FairLock {
 *
 *     private boolean isLocked = false;
 *     private Thread lockingThread = null;
 *     private List<QueueObject> waitingThreads = new ArrayList<QueueObject>();
 *
 *     public void lock() throws InterruptedException {
 *         QueueObject queueObject = new QueueObject();
 *
 *         synchronized (this) {
 *             waitingThreads.add(queueObject);
 *         }
 *
 *         boolean mustWait = true;
 *
 *         while (mustWait) {
 *             synchronized (this) {
 *                 mustWait = isLocked || waitingThreads.get(0) != queueObject;
 *             }
 *             synchronized (queueObject) {
 *                 if (mustWait) {
 *                     try {
 *                         queueObject.wait();
 *                     } catch(InterruptedException e){
 *                         waitingThreads.remove(queueObject);
 *                         throw e;
 *                     }
 *                 }
 *             }
 *         }
 *
 *         synchronized (this) {
 *             waitingThreads.remove(queueObject);
 *             isLocked = true;
 *             lockingThread = Thread.currentThread();
 *         }
 *
 *     }
 *
 * }
 * }</pre>
 * <p>
 * El ejemplo describe un metodo lock() modificado para abordar el problema de slipped conditions, pero aun presenta problemas. El
 * metodo ahora tiene tres bloques sincronizados: el primero verifica la condicion, el segundo determina si el hilo debe esperar,
 * y el tercero establece el bloqueo si no hay que esperar. Se presenta un escenario donde dos hilos, A y B, llaman a lock()
 * simultaneamente. El hilo A podria completar todo el metodo y desbloquear antes de que B avance, resultando en que B entre en
 * estado de espera innecesariamente. Esto ocurre porque la condicion ha cambiado entre el momento en que B la detecto y cuando
 * actuo sobre ella, demostrando que el problema de condiciones deslizadas persiste en esta implementacion.
 * <h3>Eliminacion del problema de las Slipped Conditions</h3>
 * <p>
 * Para eliminar el problema de las condiciones deslizadas del ejemplo anterior, el contenido del ultimo bloque sincronizado debe
 * moverse hacia el primer bloque. Naturalmente, el codigo tambien tendra que cambiarse un poco para adaptarse a este movimiento:
 * <pre>{@code
 * // Implementacion de Fair Lock sin problema de bloqueo de monitor anidado, pero con problema de señales perdidas
 * public class FairLock {
 *     private boolean isLocked = false;
 *     private Thread lockingThread = null;
 *     private List<QueueObject> waitingThreads = new ArrayList<QueueObject>();
 *
 *     public void lock() throws InterruptedException {
 *         QueueObject queueObject = new QueueObject();
 *
 *         synchronized (this) {
 *             waitingThreads.add(queueObject);
 *         }
 *
 *         boolean mustWait = true;
 *         while (mustWait) {
 *             synchronized (this) {
 *                 mustWait = isLocked || waitingThreads.get(0) != queueObject;
 *                 if (!mustWait) {
 *                     waitingThreads.remove(queueObject);
 *                     isLocked = true;
 *                     lockingThread = Thread.currentThread();
 *                     return;
 *                 }
 *             }
 *             synchronized (queueObject) {
 *                 if (mustWait) {
 *                     try {
 *                         queueObject.wait();
 *                     } catch(InterruptedException e) {
 *                         waitingThreads.remove(queueObject);
 *                         throw e;
 *                     }
 *                 }
 *             }
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * La variable local {@code mustWait} ahora se prueba y establece dentro del mismo bloque sincronizado, garantizando atomicidad.
 * Aunque mustWait se verifica fuera del bloque sincronizado, su valor solo cambia dentro de el. Un hilo que evalua mustWait como
 * falso establece atomicamente las condiciones internas para que otros hilos la evaluen como verdadera. Se menciona una
 * optimizacion con una declaracion return. Sin embargo, la implementacion aun sufre de un problema de señales perdidas: si un
 * hilo desbloquea antes de que el hilo en espera llame a wait(), la notificacion se pierde. Para resolver esto, se sugiere
 * convertir QueueObject en un semaforo con metodos {@code doWait()} y {@code doNotify()} que almacenan y reaccionan a la señal
 * internamente, evitando que se pierda incluso si doNotify() se llama antes que doWait().
 * <p>
 * Fuente: <a href="https://jenkov.com/tutorials/java-concurrency/slipped-conditions.html">Slipped Conditions</a>
 */

public class SlippedConditions {

}