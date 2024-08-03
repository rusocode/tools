package log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IMPORTANTE: esta clase es obsoleta en comparacion con la API Log4j.
 * <p>
 * El proceso de registro comienza con la creacion de un registrador (logger) que tiene un nombre especifico, generalmente basado
 * en el nombre del paquete. Los mensajes de registro se envian a objetos Handler, que los distribuyen a diversos destinos como
 * consolas, archivos o registros del sistema. El proceso sigue una serie de pasos: primero, se realiza una prueba inicial rapida;
 * luego, se crea un LogRecord para describir el mensaje; despues, se aplican filtros (si existen) para verificar si el mensaje
 * debe publicarse; y finalmente, si pasa los filtros, el LogRecord se publica en los controladores de salida. El formateo y la
 * localizacion son responsabilidad del controlador de salida, que normalmente utiliza un formateador. Es importante notar que el
 * formateo no necesita ser sincronico y puede retrasarse hasta que el LogRecord se escriba efectivamente en un receptor externo.
 * <p>
 * El objeto Handler es responsable de exportar los mensajes de registro de un registrador a diferentes destinos, como consolas,
 * archivos, servicios de red o registros del sistema operativo. Se puede habilitar o deshabilitar ajustando su nivel, y
 * generalmente utiliza las propiedades de LogManager para establecer valores predeterminados para el filtro, el formateador y el
 * nivel. Un tipo especifico, el FileHandler, puede escribir en un archivo unico o en un conjunto rotativo de archivos. En el caso
 * de archivos rotativos, cuando un archivo alcanza un tamaño limite, se cierra y se abre uno nuevo, nombrando los archivos mas
 * antiguos con numeros consecutivos añadidos al nombre base. Por defecto, aunque el almacenamiento en buffer esta habilitado en
 * las bibliotecas de E/S, cada registro se elimina al completarse.
 * <p>
 * Links: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/logging/Level.html#SEVERE">Logging levels</a>
 *
 * @author Juan Debenedetti
 */

public class Log {

    private static final Logger LOGGER = Logger.getLogger("kit.log");

    public static void start() {
        setupLogger();
        /* Registra un mensaje de informacion. Si el logger esta habilitado actualmente para el nivel de mensaje INFO, el mensaje
         * dado se reenvia a todos los objetos de controlador de salida registrados. */
        LOGGER.info("Servidor iniciado!");
    }

    private static void setupLogger() {

        LOGGER.setLevel(Level.FINEST); // Establece el nivel de registro especificando que niveles de mensajes registrara este LOGGER

        try {
            /* Crea un fh con un patron, limite de bytes y cantidad de archivos. Si se especifica un patron inutilizable, entonces
             * se utiliza el patron predeterminado "%h/java%u.log". %g es el numero de generacion para distinguir entre los
             * registros rotados (0, 1, etc,). */
            FileHandler fh = new FileHandler("logs/server%g.log", 1000000, 10);
            fh.setFormatter(new SimpleFormatter());
            // Agrega el controlador de registros para que el archivo pueda recibir mensajes del LOGGER
            LOGGER.addHandler(fh);

        } catch (SecurityException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        Log.start();
        LOGGER.log(Level.FINEST, "Hello World!");
    }

}
