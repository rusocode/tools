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
	private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(
			() -> String.valueOf(System.currentTimeMillis()));

	public static void main(String[] args) {

		/*
		// Establece el nombre
		threadLocal.set("Subproceso local");
		// Obtiene el nombre
		String threadLocalName = threadLocal.get();
		System.out.println(threadLocalName); */

		System.out.println(threadLocal.get());


	}


}
