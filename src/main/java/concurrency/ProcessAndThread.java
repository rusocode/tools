package concurrency;

import java.lang.Thread;

/**
 * Un PROCESO es un programa activo que proporciona los recursos necesarios para ejecutarse. Incluye un espacio de
 * direcciones virtuales, un codigo ejecutable, identificadores abiertos para los objetos del sistema, un contexto de
 * seguridad, un identificador de proceso unico, variables de entorno, una clase de prioridad, tamaños minimos y maximos
 * de conjuntos de trabajo y al menos un subproceso de ejecucion. Cada proceso se inicia con un solo subproceso, a
 * menudo denominado subproceso principal, pero puede crear subprocesos adicionales a partir de cualquiera de sus
 * subprocesos.
 * 
 * Un SUBPROCESO es como una CPU virtual que puede ejecutar codigo java, dentro de una aplicacion java. Cuando se
 * inicia una aplicacion java, el metodo main() es ejecutado por el main thread, un subproceso especial creado por la
 * JVM para ejecutar la aplicacion. Desde dentro de la aplicacion, puede crear e iniciar mas subprocesos que pueden
 * ejecutar partes del codigo de la aplicacion en paralelo con el main thread. Los subprocesos de java son objetos como
 * cualquier otro, siendo instancias de la clase java.lang.Thread o instancias de subclases de esta clase. Ademas de ser
 * objetos, estos subprocesos tambien pueden ejecutar codigo.
 * 
 * Tanto los procesos como los subprocesos son secuencias independientes de ejecucion. La diferencia tipica es que los
 * subprocesos (del mismo proceso) se ejecutan en un espacio de memoria compartido, mientras que los procesos se
 * ejecutan en espacios de memoria separados.
 * 
 * Los subprocesos son una funcion del entorno operativo, en lugar de una funcion de la CPU (aunque la CPU normalmente
 * tiene operaciones que hacen que los subprocesos sean eficientes).
 *
 * Nota:
 * Proceso/Aplicacion/Programa es lo mismo.
 * Subproceso/Thread/Hilo es lo mismo.
 * 
 * Recursos
 * Process vs Thread: https://www.youtube.com/watch?v=Dhf-DYO1K78
 * Intro to Processes & Threads: https://www.youtube.com/watch?v=exbKr6fnoUw (muy bien explicado)
 * Multithreading Code - Computerphile: https://www.youtube.com/watch?v=7ENFeb-J75k
 * Java Concurrency and Multithreading:
 * https://www.youtube.com/watch?v=mTGdtC9f4EU&list=PLL8woMHwr36EDxjUoCzboZjedsnhLP1j4
 * https://stackoverflow.com/questions/200469/what-is-the-difference-between-a-process-and-a-thread
 * https://www.tutorialspoint.com/difference-between-process-and-thread
 * https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html
 * 
 * @author Ruso
 * 
 */

public class ProcessAndThread extends Thread {

	public static void main(String[] args) {

		/* Hay dos formas de especificar que codigo debe ejecutar el subproceso...
		 * El primero es crear una subclase de Thread y sobreescribir el metodo run(). El segundo metodo es pasar un objeto que
		 * implemente la interfaz Runnable al constructor Thread. */

		// PRIMERA FORMA
		/* La primera forma de especificar que codigo debe ejecutar un subproceso es crear una subclase de Thread y
		 * sobreescribir el metodo run(). El metodo run() es lo que ejecuta el subproceso despues de llamar a start(). */
		// Crea y ejecuta el subproceso
		// ProcesoSubproceso subproceso = new ProcesoSubproceso();
		/* La llamada start() regresara tan pronto como se inicie el hilo. No esperara hasta que finalice el metodo run(). El
		 * metodo run() se ejecutara como si lo ejecutara una CPU diferente. */
		// subproceso.start();

		// Tambien puede crear una subclase anonima de Thread como esta
		/* Thread thread = new Thread() {
		 * public void run() {
		 * System.out.println("Thread running in anonymous class");
		 * }
		 * };
		 * 
		 * thread.start(); */

		// Inicia el subproceso con un Runnable (segunda forma)
		Thread thread = new Thread(new MyRunnable()); // o una clase anonima, o lambda...
		thread.start();
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
