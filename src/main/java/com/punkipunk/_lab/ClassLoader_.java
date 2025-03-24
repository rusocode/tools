package com.punkipunk._lab;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * <p>
 * El {@code ClassLoader} es responsable de <b>cargar las clases de Java dinamicamente en la JVM durante el tiempo de
 * ejecucion</b>. Forman parte integral del JRE, permitiendo que la JVM ejecute programas Java sin necesidad de conocer los
 * archivos o sistemas de archivos subyacentes.
 * <p>
 * Las clases Java no se cargan en memoria todas a la vez, sino bajo demanda cuando una aplicacion las requiere. Este proceso de
 * carga selectiva optimiza el uso de recursos, y es precisamente donde el ClassLoader desempeña su papel.
 * <p>
 * Cada clase es cargada por un ClassLoader especifico, determinado por el tipo y ubicacion de la clase. Para identificar que
 * ClassLoader ha cargado una clase particular, se puede utilizar el metodo {@code getClassLoader()}. Si una clase no se encuentra
 * durante el proceso de carga, se lanzara una excepcion {@code NoClassDefFoundError} o {@code ClassNotFoundException}.
 * <p>
 * En Java existen tres tipos principales de ClassLoaders (estructura tradicional pre-Java 9):
 * <ol>
 * <li><b>Bootstrap ClassLoader:</b> Implementado en codigo nativo (no en Java), carga las clases core del JDK (como
 * {@code java.lang.String}, {@code java.lang.Integer}, etc.). No tiene ClassLoader padre y se conoce como
 * <i>Primordial ClassLoader</i>. En versiones anteriores a Java 9, cargaba clases desde la ubicacion {@code rt.jar}.</li>
 * <li><b>Platform ClassLoader:</b> (Anteriormente conocido como <i>Extension ClassLoader</i> en versiones pre-Java 9) Es hijo del
 * Bootstrap ClassLoader y carga las extensiones de las clases principales. En versiones anteriores a Java 9, cargaba archivos
 * desde el directorio {@code jre/lib/ext} o directorios especificados por la propiedad del sistema {@code java.ext.dirs}. En
 * Java 9+, carga los modulos estandar que no forman parte del modulo base.</li>
 * <li><b>System ClassLoader:</b> Tambien conocido como <i>Application ClassLoader</i>, es responsable de cargar las clases
 * definidas en el <b>classpath</b> de la aplicacion o en los modulos especificados. Aqui se cargan las bibliotecas externas como
 * Hibernate, Spring, etc.</li>
 * </ol>
 * <h3>Modelo de delegacion y sistema de modulos</h3>
 * <p>
 * La clase ClassLoader utiliza un modelo de delegacion para buscar clases y recursos. Cuando se solicita cargar una clase, el
 * ClassLoader primero delega la busqueda a su ClassLoader padre antes de intentar cargarla por si mismo. Este modelo de jerarquia
 * siempre opera en el orden: Application ClassLoader → Platform ClassLoader → Bootstrap ClassLoader.
 * <p>
 * A partir de Java 9, el sistema de modulos (JPMS - Java Platform Module System) introdujo cambios significativos en como se
 * organizan y cargan las clases. Los modulos encapsulan paquetes y recursos, y cada modulo especifica explicitamente sus
 * dependencias, mejorando la modularidad y seguridad del sistema.
 * <h3>Implementacion interna</h3>
 * <p>
 * A nivel de implementacion, el Bootstrap ClassLoader esta escrito en codigo nativo, mientras que el Platform ClassLoader y el
 * System ClassLoader son instancias de clases que extienden {@code URLClassLoader} en versiones anteriores a Java 9, o de
 * implementaciones internas especificas en Java 9+.
 * <p>
 * Es posible crear ClassLoaders personalizados extendiendo la clase abstracta {@code ClassLoader} o {@code URLClassLoader}, lo
 * que permite implementar politicas de carga de clases personalizadas para casos especificos como frameworks de plugins,
 * contenedores de aplicaciones o aislamiento de clases.
 *
 * @see <a href="https://www.baeldung.com/java-classloaders">Class Loaders in Java</a>
 * @see <a href="https://www.geeksforgeeks.org/classloader-in-java/">ClassLoader in Java</a>
 * @see <a href="https://www.arquitecturajava.com/el-concepto-de-classloader/">El concepto de ClassLoader</a>
 * @see <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/ClassLoader.html">API ClassLoader (Java
 * 17)</a>
 */

public class ClassLoader_ {

