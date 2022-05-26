package concurrency;

/**
 * La clase Java ThreadLocal le permite crear variables que solo pueden ser leidas y escritas por el mismo hilo.
 * Por lo tanto, incluso si dos subprocesos ejecutan el mismo codigo y el codigo tiene una referencia a la misma variable
 * ThreadLocal, los dos subprocesos no pueden ver las variables ThreadLocal del otro. Por lo tanto, la clase Java
 * ThreadLocal proporciona una manera simple de hacer que el codigo sea seguro para subprocesos que de otro modo no seria asi.
 * <p>
 * El objetivo principal de usar un ThreadLocal en primer lugar es evitar que los diferentes hilos vean la misma instancia.
 * <br>
 * Fuentes:
 * <a href="https://jenkov.com/tutorials/java-concurrency/threadlocal.html">jenkov.com</a>
 *
 * @author Ruso
 */

public class _ThreadLocal {

	// Crea un obj de ThreadLocal de tipo String
	// private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

	/* Valor inicial de ThreadLocal usando la implementacion de la interfaz Supplier mediante una expresion
	Java Lambda mas densa. */
	/* private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(
			() -> String.valueOf(System.currentTimeMillis())); */

	public static void main(String[] args) throws InterruptedException {

		/*
		// Establece el nombre
		threadLocal.set("Subproceso local");
		// Obtiene el nombre
		String threadLocalName = threadLocal.get();
		System.out.println(threadLocalName); */

		// System.out.println(threadLocal.get());


		/* Full ThreadLocal Example
		 * Este ejemplo crea una sola instancia de MyRunnable que se pasa a dos subprocesos diferentes. Ambos subprocesos
		 * ejecutan el mEtodo run() y, por lo tanto, establecen diferentes valores en la instancia de ThreadLocal.
		 *
		 * Sin embargo, dado que es un objeto ThreadLocal, los dos subprocesos no pueden ver los valores del otro. Por
		 * lo tanto, establecen y obtienen diferentes valores. */
		MyRunnable sharedRunnableInstance = new MyRunnable();

		Thread thread1 = new Thread(sharedRunnableInstance);
		Thread thread2 = new Thread(sharedRunnableInstance);

		thread1.start();
		thread2.start();

		thread1.join(); // Espera a que termine el hilo 1
		thread2.join(); // Espera a que termine el hilo 2

	}

}

class MyRunnable implements Runnable {

	// Crea un ThreadLocal de tipo Integer
	private final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

	@Override
	public void run() {

		threadLocal.set((int) (Math.random() * 100D));

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(threadLocal.get());

	}
}
