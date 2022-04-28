package concurrency;

/**
 * Esta clase basicamente lo que hace es determinar cuales subprocesos se ejecutan, bloquean, liberan, detienen y
 * terminan. Todo este manejo lo hace el subproceso principal main que es agregado por la JVM un vez que se inicia
 * la aplicacion.
 * 
 * El ciclo de un subproceso es:
 * 1. Nuevo
 * 2. Ejecutado
 * 3. Bloqueado
 * 4. Terminado
 * 
 * ¿Cual es la diferencia entre bloquear y detener (stop)?
 * 
 * @author Ruso
 * 
 */

public class ThreadCycle {

	private static final int TIEMPO_BLOQUEADO = 2000;

	private static class Subproceso implements Runnable {

		private Thread subproceso;
		private boolean bloqueado, stopped;
		private final int TIEMPO_BLOQUEADO = 500;

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
						Thread.sleep(TIEMPO_BLOQUEADO);
					}

					// Sincroniza este bloque de codigo
					synchronized (this) {
						// Mientras el subproceso este bloqueado, espera hasta que se libere, normalmente al ser notificado o interrumpido
						while (bloqueado)
							wait();

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

		// Bloquea el subproceso principal antes de bloquear el subproceso A
		Thread.sleep(TIEMPO_BLOQUEADO);
		// 3. Bloqueado
		A.bloquear();
		// Bloquea el subproceso principal antes de liberar el subproceso A
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