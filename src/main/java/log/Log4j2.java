package log;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Log4j 2 API "Hello world": https://logging.apache.org/log4j/2.x/manual/api.html
 * Arquitectura: https://logging.apache.org/log4j/2.x/manual/architecture.html
 * Configuracion: https://logging.apache.org/log4j/2.x/manual/configuration.html
 * Appenders: https://logging.apache.org/log4j/2.x/manual/appenders.html
 * Layouts: https://logging.apache.org/log4j/2.x/manual/layouts.html
 * API: https://logging.apache.org/log4j/2.x/log4j-api/apidocs/index.html
 * Tutorial: https://mkyong.com/logging/apache-log4j-2-tutorials/
 * 
 * En el archivo config xml no se respeta el nivel del logger para la consola, pero para el logger de file si.
 * 
 * @author Ru$o
 * 
 */

public class Log4j2 {

	/* En la mayoria de los casos, las aplicaciones nombran a sus registradores pasando el nombre de la clase actual a
	 * LogManager.getLogger(...). Debido a que este uso es tan comun, Log4j 2 lo proporciona como predeterminado cuando el
	 * parametro del nombre del registrador se omite o es nulo.
	 * 
	 * Log4j2.class o Log4j2.class.getName() son dos formas diferentes de pasarle el nombre al logger, pero para una mejor
	 * legibilidad se omite.
	 * 
	 * El registrador en si no realiza acciones directas. Simplemente tiene un nombre y esta asociado con un LoggerConfig. */
	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++)
			log.debug("Testing");

		log.warn("warn!");

		/* Antes necesitabamos verificar el nivel de registro para aumentar el rendimiento:
		 * if (log.isDebugEnabled()) log.debug("{}", getNumber());
		 * 
		 * Ahora con Java 8 puede lograr el mismo efecto con una expresion lambda. Ya no es necesario verificar explicitamente
		 * el nivel de registro: */
		// log.debug("{}", () -> getNumber());

	}

	static int getNumber() {
		return 5;
	}

}
