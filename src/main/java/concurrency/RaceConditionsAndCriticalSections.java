package concurrency;

/**
 * Una condición de carrera es un problema de concurrencia que puede ocurrir dentro de una sección crítica. Una sección
 * crítica es una sección de código que es ejecutada por múltiples subprocesos y donde la secuencia de ejecución de los
 * subprocesos hace una diferencia en el resultado de la ejecución concurrente de la sección crítica.
 * 
 * Cuando el resultado de varios subprocesos que ejecutan una sección crítica puede diferir según la secuencia en la que
 * se ejecutan los subprocesos, se dice que la sección crítica contiene una condición de carrera. El término condición
 * de carrera se deriva de la metáfora de que los hilos corren a través de la sección crítica y que el resultado de esa
 * carrera impacta el resultado de ejecutar la sección crítica.
 * 
 * - Dos tipos de condiciones de carrera
 * . Leer-modificar-escribir
 * . Verifique y luego actúe
 * 
 * El patrón de lectura-modificación-escritura significa que dos o más subprocesos primero leen una variable
 * determinada, luego modifican su valor y lo vuelven a escribir en la variable. Para que esto cause un problema, el
 * nuevo valor debe depender de una forma u otra del valor anterior. El problema que puede ocurrir es que si dos
 * subprocesos leen el valor (en los registros de la CPU), modifican el valor (en los registros de la CPU) y luego
 * vuelven a escribir los valores.
 * 
 * El patrón de verificar y luego actuar significa que dos o más subprocesos verifican una condición dada, por ejemplo,
 * si un Mapa contiene un valor dado, y luego actúan en función de esa información, por ejemplo. tomando el valor del
 * Mapa. El problema puede ocurrir si dos subprocesos verifican el Mapa para un valor dado al mismo tiempo (vean que el
 * valor está presente) y luego ambos subprocesos intentan tomar (eliminar) ese valor. Sin embargo, solo uno de los
 * subprocesos puede tomar el valor. El otro subproceso obtendrá un valor nulo. Esto también podría suceder si se usara
 * una Cola en lugar de un Mapa.
 * 
 * Fuente
 * https://jenkov.com/tutorials/java-concurrency/race-conditions-and-critical-sections.html
 * 
 */

public class RaceConditionsAndCriticalSections implements Runnable {

	protected long count;

	/**
	 * - Lectura-Modificación-Escritura de secciones críticas
	 * 
	 * Imagínese si dos subprocesos, A y B, están ejecutando el método add en la misma instancia de esta clase. No hay
	 * forma de saber cuándo el sistema operativo cambia entre los dos subprocesos. La JVM no ejecuta el código del método
	 * add() como una única instrucción atómica. Más bien se ejecuta como un conjunto de instrucciones más pequeñas, similar
	 * a esto:
	 * 
	 * 1. Lea count de la memoria en el registro.
	 * 2. Agregar valor al registro.
	 * 3. Escribir registro en memoria.
	 * 
	 * Observe lo que sucede con la siguiente ejecución mixta de los hilos A y B:
	 * A: Lee count en un registro (0)
	 * B: Lee count en un registro (0)
	 * B: Agrega valor 2 al registro
	 * B: Escribe el valor del registro (2) de vuelta a la memoria. count ahora es igual a 2
	 * A: Agrega valor 3 al registro
	 * A: Escribe el valor del registro (3) de vuelta a la memoria. count ahora es igual a 3
	 * 
	 * Los dos subprocesos querían agregar los valores 2 y 3 al contador. Por lo tanto, el valor debería haber sido 5
	 * después de que los dos subprocesos completaran la ejecución. Sin embargo, como la ejecución de los dos hilos está
	 * intercalada, el resultado acaba siendo diferente.
	 * 
	 * En el ejemplo de secuencia de ejecución mencionado anteriormente, ambos subprocesos leen el valor 0 de la memoria.
	 * Luego, suman sus valores individuales, 2 y 3, al valor y vuelven a escribir el resultado en la memoria. En lugar de
	 * 5, el valor que queda en this.count será el valor escrito por el último subproceso para escribir su valor. En el caso
	 * anterior es el hilo A, pero bien podría haber sido el hilo B.
	 * 
	 */

	public void add(long value) {
		count += value;
	}

	/**
	 * - Condiciones de carrera en secciones críticas de lectura, modificación y escritura
	 * 
	 * El código del método add() del ejemplo anterior contiene una sección crítica. Cuando varios subprocesos ejecutan esta
	 * sección crítica, se producen condiciones de carrera.
	 * 
	 * Más formalmente, la situación en la que dos subprocesos compiten por el mismo recurso, donde la secuencia en la que
	 * se accede al recurso es importante, se denomina condiciones de carrera. Una sección de código que conduce a
	 * condiciones de carrera se denomina sección crítica.
	 * 
	 */

	/**
	 * - Prevención de condiciones de carrera
	 * 
	 * Para evitar que ocurran condiciones de carrera, debe asegurarse de que la sección crítica se ejecute como una
	 * instrucción atómica. Eso significa que una vez que un único subproceso lo está ejecutando, ningún otro subproceso
	 * puede ejecutarlo hasta que el primer subproceso haya abandonado la sección crítica.
	 * 
	 * Las condiciones de carrera se pueden evitar mediante la sincronización adecuada de subprocesos en secciones críticas.
	 * La sincronización de subprocesos se puede lograr utilizando un bloque sincronizado de código Java. La sincronización
	 * de subprocesos también se puede lograr utilizando otras construcciones de sincronización como bloqueos o variables
	 * atómicas como java.util.concurrent.atomic.AtomicInteger.
	 * 
	 */

	/**
	 * Observe cómo el método add() agrega valores a dos variables miembro de suma diferentes. Para evitar condiciones de
	 * carrera, la suma se ejecuta dentro de un bloque sincronizado de Java. Con esta implementación, solo un único
	 * subproceso puede ejecutar la suma al mismo tiempo.
	 * 
	 */

	@Override
	public void run() {
		// add();
	}

	public static void main(String[] args) {

		RaceConditionsAndCriticalSections instance = new RaceConditionsAndCriticalSections();

		Thread A = new Thread(instance);
		Thread B = new Thread(instance);

		A.start();
		B.start();

	}

}
