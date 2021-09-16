package log;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Log4j2 {

	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {

		log.debug("Hello from Log4j 2");

		/* Antes necesitabamos verificar el nivel de registro para aumentar el rendimiento:
		 * if (log.isDebugEnabled()) log.debug("{}", getNumber());
		 * 
		 * Ahora con Java 8, podemos hacer esto y no es necesario verificar el nivel de registro. */
		log.debug("{}", () -> getNumber());

	}

	static int getNumber() {
		return 5;
	}

}
