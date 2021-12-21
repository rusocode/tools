package concurrency.thread_pools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Interfaz pública para el grupo de subprocesos.
 */

public class ThreadPool {

	private BlockingQueue taskQueue;
	private List<PoolThreadRunnable> runnables = new ArrayList<>();
	private boolean isStopped;

	public ThreadPool(int noOfThreads, int maxNoOfTasks) {

		// Crea una cola de bloqueo con la capacidad especificada
		taskQueue = new ArrayBlockingQueue<>(maxNoOfTasks);

		for (int i = 0; i < noOfThreads; i++)
			/* Agrega los grupos de subprocesos ejecutables a la lista utilizando una cola de bloqueo para cada uno, evitando asi la
			 * congestion de subprocesos. */
			runnables.add(new PoolThreadRunnable(taskQueue));

		/* Ejecuta el grupo de subprocesos. Despues de la ejecucion, PoolThread se repite e intenta sacar una tarea de la cola
		 * nuevamente, hasta que se detiene. */
		for (PoolThreadRunnable runnable : runnables)
			new Thread(runnable).start();

	}

	public synchronized void execute(Runnable task) throws Exception {

		if (isStopped) throw new IllegalStateException("ThreadPool is stopped");

		/* Agrega el elemento pasado como parametro a este metodo si BlockingQueue tiene espacio para el internamente. Si
		 * BlockingQueue no tiene espacio internamente para este nuevo elemento, el metodo offer() devuelve falso. */
		taskQueue.offer(task);

	}

	public synchronized void stop() {
		isStopped = true;
		for (PoolThreadRunnable runnable : runnables)
			runnable.doStop();
	}

	public synchronized void waitUntilAllTasksFinished() {
		while (taskQueue.size() > 0) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
