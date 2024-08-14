package _lab;

/**
 * <h3>Interfaz de constantes</h3>
 * <p>
 * Una interfaz de constantes en Java es un patron de diseño antiguo que consiste en una interfaz que contiene solo campos
 * estaticos finales (constantes). Aunque fue comun en el pasado, actualmente <i>no</i> se considera una buena practica. Su
 * estructura tipica incluye constantes definidas como {@code public static final}. Las clases que implementan esta interfaz
 * heredan automaticamente estas constantes, y tambien se puede acceder a ellas sin implementar la interfaz. Sin embargo, este
 * enfoque presenta problemas como crear dependencias innecesarias, generar confusion sobre el proposito de las interfaces y no
 * proporcionar un encapsulamiento real. Aunque este patron aun se encuentra en codigo antiguo, se desaconseja su uso en el
 * desarrollo moderno de Java. Como alternativas modernas, se recomienda usar clases de utilidad con constantes estaticas, enums
 * para conjuntos de constantes relacionadas, o {@code static imports} para importar constantes especificas. Un ejemplo de
 * alternativa es crear una clase final con constantes estaticas y un constructor privado para evitar la instanciacion.
 * <h3>Clase de utilidad con constantes estaticas</h3>
 * <p>
 * Una clase de utilidad con constantes estaticas es un patron de diseño en Java que agrupa constantes relacionadas y metodos de
 * utilidad en una unica clase. Esta clase es final para evitar herencia, tiene un constructor privado para prevenir
 * instanciacion, y contiene campos estaticos finales (constantes) y opcionalmente metodos estaticos de utilidad. Se accede a las
 * constantes y metodos directamente a traves del nombre de la clase o usando una importancion estatica comodin
 * {@code import static Constants.*} para importar todas las constantes y metodos estaticos de la clase. Este enfoque ofrece
 * ventajas como agrupar logicamente las constantes, evitar la contaminacion del espacio de nombres global, proporcionar un punto
 * central para mantenimiento y permitir la inclusion de logica relacionada en metodos estaticos. Es mas flexible que una interfaz
 * de constantes, permite mejor control de acceso y se integra bien con las practicas de programacion orientada a objetos. Este
 * patron es ampliamente considerado como una mejor practica en comparacion con las interfaces de constantes en el desarrollo
 * moderno de Java.
 * <p>
 * Ejemplo:
 * <pre>{@code
 * public final class Constants {
 *     public static final int CONSTANT_1 = 1;
 *     public static final String CONSTANT_2 = "Value";
 *
 *     private Constants() {} // Constructor privado para evitar instanciacion
 * }
 * }</pre>
 * <p>
 * Conceptualmente, una enumeracion es una mala eleccion si las constantes no estan relacionadas. Una enumeracion representa
 * valores alternativos del mismo tipo.
 * <h3>Alternativas actuales</h3>
 * <p>
 * Las clases de utilidad con constantes estaticas siguen siendo utilizadas en el desarrollo Java actual, aunque su uso ha
 * evolucionado. Se consideran una mejor practica que las interfaces de constantes y son comunes para agrupar constantes
 * relacionadas. Sin embargo, existen alternativas modernas como enums, configuracion externalizada e inyeccion de dependencias.
 * El diseño actual tiende a ser mas orientado a objetos, buscando reducir el acoplamiento causado por constantes globales. Estas
 * clases se usan comunmente para constantes de configuracion, valores predefinidos invariables y agrupacion de constantes de
 * dominio especifico. Las buenas practicas incluyen usarlas para constantes verdaderamente relacionadas, evitar <a
 * href="https://www.metridev.com/metrics/god-class-the-definitive-guide-to-identifying-and-avoiding-it/">"god class"</a> y
 * considerar la visibilidad apropiada. La tendencia se inclina hacia configuraciones mas flexibles y externalizadas, y un mayor
 * uso de enums para ciertos tipos de constantes. En resumen, aunque siguen en uso, se aplican de manera mas cuidadosa y
 * selectiva, considerando alternativas segun el caso especifico.
 * <h3>Espacios de memoria en declaraciones byte y short</h3>
 * <p>
 * Es buena practica declarar matrices de bytes para la asignacion de valores chicos, ya que los tipos mas pequeños (byte y short)
 * basicamente solo estan destinados a matrices. Una matriz como new byte[1000] tomara 1000 bytes, y una matriz como new int[1000]
 * tomara 4000 bytes. byte y short ocupan el mismo espacio que int si fueran variables locales, variables de clase o incluso
 * variables de instancia. ¿Por que? Porque en (la mayoria) los sistemas informaticos, las direcciones de las variables estan
 * alineadas, por lo que, por ejemplo, si usa un solo byte, en realidad terminara con dos bytes, uno para la variable en si y otro
 * para el relleno.
 * <p>
 * Si nos fijamos en la constantes de las famosas clases GL11 (lwjgl) y Calendar (API Java), todas estan declaradas como int.
 * <p>
 * Links:
 * <a href="https://en.wikipedia.org/wiki/Constant_interface">Constant interface</a>
 * <a href="https://stackoverflow.com/questions/320588/interfaces-with-static-fields-in-java-for-sharing-constants">Interfaces
 * with static fields in java for sharing 'constants'</a>
 * <a href="https://stackoverflow.com/questions/479565/how-do-you-define-a-class-of-constants-in-java">How do you define a class
 * of constants in Java?</a> <a
 * href="https://stackoverflow.com/questions/14531235/in-java-is-it-more-efficient-to-use-byte-or-short-instead-of-int-and-float-inst/14532302#14532302">In
 * java, is it more efficient to use byte or short instead of int and float instead of double?</a>
 * <a href="https://stackoverflow.com/questions/27122610/why-does-the-java-api-use-int-instead-of-short-or-byte">Why does the
 * Java API use int instead of short or byte?</a>
 */

public final class Constants {

    private Constants() {
    }

    public static final byte[] MODIFICADOR_FUERZA = {1, 2, 3};

    public static final int N1 = 1;
    public static final int N2 = 2;
    public static final int N3 = 3;

    public static final String[] SKILLS = {"Numero 1", "Numero 2", "Numero 3"};

    public static final String SEPARADOR = java.io.File.separator;
    public static final String DATDIR = "assets" + SEPARADOR + "dat";

}
