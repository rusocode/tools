package concurrency;

/**
 * Crea e inicia 10 subprocesos en paralelo.
 *
 * <p>Tenga en cuenta que incluso si los subprocesos se inician en secuencia (1, 2, 3, etc.), es posible que no se
 * ejecuten de forma secuencial. Esto se debe a que, en principio, los subprocesos se ejecutan en paralelo y no
 * secuencialmente. La JVM y/o el OS determinan el orden en que se ejecutan los subprocesos. Este orden no tiene que ser
 * el mismo orden en el que se iniciaron.
 */

public class Example {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread("" + i) {
				public void run() {
					System.out.println("Thread: " + getName() + " running");
				}
			}.start();
		}
	}

}
