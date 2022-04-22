package concurrency;

/**
 * El subproceso múltiple significa que tiene varios subprocesos de ejecución dentro de la misma aplicación. Un hilo es
 * como una CPU separada que ejecuta su aplicación. Por lo tanto, una aplicación multiproceso es como una aplicación que
 * tiene varias CPU que ejecutan diferentes partes del código al mismo tiempo.
 * 
 * Un hilo de Java es como una CPU virtual que puede ejecutar su código dentro de la aplicación. Cuando se inicia una
 * aplicación, el método main() es ejecutado por el hilo principal - un hilo especial que es creado por la JVM para
 * ejecutar la aplicación. Desde el interior de su aplicación, puede crear e iniciar más subprocesos que pueden ejecutar
 * partes del código de su aplicación en paralelo con el subproceso principal.
 * 
 * Sin embargo, un hilo no es igual a una CPU. Por lo general, una sola CPU compartirá su tiempo de ejecución entre
 * varios subprocesos, cambiando entre la ejecución de cada uno de los subprocesos durante un período de tiempo
 * determinado.
 * 
 * En principio, los subprocesos se ejecutan en paralelo y no de forma secuencial. La JVM y/o el sistema operativo
 * determina el orden en el que se ejecutan los subprocesos. Este orden no tiene que ser el mismo orden en el que se
 * iniciaron.
 * 
 * Fuente:
 * http://tutorials.jenkov.com/java-concurrency/index.html
 * 
 * @author Ru$o
 * 
 */

public class Concurrency {

	public static void main(String[] args) {

		MyRunnable myRunnable = new MyRunnable();

		// Le pasa la instancia del objeto que implementa la interfaz Runnable
		Thread thread = new Thread(myRunnable);

		// Inicia el hilo
		thread.start();

		try {
			Thread.sleep(10L * 1000L); // Duerme/pausa el hilo principal por 10 segundos antes de detener el subproceso
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Detiene el subproceso
		myRunnable.doStop();
	}

}

class MyRunnable implements Runnable {

	private boolean doStop;

	public synchronized void doStop() {
		doStop = true;
	}

	private synchronized boolean keepRunning() {
		return doStop == false;
	}

	@Override
	public void run() {

		while (keepRunning()) {

			System.out.println("Running");

			try {
				Thread.sleep(3L * 1000L); // Detiene el subproceso por 3 segundos
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}