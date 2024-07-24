package concurrency.thread_pools;

/**
 * Este codigo implementa un sistema de pool de hilos personalizado. El ThreadPool gestiona un conjunto de hilos (PoolThreadRunnable)
 * que toman tareas de una cola compartida y las ejecutan. Esto permite la ejecucion concurrente de multiples tareas de manera
 * eficiente, reutilizando los hilos en lugar de crear uno nuevo para cada tarea.
 */

public class ThreadPoolMain {

    public static void main(String[] args) throws Exception {

        // Crea una instancia de ThreadPool con 3 hilos de trabajo y una capacidad maxima de 10 tareas
        ThreadPool threadPool = new ThreadPool(3, 10);

        // Envia 10 tareas al pool para su ejecucion
        for (int i = 0; i < 10; i++) {
            int taskNo = i;
            // Cada tarea simplemente imprime un mensaje con el nombre del hilo y el numero de la tarea
            threadPool.execute(() -> {
                String message = Thread.currentThread().getName() + ": Task " + taskNo;
                System.out.println(message);
            });
        }

        // Espera a que todas las tareas se completen y luego detiene el pool
        threadPool.waitUntilAllTasksFinished();
        threadPool.stop();
    }

}
