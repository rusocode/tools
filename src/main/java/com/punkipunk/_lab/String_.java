package com.punkipunk._lab;

/**
 * <p>
 * La clase String en Java es inmutable, lo que significa que cualquier operacion de modificacion produce una nueva instancia en
 * lugar de alterar la original. Aunque esto puede parecer ineficiente en terminos de uso de memoria, proporciona beneficios en
 * cuanto a seguridad en entornos multihilo. Java almacena los objetos String en el heap, pero los literales String son un caso
 * especial y se guardan en el <i>String Pool</i>, una area de memoria que contiene cadenas unicas y reutilizables. Cuando se crea
 * una cadena identica a una existente en el pool, Java devuelve una referencia a la cadena existente en lugar de crear una nueva.
 * Existen dos formas principales de crear objetos String: usando comillas dobles, lo cual almacena la cadena en el String Pool, o
 * utilizando el operador {@code new}, que crea una nueva instancia en el heap general. Es importante notar que, a partir de Java
 * 7, el String Pool se movio del PermGen al heap principal, mejorando su manejo y rendimiento. Ademas, metodos como
 * {@code intern()} permiten añadir manualmente Strings al pool, optimizando aun mas el uso de memoria en ciertas situaciones.
 * <p>
 * En Java, la comparacion de Strings requiere una atencion especial debido a su naturaleza como objetos. Mientras que el operador
 * {@code ==} se utiliza para comparar tipos primitivos, los Strings deben compararse utilizando el metodo {@code equals()}.
 * Aunque tecnicamente es posible usar == con Strings, este operador compara las referencias de los objetos y no su contenido, lo
 * que puede llevar a resultados inesperados. Dos Strings con el mismo contenido pero creados independientemente daran
 * {@code false} al usar ==, mientras que equals() comparara correctamente su contenido. Es importante mencionar que Java utiliza
 * un "pool" de Strings para optimizar el uso de memoria, lo que puede resultar en que Strings creados como literales compartan la
 * misma referencia si tienen el mismo contenido. Sin embargo, no se recomienda confiar en este comportamiento. La practica mas
 * segura y recomendada es siempre utilizar equals() para comparar Strings en Java, asegurando asi una comparacion basada en el
 * contenido y no en las referencias de los objetos.
 * <h3>string.isEmpty() vs "".equals(string)</h3>
 * <p>
 * En Java, la eleccion entre {@code string.isEmpty()} y {@code "".equals(string)} para verificar si una cadena esta vacia depende
 * del contexto y las necesidades especificas del codigo. "".equals(string) ofrece la ventaja de ser seguro contra nulos, ya que
 * el metodo equals() verifica si el argumento es nulo y devuelve falso en ese caso, sin lanzar excepciones. Por otro lado,
 * string.isEmpty() es mas explicito en su intencion y generalmente mas eficiente en terminos de rendimiento, ya que solo compara
 * la longitud de la cadena con 0. Sin embargo, string.isEmpty() lanzara una {@code NullPointerException} si string es nulo. En
 * cuanto a rendimiento, string.isEmpty() tiende a ser mas rapido que "".equals(string), ya que este ultimo realiza verificaciones
 * adicionales de tipo y longitud, y potencialmente itera sobre los caracteres de la cadena. La eleccion entre ambos metodos
 * dependera de si se necesita manejar posibles nulos sin verificacion adicional ("".equals(string)), o si se prefiere una
 * verificacion mas directa y eficiente de una cadena vacia (string.isEmpty()), asumiendo que se ha realizado o no es necesaria
 * una verificacion previa de nulos. En versiones mas recientes de Java, tambien se puede considerar el uso de String.isBlank()
 * para verificaciones que incluyan espacios en blanco, o utilizar bibliotecas como Apache Commons Lang para operaciones mas
 * robustas con cadenas.
 * <p>
 * Links:
 * <a href="https://www.javastring.net/java/string/pool">Java String Pool</a>
 * <a href="https://es.stackoverflow.com/questions/225/c%C3%B3mo-comparar-correctamente-strings-y-objetos-en-java/258">¿Como
 * comparar correctamente Strings (y objetos) en Java?</a>
 * <a href="https://www.youtube.com/watch?v=YoV2W8NHFoc">Diferencias entre String, StringBuffer y StringBuilder</a>
 * <a href="https://stackoverflow.com/questions/3321526/should-i-use-string-isempty-or-equalsstring/3321548#3321548">Should I use
 * string.isEmpty() or "".equals(string)?</a>
 */

public class String_ {

    private static String string;

    public static void main(String[] args) {
        test();
        // measureTime(10000);
    }

    private static void test() {
        String a = "Hello";
        System.out.println("hashCode = " + a.hashCode());
        a = "Hi";
        // El hashCode cambia despues de modificar el valor de la cadena, es decir, se crea un nuevo objeto
        System.out.println("hashCode = " + a.hashCode());

        // Literales de String almacenados en el Pool String
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello"); // Objeto String almacenado en el heap
        String s4 = "Hi";
        System.out.println("s1 == s2? " + (s1 == s2)); // true ya que ambas instancias hacen referencia al mismo objeto en el pool, siempre y cuando las cadenas sean iguales
        System.out.println("s1 == s3? " + (s1 == s3)); // false ya que d es un nuevo objeto aunque las cadenas coincidan
        System.out.println("s1.equals(s3)? " + s1.equals(s3)); // true por que compara las cadenas y no los objetos
        System.out.println("s1 == s4? " + (s1 == s4)); // false ya que e es una referencia a nueva cadena (objeto) en el pool
        // Mueve el objeto String al string pool
        s3 = s3.intern();
        System.out.println("s1 == s3? " + (s1 == s3));
    }

    private static void checkStringEmpty() {
        System.out.println("".equals(string)); // Seguro contra nulos pero menos enficiente en terminos de rendimiento en comparacion con isEmpty()
        System.out.println(string != null && string.isEmpty()); // Evita un NPE primero en caso de que la cadena sea nula pero mas eficiente
        string = "";
        System.out.println(string.isBlank());
    }

    /**
     * Measures the time it takes to add a specified number of strings.
     *
     * @param count number of strings.
     */
    private static void measureTime(int count) {
        String str = null;
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++)
            str += "str";
        System.out.println("Time = " + (System.nanoTime() - startTime) / 1e6 + " ms");
    }

}
