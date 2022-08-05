package _LABORATORIO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Los cargadores de clases son responsables de <b>cargar las clases de Java dinamicamente en la JVM (Java Virtual
 * Machine) durante el tiempo de ejecucion</b>. Tambien son parte del JRE (Java Runtime Environment). Por lo tanto, la
 * JVM no necesita conocer los archivos o sistemas de archivos subyacentes para ejecutar programas Java gracias a los
 * cargadores de clases.
 *
 * <p> Ademas, estas clases de Java no se cargan en la memoria todas a la vez, sino cuando las requiere una aplicacion.
 * Aqui es donde los cargadores de clases entran en escena.
 *
 * <p> No todas las clases se cargan con un solo ClassLoader. Dependiendo del tipo de clase y la ruta de la clase, se
 * decide el ClassLoader que carga esa clase en particular. Para conocer el ClassLoader que carga una clase se utiliza
 * el metodo getClassLoader(). Todas las clases se cargan en funcion de sus nombres y, si alguna de estas clases no se
 * encuentra, devuelve NoClassDefFoundError o ClassNotFoundException.
 *
 * <p> Un Java Classloader es de tres tipos:
 * <ol>
 * <li><b>BootStrap ClassLoader:</b> es un codigo maquina que carga las clases core del JDK como por ejemplo String,
 * Integer, Date, etc. cuando la JVM lo requiere. No es una clase java y su trabajo es cargar el primer ClassLoader de
 * Java puro. Carga clases desde la ubicacion rt.jar dentro del JDK. No tiene ClassLoaders principales y se lo conoce
 * como Primodial ClassLoader.
 * <li><b>Extension ClassLoader:</b> la Extension ClassLoader es un elemento secundario de BootStrap ClassLoader y carga
 * las extensiones de las clases principales de Java desde la biblioteca de extension JDK respectiva. Carga archivos
 * desde el directorio jre/lib/ext o cualquier otro directorio señalado por la propiedad del sistema java.ext.dirs.
 * <li><b>System ClassLoader:</b> este cargador de clases es uno de los mas habituales ya que se encarga de cargar las
 * clase que se encuentran dentro del <b>classpath</b> de la aplicacion. Normalmente librerias que nosotros necesitamos
 * tipo Hibernate, Spring, etc. Se lo conoce tambien como Application ClassLoader.
 * </ol>
 *
 * <h3> Estructura de clases </h3>
 * La clase ClassLoader usa un modelo de delegacion para buscar clases y recursos. Cada instancia de ClassLoader
 * tiene un cargador de clases principal asociado. Cuando se le solicite encontrar una clase o un recurso, una instancia
 * de ClassLoader delegara la busqueda de la clase o el recurso a su cargador de clases principal antes de intentar
 * encontrar la clase o el recurso en si. El cargador de clases integrado de la maquina virtual, llamado "BootStrap
 * ClassLoader", no tiene un padre en si mismo, pero puede servir como padre de una instancia de ClassLoader.
 *
 * <p> El modelo de jerarquia de delegacion de ClassLoader siempre funciona en el orden <i>Application ClassLoader->
 * Extension ClassLoader->Bootstrap ClassLoader</i>.
 *
 * <p> Lamentablemente no tenemos ni BootStrap ClassLoader, ni Extension ClassLoader ni System ClassLoader. ¿Entonces
 * como funciona esto realmente? Vamos a ir paso a paso. En primer lugar es normal que no exista una clase BootStrap
 * ClassLoader ya que no esta definida en codigo java sino en codigo nativo.
 *
 * <p> Sin embargo nos quedan dos cargadores por encontrar. Eso se debe a que los ClassLoaders son tanto clases como
 * objetos de la maquina virtual. En nuestro caso disponemos de una clase denominada URLClassLoader la cual sera la
 * encargada de instanciar dos objetos de tipo “URLClassLoader” el Extension ClassLoader y el System ClassLoader, cada
 * uno de los cuales apunta a URLs distintas.
 *
 * <p> De esta manera ya hemos solventado el problema de como la JVM gestiona cada uno de los ClassLoaders que
 * disponemos al arrancar un programa.
 *
 * <br><br> Fuentes:
 * <a href="https://www.baeldung.com/java-classloaders">Cargadores de clases en Java</a>
 * <a href="https://www.geeksforgeeks.org/classloader-in-java/">¿Que es el objeto ClassLoader?</a>
 * <a href="https://www.arquitecturajava.com/el-concepto-de-classloader/">El concepto de ClassLoader</a>
 */
public class ClassLoader_ {

