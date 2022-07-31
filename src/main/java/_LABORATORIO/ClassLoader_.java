package _LABORATORIO;

/**
 * El ClassLoader es una parte del JRE que carga dinamicamente las clases en la JVM.
 * <p>
 * Las clases no se cargan en la memoria de una sola vez, sino cuando lo requiere una aplicacion. En este punto, el JRE
 * llama al ClassLoader y estos ClassLoaders cargan las clases en la memoria dinamicamente.
 * <p>
 * No todas las clases se cargan con un solo ClassLoader. Dependiendo del tipo de clase y la ruta de la clase, se decide
 * el ClassLoader que carga esa clase en particular. Para conocer el ClassLoader que carga una clase se utiliza el
 * metodo getClassLoader(). Todas las clases se cargan en funcion de sus nombres y, si alguna de estas clases no se
 * encuentra, devuelve NoClassDefFoundError o ClassNotFoundException.
 * <p>
 * Un Java Classloader es de tres tipos:
 * <ol>
 * <li><b>BootStrap ClassLoader:</b> un cargador de clases Bootstrap es un codigo de maquina que inicia la operacion cuando la JVM
 * lo llama. No es una clase java. Su trabajo es cargar el primer ClassLoader de Java puro. Bootstrap ClassLoader carga
 * clases desde la ubicacion rt.jar. Bootstrap ClassLoader no tiene ClassLoaders principales. Tambien se llama Primodial
 * ClassLoader.</li>
 * <li><b>Extension ClassLoader:</b> la extension ClassLoader es un elemento secundario de Bootstrap ClassLoader y carga las
 * extensiones de las clases principales de Java desde la biblioteca de extension JDK respectiva. Carga archivos desde
 * el directorio jre/lib/ext o cualquier otro directorio señalado por la propiedad del sistema java.ext.dirs.</li>
 * <li><b>System ClassLoader:</b> un ClassLoader de aplicacion tambien se conoce como System ClassLoader. Carga las clases de tipo
 * de aplicacion que se encuentran en la variable de entorno CLASSPATH, -classpath o la opcion de linea de comando -cp.
 * Application ClassLoader es una clase secundaria de Extension ClassLoader.</li>
 * </ol>
 * <u>Nota</u>: el modelo de jerarquia de delegacion de ClassLoader siempre funciona en el orden
 * <i>Application ClassLoader->Extension ClassLoader->Bootstrap ClassLoader</i>. El Bootstrap ClassLoader siempre tiene la
 * prioridad mas alta, el siguiente es Extension ClassLoader y luego Application ClassLoader.
 * <p>
 * La clase ClassLoader usa un modelo de delegacion para buscar clases y recursos. Cada instancia de ClassLoader tiene
 * un cargador de clases principal asociado. Cuando se le solicite encontrar una clase o un recurso, una instancia de
 * ClassLoader delegara la busqueda de la clase o el recurso a su cargador de clases principal antes de intentar
 * encontrar la clase o el recurso en si. El cargador de clases integrado de la maquina virtual, llamado "BootStrap
 * ClassLoader", no tiene un padre en si mismo, pero puede servir como padre de una instancia de ClassLoader.
 * <p>
 * Normalmente, la maquina virtual Java carga clases desde el sistema de archivos local en funcion de la plataforma. Por
 * ejemplo, en sistemas UNIX, la maquina virtual carga clases desde el directorio definido por la variable de entorno
 * CLASSPATH.
 * <br><br>
 * Fuentes:
 * <a href="https://www.geeksforgeeks.org/classloader-in-java/">ClassLoader en Java</a>
 */
public class ClassLoader_ {

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

	public static void main(String[] args) {
		// Crea un objeto Class asociado con esta clase
		Class<?> obj = forName("_LABORATORIO.ClassLoader_");
		if (obj != null) {
			// Obtiene el cargador de clases que cargo la clase representada por este objeto
			// ClassLoader loader = obj.getClassLoader();
			System.out.println("El nombre de esta clase es: " + obj.getSimpleName());
			System.out.println("El ClassLoader de esta clase es: " + obj.getClassLoader().getName());
		}
	}

}
