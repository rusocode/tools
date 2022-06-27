package _LABORATORIO;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * El objeto Class representa la clase de tiempo de ejecucion del objeto especificado.
 * La clase Class no tiene un constructor publico. En su lugar, los objetos de Class son construidos automaticamente por
 * la JVM a medida que se cargan las clases y mediante llamadas al metodo defineClass en el class loader.
 * <br>
 * Para imprimir el nombre de clase de un objeto se utiliza el objeto Class, que se obtiene utilizando el metodo
 * getCLass(). Este metodo nos permite acceder a una serie de caracteristicas del objeto:
 * <pre>
 * {@code
 * void printClassName(Object obj) {
 * 	System.out.println("The class of " + obj + " is " + obj.getClass().getName());
 * }
 * }
 * </pre>
 * Es importante aclarar que para utilizar el objeto Class en un contexto estatico se utiliza el literal "class",
 * ejemplo: MiClase.class.getName().
 * <br>
 * <a href="https://mundosica.com/el-metodo-getclass-explicacion-y-uso-practico-construyendo-librerias-en-java-i/">...</a>
 *
 * @author Juan Debenedetti
 */

public class MiClase {

	private String valor1 = "valor default";
	private String valor2;
	private final double DECIMAL = 12.5;
	private static Game game;

	public MiClase() {

	}

	public MiClase(String valor1) {
		this.valor1 = valor1;
	}

	/**
	 * Devuelve una instancia recien asignada de la clase representada por el objeto Class.
	 */
	public static MiClase getInstance() throws InstantiationException, IllegalAccessException {
		return MiClase.class.newInstance();
	}

	/**
	 * Devuelve una instancia recien asignada de la clase representada por el objeto Class, utilizando un constructor
	 * con parametros (constructor dinamico?).
	 */
	public static MiClase getInstance(String value) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		return MiClase.class.getConstructor(String.class).newInstance(value);
	}

	public void printClassName() {
		System.out.println("El nombre de esta clase es: " + this.getClass().getSimpleName());
	}

	public void printCanonicalName() {
		System.out.println("El nombre canonico de esta clase es: " + this.getClass().getCanonicalName());
	}

	/**
	 * Imprime los atributos/campos (fields) publicos, protegidos, de acceso predeterminado (paquete) y privados
	 * declarados por la clase representada por el objeto Class.
	 */
	public void imprimirAtributos() {
		System.out.println("Atributos>");
		Field[] fields = this.getClass().getDeclaredFields();
		if (fields.length != 0) {
			for (Field field : fields) {
				try {
					System.out.println(field.getName() + ": " + field.get(this));
				} catch (IllegalArgumentException e) {
					System.err.println("El objeto especificado no es una instancia de la clase o interfaz que declara " +
							"el campo subyacente (o una subclase o implementador del mismo).");
				} catch (IllegalAccessException e) {
					System.err.println("No se puede acceder al campo subyacente.");
				}
			}
		} else System.out.println("No hay atributos declarados.");

	}

	/**
	 * Devuelve el objeto Class asociado con la clase o interfaz con el nombre de cadena proporcionado.
	 */
	public Class forName(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			System.err.println("Clase no encontrada: " + name);
		}
		return null;
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Screen newScreen = null;

		MiClase clase1 = new MiClase();
		// clase1.printClassName();
		// clase1.imprimirAtributos();

		try {

			/* Devuelve el objeto Class asociado con la clase con el nombre de cadena especificado ("_LABORATORIO.Byte_").
			 * Ahora a partir del objeto Class puedo obtener las caracteristicas del objeto especificado. */
			Class<?> clase = Class.forName("_LABORATORIO.GameScreen");

			/* Devuelve un objeto Constructor que refleja el constructor publico especificado de la clase representada
			 * por este objeto Class. El parametro ParameterTypes es una matriz de objetos Class que identifican los
			 * tipos de parametros formales del constructor, en el orden declarado (es un varargs de tipo Class). */
			Constructor<?> constructor = clase.getConstructor(Game.class);

			/* Utiliza el constructor representado por este objeto Constructor para crear e inicializar una nueva
			 * instancia de la clase de declaracion del constructor, con los parametros de inicializacion especificados. */
			newScreen = (Screen) constructor.newInstance(game);

			newScreen.print();

		} catch (ClassNotFoundException e) {
			System.err.println("Clase no encontrada! " + e.getMessage());
		} catch (NoSuchMethodException e) {
			System.out.println("Metodo no encontrado! " + e.getMessage());
		}

		// MiClase clase2 = MiClase.getInstance();
		// clase2.printCanonicalName();

	}

}
