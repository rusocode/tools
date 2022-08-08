package _lab;

import java.lang.reflect.*;

/**
 * Java proporciona una clase con el nombre Class en el paquete java.lang. Las instancias de la clase Class representan
 * clases e interfaces en una aplicacion Java en ejecucion. Los tipos primitivos (boolean, byte, char, short, int, long,
 * float y double) y la palabra clave void tambien se representan como objetos Class. No tiene constructor publico. Los
 * objetos Class son construidos automaticamente por la JVM a medida que se cargan las clases y mediante llamadas al
 * metodo defineClass en el ClassLoader. Es una clase final, por lo que no se puede extender.
 *
 * <p> Hay 3 formas de crear un objeto Class:
 * <ol>
 * <li><b>Class.forName(“className”):</b> como la clase Class no tiene ningun constructor, hay un metodo de fabrica
 * estatico presente, que es Class.forName(), y se usa para crear objetos de la clase Class asociados con el nombre de
 * clase dado. A continuacion se muestra la sintaxis:
 *
 * <pre> {@code Class clase = Class.forName(String className);} </pre>
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
 * <pre> {@code Class clase = int.class;} </pre>
 *
 * <p> Tenga en cuenta que este metodo se usa con el nombre de clase, no con la instancia. Por ejemplo:
 *
 * <pre> {@code
 * Test test = new Test();
 * Class clase = Test.class; // No error
 * Class clase = test.class; // Error
 * } </pre>
 *
 * <li><b>test.getClass():</b> este metodo esta presente en la clase Object. Devuelve la clase en tiempo de ejecucion de
 * este objeto (test). A continuacion se muestra la sintaxis:
 *
 * <pre> {@code
 * Test test = new Test();
 * Class clase = test.getClass();
 * } </pre>
 * </ol>
 *
 * <p> Una vez creado el objeto Class que representa la clase dada, este ya puede acceder a las caracteristicas de esa
 * clase. El siguiente ejemplo usa un objeto Class para imprimir el nombre de clase que representa:
 *
 * <pre> {@code
 * void printClassName(Test test) {
 * 	System.out.println("El nombre de la clase es " + test.getClass().getName());
 * }
 * } </pre>
 *
 * <br> Fuentes:
 * <a href="https://www.geeksforgeeks.org/java-lang-class-class-java-set-1/">¿Que es el objeto Class?</a>
 *
 * @author Ruso
 */

public class Class_ {

	private static final StringBuilder builder = new StringBuilder();

	private final String valor = "valor";
	private final double decimal = 12.5;

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
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Muestra todos los campos declarados por la clase o interfaz representada por este objeto Class. Esto incluye
	 * campos publicos, protegidos, default y privados, pero excluye los campos heredados. Ademas de mostrar todos los
	 * campos, tambien muestra sus valores excepto el de los privados.
	 * <br><br>
	 * Nota: si el campo privado pertenece a la misma clase, entonces si puede acceder a su valor.
	 *
	 * @param obj clase representada por el objeto Class.
	 */
	private static void printDeclaredFields(Class<?> obj) {
		try {
			Object o = obj.getDeclaredConstructor().newInstance();
			Field[] fields = obj.getDeclaredFields();
			if (fields.length != 0) {
				append("Atributos>");
				for (Field field : fields) {
					/* Si el campo no es privado o no es privado y final.
					 * TODO Reemplazar esta forma de comprobar el modificador de campo. */
					if (!(field.getModifiers() == Modifier.PRIVATE || field.getModifiers() == (Modifier.PRIVATE + Modifier.FINAL)))
						append(field.getName() + " = " + field.get(o));
					else append(field.getName() + " = No se puede acceder a este valor ya que es un campo privado!");
				}
			} else append("La clase no tiene atributos declarados!");
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
				 InstantiationException e) {
			append(e.getMessage());
		}
	}

	private static void append(String line) {
		builder.append(line);
		builder.append(System.lineSeparator());
	}

	public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

		// Crea un objeto Class que representa la clase Test
		Class<?> clase = forName("_LABORATORIO.Test");

		if (clase != null) {
			append("Nombre de clase: " + clase.getName());
			append("Nombre simple de clase: " + clase.getSimpleName());
			append("ClassLoader: " + clase.getClassLoader().getName());
			append("Paquete: " + clase.getPackage().getName());
			append("Superclase: " + clase.getSuperclass().getSimpleName());
			append("Constructor: " + clase.getConstructor());
			append("¿Representa una interfaz? " + (clase.isInterface() ? "Si" : "No"));
			append("¿Representa un tipo primitivo? " + (clase.isPrimitive() ? "Si" : "No"));
			append("¿Representa un array? " + (clase.isArray() ? "Si" : "No"));
			append("¿Representa una clase anonima? " + (clase.isAnonymousClass() ? "Si" : "No"));
			append("¿Representa una clase local? " + (clase.isLocalClass() ? "Si" : "No"));
			append("¿Representa una enumeracion? " + (clase.isEnum() ? "Si" : "No"));
			append("¿El objeto especificado es compatible con la clase? " + (clase.isInstance(new Test()) ? "Si" : "No"));

			printDeclaredFields(clase);

			// Crea un objeto Test usando el metodo newInstance()
			/* Test test = (Test) clase.getDeclaredConstructor().newInstance();
			 * test.publicMethod(); */
		}

		System.out.print(builder);

	}

}
