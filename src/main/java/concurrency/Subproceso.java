package concurrency;

/**
 * Un subproceso java es como una CPU virtual que puede ejecutar su codigo java, dentro de su aplicacion java. Cuando se
 * inicia una aplicacion java, su metodo main() es ejecutado por el main thread, un subproceso especial creado por la
 * JVM para ejecutar su aplicacion. Desde dentro de su aplicacion, puede crear e iniciar mas subprocesos que pueden
 * ejecutar partes del codigo de su aplicacion en paralelo con el main thread.
 * 
 * Los subprocesos de java son objetos como cualquier otro objeto de java. Los hilos son instancias de la clase
 * java.lang.Thread o instancias de subclases de esta clase. Ademas de ser objetos, los subprocesos de java tambien
 * pueden ejecutar codigo.
 *
 * Proceso/Aplicacion es lo mismo.
 * Subproceso/Thread/Hilo es lo mismo.
 * CPU/Nucleo es lo mismo (aunque un CPU puede tener varios nucleos).
 * Task/Tarea es la tarea que ejecuta el subproceso.
 * 
 * Todos estos terminos se relacionan entre si formando lo que se conoce como Multithreading.
 * 
 * Recursos
 * https://sites.google.com/site/silvinaeduc/home/definicion-de-sincronica-y-asincronica
 * https://stackoverflow.com/questions/200469/what-is-the-difference-between-a-process-and-a-thread
 * https://www.tutorialspoint.com/difference-between-process-and-thread
 * https://jenkov.com/tutorials/java-concurrency/index.html
 * 
 * @author Ru$o
 * 
 */

public class Subproceso {

	private static final int TIEMPO_BLOQUEADO = 2000;

	private static class Hilo implements Runnable {

		private Thread subproceso;
		private boolean suspendido, pausado;
		private final int TIEMPO_BLOQUEADO = 1000;
		private boolean bandera = false;

		public Hilo(String name) {
			subproceso = new Thread(this, name);
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
				System.out.println(subproceso.getName() + " interrumpido");
			}

			if (bandera) System.out.println();
			System.out.println("[" + subproceso.getName() + "] finalizado");
		}

		private void iniciar() {
			subproceso.start();
		}

		private void suspender() {
			suspendido = true;
			System.out.println("[" + subproceso.getName() + "] suspendido");
		}

		private synchronized void reanudar() {
			suspendido = false;
			notify(); // A diferencia de notifyAll(), este despierta uno de los hilos en espera
			System.out.println("[" + subproceso.getName() + "] reanudado");
		}

		private void pausar() {
			pausado = true;
			System.out.println("[" + subproceso.getName() + "] pausado");
		}

		public void matar() {
			try {
				/* El metodo join() que se llamamo al final, hace que el programa principal espere (synchronized) hasta que el hilo este
				 * muerto (finalize su ejecucion). */
				subproceso.join();
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