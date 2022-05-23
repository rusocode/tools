package concurrency;

/**
 * Ejemplo de como se detiene un subproceso.
 * <p>
 * Fuente:
 * <a href="http://tutorials.jenkov.com/java-concurrency/index.html">Concurrency</a>
 * <a href="https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html">Creating and Starting Java Threads</a>
 *
 * @author Ruso
 */

public class ThreadStop {

	private static class Subproceso implements Runnable {

		private final Thread subproceso;
		private boolean stopped;

		public Subproceso(String name) {
			subproceso = new Thread(this, name);
		}

		@Override
		public void run() {

			while (!isStopped()) {

				System.out.println(subproceso.getName() + " running");

				try {
					Thread.sleep(500L); // Detiene el subproceso por 3 segundos
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

		private void ejecutar() {
			subproceso.start();
		}

		public /* synchronized */ void stop() { // TODO Hace falta que lo sincronize?
			stopped = true;
		}

		/* Reemplaze el metodo keepRunning() por este, ya que es mas simple y tiene mas sentido comprobar el estado del
		 * subproceso de esta manera. */
		private synchronized boolean isStopped() {
			return stopped;
		}

	}

	public static void main(String[] args) throws InterruptedException {

		Subproceso subproceso = new Subproceso("A");

		subproceso.ejecutar();

		// Duerme el hilo principal antes de detener el subproceso
		Thread.sleep(4L * 1000L);

		subproceso.stop();

	}

}