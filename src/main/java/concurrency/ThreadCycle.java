package concurrency;

/**
 * Esta clase ejecuta el subproceso principal en paralelo con los subprocesos A y B, en donde se muestra el
 * cilo de vida.
 * <p>
 * El ciclo de un subproceso es:
 * <li>Nuevo</li>
 * <li>Ejecutado</li>
 * <li>Bloqueado</li>
 * <li>Terminado</li>
 * <p>
 * <a href="https://stackoverflow.com/questions/15680422/difference-between-wait-and-blocked-thread-states">Difference between WAIT and BLOCKED thread states</a>
 * <p>
 * ¿Cual es la diferencia entre bloquear y detener (stop)?
 *
 * @author Ruso
 */

public class ThreadCycle {

	private static final int TIEMPO_BLOQUEADO = 2000;

	private static class Subproceso implements Runnable {

		private final Thread subproceso;
		private boolean bloqueado, stopped;

		public Subproceso(String name) {
			subproceso = new Thread(this, name);
		}

		@Override
		public void run() {

			System.out.println("Ejecutando subproceso " + subproceso.getName());

			try {

				for (int i = 1; i <= 100; i++) {

					System.out.print(i + " ");

					// Cada 10 columnas salta de linea y bloquea el subproceso
					if ((i % 10) == 0) {
						System.out.println();
						Thread.sleep(500);
					}

					// Sincroniza este bloque de codigo
					synchronized (this) {
						// Mientras el subproceso este bloqueado, espera hasta que se libere, normalmente al ser notificado o interrumpido
						while (bloqueado)
							wait(); // Ahora el subproceso entra en estado de espera

						if (stopped) break;

					}

				}

			} catch (InterruptedException e) {
				System.out.println("Subproceso " + subproceso.getName() + " interrumpido");
			}

			System.out.println("Subproceso " + subproceso.getName() + " terminado");

		}

		private void ejecutar() {
			subproceso.start();
		}

		private void bloquear() {
			bloqueado = true;
			System.out.println("Subproceso " + subproceso.getName() + " bloqueado");
		}

		private synchronized void liberarBloqueo() {
			bloqueado = false;
			// A diferencia de notifyAll(), este despierta uno de los subprocesos en espera
			notify();
			System.out.println("Subproceso " + subproceso.getName() + " liberado");
		}

		private void stop() { // TODO No tiene que ser un metodo sincronizado?
			stopped = true;
			System.out.println("Subproceso " + subproceso.getName() + " detenido");
		}

		public void terminar() {
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

		System.out.println("Ejecutando subrpoceso " + Thread.currentThread().getName());

		// 1. Nuevo
		Subproceso A = new Subproceso("A");

		// 2. Ejecutado
		A.ejecutar();

		// Duerme el subproceso principal antes de bloquear el subproceso A
		Thread.sleep(TIEMPO_BLOQUEADO);
		// 3. Bloqueado
		A.bloquear();
		// Duerme el subproceso principal antes de liberar el subproceso A
		Thread.sleep(TIEMPO_BLOQUEADO);
		A.liberarBloqueo();

		// 4. Terminado
		A.terminar();

		Subproceso B = new Subproceso("B");

		Thread.sleep(TIEMPO_BLOQUEADO);
		B.ejecutar();
		Thread.sleep(TIEMPO_BLOQUEADO);
		B.stop();
		B.terminar();

		// Subproceso principal (monotarea)
		System.out.println("Subproceso " + Thread.currentThread().getName() + " terminado");
	}

}