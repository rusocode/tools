package _LABORATORIO;

import java.util.ArrayList;

/**
 * Los cargadores de clases son responsables de <b>cargar las clases de Java dinamicamente en la JVM (Java Virtual
 * Machine) durante el tiempo de ejecucion</b>. Tambien son parte del JRE (Java Runtime Environment). Por lo tanto, la
 * JVM no necesita conocer los archivos o sistemas de archivos subyacentes para ejecutar programas Java gracias a los
 * cargadores de clases.
 *
 * <p> Ademas, estas clases de Java no se cargan en la memoria todas a la vez, sino cuando las requiere una aplicacion.
 * Aqui es donde los cargadores de clases entran en escena. Son responsables de cargar las clases en la memoria.
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
 * <li><b>Extension ClassLoader:</b> la extension ClassLoader es un elemento secundario de Bootstrap ClassLoader y carga
 * las extensiones de las clases principales de Java desde la biblioteca de extension JDK respectiva. Carga archivos
 * desde el directorio jre/lib/ext o cualquier otro directorio señalado por la propiedad del sistema java.ext.dirs.
 * <li><b>System ClassLoader:</b> este cargador de clases es uno de los mas habituales ya que se encarga de cargar las
 * clase que se encuentran dentro del <b>classpath</b> de la aplicacion. Normalmente librerias que nosotros necesitamos
 * tipo Hibernate, Spring, etc. Se lo conoce tambien como Application ClassLoader.
 * </ol>
 *
 * <h3>Estructura de clases</h3>
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
	 * <p> A continuacion, el cargador de clases de extension carga la clase Logging. <b>Los cargadores de clases de
	 * extension cargan clases que son una extension de las clases basicas estandar de Java</b>.
	 *
	 * <p> Finalmente, el cargador de clases de arranque carga la clase ArrayList. <b>Un cargador de clases de arranque
	 * o primordial es el padre de todos los demas</b>.
	 * <p>
	 * Sin embargo, podemos ver que para ArrayList, muestra nulo en la salida. <b>Esto se debe a que el cargador de
	 * clases de arranque esta escrito en codigo nativo, no en Java, por lo que no aparece como una clase de Java</b>.
	 * Como resultado, el comportamiento del cargador de clases de arranque diferira entre las JVM.
	 */
	public void printClassLoaders() throws ClassNotFoundException {
		System.out.println("Cargador de clases de esta clase: " + ClassLoader_.class.getClassLoader());
		// System.out.println("Cargador de clases de Logging: " + Loggin.class.getClassLoader());
		System.out.println("Cargador de clases de ArrayList: " + ArrayList.class.getClassLoader());
	}

	public static void main(String[] args) throws ClassNotFoundException {

		ClassLoader_ loader = new ClassLoader_();

		loader.printClassLoaders();

	}

}
