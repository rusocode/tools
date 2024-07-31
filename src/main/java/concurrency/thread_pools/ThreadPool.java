package concurrency.thread_pools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <h3>Thread Pools</h3>
 * <p>
 * Un <i>thread pool</i> es un grupo de subprocesos que se pueden "reutilizar" para ejecutar tareas (tasks), de modo que cada
 * subproceso (thread) pueda ejecutar mas de una tarea. Un thread pool es una alternativa a la creacion de un nuevo subproceso
 * para cada tarea que necesita ejecutar.
 * <p>
 * La creacion de un nuevo hilo conlleva una sobrecarga de rendimiento en comparacion con la reutilizacion de un hilo que ya esta
 * creado. Es por eso que reutilizar un subproceso existente para ejecutar una tarea puede generar un rendimiento total mayor que
 * crear un nuevo subproceso por tarea.
 * <p>
 * Ademas, el uso de un grupo de subprocesos puede facilitar el control de cuantos subprocesos estan activos a la vez. Cada
 * subproceso consume una cierta cantidad de recursos de la computadora, como la memoria (RAM), por lo que si tiene demasiados
 * subprocesos activos al mismo tiempo, la cantidad total de recursos (por ejemplo, RAM) que se consume puede hacer que la
 * computadora se ralentice. p.ej. si se consume tanta RAM que el sistema operativo (SO) comienza a intercambiar RAM al disco.
 * <p>
 * Tenga en cuenta que Java ya contiene un grupo de subprocesos integrado (Java ExecutorService), por lo que puede utilizar un
 * grupo de subprocesos en Java sin tener que implementarlo usted mismo. Sin embargo, de vez en cuando es posible que desee
 * implementar su propio grupo de subprocesos para agregar funciones que no son compatibles con ExecutorService. O tal vez desee
 * implementar su propio grupo de subprocesos Java simplemente como una experiencia de aprendizaje.
 * <h3>Como funciona un grupo de subprocesos</h3>
 * <p>
 * En lugar de iniciar un nuevo subproceso para que cada tarea se ejecute simultaneamente, la tarea se puede pasar a un grupo de
 * subprocesos. Tan pronto como el grupo tiene subprocesos inactivos, la tarea se asigna a uno de ellos y se ejecuta. Internamente,
 * las tareas se insertan en una cola de bloqueo (BlockingQueue) de la que los subprocesos del grupo se retiran de la cola. Cuando
 * se inserta una nueva tarea en la cola, uno de los subprocesos inactivos la sacara de la cola con exito y la ejecutara. El resto
 * de los subprocesos inactivos del grupo se bloquearan a la espera de retirar las tareas de la cola.
 * <p>
 * <img src="funcionamiento del thread pool.PNG">
 * <h3>Casos de uso del grupo de subprocesos</h3>
 * <p>
 * Los grupos de subprocesos se utilizan a menudo en servidores de subprocesos multiples. Cada conexion que llega al servidor a
 * traves de la red se envuelve como una tarea y se pasa a un grupo de subprocesos. Los subprocesos del grupo de subprocesos
 * procesaran las solicitudes en las conexiones al mismo tiempo.
 * <h4>Implementacion</h4>
 * <p>
 * Este es un pool de hilos personalizado que maneja un numero fijo de hilos. Las tareas se añaden a una cola y los hilos del pool
 * las toman y ejecutan. El pool puede ser detenido, y proporciona un metodo para esperar a que todas las tareas se completen. Los
 * metodos son sincronizados para garantizar la seguridad en entornos multihilo.
 */

public class ThreadPool {

    // Cola bloqueante que almacena las tareas a ejecutar
    private final BlockingQueue taskQueue;
    // Lista de objetos PoolThreadRunnable, que son los hilos de trabajo
    private final List<PoolThreadRunnable> runnables = new ArrayList<>();
    private boolean stopped;

    public ThreadPool(int noOfThreads, int maxNoOfTasks) {

        // Crea una cola de bloqueo para las tareas con la capacidad especificada
        taskQueue = new ArrayBlockingQueue<>(maxNoOfTasks);

        // Crea noOfThreads instancias de PoolThreadRunnable y las añade a la lista runnables
        for (int i = 0; i < noOfThreads; i++)
            runnables.add(new PoolThreadRunnable(taskQueue));

        // Inicia un nuevo hilo para cada PoolThreadRunnable
        for (PoolThreadRunnable runnable : runnables)
            new Thread(runnable).start();

    }

    /**
     * Añade una nueva tarea a la cola si el pool no esta detenido.
     *
     * @param task nueva tarea.
     */
    public synchronized void execute(Runnable task) {
        if (stopped) throw new IllegalStateException("ThreadPool is stopped");
        /* Agrega el elemento pasado como parametro a este metodo si BlockingQueue tiene espacio para el internamente. Si
         * BlockingQueue no tiene espacio internamente para este nuevo elemento, el metodo offer() devuelve falso. El Runnable
         * esta en la cola de bloqueo internamente, esperando ser retirado de la cola. El Runnable sera retirado de la cola por un
         * PoolThread inactivo y ejecutado. Despues de la ejecucion, PoolThread realiza un bucle e intenta sacar de la cola una
         * tarea nuevamente, hasta que se detiene. */
        taskQueue.offer(task);
    }

    /**
     * Detiene todos los hilos de trabajo.
     */
    public synchronized void stop() {
        stopped = true;
        for (PoolThreadRunnable runnable : runnables)
            runnable.doStop();
    }

    /**
     * Espera hasta que todas las tareas en la cola se completen.
     */
    public synchronized void waitUntilAllTasksFinished() {
        while (!taskQueue.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

}
