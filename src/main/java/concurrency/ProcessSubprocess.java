package concurrency;

import java.lang.Thread;

/**
 * <h3>Proceso y Subproceso</h3>
 * Un PROCESO es un programa activo que proporciona los recursos necesarios para ejecutarse. Incluye un espacio de
 * direcciones virtuales, un codigo ejecutable, identificadores abiertos para los objetos del sistema, un contexto de
 * seguridad, un identificador de proceso unico, variables de entorno, una clase de prioridad, tamaños minimos y maximos
 * de conjuntos de trabajo y al menos un subproceso de ejecucion. Cada proceso se inicia con un solo subproceso, a
 * menudo denominado subproceso principal, pero puede crear subprocesos adicionales a partir de cualquiera de sus
 * subprocesos.
 * <p>
 * Un SUBPROCESO es como una CPU virtual que puede ejecutar codigo java, dentro de una aplicacion java. Cuando se
 * inicia una aplicacion java, el metodo main() es ejecutado por el main thread, un subproceso especial creado por la
 * JVM para ejecutar la aplicacion. Desde dentro de la aplicacion, puede crear e iniciar mas subprocesos que pueden
 * ejecutar partes del codigo de la aplicacion en paralelo con el main thread. Los subprocesos de java son objetos como
 * cualquier otro, siendo instancias de la clase java.lang.Thread o instancias de subclases de esta clase. Ademas de ser
 * objetos, estos subprocesos tambien pueden ejecutar codigo.
 * <p>
 * Tanto los procesos como los subprocesos son secuencias independientes de ejecucion. La diferencia tipica es que los
 * subprocesos (del mismo proceso) se ejecutan en un espacio de memoria compartido, mientras que los procesos se
 * ejecutan en espacios de memoria separados.
 * <p>
 * Los subprocesos son una funcion del entorno operativo, en lugar de una funcion de la CPU (aunque la CPU normalmente
 * tiene operaciones que hacen que los subprocesos sean eficientes).
 * <br><br>
 * <h3>Concurrencia</h3>
 * El subproceso multiple significa que tiene varios subprocesos de ejecucion dentro de la misma aplicacion. Un hilo es
 * como una CPU separada que ejecuta su aplicacion. Por lo tanto, una aplicacion multiproceso es como una aplicacion que
 * tiene varias CPU que ejecutan diferentes partes del codigo al mismo tiempo.
 * <p>
 * Sin embargo, un hilo no es igual a una CPU. Por lo general, una sola CPU compartira su tiempo de ejecucion entre
 * varios subprocesos, cambiando entre la ejecucion de cada uno de los subprocesos durante un periodo de tiempo
 * determinado.
 * <p>
 * En principio, los subprocesos se ejecutan en paralelo y no de forma secuencial. La JVM y/o el sistema operativo
 * determina el orden en el que se ejecutan los subprocesos. Este orden no tiene que ser el mismo orden en el que se
 * iniciaron.
 * <br><br>
 * Nota:
 * <br>
 * Proceso/Aplicacion/Programa es lo mismo.
 * <br>
 * Subproceso/Thread/Hilo es lo mismo.
 * <br><br>
 * Fuentes:
 * <a href="https://jenkov.com/tutorials/java-concurrency/index.html">Java Concurrency and Multithreading</a>
 * <a href="https://www.youtube.com/watch?v=Dhf-DYO1K78">Process vs Thread</a>
 * <a href="https://www.youtube.com/watch?v=exbKr6fnoUw">Intro to Processes & Threads (muy bien explicado)</a>
 * <a href="https://www.youtube.com/watch?v=7ENFeb-J75k">Multithreading Code - Computerphile</a>
 * <a href="https://stackoverflow.com/questions/200469/what-is-the-difference-between-a-process-and-a-thread">Difference between Process and Thread (stackoverflow)</a>
 * <a href="https://www.tutorialspoint.com/difference-between-process-and-thread">Difference between Process and Thread (tutorialspoint)</a>
 *
 * @author Ruso
 */

public class ProcessSubprocess extends Thread {

	public static void main(String[] args) {

		/* Hay dos formas de especificar que codigo debe ejecutar el subproceso...
		 * El primero es crear una subclase de Thread y sobreescribir el metodo run(). El segundo metodo es pasar un objeto que
		 * implemente la interfaz Runnable al constructor Thread. */

		// PRIMERA FORMA
		/* La primera forma de especificar que codigo debe ejecutar un subproceso es crear una subclase de Thread y
		 * sobreescribir el metodo run(). El metodo run() es lo que ejecuta el subproceso despues de llamar a start(). */
		// Crea y ejecuta el subproceso
		// ProcessSubprocess subproceso = new ProcessSubprocess();
		/* La llamada start() regresara tan pronto como se inicie el hilo. No esperara hasta que finalice el metodo run(). El
		 * metodo run() se ejecutara como si lo ejecutara una CPU diferente. */
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
	 * grupo de subprocesos ejecuta Runnable, es facil poner en cola las instancias de Runnable hasta que un subproceso del
	 * grupo este inactivo. Esto es un poco mas dificil de hacer con las subclases Thread. */

}