    /**
     * Imprime el conjunto de rutas del classpath.
     */
    public static void printClasspath() {
        Stream.of(System.getProperty("java.class.path")
                        .split(File.pathSeparator))
                .forEach(System.out::println);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        CustomClassLoader customClassLoader = new CustomClassLoader();
        Class<?> clase = customClassLoader.findClass("com.punkipunk._lab.ClassLoader_");
        // System.out.println(clase.getSimpleName());

        // Obtiene el cargador de clases predeterminado de la JVM
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        // System.out.println(systemClassLoader.getName());

        // printClasspath();

    }

    /**
     * <p>
     * Como podemos ver, aca hay tres cargadores de clases diferentes: aplicacion, extension y arranque (que se muestra como
     * nulo).
     * <p>
     * El cargador de clases de la aplicacion carga la clase donde se encuentra el metodo de ejemplo. <b>Un cargador de clases de
     * aplicacion o sistema carga nuestros propios archivos en el classpath</b>.
     * <p>
     * A continuacion, el cargador de clases de extension carga la clase Loggin. <b>Los cargadores de clases de extension cargan
     * clases que son una extension de las clases basicas estandar de Java</b>.
     * <p>
     * Finalmente, el cargador de clases de arranque carga la clase ArrayList. <b>Un cargador de clases de arranque o primordial
     * es el padre de todos los demas</b>.
     * <p>
     * Sin embargo, podemos ver que para ArrayList, muestra nulo en la salida. <b>Esto se debe a que el cargador de clases de
     * arranque esta escrito en codigo nativo, no en Java, por lo que no aparece como una clase de Java</b>. Como resultado, el
     * comportamiento del cargador de clases de arranque diferira entre las JVM.
     */
    public void printClassLoaders() {
        System.out.println("Cargador de clases de esta clase: " + ClassLoader_.class.getClassLoader());
        // System.out.println("Cargador de clases de Logging: " + Loggin.class.getClassLoader());
        System.out.println("Cargador de clases de ArrayList: " + ArrayList.class.getClassLoader());
    }

    /**
     * <p>
     * La subclase del cargador de clases de aplicacion debe definir los metodos {@link #findClass findClass()} y
     * {@link #loadClassData loadClassData()} para cargar una clase de la aplicacion. Una vez que haya descargado los bytes que
     * componen la clase, debe usar el metodo {@code defineClass()} para crear una instancia de clase.
     * <p>
     * El siguiente ejemplo, define un cargador de clases personalizado.
     * <p>
     * <i>¿Por que no utilizo el metodo {@code findClass()} directamente desde un objeto {@code ClassLoader}?</i>
     * <p>
     * Porque el metodo {@code findClass()} esta protegido y solo es accesible desde una subclase que extienda
     * {@code ClassLoader}. Por lo tanto este metodo debe ser anulado por las implementaciones del cargador de clases que siguen
     * el modelo de delegacion para cargar clases, y el metodo {@code loadClassData()} lo invocara despues de verificar el
     * cargador de clases principal para la clase solicitada. La implementacion predeterminada lanza una
     * {@code ClassNotFoundException}.
     * <h3>Nombres binarios</h3>
     * <p>
     * Cualquier nombre de clase proporcionado como un parametro {@code String} a los metodos {@code ClassLoader} debe ser un
     * nombre binario segun lo define la especificacion del lenguaje Java.
     * <p>
     * Ejemplos de nombres de clase validos incluyen:
     * <pre>{@code
     *   "java.lang.String"
     *   "javax.swing.JSpinner$DefaultEditor"
     *   "java.security.KeyStore$Builder$FileBuilder$1"
     *   "java.net.URLClassLoader$3$1"
     * }</pre>
     */
    private static class CustomClassLoader extends ClassLoader {

        /**
         * Encuentra la clase con el nombre binario especificado y la carga en un array de bytes.
         *
         * @param name el nombre binario de la clase
         * @return el objeto Class que se creo a partir de los datos de clase especificados
         */
        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            // A diferencia del metodo loadClass(), este devuelve una matriz de bytes
            byte[] bytes = loadClassData(name);
            // Convierte la matriz de bytes a una instancia de Class
            return defineClass(name, bytes, 0, bytes.length);
        }

        /**
         * Carga los datos de la clase con el nombre binario especificado.
         * <p>
         * Obtiene el flujo de entrada del recurso especificado. Lee los bytes del flujo de entrada y los escribe en el buffer de
         * bytes del flujo de salida.
         *
         * @param name el nombre binario de la clase
         * @return el array con los bytes del archivo .class
         */
        private byte[] loadClassData(String name) throws ClassNotFoundException {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name.replace('.', File.separatorChar) + ".class");
            if (inputStream == null) throw new ClassNotFoundException();
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue;
            try {
                while ((nextValue = inputStream.read()) != -1) byteStream.write(nextValue);
            } catch (IOException e) {
                System.err.println("Errror: " + e.getMessage());
            }
            return byteStream.toByteArray();
        }

    }

}
