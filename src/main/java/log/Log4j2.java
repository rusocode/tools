package log;

import org.apache.logging.log4j.Logger;

import _LABORATORIO.Prueba;

import org.apache.logging.log4j.LogManager;

/**
 * Log4j 2 API "Hello world": https://logging.apache.org/log4j/2.x/manual/api.html
 * Configuracion: https://logging.apache.org/log4j/2.x/manual/configuration.html
 * API: https://logging.apache.org/log4j/2.x/log4j-api/apidocs/index.html
 * Tutorial: https://mkyong.com/logging/apache-log4j-2-tutorials/
 * 
 * @author Ru$o
 * 
 */

public class Log4j2 {

	/* En la mayoria de los casos, las aplicaciones nombran a sus registradores pasando el nombre de la clase actual a
	 * LogManager.getLogger(...). Debido a que este uso es tan comun, Log4j 2 lo proporciona como predeterminado cuando el
	 * parametro del nombre del registrador se omite o es nulo.
	 * Log4j2.class o Log4j2.class.getName() son dos formas diferentes de pasarle el nombre al logger, pero para una mejor
	 * legibilidad se omite. */
	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {

		log.debug("Testing Log4j 2");

		/* Antes necesitabamos verificar el nivel de registro para aumentar el rendimiento:
		 * if (log.isDebugEnabled()) log.debug("{}", getNumber());
		 * 
		 * Ahora con Java 8 puede lograr el mismo efecto con una expresion lambda. Ya no es necesario verificar explicitamente
		 * el nivel de registro: */
		log.debug("{}", () -> getNumber());

		new Prueba();

	}

	static int getNumber() {
		return 5;
	}

}
