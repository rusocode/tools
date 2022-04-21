package concurrency;

public class Estados {

	private static final int TIEMPO_BLOQUEADO = 2000;

	private static class Hilo implements Runnable {

		private Thread hilo;
		private boolean suspendido, pausado;
		private final int TIEMPO_BLOQUEADO = 1000;
		private boolean bandera = false;

		public Hilo(String name) {
			hilo = new Thread(this, name);
		}

		@Override
		public void run() {
			

			System.out.println("[" + Thread.currentThread().getName() + "] iniciando...");

			try {

				for (int i = 1; i < 100; i++) {

					if (i == 99) bandera = true;

					System.out.print(i + " ");

					// Thread.sleep(100);

					// Cada 10 columnas salta de linea y suspende el hilo
					if ((i % 10) == 0) {
						System.out.println();
						// Suspende el hilo temporalmente por una cantidad determinada en milisegundos
						Thread.sleep(TIEMPO_BLOQUEADO);
					}

					// Sincroniza el bloque para que no mas de un hilo pueda acceder a este bloque
					synchronized (this) {

						/* Mientras el hilo actual este suspendido, entonces hace que espere hasta que se despierte, normalmente al ser
						 * notificado o interrumpido. */
						while (suspendido)
							wait();

						// Si esta pausado libera el bloqueo
						if (pausado) break;
					}

				}

			} catch (InterruptedException e) {
				System.out.println(hilo.getName() + " interrumpido");
			}

			if (bandera) System.out.println();
			System.out.println("[" + hilo.getName() + "] finalizado");
		}

		private void iniciar() {
			hilo.start();
		}

		private void suspender() {
			suspendido = true;
			System.out.println("[" + hilo.getName() + "] suspendido");
		}

		private synchronized void reanudar() {
			suspendido = false;
			notify(); // A diferencia de notifyAll(), este despierta uno de los hilos en espera
			System.out.println("[" + hilo.getName() + "] reanudado");
		}

		private void pausar() {
			pausado = true;
			System.out.println("[" + hilo.getName() + "] pausado");
		}

		public void matar() {
			try {
				/* El metodo join() que se llamamo al final, hace que el programa principal espere (synchronized) hasta que el hilo este
				 * muerto (finalize su ejecucion). */
				hilo.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws InterruptedException {

		/* El hilo principal se encarga de suspender y reaundar los hilos creados. */
		System.out.println("Hilo pirncipal (" + Thread.currentThread().getName() + ") iniciando...");

		/* Ciclos del hilo */

		// 1- Nuevo
		Hilo h1 = new Hilo("Hilo 1");

		// 2- Ejecutado
		h1.iniciar();

		// 3- Bloqueado
		Thread.sleep(TIEMPO_BLOQUEADO); // Espera un segundo antes de suspender el hilo
		h1.suspender();

		Thread.sleep(TIEMPO_BLOQUEADO); // Espera un segundo antes de reanudar el hilo
		h1.reanudar();

		// 4- Muerto
		h1.matar();

		// ----

		Hilo h2 = new Hilo("Hilo 2");

		Thread.sleep(TIEMPO_BLOQUEADO);
		h2.iniciar();

		Thread.sleep(TIEMPO_BLOQUEADO);
		h2.pausar();

		h2.matar();

		// Hilo principal (monotarea)
		System.out.println("Hilo principal (main) finalizado!");
	}

}