	/**
	 * Como podemos ver, aca hay tres cargadores de clases diferentes: aplicacion, extension y arranque (que se muestra
	 * como nulo).
	 *
	 * <p> El cargador de clases de la aplicacion carga la clase donde se encuentra el metodo de ejemplo. <b>Un cargador
	 * de clases de aplicacion o sistema carga nuestros propios archivos en el classpath</b>.
	 *
	 * <p> A continuacion, el cargador de clases de extension carga la clase Loggin. <b>Los cargadores de clases de
	 * extension cargan clases que son una extension de las clases basicas estandar de Java</b>.
	 *
	 * <p> Finalmente, el cargador de clases de arranque carga la clase ArrayList. <b>Un cargador de clases de arranque
	 * o primordial es el padre de todos los demas</b>.
	 * <p>
	 * Sin embargo, podemos ver que para ArrayList, muestra nulo en la salida. <b>Esto se debe a que el cargador de
	 * clases de arranque esta escrito en codigo nativo, no en Java, por lo que no aparece como una clase de Java</b>.
	 * Como resultado, el comportamiento del cargador de clases de arranque diferira entre las JVM.
	 */
	public void printClassLoaders() {
		System.out.println("Cargador de clases de esta clase: " + ClassLoader_.class.getClassLoader());
		// System.out.println("Cargador de clases de Logging: " + Loggin.class.getClassLoader());
		System.out.println("Cargador de clases de ArrayList: " + ArrayList.class.getClassLoader());
	}

	/**
	 * La subclase del cargador de clases de aplicacion debe definir los metodos {@link #findClass findClass} y {@code
	 * loadClassData} para cargar una clase de la aplicacion. Una vez que haya descargado los bytes que componen la
	 * clase, debe usar el metodo {@code defineClass} para crear una instancia de clase.
	 *
	 * <p> El siguiente ejemplo, define un cargador de clases personalizado que extiende {@code ClassLoader}
	 * predeterminado.
	 *
	 * <p> <i>¿Por que no utilizo el metodo {@code findClass} directamente desde un objeto {@code ClassLoader}?</i>
	 * <br>
	 * Porque el metodo {@code findClass} esta protegido y solo es accesible desde una subclase que extienda {@code
	 * ClassLoader}. Por lo tanto este metodo debe ser anulado por las implementaciones del cargador de clases que
	 * siguen el modelo de delegacion para cargar clases, y el metodo {@code loadClassData} lo invocara despues de
	 * verificar el cargador de clases principal para la clase solicitada. La implementacion predeterminada lanza una
	 * ClassNotFoundException.
	 *
	 * <br><br>
	 * <h3> Nombres binarios </h3>
	 * Cualquier nombre de clase proporcionado como un parametro {@code String} a los metodos {@code ClassLoader} debe
	 * ser un nombre binario segun lo define la <cite>especificacion del lenguaje Java™</cite>.
	 *
	 * <p> Ejemplos de nombres de clase validos incluyen:
	 * <blockquote><pre>
	 *   "java.lang.String"
	 *   "javax.swing.JSpinner$DefaultEditor"
	 *   "java.security.KeyStore$Builder$FileBuilder$1"
	 *   "java.net.URLClassLoader$3$1"
	 * </pre></blockquote>
	 */
	private static class CustomClassLoader extends ClassLoader {

		/**
		 * Encuentra la clase con el nombre binario especificado y la carga en un array de bytes.
		 *
		 * @param name el nombre binario de la clase.
		 * @return el objeto Class que se creo a partir de los datos de clase especificados.
		 */
		@Override
		public Class<?> findClass(String name) throws ClassNotFoundException {
			// A diferencia del metodo loadClass(), este devuelve una matriz de bytes
			byte[] b = loadClassData(name);
			// Convierte la matriz de bytes a una instancia de Class
			return defineClass(name, b, 0, b.length);
		}

		/**
		 * Carga la clase con el nombre binario especificado.
		 *
		 * <p> Obtiene el flujo de entrada del recurso especificado. Lee los bytes del flujo de entrada y los escribe
		 * en el buffer de bytes del flujo de salida.
		 *
		 * @param name el nombre binario de la clase.
		 * @return el array con los bytes del archivo .class.
		 */
		private byte[] loadClassData(String name) throws ClassNotFoundException {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name.replace('.', File.separatorChar) + ".class");
			if (inputStream == null) throw new ClassNotFoundException();
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			int nextValue;
			try {
				while ((nextValue = inputStream.read()) != -1) byteStream.write(nextValue);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return byteStream.toByteArray();
		}

	}

	public static void main(String[] args) throws ClassNotFoundException {
		/* CustomClassLoader customLoader = new CustomClassLoader();
		 * Class<?> clase = customLoader.findClass("_LABORATORIO.ClassLoader_");
		 * System.out.println(clase.getSimpleName()); */

		String classpath = System.getProperty("java.class.path");
		String[] paths = classpath.split(System.getProperty("path.separator"));
		for (String path : paths) System.out.println(path);

	}

}
