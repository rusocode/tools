
package concurrency.thread_pools;

import java.util.concurrent.BlockingQueue;

/**
 * Implementa los subprocesos que ejecutan las tareas.
 */

public class PoolThreadRunnable implements Runnable {

	private Thread thread;
	private BlockingQueue taskQueue;
	private boolean isStopped;

	public PoolThreadRunnable(BlockingQueue taskQueue) {
		this.taskQueue = taskQueue;
	}

	@Override
	public void run() {

		thread = Thread.currentThread(); // ?

		// Si el hilo no esta detenido
		while (!isStopped()) {

			try {

				/* Recupera y elimina el encabezado de esta cola (primer tarea). Si la cola no contiene ningun elemento, el metodo
				 * take() bloqueara el hilo que llama a take() hasta que se inserte un elemento en la cola. En caso que de que haya
				 * un elemento o mas, entonces la tarea se asigna a un subproceso (inactivo) dentro del grupo de subprocesos y lo
				 * ejecuta. */
				Runnable runnable = (Runnable) taskQueue.take();
				// Ejecuta la tarea
				runnable.run();

			} catch (Exception e) {
				// Registre o informe de alguna otra manera la excepcion, pero mantenga vivo el subproceso del grupo
			}
		}

	}

	public synchronized void doStop() {
		isStopped = true;
		// Rompe el hilo del grupo fuera de la llamada de la cola
		thread.interrupt();
	}

	public synchronized boolean isStopped() {
		return isStopped;
	}

}
