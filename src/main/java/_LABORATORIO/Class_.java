package _LABORATORIO;

import java.lang.reflect.*;

/**
 * Java proporciona una clase con el nombre Class en el paquete java.lang. Las instancias de la clase Class representan
 * clases e interfaces en una aplicacion Java en ejecucion. Los tipos primitivos (boolean, byte, char, short, int, long,
 * float y double) y la palabra clave void tambien se representan como objetos Class. No tiene constructor publico. Los
 * objetos de Class son construidos automaticamente por la JVM a medida que se cargan las clases y mediante llamadas al
 * metodo defineClass en el ClassLoader. Es una clase final, por lo que no se puede extender.
 *
 * <p> Hay 3 formas de crear un objeto Class:
 * <ol>
 * <li><b>Class.forName(“className”):</b> como la clase Class no tiene ningun constructor, hay un metodo de fabrica
 * estatico presente, que es Class.forName(), y se usa para crear objetos de la clase Class asociados con el nombre de
 * clase dado. A continuacion se muestra la sintaxis:
 *
 * <pre> {@code Class c = Class.forName(String className);} </pre>
 *
 * <p> La declaracion anterior crea el objeto Class para la clase pasada como un argumento String (className). Tenga en
 * cuenta que el parametro className debe ser el nombre completo de la clase deseada para la que se creara el objeto
 * Class. Los metodos en cualquier clase en Java que devuelven el mismo objeto de clase tambien se conocen como metodos
 * de fabrica. El nombre de clase para el que se va a crear el objeto Class se determina en tiempo de ejecucion.
 * <li><b>Test.class:</b> cuando escribimos .class despues de un nombre de clase, hace referencia al objeto Class que
 * representa la clase dada. Se usa principalmente con tipos de datos primitivos y solo cuando conocemos el nombre de la
 * clase. El nombre de clase para el que se va a crear el objeto Class se determina en tiempo de compilacion. A
 * continuacion se muestra la sintaxis:
 *
 * <pre> {@code Class c = int.class;} </pre>
 *
 * <p> Tenga en cuenta que este metodo se usa con el nombre de la clase, no con las instancias de la clase. Por ejemplo:
 *
 * <pre> {@code
 * Test test = new Test();
 * Class c = Test.class; // No error
 * Class c = test.class; // Error
 * } </pre>
 *
 * <li><b>test.getClass():</b> este metodo esta presente en la clase Object. Devuelve la clase en tiempo de ejecucion de
 * este objeto (test). A continuacion se muestra la sintaxis:
 *
 * <pre> {@code
 * Test test = new Test();
 * Class c = test.getClass();
 * } </pre>
 * </ol>
 *
 * <p> Una vez creado el objeto Class que representa la clase del objeto dado, este ya puede acceder a una serie de
 * caracteristicas de ese objeto. El siguiente ejemplo usa un objeto Class para imprimir el nombre de clase de un objeto:
 *
 * <pre> {@code
 * void printClassName(Object obj) {
 * 	System.out.println("The class of " + obj + " is " + obj.getClass().getName());
 * }
 * } </pre>
 *
 * <br> Fuentes:
 * <a href="https://www.geeksforgeeks.org/java-lang-class-class-java-set-1/">¿Que es el objeto Class?</a>
 *
 * @author Ruso
 */

public class Class_ {

	private String valor1 = "valor default", valor2;
	private final double decimal = 12.5;

	public Class_() {

	}

	public Class_(String valor1) {
		this.valor1 = valor1;
	}

	/**
	 * Devuelve el objeto Class asociado con la clase o interfaz con el nombre de cadena dado.
	 *
	 * @param className el nombre completo de la clase especificada.
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

		StringBuilder sb = new StringBuilder();

		Test test = new Test();
		Class<?> c1 = forName("_LABORATORIO.Test");
		Class<?> c2 = forName("java.lang.Thread");

		if (true) {
			if (c1 != null) {
				sb.append("Nombre de clase asociado con c1: " + c1.getName() +
						"\nNombre simple de clase asociado con c1: " + c1.getSimpleName() +
						"\nClassLoader de c1: " + c1.getClassLoader().getName() +
						"\nPaquete del objeto que representa c1: " + c1.getPackage() +
						"\nSuperclase del objeto que representa c1: " + c1.getSuperclass() +
						"\n¿c1 representa una interfaz? " + (c1.isInterface() ? "Si" : "No") +
						"\n¿c1 representa un tipo primitivo? " + (c1.isPrimitive() ? "Si" : "No") +
						"\n¿c1 representa un array? " + (c1.isArray() ? "Si" : "No") +
						"\n¿c1 representa una clase anonima? " + (c1.isAnonymousClass() ? "Si" : "No") +
						"\n¿c1 representa una clase local? " + (c1.isLocalClass() ? "Si" : "No") +
						"\n¿c1 representa una enumeracion? " + (c1.isEnum() ? "Si" : "No") +
						"\n¿El objeto especificado es compatible con el objeto representado por c1? " + (c1.isInstance(test) ? "Si" : "No") +
						"\n¿La clase Thread se asigna en el objeto representado por c1? " + (c1.isAssignableFrom(c2) ? "Si" : "No"));

				Field[] fields = c1.getFields();
				Method[] methods = c1.getMethods();

				sb.append("\nAtributos publicos del objeto representado por c1: ");
				for (Field field : fields) sb.append("\n" + field);

				sb.append("\nMetodos publicos del objeto representado por c1: ");
				for (Method method : methods) sb.append("\n" + method);

			}

			System.out.println(sb.toString());
		}

		/* Crea una nueva instancia de la clase representada por este objeto Class
		 Test test = (Test) c.newInstance();
		 test.someMethod(); */

		/* Devuelve un objeto Constructor que refleja el constructor publico especificado de la clase representada
		 * por este objeto Class. El parametro ParameterTypes es una matriz de objetos Class que identifican los
		 * tipos de parametros formales del constructor, en el orden declarado (es un varargs de tipo Class). */
		// Constructor<?> constructor = obj.getConstructor(Test.class);

		/* Utiliza el constructor representado por este objeto Constructor para crear e inicializar una nueva
		 * instancia de la clase de declaracion del constructor, con los parametros de inicializacion especificados. */
		// test = (Test) constructor.newInstance(test);

	}

}
