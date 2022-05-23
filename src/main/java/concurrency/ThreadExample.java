package concurrency;

/**
 * Ejemplo en donde se crean e inician 10 subprocesos.
 * Tenga en cuenta que incluso si los subprocesos se inician en secuencia (1, 2, 3, etc.), es posible que no se ejecuten
 * de forma secuencial, lo que significa que el subproceso 1 puede no ser el primer subproceso en escribir su nombre en System.out.
 * Esto se debe a que, en principio, los subprocesos se ejecutan en paralelo y no secuencialmente. La JVM y/o el sistema operativo
 * determinan el orden en que se ejecutan los subprocesos. Este orden no tiene que ser el mismo orden en el que se iniciaron.
 */

public class ThreadExample {
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName());
		for (int i = 0; i < 10; i++) {
			new Thread("" + i) {
				public void run() {
					System.out.println("Thread: " + getName() + " running");
				}
			}.start();
		}
	}

}
