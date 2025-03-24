package com.punkipunk._lab;

import java.io.File;
import java.util.stream.Stream;

/**
 * <h1>Recursos y Classpath en Java</h1>
 * <h2>¿Que son los recursos?</h2>
 * <p>
 * Un recurso es cualquier archivo no ejecutable que forma parte de una aplicacion y que se utiliza durante su ejecucion. Los
 * recursos son archivos que el programa necesita para funcionar correctamente, pero que no son codigo en si. Entre los tipos
 * comunes de recursos encontramos archivos estaticos como textos, configuraciones, imagenes, audios, hojas de estilo y scripts.
 * Los recursos se caracterizan por ser generalmente de solo lectura durante la ejecucion del programa, se empaquetan junto con
 * las clases Java dentro del JAR o WAR, se accede a ellos a traves del <i>classpath</i> usando el {@code ClassLoader}, permiten
 * separar datos y configuracion del codigo fuente, y facilitan la internacionalizacion y localizacion de aplicaciones. Las
 * principales ventajas de usar recursos incluyen la modularidad al separar claramente codigo y datos, la mantenibilidad al
 * permitir modificar contenido sin cambiar codigo, la portabilidad ya que los recursos viajan junto con el codigo en los archivos
 * JAR, y la flexibilidad porque los recursos pueden ser reemplazados o modificados sin recompilar.
 * <h2>Classpath</h2>
 * <p>
 * El classpath en Java es la ruta o conjunto de rutas que la JVM utiliza para buscar clases y recursos durante la ejecucion de un
 * programa. Funciona de manera similar a la variable PATH del sistema operativo: asi como el sistema busca ejecutables en las
 * rutas de PATH, la JVM busca clases y recursos en las rutas del classpath. El classpath puede contener directorios donde se
 * encuentran archivos .class organizados en estructura de paquetes, archivos JAR que contienen clases compiladas y recursos, y
 * otros archivos necesarios para la aplicacion. Se puede especificar el classpath de varias formas: mediante la opcion
 * {@code -cp} o {@code -classpath} al ejecutar aplicaciones Java desde la linea de comandos, configurando la variable de entorno
 * {@code CLASSPATH}, o en tiempo de ejecucion utilizando ClassLoaders personalizados. Dentro de una aplicacion, se puede
 * verificar el classpath actual mediante {@code System.getProperty("java.class.path")}. Cuando Java necesita cargar una clase,
 * por ejemplo cuando encontramos una instruccion como {@code import com.punkipunk.utils.Global;}, el {@code ClassLoader} buscara
 * en todas las rutas del classpath hasta encontrar el archivo {@code com/punkipunk/utils/Global.class}. Si la clase no se
 * encuentra en ninguna de las rutas del classpath, se producira un {@code ClassNotFoundException}. El classpath es fundamental
 * para la modularidad de aplicaciones Java, ya que permite organizar el codigo en paquetes y bibliotecas que pueden reutilizarse
 * en diferentes proyectos, facilitando asi la gestion de dependencias y la estructura del programa.
 * <h2>Acceso a Recursos</h2>
 * <p>
 * En Java se puede acceder a los recursos principalmente mediante dos metodos: {@code getResource()} y
 * {@code getResourceAsStream()}. El metodo {@code getResource()} devuelve una URL que apunta al recurso, mientras que
 * {@code getResourceAsStream()} abre directamente un flujo de entrada ({@code InputStream}) para leer el contenido del recurso.
 * Existen dos formas principales de invocar estos metodos: a traves de una instancia de {@code Class} o mediante el
 * {@code ClassLoader}. Cuando se usa {@code Class}, por ejemplo {@code ResourceLoader.class.getResource("config.properties")},
 * las rutas relativas se resuelven en relacion al paquete donde se encuentra la clase; si usamos una ruta sin barra inicial como
 * {@code "config.properties"}, se buscara en el mismo paquete de la clase dentro del classpath. Si se usa una ruta con barra
 * inicial como {@code "/config.properties"}, se buscara desde la raiz del classpath. Por otro lado, cuando se usa el
 * {@code ClassLoader}, como {@code ResourceLoader.class.getClassLoader().getResource("config.properties")}, todas las rutas se
 * consideran absolutas desde la raiz del classpath, sin importar si llevan o no barra inicial. Una vez obtenido el recurso, se
 * puede leer su contenido convirtiendo la URL a un {@code InputStream} o usando directamente {@code getResourceAsStream()}. Para
 * facilitar el acceso, es comun crear metodos de utilidad que manejen las excepciones y cierren correctamente los streams. El
 * acceso a recursos funciona tanto en aplicaciones normales como dentro de archivos JAR, lo que hace que sea el metodo preferido
 * para incluir configuraciones, plantillas o archivos de datos con la aplicacion.
 * <table border="1" cellpadding="5">
 * <tr>
 * <th>Metodo</th>
 * <th>Retorno</th>
 * <th>Caracteristicas</th>
 * </tr>
 * <tr>
 * <td>{@code getResource()}</td>
 * <td>URL</td>
 * <td>Devuelve una URL al recurso especificado</td>
 * </tr>
 * <tr>
 * <td>{@code getResourceAsStream()}</td>
 * <td>InputStream</td>
 * <td>Abre la URL del recurso y devuelve directamente un stream para lectura</td>
 * </tr>
 * </table>
 * <p>
 * <i>Nota: {@code getResourceAsStream()} generalmente se considera mas versatil, especialmente cuando se trabaja con recursos
 * dentro de archivos JAR, ya que maneja transparentemente la apertura del stream sin depender de la interpretacion de la URL.</i>
 * <h2>Rutas relativas y absolutas</h2>
 * <p>
 * En Java, las rutas relativas y absolutas son dos formas diferentes de especificar la ubicacion de recursos. Las rutas relativas
 * son aquellas que no comienzan con un separador de directorios (/) y se resuelven en relacion a algun contexto. Cuando se usan
 * con {@code Class.getResource()}, se interpretan en relacion al paquete donde se encuentra la clase; por ejemplo, si tenemos una
 * clase en el paquete {@code com.punkipunk._lab} y usamos la ruta relativa {@code config.properties}, Java buscara el archivo en
 * {@code src/main/resources/com/punkipunk/_lab/config.properties} si es que este directorio esta creado, ya que por lo general se
 * usa el {@code ClassLoader} para evitar crear esta estructura de paquetes dentro de los recursos. Por otro lado, las rutas
 * absolutas son aquellas que comienzan con un separador (/) y siempre se resuelven desde la raiz del classpath,
 * independientemente del paquete donde se encuentre la clase que realiza la llamada; por ejemplo, si usamos
 * {@code config.properties}, Java buscara el archivo exactamente en esa ubicacion desde la raiz del classpath. Un aspecto
 * importante a considerar es que cuando se usa {@code ClassLoader.getResource()} o {@code ClassLoader.getResourceAsStream()},
 * todas las rutas se tratan como absolutas (desde la raiz del classpath), incluso si no comienzan con separador. Esto significa
 * que {@code ClassLoader.getResource("config.properties")} y {@code Class.getResource("/config.properties")} son funcionalmente
 * equivalentes. Esta distincion es fundamental para entender como Java localiza los recursos, especialmente cuando se trabaja con
 * aplicaciones empaquetadas en archivos JAR donde la estructura de directorios fisica no es directamente accesible.
 * <h2>Conclusion</h2>
 * <p>
 * Cuando usas {@code ClassLoader.getResource()}, <b>todas</b> las rutas se interpretan como absolutas desde la raiz del
 * classpath, sin importar si tienen o no una barra inicial.
 * <p>
 * Para que la JVM pueda acceder a {@code config.properties} desde una ruta relativa, requiere replicar la estructura de paquetes
 * ({@code com.punkipunk._lab}) en los recursos. Esta es una de las razones por las que se prefiere el enfoque de
 * {@code ClassLoader}, ya que no es necesario replicar la estructura de paquetes en los recursos, simplemente pones el archivo en
 * la raiz de {@code src/main/resources/} y accedes con:
 * <pre>{@code
 * ResourceLoader.class.getClassLoader().getResource("config.properties")
 * }</pre>
 * <p>
 * o especificando una ruta absoluta desde una intancia de {@code Class}:
 * <pre>{@code
 * ResourceLoader.class.getResource("/config.properties")
 * }</pre>
 * <p>
 * Los recursos <b>no</b> deben colocarse junto al codigo fuente en {@code src/main/java/} porque los archivos en
 * {@code src/main/java/} se tratan como archivos a compilar, no como recursos, ya que estos van en {@code src/main/resources/}.
 * <p>
 * El directorio {@code target/classes} es parte de la estructura estandar que Maven crea durante el proceso de construccion de un
 * proyecto Java.
 * <p>
 * Cuando ejecutas el ciclo de vida de construccion de Maven (especialmente las fases "compile" o posteriores), Maven crea el
 * directorio {@code target} y dentro de el, el subdirectorio {@code classes}. En este directorio {@code target/classes} es donde
 * Maven coloca los archivos .class compilados a partir de los archivos fuente Java.
 * <p>
 * Ademas de los archivos .class compilados, el directorio {@code target/classes} tambien contiene archivos de recursos que son
 * cualquier recurso dentro del directorio {@code src/main/resources/} que sera copiado a {@code target/classes} durante la
 * compilacion.
 * <p>
 * En general, este directorio contiene todo lo necesario para la ejecucion de tu aplicacion, excluyendo las dependencias externas
 * (que normalmente se manejan en el classpath o se empaquetan en el archivo JAR final segun tu configuracion).
 * <p>
 * Por lo tanto, al intentar leer un archivo de {@code src/main/resources/}, lo hacemos desde la raiz del classpath del proyecto.
 * <p>
 * <b>Resumiendo:</b>
 * <ul>
 * <li>{@code ClassLoader.getResource(ruta)} → Siempre absoluta (desde la raiz)
 * <li>{@code Class.getResource("/ruta")} → Absoluta (desde la raiz)
 * <li>{@code Class.getResource("ruta")} → Relativa (al paquete de la clase)
 * </ul>
 *
 * @see <a
 * href="https://mkyong.com/java/java-read-a-file-from-resources-folder/#:~:text=In%20Java%2C%20we%20can%20use,content%20InputStream%20is%20%3D%20getClass()">Read
 * a file from resources folder</a>
 * @see <a href="https://stackoverflow.com/questions/2396493/what-is-a-classpath-and-how-do-i-set-it">What is a classpath and how
 * do I set it?</a>
 * @see <a
 * href="https://stackoverflow.com/questions/6608795/what-is-the-difference-between-class-getresource-and-classloader-getresource">Whatf
 * is the difference between Class.getResource() and ClassLoader.getResource()?</a>
 * @see <a href="https://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java">How to read text
 * file from classpath in Java?</a>
 */

public class ResourceLoader {

    public static void main(String[] args) {

        printClasspath();

        /* Es importante aclarar que como estoy en un entorno estatico, utilizo la palabra reservada class para obtener la
         * instancia de Class en vez de utilizar getClass(). */

        // System.out.println(ResourceLoader.class.getClassLoader().getResource("config.properties").getPath()); // Ruta absoluta (siempre desde la raiz del classpath)
        // System.out.println(ResourceLoader.class.getResource("/config.properties").getPath()); // Ruta absoluta
        // System.out.println(ResourceLoader.class.getClassLoader().getResource("textures/dragon.png").getPath()); // Ruta absoluta (siempre desde la raiz del classpath)

        // System.out.println(ResourceLoader.class.getResource("config.properties").getPath()); // Ruta relativa al paquete

    }

    /**
     * Imprime el conjunto de rutas del classpath.
     */
    public static void printClasspath() {
        Stream.of(System.getProperty("java.class.path")
                        .split(File.pathSeparator))
                .forEach(System.out::println);
    }

}
