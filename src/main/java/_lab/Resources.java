package _lab;

import java.io.InputStream;

import util.Testing;

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
 * <br><br>
 *
 * <h2> Classpath </h2>
 * El classpath es la ruta al directorio que usa el {@code ClassLoader} para cargar los recursos requeridos por el
 * programa. Se pueden verificar los valores del classpath dentro de la aplicacion usando la propiedad del sistema
 * {@code System.getProperty("java.class.path")}.
 *
 * <p> Para que otras clases esten disponibles en la clase actual, se coloca algo como esto en la parte superior del
 * archivo fuente:
 *
 * <pre> {@code import util.Testing;} </pre>
 *
 * <p> Asi que mas adelante en tu programa cuando dices:
 *
 * <pre> {@code Testing test = new Testing();} </pre>
 *
 * <p> La JVM sabra donde encontrar la clase compilada.
 *
 * <p> Seria poco practico hacer que la VM busque en todas las carpetas de su maquina, por lo que debe proporcionarle a
 * la VM una lista de lugares para buscar. Esto se hace colocando carpetas y archivos jar en su classpath.
 *
 * <p> Antes de hablar sobre como se configura el classpath, hablemos sobre los archivos .class, los paquetes y los
 * archivos .jar.
 *
 * <p> Primero, supongamos que Testing es algo que creaste como parte de tu proyecto, y esta en un directorio en tu
 * proyecto llamado {@code target/classes}. El archivo .class estaria en <i>target/classes/util/Testing.class</i> (junto
 * con todos los demas archivos de ese paquete). Para llegar a ese archivo, su ruta simplemente necesitaria contener la
 * carpeta 'target', no toda la estructura del paquete, ya que su declaracion de importacion proporciona  toda esa
 * informacion a la VM.
 *
 * <p> Ahora supongamos que empaqueta utilidades en un archivo .jar y lo coloca en el directorio lib de su proyecto.
 * Ahora necesitaria poner lib/utilidades.jar en su classpath. La maquina virtual buscara dentro del archivo jar la
 * parte util y encontrara su clase.
 *
 * <p> <b>Entonces, los classpaths contienen:</b>
 * <ul>
 * <li> Archivos JAR, y
 * <li> Rutas a la parte superior de las jerarquias de paquetes.
 * </ul>
 *
 * <p> Piense en ello como la respuesta de Java a la variable de entorno PATH: los sistemas operativos buscan EXE en PATH,
 * Java busca clases y paquetes en classpath.
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
 * <p> En terminos de Java, una ruta relativa comienza con un nombre y una ruta absoluta comienza con un separador (/).
 * Cuando se resuelve una ruta absoluta, los cargadores de clases interpretan el separador como la raiz del espacio de
 * nombres. Por el contrario, una ruta relativa <i>puede</i> resolverse en relacion con cualquier "carpeta" en el
 * espacio de nombres. La carpeta utilizada dependera del objeto que utilice para resolver la ruta.
 *
 * <p> La principal diferencia entre las versiones {@code Class} y {@code ClassLoader} de los metodos
 * {@code getResource} y {@code getResourceAsStream}, se encuentra en la forma en que se interpretan las rutas
 * relativas.
 * <ul>
 * <li> Los metodos de {@code Class} resuelven una ruta relativa en la "carpeta" que corresponde al paquete de clases.
 * <li> Los metodos de {@code ClassLoader} tratan las rutas relativas como si fueran absolutas; es decir, resolverlos en
 * la "carpeta raiz" del espacio de nombres de classpath.
 * </ul>
 * <p>
 * Class.getResource puede tomar un nombre de recurso "relativo", que se trata en relacion con el paquete de la clase.
 * Como alternativa, puede especificar un nombre de recurso "absoluto" utilizando una barra inclinada inicial. Las rutas
 * de recursos del cargador de clases siempre se consideran absolutas.
 *
 * <p> Asi que los siguientes son basicamente equivalentes:
 *
 * <pre> {@code
 * foo.bar.Baz.class.getResource("xyz.txt");
 * foo.bar.Baz.class.getClassLoader().getResource("foo/bar/xyz.txt");
 * } </pre>
 *
 * <p> Y tambien lo son estos (pero son diferentes de los anteriores):
 *
 * <pre> {@code
 * foo.bar.Baz.class.getResource("/data/xyz.txt");
 * foo.bar.Baz.class.getClassLoader().getResource("data/xyz.txt");
 * } </pre>
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
		 * ('\u002e').
		 *
		 * Desde Class, la ruta es relativa al paquete de la clase a menos que incluye una barra inclinada inicial, por
		 * lo que si no desea utilizar el paquete actual, incluya una barra inclinada como esta: */
		inputFromClass = clase.getResourceAsStream("/textures/bola.png"); // Ruta absoluta

		System.out.println(inputFromClass != null);

		/* Desde ClassLoader, todas las rutas ya son "absolutas", no hay contexto de los que podrian ser relativos. Por
		 * lo tanto, no necesita una barra inclinada inicial. */
		inputFromClassLoader = clase.getClassLoader().getResourceAsStream("textures/bola.png"); // Ruta relativa

		System.out.println(inputFromClassLoader != null);

	}


}
