package concurrency;

/**
 * La sincronizacion en Java es la capacidad de controlar el acceso de multiples hilos a cualquier recurso
 * compartido, siendo esta una mejor opcion cuando queremos permitir que solo un hilo acceda al recurso compartido.
 * La sincronizacion se basa en una entidad interna conocida como bloqueo o monitor. Cada objeto tiene un candado
 * asociado. Por convencion, un hilo que necesita acceso constante a los campos de un objeto tiene que adquirir el
 * bloqueo del objeto antes de acceder a ellos, y luego liberar el bloqueo cuando haya terminado con ellos. Si no hay
 * sincronizacion, la salida es inconsistente.
 * <br><br>
 * La sincronizacion se utiliza principalmente para:
 * <ol>
 * 		<li>Evitar la interferencia del hilo.</li>
 * 		<li>Evitar problemas de consistencia.</li>
 * </ol>
 * Todos los bloques sincronizados en el mismo objeto solo pueden tener un subproceso ejecutandose dentro de ellos
 * al mismo tiempo. Todos los demas subprocesos que intentan ingresar al bloque sincronizado se bloquean hasta que
 * el subproceso dentro del bloque sincronizado sale del bloque.
 * <br><br>
 * La palabra clave sincronizada se puede utilizar para marcar cuatro tipos diferentes de bloques:
 * <ol>
 * 		<li>Metodos de instancia</li>
 * 		<li>Metodos estaticos</li>
 * 		<li>Bloques de codigo dentro de metodos de instancia</li>
 * 		<li>Bloques de codigo dentro de metodos estaticos</li>
 * </ol>
 * Cita de Sun:
 * <br>
 * <b>synchronized</b> <i>"Los metodos permiten una estrategia simple para prevenir la interferencia del hilo y los errores de
 * consistencia de la memoria: si un objeto es visible para mas de un hilo, todas las lecturas o escrituras en las
 * variables de ese objeto se realizan a traves de metodos sincronizados."</i>
 * <p>
 * En pocas palabras: cuando tiene dos subprocesos que leen y escriben en el mismo "recurso", digamos una variable
 * nombrada altura, debe asegurarse de que estos subprocesos accedan a la variable de forma atomica. Sin la palabra clave
 * synchronized, es posible que el hilo A no vea el cambio que se hizo en el hilo B a la variable altura o, lo que es peor,
 * puede que solo se haya cambiado a la mitad. Esto no seria lo que logicamente esperas.
 * <br><br>
 * <h3>Sincronizacion y visibilidad de datos</h3>
 * Sin el uso de la palabra clave synchronized (o la palabra clave volatile de Java), no hay garantia de que cuando un subproceso
 * cambia el valor de una variable compartida con otros subprocesos (por ejemplo, a traves de un objeto al que todos los
 * subprocesos tienen acceso), los otros subprocesos pueden ver el valor cambiado. No hay garantias sobre cuando una variable
 * guardada en un registro de CPU por un subproceso se "compromete" en la memoria principal, y no hay garantia sobre cuando
 * otros subprocesos "actualizan" una variable guardada en un registro de CPU desde la memoria principal.
 * <br><br>
 * La palabra clave synchronized cambia eso. Cuando un subproceso ingresa a un bloque sincronizado, actualizara los valores
 * de todas las variables visibles para el subproceso. Cuando un subproceso sale de un bloque sincronizado, todos los cambios
 * en las variables visibles para el subproceso se confirmaran en la memoria principal. Esto es similar a como funciona la
 * palabra clave volatil.
 * <br><br>
 * <h3>Sobrecarga de rendimiento de bloque sincronizado</h3>
 * Hay una pequeña sobrecarga de rendimiento asociada con la entrada y salida de un bloque sincronizado en Java. A medida que Java
 * ha evolucionado, esta sobrecarga de rendimiento se ha reducido, pero todavia hay que pagar un pequeño precio.
 * <br><br>
 * La sobrecarga de rendimiento de ingresar y salir de un bloque sincronizado es principalmente algo de lo que preocuparse si
 * ingresa y sale de un bloque sincronizado muchas veces dentro de un ciclo cerrado mas o menos.
 * <br><br>
 * Ademas, trate de no tener bloques sincronizados mas grandes de lo necesario. En otras palabras, solo sincronice las operaciones
 * que son realmente necesarias para sincronizar, para evitar bloquear otros subprocesos para que no ejecuten operaciones que no
 * tienen que sincronizarse. Solo las instrucciones absolutamente necesarias en bloques sincronizados. Eso deberia aumentar
 * el paralelismo de su codigo.
 * <br><br>
 * <b><i>Tener una comprension basica del modelo de memoria de Java es realmente importante para obtener una concurrencia
 * correcta.</i></b>
 * <br>
 * Fuentes:
 * <a href="https://jenkov.com/tutorials/java-concurrency/synchronized.html">jenkov.com</a>
 *
 * @author Ruso
 */

public class Synchronized extends Thread {

	private int n1, n3;
	private static int n2, n4;

	/* -Metodo de instancia sincronizado
	 * Se sincronizan en la instancia (objeto) que posee el metodo. Por lo tanto, cada instancia tiene sus metodos
	 * sincronizados en un objeto diferente: la instancia propietaria.
	 *
	 * Solo un subproceso por instancia puede ejecutarse dentro de un metodo de instancia sincronizado. Si existe mas de
	 * una instancia, un subproceso a la vez puede ejecutarse dentro de un metodo de instancia sincronizada por instancia.
	 * Un hilo por instancia. */
	public synchronized void add(int value) {
		n1 += value;
	}

	/* -Metodo estatico sincronizado
	 * Se sincronizan en el objeto de clase de la clase a la que pertenece el metodo. Dado que solo existe un objeto de
	 * clase en la VM de Java por clase, solo un subproceso puede ejecutarse dentro de un metodo sincronizado estatico
	 * en la misma clase. */
	public static synchronized void subtract(int value) {
		n2 -= value;
	}

	/* -Bloque sincronizado en metodo de instancia
	 * Observe como la construccion del bloque sincronizado toma un objeto entre parentesis. En el ejemplo, se usa "this",
	 * que es la instancia en la que se llama al metodo split. El objeto tomado entre parentesis por la construccion
	 * sincronizada se denomina objeto monitor. Se dice que el codigo esta sincronizado en el objeto monitor. Un metodo de
	 * instancia sincronizado utiliza el objeto al que pertenece como objeto de supervision. */
	public void split(int value) {
		synchronized (this) {
			n3 /= value;
		}
	}

	/* -Bloque sincronizado en metodo estatico
	 * Los bloques sincronizados tambien se pueden usar dentro de metodos estaticos. Este metodo se sincroniza en el objeto de
	 * clase de la clase a la que pertenece el metodo: */
	public static void multiply(int value) {
		synchronized (Synchronized.class) {
			n4 *= value;
		}
	}
}
