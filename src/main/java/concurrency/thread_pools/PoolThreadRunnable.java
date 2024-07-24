
package concurrency.thread_pools;

import java.util.concurrent.BlockingQueue;

/**
 * Esta clase representa un hilo de trabajo en el pool de hilos. Cada instancia de esta clase se ejecuta en su propio hilo,
 * tomando tareas de la cola compartida y ejecutandolas. El hilo puede ser detenido de manera segura usando el metodo doStop(). El
 * uso de BlockingQueue permite que los hilos esperen eficientemente cuando no hay tareas disponibles. Los metodos doStop() e
 * isStopped() son sincronizados para garantizar la seguridad en entornos multihilo.
 * <p>
 * Esta implementacion permite que multiples hilos trabajen en paralelo, tomando tareas de una cola compartida, lo que es la base
 * del funcionamiento del pool de hilos.
 */

public class PoolThreadRunnable implements Runnable {

    // Almacena una referencia al hilo actual que esta ejecutando esta instancia
    private Thread thread;
    // Cola bloqueante compartida de donde se toman las tareas a ejecutar
    private final BlockingQueue taskQueue;
    private boolean stopped;

    /**
     * Inicializa la instancia con la cola de tareas compartida.
     *
     * @param taskQueue cola de tareas.
     */
    public PoolThreadRunnable(BlockingQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        thread = Thread.currentThread();
        // Mientras el hilo no este detenido
        while (!isStopped()) {
            try {
                /* Intenta tomar una tarea de la cola (taskQueue.take()). Este metodo es bloqueante, es decir, si la cola esta
                 * vacia, el hilo esperara hasta que haya una tarea disponible. */
                Runnable runnable = (Runnable) taskQueue.take();
                // Ejecuta la tarea
                runnable.run();
            } catch (Exception e) {
                // Registre o informe de alguna otra manera la excepcion, pero mantenga vivo el subproceso del grupo
                System.out.println("Error:" + e.getMessage());
            }
        }
    }

    public synchronized void doStop() {
        stopped = true;
        // Interrumpe el hilo, lo que puede hacer que salga del metodo take() si estaba esperando por una tarea
        thread.interrupt();
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

}
