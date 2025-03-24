package com.punkipunk._lab;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * <p>
 * Java proporciona una clase con el nombre {@code Class} en el paquete {@code java.lang}. Las instancias de la clase
 * {@code Class} representan clases e interfaces en una aplicacion Java en ejecucion. Los tipos primitivos (boolean, byte, char,
 * short, int, long, float y double) y la palabra clave {@code void} tambien se representan como objetos Class.
 * <p>
 * Esta clase no tiene constructor publico. Los objetos Class son construidos automaticamente por la JVM a medida que se cargan
 * las clases y mediante llamadas al metodo {@code defineClass()} en el {@code ClassLoader}. Es una clase final, por lo que no se
 * puede extender.
 * <p>
 * <b>Formas de obtener un objeto Class:</b>
 * <ol>
 * <li><b>{@code Class.forName("className")}</b>: metodo de fabrica estatico que crea objetos de la clase Class asociados
 * con el nombre de clase proporcionado. Este metodo carga dinamicamente una clase:
 * <pre>{@code Class<?> clase = Class.forName("paquete.NombreClase");}</pre>
 * <p>
 * Esta declaracion crea el objeto Class para la clase pasada como argumento String. El parametro debe ser el nombre completo
 * (fully qualified name) de la clase deseada. El nombre de la clase se determina en tiempo de ejecucion y puede lanzar
 * {@code ClassNotFoundException} si la clase no existe.
 * <li><b>{@code NombreClase.class}</b>: cuando escribimos .class despues de un nombre de clase, hace referencia al
 * objeto Class que representa la clase dada. Se usa principalmente cuando conocemos el nombre de la clase en tiempo de compilacion:
 * <pre>{@code
 * // Para clases
 * Class<?> claseString = String.class;
 *
 * // Para tipos primitivos
 * Class<?> claseInt = int.class;
 * Class<?> claseVoid = void.class;
 * }</pre>
 * <p>
 * Esta forma usa el <i>literal de clase</i> del nombre de clase y es verificada en tiempo de compilacion, evitando excepciones
 * en tiempo de ejecucion. Ejemplos:
 * <pre>{@code
 * Test test = new Test();
 * Class<?> clase = Test.class; // Correcto
 * Class<?> claseError = test.class; // Error de compilacion
 * }</pre>
 * <li><b>{@code objeto.getClass()}</b>: este metodo esta presente en la clase {@code Object}. Devuelve la clase en
 * tiempo de ejecucion del objeto invocante:
 * <pre>{@code
 * Test test = new Test();
 * Class<?> clase = test.getClass();
 * }</pre>
 * <p>
 * Este metodo es util cuando solo tenemos acceso a la instancia de un objeto y necesitamos informacion sobre su clase.
 * Notese que {@code getClass()} siempre devuelve el tipo exacto del objeto, que puede ser una subclase del tipo declarado.
 * </ol>
 * <p>
 * <b>Usos comunes de la clase Class:</b>
 * </p>
 * <ul>
 * <li>Obtener metadatos de una clase (metodos, campos, constructores, etc.)
 * <li>Crear instancias mediante reflexion
 * <li>Verificar tipos en tiempo de ejecucion
 * <li>Cargar recursos asociados a una clase
 * </ul>
 * <p>
 * Una vez creado el objeto Class, este puede acceder a las caracteristicas de la clase que representa. El
 * siguiente ejemplo utiliza un objeto Class para imprimir el nombre completo de la clase:
 * <pre>{@code
 * void printClassName(Object objeto) {
 *     System.out.println("El nombre completo de la clase es: " + objeto.getClass().getName());
 *     System.out.println("El nombre simple de la clase es: " + objeto.getClass().getSimpleName());
 * }
 * }</pre>
 *
 * @see <a href="https://www.geeksforgeeks.org/java-lang-class-class-java-set-1/">Java.lang.Class class in Java | Set 1</a>
 */

public class Class_ {

    private static final StringBuilder builder = new StringBuilder();

    /**
     * Devuelve el objeto Class asociado con la clase o interfaz con el nombre de cadena dado.
     *
     * @param className el nombre completo de la clase especificada
     * @return el objeto Class para la clase con el nombre especificado
     */
    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * <p>
     * Muestra todos los campos declarados por la clase o interfaz representada por este objeto Class. Esto incluye campos
     * publicos, protegidos, default y privados, pero excluye los campos heredados. Ademas de mostrar todos los campos, tambien
     * muestra sus valores excepto el de los privados.
     * <p>
     * <i>Nota: si el campo privado pertenece a la misma clase, entonces si puede acceder a su valor.</i>
     *
     * @param obj clase representada por el objeto Class
     */
    private static void printDeclaredFields(Class<?> obj) {
        try {
            Object o = obj.getDeclaredConstructor().newInstance();
            Field[] fields = obj.getDeclaredFields();
            if (fields.length != 0) {
                append("Attributes:");
                for (Field field : fields) {
                    /* Si el campo no es privado o no es privado y final.
                     * TODO Reemplazar esta forma de comprobar el modificador de campo. */
                    if (!(field.getModifiers() == Modifier.PRIVATE || field.getModifiers() == (Modifier.PRIVATE + Modifier.FINAL)))
                        append(field.getName() + " = " + field.get(o));
                    else append("The value of the '" + field.getName() + "' cannot be accessed because it is private");
                }
            } else append("The class has no declared attributes!");
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException e) {
            append(e.getMessage());
        }
    }

    private static void append(String line) {
        builder.append(line);
        builder.append(System.lineSeparator());
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        // Crea un objeto Class que representa la clase Test
        Class<?> clase = forName("com.punkipunk._lab.Test");

        if (clase != null) {
            append("Class name: " + clase.getName());
            append("Simple class name: " + clase.getSimpleName());
            append("ClassLoader: " + clase.getClassLoader());
            append("ClassLoader name: " + clase.getClassLoader().getName());
            append("Package name: " + clase.getPackage().getName());
            append("Superclass name: " + clase.getSuperclass().getSimpleName());
            append("Constructor: " + clase.getConstructor());
            append("Is interface? " + (clase.isInterface() ? "Yes" : "No"));
            append("Is primitive? " + (clase.isPrimitive() ? "Yes" : "No"));
            append("Is array? " + (clase.isArray() ? "Yes" : "No"));
            append("Is anonymous class? " + (clase.isAnonymousClass() ? "Yes" : "No"));
            append("Is local class? " + (clase.isLocalClass() ? "Yes" : "No"));
            append("Is enum? " + (clase.isEnum() ? "Yes" : "No"));
            append("Is the specified object compatible with the class? " + (clase.isInstance(new Test()) ? "Yes" : "No"));

            printDeclaredFields(clase);

            // Crea un objeto Test usando el metodo newInstance()
            /* Test test = (Test) clase.getDeclaredConstructor().newInstance();
             * test.publicMethod(); */
        }

        System.out.print(builder);

    }

}
