package concurrency;

/**
 * Esta clase ejecuta el subproceso principal (main) en paralelo con los subprocesos A y B, en donde se muestra el cilo
 * de vida de cada uno.
 *
 * <p>El ciclo de un subproceso es:
 *
 * <ul>
 * <li>Nuevo
 * <li>Ejecutado
 * <li>Bloqueado
 * <li>Muerto
 * </ul>
 *
 * <p>El subproceso se puede bloquear con los metodos {@code wait()} o {@code sleep()}. Esto permite al subproceso
 * liberar la CPU en espera a que otro subproceso termine o le notifique que puede continuar, o bien a que termine un
 * proceso de I/O, o bien a que termine una espera provocada por la funcion {@code sleep()}. Tras eso volvera al estado
 * ejecutable.
 *
 * <p>Para matar un subproceso es necesario esperar hasta que se complete el bucle principal {@code run()} o detenerlo
 * desde el metodo {@link Subproceso#stop() stop()}. Una vez que se complete el bucle o se detenga, se mata al
 * subproceso. Sino se detiene, el subproceso principal esperara hasta que el subproceso actual termine de ejecutarse.
 *
 * <p>Recursos:
 * <a href="https://stackoverflow.com/questions/15680422/difference-between-wait-and-blocked-thread-states">Difference between WAIT and BLOCKED thread states</a>
 * <a href="http://www.jtech.ua.es/dadm/2011-2012/restringido/android-av/sesion01-apuntes.html">Hilos</a>
 *
 * @author Juan Debenedetti
 */

public class ThreadCycle {

	private static final int TIEMPO_DORMIDO = 1000;

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

				for (int i = 1; i <= 50; i++) {

					System.out.print(i + " ");

					// Cada 10 columnas salta de linea y pausa el subproceso
					if ((i % 10) == 0) {
						System.out.println();
						Thread.sleep(500);
					}

					// Sincroniza este bloque de codigo
					synchronized (this) {

						/* Mientras el subproceso este bloqueado, entra en estado de espera hasta que se libere,
						 * normalmente al ser notificado o interrumpido. */
						while (blocked) wait();

						if (stopped) break;

					}

				}

			} catch (InterruptedException e) {
				System.out.println("Subproceso " + subproceso.getName() + " interrumpido");
			}

		}

		private synchronized void start() {
			subproceso.start();
		}

		private synchronized void block() {
			blocked = true;
			System.out.println("Subproceso " + subproceso.getName() + " bloqueado");
		}

		private synchronized void unlock() {
			blocked = false;
			notify(); // A diferencia de notifyAll(), este despierta uno de los subprocesos en espera
			System.out.println("Subproceso " + subproceso.getName() + " desbloqueado");
		}

		private synchronized void stop() {
			stopped = true;
			System.out.println("Subproceso " + subproceso.getName() + " detenido");
		}

		/**
		 * Este metodo se llama una vez que finalize el bucle principal.
		 */
		public void kill() {
			try {
				/* El metodo join() que se llamamo al final, hace que el subproceso principal espere (synchronized)
				 * hasta que el subproceso actual termine. */
				subproceso.join();
			} catch (InterruptedException e) {
				System.out.println("Subproceso " + subproceso.getName() + " interrumpido");
			}

			if (!subproceso.isAlive()) System.out.println("Subproceso " + subproceso.getName() + " muerto!");

		}

	}

	public static void main(String[] args) throws InterruptedException {

		// Subproceso principal (monotarea)
		System.out.println("Ejecutando subrpoceso " + Thread.currentThread().getName());

		// 1. Nuevo
		Subproceso A = new Subproceso("A");

		// 2. Ejecutado
		A.start();

		// 3. Bloqueado
		/* Suspende (deja de ejecutarse temporalmente) el subproceso principal durante el numero especificado de
		 * milisegundos, antes de bloquear el subproceso A. */
		Thread.sleep(TIEMPO_DORMIDO); // Libera la CPU durante un tiempo
		A.block();
		Thread.sleep(TIEMPO_DORMIDO);
		A.unlock();

		// 4. Muerto
		A.kill();

		Subproceso B = new Subproceso("B");
		Thread.sleep(TIEMPO_DORMIDO);
		B.start();
		Thread.sleep(TIEMPO_DORMIDO);
		B.stop();
		B.kill();

		System.out.println("Subproceso " + Thread.currentThread().getName() + " terminado");
	}

}