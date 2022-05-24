package concurrency;

/**
 * Una condicion de carrera es un problema de concurrencia que puede ocurrir dentro de una seccion critica. Una seccion
 * critica es una seccion de codigo que es ejecutada por multiples subprocesos y donde la secuencia de ejecucion de los
 * subprocesos hace una diferencia en el resultado de la ejecucion concurrente de la seccion critica.
 * <p>
 * Cuando el resultado de varios subprocesos que ejecutan una seccion critica puede diferir segun la secuencia en la que
 * se ejecutan los subprocesos, se dice que la seccion critica contiene una condicion de carrera. El termino condicion
 * de carrera se deriva de la metafora de que los hilos corren a traves de la seccion critica y que el resultado de esa
 * carrera impacta el resultado de ejecutar la seccion critica.
 * <p>
 * Hay dos tipos de condiciones de carrera:
 * - Leer, modificar y escribir
 * - Verificar y luego actuar
 * <p>
 * El patron de lectura-modificacion-escritura significa que dos o mas subprocesos primero leen una variable
 * determinada, luego modifican su valor y lo vuelven a escribir en la variable. Para que esto cause un problema, el
 * nuevo valor debe depender de una forma u otra del valor anterior. El problema que puede ocurrir es que si dos
 * subprocesos leen el valor (en los registros de la CPU), modifican el valor (en los registros de la CPU) y luego
 * vuelven a escribir los valores.
 * <p>
 * Imaginese si dos subprocesos, A y B, estan ejecutando el metodo increment en la misma instancia de esta clase. No hay
 * forma de saber cuando el sistema operativo cambia entre los dos subprocesos. La JVM no ejecuta el codigo del metodo
 * increment() como una unica instruccion atomica. Mas bien se ejecuta como un conjunto de instrucciones mas pequeñas,
 * similar a esto:
 * <p>
 * 1. Lee count de la memoria en el registro.
 * 2. Agrega un valor al registro.
 * 3. Escribe el registro en memoria.
 * <p>
 * Los dos subprocesos querian incrementar el valor del contador en 1. Por lo tanto, el valor deberia haber sido 1 para
 * A y 2 para B despues de que los dos subprocesos completaran la ejecucion. Sin embargo, como la ejecucion de los dos
 * hilos esta intercalada, el resultado acaba siendo diferente.
 * <p>
 * En el ejemplo de secuencia de ejecucion mencionado anteriormente, ambos subprocesos leen el valor 0 de la memoria.
 * Luego, incrementan el valor en 1 y vuelven a escribir el resultado en la memoria. En lugar de 1 para A y 2 para B, el
 * valor que queda en count sera el valor escrito por el ultimo subproceso. En el caso anterior es el hilo A, pero bien
 * podria haber sido el hilo B.
 * <p>
 * Fuente
 * <a href="https://jenkov.com/tutorials/java-concurrency/race-conditions-and-critical-sections.html">Race Conditions and Critical Sections</a>
 * <a href="https://www.javatpoint.com/race-condition-in-java#:~:text=Java%20is%20a%20multi%2Dthreaded,condition%20is%20a%20concurrency%20bug">javatpoint.com</a>
 */

public class RaceConditionsAndCriticalSections implements Runnable {

	private int count = 0;

	@Override
	public void run() {

		/* Para evitar que ocurran condiciones de carrera, debe asegurarse de que la seccion critica se ejecute como una
		 * instruccion atomica. Eso significa que una vez que un unico subproceso lo esta ejecutando, ningun otro subproceso
		 * puede ejecutarlo hasta que el primer subproceso haya abandonado la seccion critica.
		 *
		 * Las condiciones de carrera se pueden evitar mediante la sincronizacion adecuada de subprocesos en secciones criticas.
		 * La sincronizacion de subprocesos se puede lograr utilizando un bloque sincronizado de codigo Java.
		 *
		 * Para evitar condiciones de carrera, el incremento de la varibale se ejecuta dentro de un bloque sincronizado. Con
		 * esta implementacion, solo un unico subproceso puede ejecutar el incremento al mismo tiempo. */

		// SECCION CRITICA
		synchronized (this) {

			increment();

			System.out.println("Subproceso " + Thread.currentThread().getName() + " \n Valor de count despues del incremento: " + this.getValue());

		}

	}

	/* Tambien se puede utilizar la sincronizacion de metodo. */
	public /* synchronized */ void increment() {

		// Bloquea el hilo para que sea mas obvio el resultado erroneo
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		count++;
	}

	public int getValue() {
		return count;
	}

	public static void main(String args[]) {

		// Crea un objeto que implementa la interfaz Runnable
		RaceConditionsAndCriticalSections instance = new RaceConditionsAndCriticalSections();

		// Crea 2 subprocesos en donde se le pasa la instancia compartida y un nombre a cada uno
		Thread A = new Thread(instance, "A");
		Thread B = new Thread(instance, "B");

		/* Ejecuta los subprocesos conduciendo a una condicion de carrera en caso de que la seccion critica no este
		 * sincronizada. */
		A.start();
		B.start();
	}

}
