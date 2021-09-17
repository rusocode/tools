package log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Test {
	private static final Logger log = LogManager.getLogger();

	public Test() {
		log.info("Estoy en la clase test");
	}

}
