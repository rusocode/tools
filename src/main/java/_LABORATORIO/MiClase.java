package _LABORATORIO;

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
	 * con parametros.
	 */
	public static MiClase getInstance(String value) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		return MiClase.class.getConstructor(String.class).newInstance(value);
	}

	public void printClassName() {
		System.out.println("El nombre de esta clase es: " + this.getClass().getSimpleName());
	}

	public void printCanonicalName() {
		System.out.println(this.getClass().getCanonicalName());
	}

	// Muestra los atributos existentes en la clase (nombre y valor)
	public void imprimirAtributos() {
		System.out.println("Atributos>");
		Field[] fields = this.getClass().getDeclaredFields();
		if (fields.length != 0) {
			for (Field field : fields) {
				try {
					String fieldName = field.getName();
					Object fieldValue = field.get(this);
					System.out.println(fieldName + ": " + fieldValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} else System.out.println("No existen atributos.");

	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		MiClase clase1 = new MiClase();
		clase1.printClassName();

		MiClase clase2 = MiClase.getInstance();
		clase2.printCanonicalName();

		/* MiClase clase1 = new MiClase("valor 1");
		System.out.println(clase1.valor1);

		String nombre = "";
		// Devuelve el nombre completo (ruta) de la variable clase1
		System.out.println("Clase de la variable clase1: " + clase1.getClass());
		System.out.println("Clase de la variable nombre: " + nombre.getClass());

		MiClase clase2 = MiClase.getConstructor();
		System.out.println(clase2.valor1);

		// Creando un objeto desde un constructor dinamico
		MiClase clase3 = MiClase.getConstructor("valor 1 - constructor dinamico");
		System.out.println(clase3.valor1);

		// Otra forma de crear objetos de la propia clase
		try {
			MiClase clase4 = MiClase.class.newInstance();
			System.out.println(clase4.getClassName());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}  */

		// System.out.println(clase.getNombreClase());
		// clase.imprimirAtributos();

		/* System.out.println("Ruta de clase en los paquetes de la API de java: " + sb.getClass().getName());
		 * System.out.println("Nombre de clase: " + sb.getClass().getSimpleName());
		 * System.out.println("sb es instancia de String?: " + sb.getClass().isInstance(args)); */

	}

}
