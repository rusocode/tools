package com.punkipunk.concurrency;

/**
 * <p>
 * Un lock de read/write es una implementacion avanzada que permite multiples lectores simultaneos de un recurso, pero solo un
 * escritor a la vez. Este tipo de bloqueo es util en aplicaciones donde la lectura es mas frecuente que la escritura. Mientras
 * varios hilos pueden leer el recurso al mismo tiempo sin problemas, cuando un hilo necesita escribir, debe tener acceso
 * exclusivo sin otras lecturas o escrituras concurrentes. Java 5 incluye implementaciones de bloqueos de lectura/escritura en el
 * paquete java.util.concurrent, aunque es valioso comprender la teoria detras de su funcionamiento.
 * <h3>Implementacion de Read/Write Lock</h3>
 * <p>
 * Primero, resumamos las condiciones para obtener acceso de lectura y escritura al recurso:
 * <pre>{@code
 * Read Access  Si ningun hilo esta escribiendo y ningun hilo ha solicitado acceso de escritura.
 * Write Access Si no hay hilos leyendo o escribiendo.
 * }</pre>
 * <p>
 * Un hilo puede leer un recurso si no hay escrituras en curso ni solicitudes de escritura pendientes. Se priorizan las
 * solicitudes de escritura para evitar la inanicion (starvation) de los hilos escritores. Los hilos lectores solo obtienen acceso
 * si no hay bloqueos ni solicitudes de escritura activos. Un hilo escritor obtiene acceso cuando ningun otro hilo esta leyendo o
 * escribiendo. La secuencia de solicitudes de escritura no es relevante, a menos que se busque garantizar equidad entre los hilos
 * escritores. Este enfoque equilibra el acceso al recurso, permitiendo lecturas concurrentes mientras protege las operaciones de
 * escritura y evita el bloqueo indefinido de los hilos escritores.
 * <p>
 * Con estas reglas simples en mente, podemos implementar {@code ReadWriteLock} como se muestra a continuacion:
 * <pre>{@code
 * public class ReadWriteLock {
 *
 *     private int readers;
 *     private int writers;
 *     private int writeRequests;
 *
 *     // Todos los subprocesos obtienen acceso de lectura a menos que haya un
 *     // subproceso con acceso de escritura o que uno o mas subprocesos hayan
 *     // solicitado acceso de escritura.
 *     public synchronized void lockRead() throws InterruptedException {
 *         while (writers > 0 || writeRequests > 0) wait();
 *         readers++;
 *     }
 *
 *     public synchronized void unlockRead() {
 *         readers--;
 *         notifyAll();
 *     }
 *
 *     // Un subproceso puede obtener acceso de escritura si no hay subprocesos
 *     // con acceso de lectura al recurso ni subprocesos con acceso de escritura
 *     // al recurso.
 *     public synchronized void lockWrite() throws InterruptedException {
 *         writeRequests++;
 *         while (readers > 0 || writers > 0) wait();
 *         writeRequests--;
 *         writers++;
 *     }
 *
 *     public synchronized void unlockWrite() throws InterruptedException {
 *         writers--;
 *         notifyAll();
 *     }
 *
 * }
 * }</pre>
 * <p>
 * ReadWriteLock implementa metodos de bloqueo y desbloqueo para accesos de lectura y escritura. El metodo {@code lockRead()}
 * permite acceso de lectura a menos que haya escrituras activas o solicitadas. {@code lockWrite()} gestiona el acceso de
 * escritura, permitiendolo solo cuando no hay lecturas ni escrituras en curso. Tanto {@code unlockRead()} como
 * {@code unlockWrite()} utilizan {@code notifyAll()} en lugar de {@code notify()} para evitar situaciones de bloqueo y permitir
 * que multiples lectores accedan simultaneamente cuando es posible. Esta implementacion equilibra eficientemente el acceso entre
 * lectores y escritores, priorizando las solicitudes de escritura para prevenir la inanicion de los hilos escritores, mientras
 * permite la concurrencia de lectores cuando es seguro hacerlo.
 * <h3>Read/Write Lock Reentrance</h3>
 * <p>
 * La clase ReadWriteLock mostrada anteriormente no es reentrante. Si un hilo que tiene acceso de escritura lo solicita
 * nuevamente, se bloqueara porque ya hay un escritor: el mismo. Ademas, considere este caso: <ol
 * <li>El hilo 1 obtiene acceso de lectura.
 * <li>El hilo 2 solicita acceso de escritura pero esta bloqueado porque hay un lector.
 * <li>El hilo 1 vuelve a solicitar acceso de lectura (vuelve a ingresar al bloqueo), pero se bloquea porque hay una solicitud de
 * escritura.
 * </ol>
 * <p>
 * En esta situacion, el ReadWriteLock anterior se bloquearia, una situacion similar a un deadlock. No se concedera ningun hilo
 * que solicite acceso de lectura ni de escritura.
 * <p>
 * Para que ReadWriteLock sea reentrante, es necesario realizar algunos cambios. El reingreso de lectores y escritores se tratara
 * por separado.
 * <h3>Read Reentrance</h3>
 * <p>
 * Para que ReadWriteLock sea reentrante para los lectores, primero estableceremos las reglas para la reentrada de lectura:
 * <ul>
 * <li>A un hilo se le concede reentrada de lectura si puede obtener acceso de lectura (sin escritores ni solicitudes de
 * escritura), o si ya tiene acceso de lectura (independientemente de las solicitudes de escritura).
 * </ul>
 * <p>
 * Para determinar si un subproceso ya tiene acceso de lectura, se mantiene en un Map una referencia a cada subproceso al que se
 * le ha concedido acceso de lectura junto con cuantas veces ha adquirido el bloqueo de lectura. Al determinar si se puede otorgar
 * acceso de lectura, se verificara que este Map tenga una referencia al hilo de llamada. Asi es como se ven los metodos
 * lockRead() y unlockRead() despues de ese cambio:
 * <pre>{@code
 * public class ReadWriteLock {
 *
 *     private Map<Thread, Integer> readingThreads = new HashMap<Thread, Integer>();
 *
 *     private int writers;
 *     private int writeRequests;
 *
 *     public synchronized void lockRead() throws InterruptedException {
 *         Thread callingThread = Thread.currentThread();
 *         while (!canGrantReadAccess(callingThread)) wait();
 *         readingThreads.put(callingThread, (getAccessCount(callingThread) + 1));
 *     }
 *
 *     public synchronized void unlockRead() {
 *         Thread callingThread = Thread.currentThread();
 *         int accessCount = getAccessCount(callingThread);
 *         if (accessCount == 1) readingThreads.remove(callingThread);
 *         else readingThreads.put(callingThread, (accessCount -1));
 *         notifyAll();
 *     }
 *
 *     // Verifica si puede conceder acceso a lectura
 *     private boolean canGrantReadAccess(Thread callingThread) {
 *         if (writers > 0) return false;
 *         if (isReader(callingThread)) return true;
 *         if (writeRequests > 0) return false;
 *         return true;
 *     }
 *
 *     private int getReadAccessCount(Thread callingThread) {
 *         Integer accessCount = readingThreads.get(callingThread);
 *         if (accessCount == null) return 0;
 *         return accessCount.intValue();
 *     }
 *
 *     private boolean isReader(Thread callingThread) {
 *         return readingThreads.get(callingThread) != null;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Como puede ver, la reentrada de lectura solo se otorga si actualmente no hay subprocesos escribiendo en el recurso. Ademas, si
 * el hilo que llama ya tiene acceso de lectura, esto tiene prioridad sobre cualquier solicitud de escritura.
 * <h3>Write Reentrance</h3>
 * <p>
 * La reentrada de escritura se concede solo si el hilo ya tiene acceso de escritura. Asi es como se ven los metodos lockWrite() y
 * unlockWrite() despues de ese cambio:
 * <pre>{@code
 * public class ReadWriteLock {
 *
 *     private Map<Thread, Integer> readingThreads = new HashMap<Thread, Integer>();
 *
 *     private int writeAccesses;
 *     private int writeRequests;
 *     private Thread writingThread;
 *
 *     public synchronized void lockWrite() throws InterruptedException {
 *         writeRequests++;
 *         Thread callingThread = Thread.currentThread();
 *         while (!canGrantWriteAccess(callingThread)) wait();
 *         writeRequests--;
 *         writeAccesses++;
 *         writingThread = callingThread;
 *     }
 *
 *     public synchronized void unlockWrite() throws InterruptedException {
 *         writeAccesses--;
 *         if (writeAccesses == 0) writingThread = null;
 *         notifyAll();
 *     }
 *
 *     private boolean canGrantWriteAccess(Thread callingThread) {
 *         if (hasReaders()) return false;
 *         if (writingThread == null) return true;
 *         if (!isWriter(callingThread)) return false;
 *         return true;
 *     }
 *
 *     private boolean hasReaders() {
 *         return readingThreads.size() > 0;
 *     }
 *
 *     private boolean isWriter(Thread callingThread) {
 *         return writingThread == callingThread;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como el subproceso que actualmente tiene el bloqueo de escritura ahora se tiene en cuenta al determinar si el
 * subproceso que realiza la llamada puede obtener acceso de escritura.
 * <h3>Read to Write Reentrance</h3>
 * <p>
 * A veces es necesario que un hilo que tiene acceso de lectura tambien obtenga acceso de escritura. Para que esto se permita, el
 * hilo debe ser el unico lector. Para lograr esto, el metodo {@code writeLock()} debe cambiarse un poco. Asi es como se veria:
 * <pre>{@code
 * public class ReadWriteLock {
 *
 *     private Map<Thread, Integer> readingThreads = new HashMap<Thread, Integer>();
 *
 *     private int writeAccesses;
 *     private int writeRequests;
 *     private Thread writingThread;
 *
 *     public synchronized void lockWrite() throws InterruptedException {
 *         writeRequests++;
 *         Thread callingThread = Thread.currentThread();
 *         while (!canGrantWriteAccess(callingThread)) wait();
 *         writeRequests--;
 *         writeAccesses++;
 *         writingThread = callingThread;
 *     }
 *
 *     public synchronized void unlockWrite() throws InterruptedException {
 *         writeAccesses--;
 *         if (writeAccesses == 0) writingThread = null;
 *         notifyAll();
 *     }
 *
 *     private boolean canGrantWriteAccess(Thread callingThread) {
 *         if (isOnlyReader(callingThread)) return true;
 *         if (hasReaders()) return false;
 *         if (writingThread == null) return true;
 *         if (!isWriter(callingThread)) return false;
 *         return true;
 *     }
 *
 *     private boolean hasReaders() {
 *         return readingThreads.size() > 0;
 *     }
 *
 *     private boolean isWriter(Thread callingThread) {
 *         return writingThread == callingThread;
 *     }
 *
 *     private boolean isOnlyReader(Thread thread) {
 *         return readingThreads.size() == 1 && readingThreads.get(callingThread) != null;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Ahora la clase ReadWriteLock tiene acceso de lectura y escritura reentrante.
 * <h3>Write to Read Reentrance</h3>
 * <p>
 * A veces, un hilo que tiene acceso de escritura tambien necesita acceso de lectura. A un escritor siempre se le debe conceder
 * acceso de lectura si lo solicita. Si un hilo tiene acceso de escritura, ningun otro hilo puede tener acceso de lectura ni de
 * escritura, por lo que no es peligroso. Asi es como se vera el metodo {@code canGrantReadAccess()} con ese cambio:
 * <pre>{@code
 * public class ReadWriteLock{
 *
 *     private boolean canGrantReadAccess(Thread callingThread) {
 *         if (isWriter(callingThread)) return true;
 *         if (writingThread != null) return false;
 *         if (isReader(callingThread) return true;
 *         if (writeRequests > 0) return false;
 *         return true;
 *     }
 *
 * }
 * }</pre>
 * <h3>ReadWriteLock completamente reentrante</h3>
 * <p>
 * A continuacion se muestra la implementacion ReadWriteLock de reentrada completa. He realizado algunas refactorizaciones en las
 * condiciones de acceso para hacerlas mas faciles de leer y, por lo tanto, mas facil convencerse de que son correctas.
 * <pre>{@code
 * public class ReadWriteLock {
 *
 *     private Map<Thread, Integer> readingThreads = new HashMap<Thread, Integer>();
 *
 *     private int writeAccesses, writeRequests;
 *     private Thread writingThread;
 *
 *     public synchronized void lockRead() throws InterruptedException {
 *         Thread callingThread = Thread.currentThread();
 *         while (!canGrantReadAccess(callingThread)) wait();
 *         readingThreads.put(callingThread, (getReadAccessCount(callingThread) + 1));
 *     }
 *
 *     private boolean canGrantReadAccess(Thread callingThread) {
 *         if (isWriter(callingThread)) return true;
 *         if (hasWriter()) return false;
 *         if (isReader(callingThread)) return true;
 *         if (hasWriteRequests()) return false;
 *         return true;
 *     }
 *
 *     public synchronized void unlockRead() {
 *         Thread callingThread = Thread.currentThread();
 *         if (!isReader(callingThread)) throw new IllegalMonitorStateException("Calling Thread does not hold a read lock on this ReadWriteLock");
 *         int accessCount = getReadAccessCount(callingThread);
 *         if (accessCount == 1) readingThreads.remove(callingThread);
 *         else readingThreads.put(callingThread, (accessCount -1));
 *         notifyAll();
 *     }
 *
 *     public synchronized void lockWrite() throws InterruptedException {
 *         writeRequests++;
 *         Thread callingThread = Thread.currentThread();
 *         while(!canGrantWriteAccess(callingThread)) wait();
 *         writeRequests--;
 *         writeAccesses++;
 *         writingThread = callingThread;
 *     }
 *
 *     public synchronized void unlockWrite() throws InterruptedException {
 *         if (!isWriter(Thread.currentThread()) throw new IllegalMonitorStateException("Calling Thread does not hold the write lock on this ReadWriteLock");
 *         writeAccesses--;
 *         if (writeAccesses == 0) writingThread = null;
 *         notifyAll();
 *     }
 *
 *     private boolean canGrantWriteAccess(Thread callingThread) {
 *         if (isOnlyReader(callingThread)) return true;
 *         if (hasReaders()) return false;
 *         if (writingThread == null) return true;
 *         if (!isWriter(callingThread)) return false;
 *         return true;
 *     }
 *
 *     private int getReadAccessCount(Thread callingThread) {
 *         Integer accessCount = readingThreads.get(callingThread);
 *         if (accessCount == null) return 0;
 *         return accessCount.intValue();
 *     }
 *
 *     private boolean hasReaders() {
 *         return readingThreads.size() > 0;
 *     }
 *
 *     private boolean isReader(Thread callingThread) {
 *         return readingThreads.get(callingThread) != null;
 *     }
 *
 *     private boolean isOnlyReader(Thread callingThread) {
 *         return readingThreads.size() == 1 && readingThreads.get(callingThread) != null;
 *     }
 *
 *     private boolean hasWriter() {
 *         return writingThread != null;
 *     }
 *
 *     private boolean isWriter(Thread callingThread) {
 *         return writingThread == callingThread;
 *     }
 *
 *     private boolean hasWriteRequests() {
 *         return this.writeRequests > 0;
 *     }
 *
 * }
 * }</pre>
 * <h3>Llamar a unlock() desde una clausula finally</h3>
 * <p>
 * Al proteger una seccion critica con ReadWriteLock, y la seccion critica puede generar excepciones, es importante llamar a los
 * metodos readUnlock() y writeUnlock() desde dentro de una clausula {@code finally}. Al hacerlo, se garantiza que ReadWriteLock
 * este desbloqueado para que otros subprocesos puedan bloquearlo. Aqui hay un ejemplo:
 * <pre>{@code
 * lock.lockWrite();
 * try {
 *     // Do critical section code, which may throw exception
 * } finally {
 *     lock.unlockWrite();
 * }
 * }</pre>
 * <p>
 * Fuente: <a href="https://jenkov.com/tutorials/java-concurrency/read-write-locks.html">Read / Write Locks in Java</a>
 */

public class ReadWriteLock {
}
