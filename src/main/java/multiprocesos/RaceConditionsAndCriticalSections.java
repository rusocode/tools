package multiprocesos;

/**
 * Una condición de carrera es un problema de concurrencia que puede ocurrir dentro de una sección crítica. Una sección
 * crítica es una sección de código que es ejecutada por múltiples subprocesos y donde la secuencia de ejecución de los
 * subprocesos marca una diferencia en el resultado de la ejecución concurrente de la sección crítica.
 * 
 * Cuando el resultado de varios subprocesos que ejecutan una sección crítica puede diferir según la secuencia en la que
 * se ejecutan los subprocesos, se dice que la sección crítica contiene una condición de carrera. El término condición
 * de carrera proviene de la metáfora de que los hilos corren a través de la sección crítica y que el resultado de esa
 * carrera impacta el resultado de ejecutar la sección crítica.
 * 
 * Las condiciones de carrera pueden ocurrir cuando dos o más subprocesos leen y escriben la misma variable de acuerdo
 * con uno de estos dos patrones:
 * 
 * ° Leer-modificar-escribir
 * ° Verificar y luego actuar
 * 
 * Pero si varios subprocesos leen el mismo recurso, no se producen condiciones de carrera.
 * 
 * -La regla de escape del control de subprocesos
 * "Si un recurso se crea, utiliza y elimina dentro el control del mismo hilo, y nunca escapa al control de este hilo,
 * el uso de ese recurso es seguro para subprocesos."
 * 
 * Fuentes:
 * http://tutorials.jenkov.com/java-concurrency/race-conditions-and-critical-sections.html
 * http://tutorials.jenkov.com/java-concurrency/thread-safety.html
 * 
 * @author Ru$o
 * 
 */

public class RaceConditionsAndCriticalSections {

	StringBuilder builder = new StringBuilder();

	public static void main(String[] args) throws InterruptedException {

		RaceConditionsAndCriticalSections sharedInstance = new RaceConditionsAndCriticalSections();

		/* Las dos instancias de MyRunnable comparten la misma instancia de RaceConditionsAndCriticalSections. Por lo tanto,
		 * cuando llaman al metodo add() en la instancia de RaceConditionsAndCriticalSections, conduce a una condicion de
		 * carrera. */
		new Thread(new MyRunnable(sharedInstance)).start();
		new Thread(new MyRunnable(sharedInstance)).start();

		/* Sin embargo, si dos subprocesos llaman al metodo add() simultaneamente EN DIFERENTES INSTANCIAS, no conduce a una
		 * condicion de carrera. */
//		new Thread(new MyRunnable(new RaceConditionsAndCriticalSections())).start();
//		new Thread(new MyRunnable(new RaceConditionsAndCriticalSections())).start();

	}

	/* "Seccion critica" de lectura-modificacion-escritura que puede fallar si se ejecuta por varios subprocesos
	 * simultaneamente, siendo esto una condicion de carrera.
	 * 
	 * Para evitar que ocurran condiciones de carrera, debe asegurarse de que la seccion critica se ejecute como una
	 * instruccion atomica. Eso significa que una vez que un solo subproceso lo esta ejecutando, ningun otro subproceso
	 * puede ejecutarlo hasta que el primer subproceso haya abandonado la seccion critica.
	 * 
	 * Las condiciones de carrera pueden evitarse mediante una sincronizacion de subprocesos adecuada en las secciones
	 * criticas. */
	private void add(String text) {
		builder.append(text);
	}

	private static class MyRunnable implements Runnable {

		RaceConditionsAndCriticalSections instance;

		public MyRunnable(RaceConditionsAndCriticalSections instance) {
			this.instance = instance;

		}

		@Override
		public void run() {
			instance.add("text");
		}

	}

}
