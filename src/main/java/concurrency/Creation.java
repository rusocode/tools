package concurrency;

/**
 * Muestra la distintas formas de crear subprocesos.
 *
 * <p>Recursos:
 * <a href="https://www.youtube.com/watch?v=eQk5AWcTS8w">Crear subprocesos</a>
 */

public class Creation extends Thread {

	public static void main(String[] args) {

		/* Hay dos formas de especificar que codigo debe ejecutar el subproceso...
		 * El primero es crear una subclase de Thread y sobreescribir el metodo run(). El segundo metodo es pasar un
		 * objeto que implemente la interfaz Runnable al constructor Thread. */

		// PRIMERA FORMA
		/* La primera forma de especificar que codigo debe ejecutar un subproceso es crear una subclase de Thread y
		 * sobreescribir el metodo run(). El metodo run() es lo que ejecuta el subproceso despues de llamar a start(). */
		// Crea y ejecuta el subproceso
		// ProcessSubprocess subproceso = new ProcessSubprocess();
		/* La llamada start() regresara tan pronto como se inicie el hilo. No esperara hasta que finalice el metodo
		 * run(). El  metodo run() se ejecutara como si lo ejecutara una CPU diferente. */
		// subproceso.start();

		// Subclase anonima de Thread
		/* Thread thread = new Thread() {
		 * public void run() {
		 * System.out.println("Thread running in anonymous class");
		 * }
		 * };
		 *
		 * thread.start(); */

		// Lambda
		/* Thread subproceso = new Thread(() -> {
		 *	System.out.println("Thread running in lambda");
		 * });
		 * subproceso.start(); */

		// Inicia el subproceso con un Runnable (segunda forma)
		Thread subproceso = new Thread(new MyRunnable()); // o una clase anonima, o lambda...
		subproceso.start();
	}

	@Override
	public void run() {
		System.out.println("Thread running");
	}

	// SEGUNDA FORMA
	/* La segunda forma de especificar que codigo debe ejecutar un subproceso es creando una clase que implemente la
	 * interfaz Runnable. Un objeto que implementa la interfaz Runnable puede ser ejecutado por un subproceso.
	 *
	 * Hay tres formas de implementar la interfaz Runnable:
	 * 1. Creando una clase que implemente la interfaz Runnable.
	 * 2. Creando una clase anonima que implemente la interfaz Runnable.
	 * 3. Creando un lambda que implemente la interfaz Runnable. */

	/* 1 */
	private static class MyRunnable implements Runnable {

		@Override
		public void run() {
			System.out.println("Runnable running");
		}

	}

	// 2
	/* Runnable runnable = new Runnable() {
	 * public void run() {
	 * System.out.println("Runnable running");
	 * }
	 * }; */

	// 3
	/* Runnable runnable = () -> {
	 * System.out.println("Runnable running");
	 * }; */

	/* Subclase o Runnable?
	 *
	 * No hay reglas sobre cual de los dos metodos es el mejor. Ambos metodos funcionan. Sin embargo, personalmente,
	 * prefiero implementar Runnable y entregar una instancia de la implementacion a una instancia de Thread. Cuando un
	 * grupo de subprocesos (thread pools) ejecuta Runnable, es facil poner en cola las instancias de Runnable hasta que
	 * un subproceso del grupo este inactivo. Esto es un poco mas dificil de hacer con las subclases Thread. */


}
