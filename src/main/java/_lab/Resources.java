package _lab;

import java.io.InputStream;

/**
 * Un recurso puede ser algo tan simple como un archivo o un directorio. El uso mas comun de los recursos es el
 * agrupamiento de imagenes, sonidos y datos de solo lectura.
 *
 * <p> En Java, podemos usar los metodos {@code getResource} o {@code getResourceAsStream} para leer archivos desde el
 * classpath. La diferencia entre {@code getResource} y {@code getResourceAsStream} es que el primero devuelve una URL,
 * mientras que el segundo abre esa URL y devuelve un {@code InputStream}.
 *
 * <p> <i>Nota: El metodo {@code getResource} no funciona en el archivo JAR y el metodo {@code getResourceAsStream}
 * funciona en todas partes.</i>
 *
 * <p> El <b>classpath</b> es la ruta al directorio que usa el {@code ClassLoader} para cargar los recursos requeridos
 * por el programa. Se pueden verificar los valores del classpath dentro de la aplicacion usando la propiedad del
 * sistema {@code System.getProperty("java.class.path")}.
 *
 * <br><br>
 *
 * <h2> Tipos de rutas </h2>
 * Una <i>ruta relativa</i> es la ruta a un archivo en la que se asume que el directorio de trabajo donde se ejecuta
 * la aplicacion, es mas conocida como el directorio raiz.
 *
 * <p> Una <i>ruta absoluta</i> es la ruta de acceso completa a un archivo, incluido el nombre de la particion o disco
 * en que se encuentra.
 *
 * <p> Una ruta relativa comienza con un nombre y una ruta absoluta comienza con un separador (/). Cuando se resuelve
 * una ruta absoluta, los cargadores de clases interpretan el separador como la raiz del espacio de nombres. Por el
 * contrario, una ruta relativa <i>puede</i> resolverse en relacion con cualquier "carpeta" en el espacio de nombres.
 * La carpeta utilizada dependera del objeto que utilice para resolver la ruta.
 *
 * <p> La principal diferencia entre las versiones {@code Class} y {@code ClassLoader} de los metodos
 * {@code getResource} y {@code getResourceAsStream}, se encuentra en la forma en que se interpretan las rutas
 * relativas.
 * <ul>
 * <li> Los metodos de {@code Class} resuelven una ruta relativa en la "carpeta" que corresponde al paquete de clases.
 * <li> Los metodos de {@code ClassLoader} tratan las rutas relativas como si fueran absolutas; es decir, resolverlos en
 * la "carpeta raiz" del espacio de nombres de classpath.
 * </ul>
 *
 * <br> Fuentes:
 * <a href="https://mkyong.com/java/java-read-a-file-from-resources-folder/#:~:text=In%20Java%2C%20we%20can%20use,content%20InputStream%20is%20%3D%20getClass()">Leer un archivo de la carpeta de resources</a>
 * <a href="https://stackoverflow.com/questions/2396493/what-is-a-classpath-and-how-do-i-set-it">¿Que es un classpath y como lo configuro?</a>
 * <a href="https://stackoverflow.com/questions/6608795/what-is-the-difference-between-class-getresource-and-classloader-getresource">¿Cual es la diferencia entre Class.getResource() y ClassLoader.getResource()?</a>
 * <a href="https://stackoverflow.com/questions/941754/how-to-get-a-path-to-a-resource-in-a-java-jar-file">¿Como obtener una ruta a un recurso en un archivo JAR?</a>
 * <a href="https://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java">¿Como leer realmente el archivo de texto de classpath?</a>
 */

public class Resources {

	public static void main(String[] args) {

		Class<?> clase = Resources.class;
		InputStream inputFromClass, inputFromClassLoader;

		/* Construye un nombre de recurso absoluto a partir del nombre de recurso dado usando este algoritmo:
		 * - Si el nombre comienza con '/' ('\u002f'), el nombre absoluto del recurso es la parte del nombre que sigue a
		 * '/'.
		 * - De lo contrario, el nombre absoluto es de la siguiente forma: modified_package_name/name
		 * Donde el nombre_paquete_modificado es el nombre del paquete de este objeto con '/' sustituido por '.'
		 * ('\u002e'). */
		inputFromClass = clase.getResourceAsStream("/textures/bola.png"); // Ruta absoluta

		System.out.println(inputFromClass != null);

		inputFromClassLoader = clase.getClassLoader().getResourceAsStream("textures/bola.png"); // Ruta relativa

		System.out.println(inputFromClassLoader != null);

	}


}
