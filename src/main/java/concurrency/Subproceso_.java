package concurrency;

import java.lang.Thread;

/**
 * Un subproceso java es como una CPU virtual que puede ejecutar su codigo java, dentro de su aplicacion java. Cuando se
 * inicia una aplicacion java, su metodo main() es ejecutado por el main thread, un subproceso especial creado por la
 * JVM para ejecutar su aplicacion. Desde dentro de su aplicacion, puede crear e iniciar mas subprocesos que pueden
 * ejecutar partes del codigo de su aplicacion en paralelo con el main thread.
 * 
 * Los subprocesos de java son objetos como cualquier otro objeto de java. Los hilos son instancias de la clase
 * java.lang.Thread o instancias de subclases de esta clase. Ademas de ser objetos, los subprocesos de java tambien
 * pueden ejecutar codigo.
 *
 * Proceso/Aplicacion es lo mismo.
 * Subproceso/Thread/Hilo es lo mismo.
 * CPU/Nucleo es lo mismo (aunque un CPU puede tener varios nucleos).
 * Task/Tarea es la tarea que ejecuta el subproceso.
 * 
 * Todos estos terminos se relacionan entre si formando lo que se conoce como Multithreading.
 * 
 * Recursos
 * https://sites.google.com/site/silvinaeduc/home/definicion-de-sincronica-y-asincronica
 * https://stackoverflow.com/questions/200469/what-is-the-difference-between-a-process-and-a-thread
 * https://www.tutorialspoint.com/difference-between-process-and-thread
 * https://jenkov.com/tutorials/java-concurrency/index.html
 * 
 * @author Ru$o
 * 
 */

public class Subproceso_ {

	public static void main(String[] args) {

		// Creacion del subproceso
		Thread subproceso = new Thread() {
			public void run() {
				System.out.println("Thread Running");
			}
		};

		// Ejecucion del subproceso
		subproceso.start();

	}

}
