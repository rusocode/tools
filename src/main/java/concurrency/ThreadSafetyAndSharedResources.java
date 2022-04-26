package concurrency;

/**
 * El código que es seguro llamar por múltiples subprocesos simultáneamente se denomina subproceso seguro. Si un
 * fragmento de código es seguro para subprocesos, no contiene condiciones de carrera. La condición de carrera solo
 * ocurre cuando varios subprocesos actualizan los recursos compartidos. Por lo tanto, es importante saber qué recursos
 * comparten los subprocesos de Java cuando se ejecutan.
 * 
 * -La regla de escape del control de subprocesos
 * "Si un recurso se crea, utiliza y elimina dentro el control del mismo hilo, y nunca escapa al control de este hilo,
 * el uso de ese recurso es seguro para subprocesos."
 * 
 * Los recursos pueden ser cualquier recurso compartido como un objeto, una matriz, un archivo, una conexión a la base
 * de datos, un socket, etc. En Java, no siempre se desechan los objetos de forma explícita, por lo que "desechar"
 * significa perder o anular la referencia al objeto.
 * 
 * Fuentes:
 * http://tutorials.jenkov.com/java-concurrency/race-conditions-and-critical-sections.html
 * http://tutorials.jenkov.com/java-concurrency/thread-safety.html
 * 
 * @author Ruso
 * 
 */

public class ThreadSafetyAndSharedResources {

	private StringBuilder builder = new StringBuilder();

	public static void main(String[] args) throws InterruptedException {

		ThreadSafetyAndSharedResources sharedInstance = new ThreadSafetyAndSharedResources();

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

	private void add(String text) {
		builder.append(text);
	}

	/* Las variables locales se almacenan en la propia pila de cada subproceso. Eso significa que las variables locales
	 * nunca se comparten entre subprocesos. Eso también significa que todas las variables primitivas locales son seguras
	 * para subprocesos. */
	public void someMethod() {
		long threadSafeInt = 0;
		threadSafeInt++;
	}

	public void someMethod2() {
		LocalObject localObject = new LocalObject();
		localObject.callMethod();
	}

	private static class MyRunnable implements Runnable {

		ThreadSafetyAndSharedResources instance;

		public MyRunnable(ThreadSafetyAndSharedResources instance) {
			this.instance = instance;
		}

		@Override
		public void run() {
			instance.add("text");
		}

	}

	private class LocalObject {
		public void callMethod() {

		}

	}

}
