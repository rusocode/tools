package concurrency;

/**
 * Ejemplo de como se detiene un subproceso.
 *
 * <p>Recursos:
 * <a href="https://www.youtube.com/watch?v=eQk5AWcTS8w">Detener subprocesos</a>
 *
 * @author Juan Debenedetti
 */

public class Stop {

	private static final int TIEMPO_BLOQUEADO = 1000;

	private static class Subproceso implements Runnable {

		private final Thread subproceso;
		private boolean stopped;

		public Subproceso(String name) {
			subproceso = new Thread(this, name);
		}

		@Override
		public void run() {

			// Mientras el subproceso no este detenido
			while (!isStopped()) {

				System.out.println("Ejecutando subproceso " + subproceso.getName());

				try {
					Thread.sleep(TIEMPO_BLOQUEADO);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

		private synchronized void start() {
			subproceso.start();
		}

		public synchronized void stop() { // TODO Hace falta que lo sincronize?
			stopped = true;
		}

		/* Reemplaze el metodo keepRunning() por este, ya que es mas simple y tiene mas sentido comprobar el estado del
		 * subproceso de esta manera. */
		private synchronized boolean isStopped() {
			return stopped;
		}

	}

	public static void main(String[] args) throws InterruptedException {

		System.out.println("Ejecutando subrpoceso " + Thread.currentThread().getName());

		Subproceso A = new Subproceso("A");

		A.start();

		Thread.sleep(4L * TIEMPO_BLOQUEADO);

		A.stop();

	}

}