package _lab;

import java.io.InputStream;

/**
 * Un recurso puede ser algo tan simple como un archivo o un directorio, o puede ser una referencia a un objeto mas
 * complicado, como una consulta a una base de datos o un motor de busqueda.
 *
 * <p> Una <i>ruta relativa</i> es la ruta a un archivo en la que se asume que el directorio de trabajo donde se ejecuta
 * la aplicacion, es mas conocida como el directorio raiz.
 *
 * <p> Una <i>ruta absoluta</i> es la ruta de acceso completa a un archivo, incluido el nombre de la particion o disco
 * en que se encuentra.
 *
 * <p> El <b>classpath</b> es la ruta al directorio que usa el {@code ClassLoader} para cargar los recursos requeridos
 * por el programa. Se pueden verificar los valores del classpath dentro de la aplicacion usando la propiedad del
 * sistema {@code System.getProperty("java.class.path")}.
 *
 * <p> En Java podemos usar el metodo {@code getResource} o {@code getResourceAsStream} para obtener un recurso del
 * classpath. La diferencia entre {@code getResource} y {@code getResourceAsStream} es que el primero devuelve una URL,
 * mientras que el segundo abre esa URL y devuelve un {@code InputStream}.
 *
 * <p> La llamada a {@code getResource("/...")} siempre regresa {@code null} ya que el cargador de clases no elimina la
 * barra principal de la ruta, por lo que la busqueda siempre falla. Solo {@code getResource()} maneja un inicio con
 * barra, como una ruta absoluta relativa a la ruta de clases.
 *
 * <br><br> Fuentes:
 * <a href="https://javarevisited.blogspot.com/2011/01/how-classpath-work-in-java.html">¿Como funciona el classpath?</a>
 * <a href="https://learntutorials.net/es/java/topic/2433/recursos--en-classpath-#:~:text=Un%20recurso%20son%20datos%20de,recursos%20con%20los%20m%C3%A9todos%20ClassLoader">...</a>.
 * <a href="https://stackoverflow.com/questions/2396493/what-is-a-classpath-and-how-do-i-set-it">...</a>
 * <a href="https://mkyong.com/java/java-read-a-file-from-resources-folder/#:~:text=In%20Java%2C%20we%20can%20use,content%20InputStream%20is%20%3D%20getClass()">...</a>.
 * <a href="https://stackoverflow.com/questions/6608795/what-is-the-difference-between-class-getresource-and-classloader-getresource">...</a>
 * <a href="https://stackoverflow.com/questions/941754/how-to-get-a-path-to-a-resource-in-a-java-jar-file">...</a>
 * <a href="https://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java">...</a>
 */

public class Resources {

	public static void main(String[] args) {

		Class<?> clase = Resources.class;
		InputStream inputClass, inputClassLoader;

		inputClass = clase.getResourceAsStream("/textures/bola.png");

		System.out.println(inputClass != null);

		inputClassLoader = clase.getClassLoader().getResourceAsStream("textures/bola.png");

		System.out.println(inputClassLoader != null);

	}


}
