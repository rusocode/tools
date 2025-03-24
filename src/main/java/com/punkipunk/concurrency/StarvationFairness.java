package com.punkipunk.concurrency;

/**
 * <p>
 * Si a un subproceso no se le concede tiempo de CPU porque otros subprocesos lo acaparan todo, se denomina "starvation"
 * (inanicion). El subproceso "starved to death" (muere de hambre) porque a otros subprocesos se les permite el tiempo de CPU en
 * lugar de el. La solucion al starvation se llama "fairness" (justicia): que todos los subprocesos tengan una oportunidad justa de
 * ejecutarse.
 * <h3>Causas del Starvation en Java</h3>
 * <p>
 * Las siguientes tres causas comunes pueden provocar la starvation de subprocesos en Java:
 * <ol>
 * <li>Los subprocesos con alta prioridad absorben todo el tiempo de CPU de los subprocesos con menor prioridad.
 * <li>Los subprocesos se bloquean indefinidamente esperando ingresar a un bloque sincronizado, porque a otros subprocesos se les
 * permite constantemente el acceso antes que el.
 * <li>Los subprocesos que esperan en un objeto (llamado wait() en el) permanecen esperando indefinidamente porque otros
 * subprocesos se despiertan constantemente en lugar de el.
 * </ol>
 * <h4>Los subprocesos con alta prioridad absorben todo el tiempo de CPU de los subprocesos con menor prioridad</h4>
 * <p>
 * Puede establecer la prioridad del hilo de cada hilo individualmente. Cuanto mayor sea la prioridad, mas tiempo de CPU se le
 * otorga al subproceso. Puede establecer la prioridad de los subprocesos entre 1 y 10. La forma exacta en que se interpreta esto
 * depende del sistema operativo en el que se ejecuta su aplicacion. Para la mayoria de las aplicaciones es mejor dejar la
 * prioridad sin cambios.
 * <h4>Los hilos estan bloqueados indefinidamente esperando ingresar a un bloque sincronizado</h4>
 * <p>
 * Los bloques de codigo sincronizados de Java pueden ser otra causa de starvation. El bloque de codigo sincronizado de Java no
 * garantiza la secuencia en la que se permite la entrada a los subprocesos que esperan ingresar al bloque sincronizado. Esto
 * significa que existe un riesgo teorico de que un hilo permanezca bloqueado para siempre al intentar ingresar al bloque, porque
 * a otros hilos se les concede acceso constantemente antes que el. Este problema se llama "starvation", y un subproceso "muere de
 * hambre" porque a otros subprocesos se les permite tiempo de CPU en lugar de el.
 * <h4>Los hilos que esperan en un objeto (llamado wait() en el) permanecen esperando indefinidamente</h4>
 * <p>
 * El metodo notify() no garantiza que subproceso se activa si varios subprocesos han llamado a wait() en el objeto en el que se
 * llama a notify(). Podria ser cualquiera de los hilos en espera. Por lo tanto, existe el riesgo de que un subproceso que espera
 * en un determinado objeto nunca se despierte porque siempre se despiertan otros subprocesos en espera en lugar de el.
 * <h3>Implementacion de Fairness en Java</h3>
 * <p>
 * Si bien no es posible implementar un 100% de fairness en Java, aun podemos implementar nuestras construcciones de
 * sincronizacion para aumentar la equidad entre subprocesos.
 * <p>
 * Primero, estudiemos un bloque de codigo sincronizado simple:
 * <pre>{@code
 * public class Synchronizer {
 *
 *     public synchronized void doSynchronized() {
 *         // Hacer mucho trabajo que lleve mucho tiempo
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Si mas de un subproceso llama al metodo {@code doSynchronized()}, algunos de ellos se bloquearan hasta que el primer subproceso
 * al que se le haya concedido acceso haya abandonado el metodo. Si mas de un subproceso esta bloqueado esperando acceso, no hay
 * garantia sobre a que subproceso se le concedera acceso a continuacion.
 * <h4>Usar Locks en lugar de bloques sincronizados</h4>
 * <p>
 * Para aumentar la equidad de los hilos en espera, primero cambiaremos el bloque de codigo para que este protegido por un candado
 * en lugar de un bloque sincronizado:
 * <pre>{@code
 * public class Synchronizer {
 *
 *     Lock lock = new Lock();
 *
 *     public void doSynchronized() throws InterruptedException {
 *         this.lock.lock();
 *         // Seccion critica, hacer mucho trabajo que lleve mucho tiempo
 *         this.lock.unlock();
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como el metodo doSynchronized() ya no se declara sincronizado. En cambio, la seccion critica esta protegida por las
 * llamadas lock.lock() y lock.unlock().
 * <p>
 * Una implementacion simple de la clase Lock podria verse asi:
 * <pre>{@code
 * public class Lock {
 *
 *     private boolean isLocked;
 *     private Thread lockingThread;
 *
 *     public synchronized void lock() throws InterruptedException {
 *         while (isLocked) {
 *             wait();
 *         }
 *         isLocked = true;
 *         lockingThread = Thread.currentThread();
 *     }
 *
 *     public synchronized void unlock() {
 *         if (this.lockingThread != Thread.currentThread()) {
 *             throw new IllegalMonitorStateException("Calling thread has not locked this lock");
 *         }
 *         isLocked = false;
 *         lockingThread = null;
 *         notify();
 *     }
 * }
 * }</pre>
 * <p>
 * La clase Synchronizer implementa un mecanismo de bloqueo donde los hilos se bloquean al intentar acceder al metodo lock() si
 * este ya esta en uso. Los hilos bloqueados esperan en una llamada wait() dentro de un bucle while, liberando el bloqueo de
 * sincronizacion y permitiendo que otros hilos entren al metodo lock(). Se asume que el codigo entre lock() y unlock() tarda
 * mucho en ejecutarse, lo que significa que la mayor parte del tiempo de espera ocurre en la llamada wait(). Ni los bloques
 * sincronizados ni wait() ofrecen garantias sobre que hilo obtendra acceso primero, lo que implica que esta implementacion de
 * Lock no mejora la equidad respecto a la version sincronizada. Sin embargo, se sugiere que si cada hilo llamara a wait() en un
 * objeto separado, la clase Lock podria decidir especificamente que hilo despertar, mejorando asi el control sobre la equidad en
 * el acceso a los recursos compartidos.
 * <h4>Un Fair Lock</h4>
 * <p>
 * A continuacion se muestra la clase Lock anterior convertida en un candado justo llamado FairLock. Notaras que la implementacion
 * ha cambiado un poco con respecto a la sincronizacion y wait()/notify() en comparacion con la clase Lock mostrada
 * anteriormente.
 * <p>
 * Exactamente como llegue a este diseño a partir de la clase de bloqueo anterior es una historia mas larga que involucra varios
 * pasos de diseño incrementales, cada uno de los cuales soluciona el problema del paso anterior: Nested Monitor Lockout, Slipped
 * Conditions, and Missed Signals. Esa discusion se omite en este texto para que sea breve, pero cada uno de los pasos se analiza
 * en los textos apropiados sobre el tema. Lo importante es que cada hilo que llama a lock() ahora esta en cola, y solo el primer
 * hilo en la cola puede bloquear la instancia de FairLock, si esta desbloqueada. Todos los demas subprocesos se estacionan
 * esperando hasta llegar al principio de la cola.
 * <pre>{@code
 * public class FairLock {
 *
 *     private boolean isLocked;
 *     private Thread lockingThread;
 *     private List<QueueObject> waitingThreads = new ArrayList<>();
 *
 *     public void lock() throws InterruptedException {
 *         QueueObject queueObject = new QueueObject();
 *         boolean isLockedForThisThread = true;
 *         synchronized (this) {
 *             waitingThreads.add(queueObject);
 *         }
 *
 *         while (isLockedForThisThread) {
 *             synchronized (this) {
 *                 isLockedForThisThread = isLocked || waitingThreads.get(0) != queueObject;
 *                 if (!isLockedForThisThread) {
 *                     isLocked = true;
 *                     waitingThreads.remove(queueObject);
 *                     lockingThread = Thread.currentThread();
 *                     return;
 *                 }
 *             }
 *
 *             try {
 *                 queueObject.doWait();
 *             } catch(InterruptedException e) {
 *                 synchronized (this) {
 *                     waitingThreads.remove(queueObject);
 *                 }
 *                 throw e;
 *             }
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
 *             waitingThreads.get(0).doNotify();
 *         }
 *     }
 *
 * }
 * }</pre>
 * <pre>{@code
 * public class QueueObject {
 *
 *     private boolean isNotified;
 *
 *     public synchronized void doWait() throws InterruptedException {
 *         while (!isNotified) {
 *             wait();
 *         }
 *         isNotified = false;
 *     }
 *
 *     public synchronized void doNotify() {
 *         isNotified = true;
 *         notify();
 *     }
 *
 *     public boolean equals(Object o) {
 *         return this == o;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * En resumen, el metodo lock() ya no se declara synchronized, sino que se utilizan bloques sincronizados especificos. FairLock
 * crea y encola una instancia de QueueObject para cada hilo que llama a lock(). El hilo que llama a unlock() despierta solo al
 * hilo superior en la cola, garantizando la equidad. El estado de lock se prueba y establece dentro del mismo bloque sincronizado
 * para evitar condiciones de slipped. QueueObject funciona como un semaforo, almacenando señales internamente para evitar señales
 * perdidas. La llamada a queueObject.doWait() se coloca fuera del bloque sincronizado para evitar bloqueos anidados. Finalmente,
 * queueObject.doWait() se llama dentro de un bloque try-catch para manejar posibles InterruptedExceptions y retirar el hilo de la
 * cola si es necesario.
 * <h4>Una nota sobre el rendimiento</h4>
 * <p>
 * Si compara las clases Lock y FairLock, notara que suceden algo mas dentro de lock() y unlock() en la clase FairLock. Este
 * codigo adicional hara que FairLock sea un mecanismo de sincronizacion ligeramente mas lento que Lock. El impacto que esto
 * tendra en su aplicacion depende de cuanto tiempo tarde en ejecutarse el codigo en la seccion critica protegida por FairLock.
 * Cuanto mas tarde en ejecutarse, menos significativa sera la sobrecarga adicional del sincronizador. Por supuesto, tambien
 * depende de la frecuencia con la que se llama a este codigo.
 * <p>
 * Fuente: <a href="https://jenkov.com/tutorials/java-concurrency/starvation-and-fairness.html">Starvation and Fairness</a>
 */

public class StarvationFairness {
}
