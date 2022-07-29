package _LABORATORIO;

import java.io.InputStream;

/**
 * El ClassLoader es una clase que se utiliza para cargar archivos <code>.class</code>. El codigo java se compila en ese
 * archivo mediante el compilador javac y la JVM ejecuta ese bytecode. ClassLoader es responsable de cargar archivos
 * .class desde sistemas de archivos, redes o cualquier otra fuente.
 * <p>
 * El classpath es la ruta al directorio que usa el ClassLoader para buscar y cargar clases, recursos y jar requeridos
 * por el programa. Se pueden verificar los valores del classpath dentro de la aplicacion usando la siguiente
 * propiedad del sistema <code>System.getProperty("java.class.path")</code>.
 * <p>
 * Un recurso puede ser algo tan simple como un archivo o un directorio, o puede ser una referencia a un objeto mas
 * complicado, como una consulta a una base de datos o un motor de busqueda.
 * En Java, podemos usar getResourceAsStream() o getResource() para leer un archivo o varios archivos de una carpeta de
 * recursos o raiz de classpath.
 * <p>
 * La diferencia entre el getResource() y getResourceAsStream() es que el primero devuelve una URL, mientras que el
 * segundo abre esa URL y devuelve un InputStream.
 * getClassLoader().getResource("/...") siempre regresa null: ya que el cargador de clases no elimina la barra /
 * principal de la ruta, por lo que la busqueda siempre falla. Solo getClass().getResource() maneja un inicio / como una
 * ruta absoluta relativa a la ruta de clases.
 * <p>
 * getClassLoader().getResource() toma rutas que no comienzan con /, y siempre comienzan en la raiz del classpath.
 * <p>
 * getResource() toma un camino que puede comenzar con /. Si es asi, comienza en la raiz del classpath. De lo contrario,
 * comienza en el paquete de la clase en la que se llama al metodo.
 * <p>
 * Entonces getClassLoader().getResource("foo/bar.txt") es quivalente a getResource("/foo/bar.txt").
 * <p>
 * Y, asumiendo que getClass() devuelve una clase que esta en el paquete foo, getClass().getResource("bar.txt") es
 * equivalente a getClass().getClassLoader().getResource("foo/bar.txt").
 * <br><br>
 * Fuentes:
 * <a href="https://javarevisited.blogspot.com/2011/01/how-classpath-work-in-java.html">¿Como funciona el classpath?</a>
 * <a href="https://stackoverflow.com/questions/14739550/difference-between-getclass-getclassloader-getresource-and-getclass-getres">...</a>
 * <a href="https://stackoverrun.com/es/q/3990978">...</a>
 *
 *
 * https://javarevisited.blogspot.com/2012/12/how-classloader-works-in-java.html
 * https://learntutorials.net/es/java/topic/2433/recursos--en-classpath-#:~:text=Un%20recurso%20son%20datos%20de,recursos%20con%20los%20m%C3%A9todos%20ClassLoader.
 * https://stackoverflow.com/questions/2396493/what-is-a-classpath-and-how-do-i-set-it
 * https://mkyong.com/java/java-read-a-file-from-resources-folder/#:~:text=In%20Java%2C%20we%20can%20use,content%20InputStream%20is%20%3D%20getClass().
 * https://stackoverflow.com/questions/6608795/what-is-the-difference-between-class-getresource-and-classloader-getresource
 * https://stackoverflow.com/questions/941754/how-to-get-a-path-to-a-resource-in-a-java-jar-file
 * https://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
 *
 *
 */

public class Resources {

	public static void main(String[] args) {

		InputStream stream;

		/* Desde class, la ruta es relativa (depende) al paquete de la clase a menos que incluya una barra inclinada, por lo que
		 * si no quiere usar el paquete actual, incluya una barra. */
		stream = Resources.class.getResourceAsStream("/bola.png");
		System.out.println(stream != null);

		/* Desde classLoader(), todas las rutas ya son "absolutas", no hay contexto de los cuales podrian ser relativas. Por lo
		 * tanto, no necesita una barra inclinada. */
		stream = Resources.class.getClassLoader().getResourceAsStream("bola.png");
		System.out.println(stream != null);

	}

	// Estos dos metodos ubican el recurso de manera diferente
	/* La ruta se interpreta como una ruta local al paquete de la clase desde la que lo esta llamando. Si su ruta comienza
	 * con /, entonces se considerara una ruta absoluta y comenzara a buscar desde la raiz de la ruta de clases. */
	// InputStream clase1 = Recursos.class.getResourceAsStream("");
	/* Considerara todos los caminos como caminos absolutos. */
	// InputStream clase2 = classLoader.getResourceAsStream("");
	/* Cada vez que menciono una ubicacion en esta publicacion, podria ser una ubicacion en su propio sistema de archivos, o
	 * dentro del archivo jar correspondiente, dependiendo de la clase y / o ClassLoader desde donde esta cargando el
	 * recurso. */

}
