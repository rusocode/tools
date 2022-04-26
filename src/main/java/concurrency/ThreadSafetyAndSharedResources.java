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

		/**
		 * - Object Member Variables
		 * Las variables de miembro de objeto (campos) se almacenan en el heap junto con el objeto. Por lo tanto, si dos
		 * subprocesos llaman a un método en la misma instancia de objeto y este método actualiza las variables de miembro del
		 * objeto, el método no es seguro para subprocesos.
		 * 
		 * Las dos instancias de MyRunnable comparten la misma instancia de objeto. Por lo tanto, cuando llaman al metodo add()
		 * en la instancia de objeto, conduce a una condicion de carrera.
		 */
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

	/**
	 * - Local Variables
	 * Las variables locales se almacenan en la propia pila de cada subproceso. Eso significa que las variables locales
	 * nunca se comparten entre subprocesos. Eso también significa que todas las variables primitivas locales son seguras
	 * para subprocesos.
	 */
	public void someMethod() {
		long threadSafeInt = 0;
		threadSafeInt++;
	}

	/**
	 * - Local Object References
	 * Las referencias locales a objetos son un poco diferentes. La referencia en sí no se comparte. Sin embargo, el objeto
	 * al que se hace referencia no se almacena en la pila local de cada subproceso. Todos los objetos se almacenan en el
	 * montón compartido.
	 * 
	 * Si un objeto creado localmente nunca escapa del método en el que se creó, es seguro para subprocesos. De hecho,
	 * también puede pasarlo a otros métodos y objetos siempre que ninguno de estos métodos u objetos haga que el objeto
	 * pasado esté disponible para otros subprocesos.
	 */
	public void someMethod2() {
		LocalObject localObject = new LocalObject();
		localObject.callMethod();
		method2(localObject);
	}

	public void method2(LocalObject localObject) {
		localObject.setValue("value");
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

		String value;

		public void callMethod() {

		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}
