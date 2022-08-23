package concurrency;

/**
 * Esta clase ejecuta el subproceso principal (<a href="https://es.wikipedia.org/wiki/Monotarea">monotarea</a>) en
 * paralelo con los subprocesos A y B, en donde se muestra el cilo de vida de cada uno. El metodo {@code stop()} SOLO se
 * utiliza en este caso, ya que el ejemplo muestra el ciclo del subproceso iterado por un for y necesita de alguna
 * forma, salir del bucle. En caso contrario se utilizaria un bucle {@code while()}.
 *
 * <p>El ciclo de un subproceso es:
 *
 * <ol>
 * <li>Nuevo
 * <li>Ejecutable
 * <li>Bloqueado <i>(opcional)</i>
 * <li>Muerto
 * </ol>
 *
 * <p>El subproceso se puede bloquear con los metodos {@code wait()} o {@code sleep()}. Esto permite al subproceso
 * liberar la CPU y entrar en espera hasta que otro subproceso termine o le notifique que puede continuar, o bien cuando
 * se acaben los milisegundos del metodo {@code sleep()}. Tras eso volvera al estado ejecutable.
 *
 * <p>Un subproceso muere cuando se completa el bucle principal {@code run()} o se detiene desde el metodo {@link Subproceso#stop() stop()},
 * siempre y cuando, se llame a la funcion {@code join()} al final. Sino se detiene, el subproceso principal esperara
 * hasta que el subproceso actual termine de ejecutarse.
 *
 * <p>Los terminos <i>terminado/muerto/detenido</i> son lo mismo. Yo personalmente uso matar para indicar el fin del
 * subproceso. Los terminos <i>pausado/dormido/bloqueado/suspendido</i> son lo mismo. Yo personalmente uso bloqueado.
 *
 * <p>BLOCKED y WAIT tienen distintos conceptos.
 *
 * <p>En el estado BLOCKED, un subproceso esta a punto de ingresar a un bloque sincronizado, pero hay otro subproceso
 * ejecutandose actualmente dentro de ese bloque en el mismo objeto. El primer subproceso debe esperar a que el segundo
 * subproceso salga de su bloque.
 *
 * <p>En el estado WAIT, un subproceso esta esperando una señal de otro subproceso. Esto sucede normalmente llamando al
 * metodo {@code wait()} o {@code join()}. El subproceso permanecera en este estado hasta que otro subproceso llame a
 * {@code notify()}, o muera.
 *
 * <p>Recursos:
 * <a href="http://www.jtech.ua.es/dadm/2011-2012/restringido/android-av/sesion01-apuntes.html">Hilos</a>
 * <a href="https://stackoverflow.com/questions/15680422/difference-between-wait-and-blocked-thread-states">Diferencia entre los estados de subproceso WAIT y BLOCKED</a>
 *
 * @author Juan Debenedetti
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

				for (int i = 1; i <= 50; i++) {

					System.out.print(i + " ");

					// Cada 10 columnas salta de linea y pausa el subproceso
					if ((i % 10) == 0) {
						System.out.println();
						Thread.sleep(500);
					}

					synchronized (this) {

						/* Mientras el subproceso este bloqueado, entra en estado de espera hasta que se libere,
						 * normalmente al ser notificado o interrumpido. */
						while (blocked) wait();

						if (stopped) break;

					}

				}

			} catch (InterruptedException e) {
				System.err.println("Subproceso " + subproceso.getName() + " interrumpido");
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
			notify();
			System.out.println("Subproceso " + subproceso.getName() + " desbloqueado");
		}

		/**
		 * Importante para salir del bucle principal.
		 */
		private void stop() {
			stopped = true;
			System.out.println("Subproceso " + subproceso.getName() + " detenido");
		}

		/**
		 * Este metodo se llama una vez que finalize el bucle principal.
		 */
		public void kill() {
			try {
				// El metodo join bloquea el subproceso principal hasta que termine de ejecutarse el subproceso actual
				subproceso.join();
			} catch (InterruptedException e) {
				System.err.println("Subproceso " + subproceso.getName() + " interrumpido");
			}

			if (!subproceso.isAlive()) System.out.println("Subproceso " + subproceso.getName() + " muerto");

		}

	}

	public static void main(String[] args) throws InterruptedException {

		// Subproceso principal
		System.out.println("Ejecutando subrpoceso " + Thread.currentThread().getName());

		// 1. Nuevo
		Subproceso A = new Subproceso("A");

		// 2. Ejecutable
		A.start();

		// 3. Bloqueado
		// Bloquea el subproceso principal durante el numero especificado de milisegundos, antes de bloquear el subproceso A
		Thread.sleep(TIEMPO_BLOQUEADO);
		A.block();
		Thread.sleep(TIEMPO_BLOQUEADO);
		A.unlock();

		// 4. Muerto
		A.kill();

		Subproceso B = new Subproceso("B");
		Thread.sleep(TIEMPO_BLOQUEADO);
		B.start();
		Thread.sleep(TIEMPO_BLOQUEADO);
		B.stop();
		B.kill();

		System.out.println("Subproceso " + Thread.currentThread().getName() + " terminado");
	}

}