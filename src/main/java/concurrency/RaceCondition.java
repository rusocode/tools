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
 * <div>
 * 		<li>Leer, modificar y escribir</li>
 * 		<li>Verificar y luego actuar</li>
 * </div>
 * El patron de lectura-modificacion-escritura significa que dos o mas subprocesos primero leen una variable
 * determinada, luego modifican su valor y lo vuelven a escribir en la variable. Para que esto cause un problema, el
 * nuevo valor debe depender de una forma u otra del valor anterior. El problema que puede ocurrir es que si dos
 * subprocesos leen el valor (en los registros de la CPU), modifican el valor (en los registros de la CPU) y luego
 * vuelven a escribir los valores.
 * <p>
 * Imaginese si dos subprocesos, A y B, estan ejecutando el metodo increment() en la misma instancia de esta clase. No hay
 * forma de saber cuando el sistema operativo cambia entre los dos subprocesos. La JVM no ejecuta el codigo del metodo
 * increment() como una unica instruccion atomica. Mas bien se ejecuta como un conjunto de instrucciones mas pequeñas,
 * similar a esto:
 * <ol>
 * 	<li>Lee c de la memoria en el registro.</li>
 *  <li>Agrega un valor al registro.</li>
 *  <li>Escribe el registro en memoria.</li>
 * </ol>
 * Los dos subprocesos querian incrementar el valor del contador en 1. Por lo tanto, el valor deberia haber sido 2 despues
 * de que los dos subprocesos completaran la ejecucion. Sin embargo, como la ejecucion de los dos hilos esta intercalada, el
 * resultado acaba siendo diferente.
 * <p>
 * En el ejemplo de secuencia de ejecucion mencionado anteriormente, ambos subprocesos leen el valor 0 de la memoria.
 * Luego, incrementan sus valores en 1, al valor y vuelven a escribir el resultado en la memoria. En lugar de 2, el
 * valor que queda en c sera el valor escrito por el ultimo subproceso. En el caso anterior es el hilo A, pero bien
 * podria haber sido el hilo B.
 * <br>
 * Fuentes:
 * <a href="https://jenkov.com/tutorials/java-concurrency/race-conditions-and-critical-sections.html">jenkov.com</a>
 * <a href="https://www.javatpoint.com/race-condition-in-java#:~:text=Java%20is%20a%20multi%2Dthreaded,condition%20is%20a%20concurrency%20bug">javatpoint.com</a>
 * <a href="https://www.netjstech.com/2015/06/race-condition-in-java-multi-threading.html#:~:text=Race%20condition%20in%20Java%20occurs,thus%20the%20name%20race%20condition">netjstech.com</a>
 */

public class RaceCondition implements Runnable {

	// Recurso compartido
	private int c;

	public RaceCondition() {

	}

	@Override
	public void run() {

		/* Para corregir la condicion de carrera, necesitamos tener una forma de restringir el acceso a los recursos a
		 * un solo subproceso a la vez. Tenemos que usar la palabra clave synchronized para sincronizar el acceso al recurso compartido. */

		// SECCION CRITICA
		// synchronized (this) {

		increment();
		System.out.println("Subproceso " + Thread.currentThread().getName() + "\n Valor despues del incremento " + getValue());

		decrement();
		System.out.println("Subproceso " + Thread.currentThread().getName() + "\n Valor despues del decremento " + getValue());

		// }

	}

	public void increment() {

		/* Aqui se simula cierto retraso usando sleep(), ya que en un sistema de produccion real puede haber muchos
		 * procesos ejecutandose y muchos usuarios pueden estar accediendo a la misma aplicacion en un momento dado.
		 * En ese tipo de escenario, no podemos estar seguros de cuando ocurrira el cambio de contexto entre los subprocesos
		 * que compiten por el ciclo de la CPU. Es por eso que los errores relacionados con la condicion de carrera son muy
		 * dificiles de encontrar y es posible que ni siquiera pueda reproducirlos, ya que ese tipo de cambio de contexto
		 * puede no ocurrir cuando intenta reproducir el error relacionado con la condicion de carrera. */
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		c++;
	}

	public void decrement() {
		c--;
	}

	public int getValue() {
		return c;
	}

	public static void main(String[] args) {

		RaceCondition instance = new RaceCondition();

		/* Crea 2 subprocesos en donde se le pasa la instancia compartida y un nombre a cada uno. Por lo tanto,
		 * al compartir la misma instancia, comparten la visibilidad de los datos en memoria. Esto se hace a proposito
		 * para generar una condicion de carrera. Si ambos subprocesos usaran un objeto diferente cada uno, entonces
		 * no habria problema de constistencia, ya que los metodos se llamarian en diferentes instancias y por lo tanto
		 * estarian sincronizados. */
		Thread A = new Thread(instance, "A");
		Thread B = new Thread(instance, "B");

		/* Ejecuta los subprocesos conduciendo a una condicion de carrera en caso de que la seccion critica no este
		 * sincronizada. */
		A.start();
		B.start();
	}

}
