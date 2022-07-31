package _LABORATORIO;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Java proporciona una clase con el nombre Class en el paquete java.lang. Las instancias de la clase Class representan
 * clases e interfaces en una aplicacion Java en ejecucion. Los tipos primitivos de Java (boolean, byte, char, short,
 * int, long, float y double) y la palabra clave void tambien se representan como objetos Class. No tiene constructor
 * publico. Los objetos de Class son construidos automaticamente por la JVM a medida que se cargan las clases y mediante
 * llamadas al metodo defineClass en el ClassLoader. Es una clase final, por lo que no se puede extender.
 * <p>
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
 * ejemplo: Class_.class.getName().
 * <br><br>
 * Fuentes:
 * <a href="https://mundosica.com/el-metodo-getclass-explicacion-y-uso-practico-construyendo-librerias-en-java-i/">...</a>
 *
 * @author Juan Debenedetti
 */

public class Class_ {

	private String valor1 = "valor default";
	private String valor2;
	private final double DECIMAL = 12.5;

	public Class_() {

	}

	public Class_(String valor1) {
		this.valor1 = valor1;
	}

	/**
	 * Devuelve el objeto Class asociado con la clase o interfaz con el nombre de cadena dado.
	 *
	 * @param className el ombre completo de la clase especificada.
	 * @return el objeto Class para la clase con el nombre especificado.
	 */
	public static Class<?> forName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			System.err.println("No se pudo localizar la clase " + className + "...\n" + e.getMessage());
		}
		return null;
	}

	/**
	 * Devuelve una instancia recien asignada de la clase representada por el objeto Class.
	 */
	public static Class_ getInstance() throws InstantiationException, IllegalAccessException {
		return Class_.class.newInstance();
	}

	/**
	 * Devuelve una instancia recien asignada de la clase representada por el objeto Class, utilizando un constructor
	 * con parametros (constructor dinamico?).
	 */
	public static Class_ getInstance(String value) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		return Class_.class.getConstructor(String.class).newInstance(value);
	}

	public static void printClassName(Object obj) {
		System.out.println("La clase de " + obj + " es " + obj.getClass().getName());
	}

	public void printCanonicalName() {
		System.out.println("El nombre canonico de esta clase es: " + this.getClass().getCanonicalName());
	}

	/**
	 * Imprime los atributos/campos (fields) publicos, protegidos, de acceso predeterminado (paquete) y privados,
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

	public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

		Test test = new Test();

		/* Como la clase Class no tiene ningun constructor, hay un metodo de fabrica estatico presente, que es
		 * Class.forName(), y se usa para crear objetos de la clase Class asociados con el nombre de clase dado. */
		Class<?> obj = forName("_LABORATORIO.Test");
		System.out.println(obj.getSimpleName());

		printClassName(test);

		/* Devuelve un objeto Constructor que refleja el constructor publico especificado de la clase representada
		 * por este objeto Class. El parametro ParameterTypes es una matriz de objetos Class que identifican los
		 * tipos de parametros formales del constructor, en el orden declarado (es un varargs de tipo Class). */
		Constructor<?> constructor = obj.getConstructor(Test.class);

		/* Utiliza el constructor representado por este objeto Constructor para crear e inicializar una nueva
		 * instancia de la clase de declaracion del constructor, con los parametros de inicializacion especificados. */
		test = (Test) constructor.newInstance(test);

	}

}
