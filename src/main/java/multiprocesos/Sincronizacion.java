package multiprocesos;

/**
 * La sincronizacion en Java es la capacidad de controlar el acceso de multiples Thread a cualquier recurso
 * compartido, siendo esta una mejor opcion cuando queremos permitir que solo un Thread acceda al recurso compartido.
 * La sincronizacion se basa en una entidad interna conocida como bloqueo o monitor. Cada objeto tiene un candado
 * asociado. Por convencion, un hilo que necesita acceso constante a los campos de un objeto tiene que adquirir el
 * bloqueo del objeto antes de acceder a ellos, y luego liberar el bloqueo cuando haya terminado con ellos. Si no hay
 * sincronizacion, la salida es inconsistente.
 * 
 * Siempre se entienden mejor las cosas tomando ejemplos de la realidad...
 * Tenemos un baño en donde rulo entra primero. Al cerrar la puerta adquiere el bloqueo, por lo tanto
 * ruso no puede entrar, evitando que se genere un desastre en el inodoro.
 * Esto se podria entender como una sincronizacion de rulo y ruso para usar el baño correctamente, en donde la puerta
 * es el bloqueo.
 * 
 * ¿Por que utilizar la sincronizacion?
 * La sincronizacion se utiliza principalmente para:
 * 1. Evitar la interferencia del hilo.
 * 2. Evitar problemas de consistencia.
 * 
 * Hay dos tipos de sincronizacion de subprocesos:
 * -La sincronizacion de subprocesos mutua exclusiva que ayuda a evitar que los hilos interfieran entre si mientras
 * comparten datos. Esto se puede hacer de tres formas en Java:
 * 1. Por metodo sincronizado.
 * 2. Por bloque sincronizado.
 * 3. Por sincronizacion estatica.
 * -Cooperacion (comunicacion entre subprocesos en java)
 * 
 * Cita de Sun:
 * synchronized "Los metodos permiten una estrategia simple para prevenir la interferencia del hilo y los errores de
 * consistencia de la memoria: si un objeto es visible para mas de un hilo, todas las lecturas o escrituras en las
 * variables de ese objeto se realizan a traves de metodos sincronizados."
 * 
 * En pocas palabras: cuando tiene dos subprocesos que leen y escriben en el mismo "recurso", digamos una variable
 * nombrada foo, debe asegurarse de que estos subprocesos accedan a la variable de forma atomica. Sin la palabra clave
 * synchronized, es posible que el hilo 1 no vea el cambio que se hizo en el hilo 2 foo o, lo que es peor,
 * puede que solo se haya cambiado a la mitad. Esto no seria lo que logicamente esperas.
 * 
 * Tener una comprension basica del modelo de memoria de Java es realmente importante para obtener una concurrencia
 * correcta.
 * https://en.wikipedia.org/wiki/Java_memory_model
 * 
 * @author Juan Debenedetti aka Ru$o
 * 
 */

// Extendiendo la clase Thread es otra forma de aplicar el metodo run()
public class Sincronizacion extends Thread {

	private Objeto objeto;
	private String name;

	public Sincronizacion() {
	}

	public Sincronizacion(String name) {
		this.name = name;
	}

	public Sincronizacion(String name, Objeto objeto) {
		this.name = name;
		this.objeto = objeto;
	}

	@Override
	public void run() {
		// objeto.metodo(name);
		metodo(name);
	}

	// Clase interna para sincronizar hilos (se declara estatica para poder crear objetos desde el main)
	private static class Objeto {

		/* Usando synchronized se bloquea la llamada del siguiente hilo al metodo, siempre que la ejecucion del hilo anterior
		 * no haya finalizado. Los hilos pueden acceder a este metodo de uno en uno. Sin synchronized todos los hilos pueden
		 * acceder a este metodo simultaneamente.
		 * Cuando un hilo llama al metodo sincronizado del objeto, adquiere el bloqueo. Entonces cualquier hilo nuevo, no puede
		 * llamar al metodo sincronizado del MISMO objeto siempre que el hilo anterior que habia adquirido el bloqueo no se
		 * libere. */
		private synchronized void metodo(String name) {

			System.out.println("Ejecutando el " + name + "...");

			for (int i = 1; i <= 5; i++) {
				System.out.print(i + " ");
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					System.out.println(e);
				}
			}

			System.out.println();
		}

	}

	private synchronized void metodo(String name) {

		System.out.println("Ejecutando el " + name + "...");

		for (int i = 1; i <= 5; i++) {
			System.out.print(i + " ");
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		System.out.println();

	}

	/* Usando un metodo sincronizado estatico, el bloqueo estara en la clase, no en el objeto.
	 * El hilo adquiere el bloqueo asociado con la clase, por lo tanto cualquier otro hilo no podra llamar a ningun metodo
	 * sincronizado estatico de la clase siempre que el bloqueo de nivel de clase no sea liberado por el hilo que
	 * actualmente mantiene el bloqueo. */
	private static synchronized void metodoEstatico(String name) {

		System.out.println("Ejecutando el " + name + "...");

		for (int i = 1; i <= 5; i++) {
			System.out.print(i + " ");
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		System.out.println();

	}

	private static class T extends Thread {

		@Override
		public void run() {
			metodo();
		}

		private synchronized void metodo() {

			System.out.println("Ejecutando el " + Thread.currentThread().getName() + "...");

			for (int i = 1; i <= 5; i++) {
				System.out.print(i + " ");
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					System.out.println(e);
				}
			}

			System.out.println();

		}
	}

	private static class R implements Runnable {

		private Sincronizacion s;
		private String name;

		public R(Sincronizacion s, String name) {
			this.s = s;
			this.name = name;
		}

		@Override
		public void run() {
			s.metodo(name);
		}

	}

	public static void main(String[] args) {

		/* Si uso dos objetos para compartir un recurso (un metodo en este caso) aunque use synchronized, los hilos no se van a
		 * sincronizar ya que ambos pertenecen a distintos objetos, en cambio si uso un solo objeto los hilos se sincronizan.
		 * TODO ¿Utilizando los metodos wait() y notifyAll() se sincronizarian los hilos de distintos objetos? */
//		Objeto objeto = new Objeto();
//		new Sincronizacion("Hilo 1", objeto).start();
//		new Sincronizacion("Hilo 2", objeto).start();}

		// La llamada del metodo sincronizado estatico es lo mismo que usar un solo objeto para la sincronizacion
//		new Sincronizacion("Hilo 1").start();
//		new Sincronizacion("Hilo 2").start();

		// Manejo de hilos usando la clase Thread
//		T h = new T();
//		new Thread(h).start();
//		new Thread(h).start();

		// Manejo de hilos usando la interfaz Runnable
		Sincronizacion s = new Sincronizacion();
		new Thread(new R(s, "Hilo 1")).start();
		new Thread(new R(s, "Hilo 2")).start();

	}

}
