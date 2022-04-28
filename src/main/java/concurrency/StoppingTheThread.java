package concurrency;

/**
 * Ejemplo de como se detiene un subproceso.
 * 
 * Fuente
 * https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html
 * 
 */

public class StoppingTheThread implements Runnable {

	private boolean stopped;

	@Override
	public void run() {

		while (!isStopped()) {

			// Obtiene el nombre del subproceso
			System.out.println(Thread.currentThread().getName() + " Running");

			// Bloquea el subproceso cada medio segundo
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized void stop() {
		stopped = true;
	}

	/* Reemplaze el metodo keepRunning() por este, ya que es mas simple y tiene mas sentido comprobar el estado del
	 * subproceso de esta manera. */
	private synchronized boolean isStopped() {
		return stopped;
	}

	/* private synchronized boolean keepRunning() {
	 * return stopped == false;
	 * } */

	public static void main(String[] args) {

		/* Es importante tener en cuenta que esta aplicacion ejecuta dos subprocesos, el main thread y el subproceso que llama
		 * al metodo run. */

		StoppingTheThread runnable = new StoppingTheThread();
		// No puedo usar "this" en un contexto estatico
		Thread subproceso = new Thread(runnable, "Subproceso 1");
		subproceso.start();

		/* Se puede hacer una megasimplificacion en una linea, pero no existe una instancia para llamar a los metodos de la
		 * clase que implementa la interfaz Runnable. */
		// new Thread(new StopThread()).start();

		// Bloquea el subproceso antes de detenerlo
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		runnable.stop();

	}

}
