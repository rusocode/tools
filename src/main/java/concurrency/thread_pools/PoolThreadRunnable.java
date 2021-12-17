
package concurrency.thread_pools;

import java.util.concurrent.BlockingQueue;

/**
 * Clase PoolThreadRunnable que implementa la interfaz Runnable, por lo que puede ser ejecutada por un hilo de Java.
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
		thread = Thread.currentThread();
		// Si el hilo no esta detenido
		while (!isStopped()) {
			try {
				Runnable runnable = (Runnable) taskQueue.take();
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
