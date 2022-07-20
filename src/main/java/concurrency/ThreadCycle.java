package concurrency;

/**
 * Esta clase ejecuta el subproceso principal en paralelo con los subprocesos A y B, en donde se muestra el
 * cilo de vida.
 * <br><br>
 * El ciclo de un subproceso es:
 * <li>Nuevo</li>
 * <li>Ejecutado</li>
 * <li>Bloqueado</li>
 * <li>Terminado</li>
 * <br>
 * <a href="https://stackoverflow.com/questions/15680422/difference-between-wait-and-blocked-thread-states">Difference between WAIT and BLOCKED thread states</a>
 *
 * @author Ruso
 */

public class ThreadCycle {

	private static final int TIEMPO_BLOQUEADO = 1000;

	private static class Subproceso implements Runnable {

		private final Thread subproceso;
		private boolean blocked, stopped;

		public Subproceso(String name) {
			subproceso = new Thread(this, name);
		}

		@Override
		public void run() {

			System.out.println("Ejecutando subproceso " + subproceso.getName());

			try {

				for (int i = 1; i <= 100; i++) {

					System.out.print(i + " ");

					// Cada 10 columnas salta de linea y pausa el subproceso
					if ((i % 10) == 0) {
						System.out.println();
						Thread.sleep(500);
					}

					// Sincroniza este bloque de codigo
					synchronized (this) {
						/* Mientras el subproceso este bloqueado, entra en estado de espera hasta que se desbloquee,
						 * normalmente al ser notificado o interrumpido. */
						while (blocked)
							wait();

						if (stopped) break;

					}

				}

			} catch (InterruptedException e) {
				System.out.println("Subproceso " + subproceso.getName() + " interrumpido");
			}

			System.out.println("Subproceso " + subproceso.getName() + " terminado");

		}

		private void start() {
			subproceso.start();
		}

		private void block() {
			blocked = true;
			System.out.println("Subproceso " + subproceso.getName() + " bloqueado");
		}

		private synchronized void unlock() {
			blocked = false;
			notify(); // A diferencia de notifyAll(), este despierta uno de los subprocesos en espera
			System.out.println("Subproceso " + subproceso.getName() + " desbloqueado");
		}

		/**
		 * TODO No tiene que ser un metodo sincronizado?
		 */
		private void stop() {
			stopped = true;
			System.out.println("Subproceso " + subproceso.getName() + " detenido");
		}

		public void join() {
			try {
				/* El metodo join() que se llamamo al final, hace que el subproceso principal espere (synchronized) hasta que el
				 * subproceso actual termine. */
				subproceso.join();
			} catch (InterruptedException e) {
				System.out.println("Subproceso " + subproceso.getName() + " interrumpido");
			}

		}

	}

	public static void main(String[] args) throws InterruptedException {

		// Subproceso principal (monotarea)
		System.out.println("Ejecutando subrpoceso " + Thread.currentThread().getName());

		// 1. Nuevo
		Subproceso A = new Subproceso("A");

		// 2. Ejecutado
		A.start();

		// Pausa el subproceso principal antes de bloquear el subproceso A
		Thread.sleep(TIEMPO_BLOQUEADO);
		// 3. Bloqueado
		A.block();
		// Pausa el subproceso principal antes de desbloquear el subproceso A
		Thread.sleep(TIEMPO_BLOQUEADO);
		A.unlock();

		// 4. Terminado
		A.join();

		Subproceso B = new Subproceso("B");

		// Pausa el subproceso principal antes de ejecutar el subproceso B
		Thread.sleep(TIEMPO_BLOQUEADO);
		B.start();
		Thread.sleep(TIEMPO_BLOQUEADO);
		B.stop();
		B.join();

		System.out.println("Subproceso " + Thread.currentThread().getName() + " terminado");
	}

}