package concurrency;

/**
 * El codigo que es seguro llamar por multiples subprocesos simultaneamente se denomina subproceso seguro. Si un
 * fragmento de codigo es seguro para subprocesos, no contiene condiciones de carrera. La condicion de carrera solo
 * ocurre cuando varios subprocesos actualizan los recursos compartidos. Por lo tanto, es importante saber que recursos
 * comparten los subprocesos de Java cuando se ejecutan.
 * <br><br>
 * <h3>La regla de escape del control de subprocesos</h3>
 * <i>"Si un recurso se crea, utiliza y elimina dentro el control del mismo hilo, y nunca escapa al control de este hilo,
 * el uso de ese recurso es seguro para subprocesos."</i>
 * <br><br>
 * Los recursos pueden ser cualquier recurso compartido como un objeto, una matriz, un archivo, una conexion a la base
 * de datos, un socket, etc. En Java, no siempre se desechan los objetos de forma explicita, por lo que "desechar"
 * significa perder o anular la referencia al objeto.
 * <br>
 * Fuentes:
 * <a href="https://jenkov.com/tutorials/java-concurrency/thread-safety.html">jenkov.com/</a>
 *
 * @author Ruso
 */

public class SharedResources {

	private StringBuilder builder = new StringBuilder();

	public static void main(String[] args) throws InterruptedException {

		SharedResources sharedInstance = new SharedResources();

		/* - Variables de miembro de objeto
		 * Las variables de miembro de objeto (campos) se almacenan en el heap junto con el objeto. Por lo tanto, si dos
		 * subprocesos llaman a un metodo en la misma instancia de objeto y este método actualiza las variables de miembro del
		 * objeto, el método no es seguro para subprocesos.

		 * Las dos instancias de MyRunnable comparten la misma instancia de objeto. Por lo tanto, cuando llaman al metodo add()
		 * en la instancia de objeto, conduce a una condicion de carrera. */
		new Thread(new MyRunnable(sharedInstance)).start();
		new Thread(new MyRunnable(sharedInstance)).start();

		/* Sin embargo, si dos subprocesos llaman al metodo add() simultaneamente EN DIFERENTES INSTANCIAS, no conduce a una
		 * condicion de carrera. */
		// new Thread(new MyRunnable(new RaceConditionsAndCriticalSections())).start();
		// new Thread(new MyRunnable(new RaceConditionsAndCriticalSections())).start();

	}

	private void add(String text) {
		builder.append(text);
	}

	/**
	 * <h3>Variables locales</h3>
	 * Las variables locales se almacenan en la propia pila (stack) de cada subproceso. Eso significa que las variables locales
	 * nunca se comparten entre subprocesos. Eso tambien significa que todas las variables primitivas locales son seguras
	 * para subprocesos.
	 */
	public void someMethod() {
		long threadSafeInt = 0;
		threadSafeInt++;
	}

	/**
	 * <h3>Referencias a objetos locales</h3>
	 * Las referencias locales a objetos son un poco diferentes. La referencia en si no se comparte. Sin embargo, el objeto
	 * al que se hace referencia no se almacena en la pila local de cada subproceso. Todos los objetos se almacenan en el
	 * monton compartido (heap).
	 * <p>
	 * Si un objeto creado localmente nunca escapa del metodo en el que se creo, es seguro para subprocesos. De hecho,
	 * tambien puede pasarlo a otros metodos y objetos siempre que ninguno de estos metodos u objetos haga que el objeto
	 * pasado este disponible para otros subprocesos.
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

		SharedResources instance;

		public MyRunnable(SharedResources instance) {
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
