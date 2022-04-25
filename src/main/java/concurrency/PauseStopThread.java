package concurrency;

/**
 * Ejecucion, pausa y detencion de un subproceso.
 * 
 * Fuente
 * https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html
 * 
 */

public class PauseStopThread implements Runnable {

	private boolean stopped;

	@Override
	public void run() {

		while (!isStopped()) {

			// Obtiene el nombre del subproceso
			System.out.println(Thread.currentThread().getName() + " Running");

			// Pausa el subproceso cada medio segundo
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized void doStop() {
		stopped = true;
	}

	/* Reemplaze el metodo keepRunning() por este, ya que es mas simple y tiene mas sentido comprobar el estado del
	 * subproceso de esta manera. */
	public synchronized boolean isStopped() {
		return stopped;
	}

	/* private synchronized boolean keepRunning() {
	 * return stopped == false;
	 * } */

	public static void main(String[] args) {

		PauseStopThread runnable = new PauseStopThread();
		// No puedo usar "this" en un contexto estatico
		Thread subproceso = new Thread(runnable, "Subproceso 1");
		subproceso.start();

		/* Se puede hacer una megasimplificacion en una linea, pero no existe una instancia para llamar a los metodos de la
		 * clase que implementa la interfaz Runnable. */
		// new Thread(new StopThread()).start();

		// Pausa el subproceso por 2 segundos y lo detiene
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		runnable.doStop();

	}

}
