package properties;

import java.io.*;
import java.util.Properties;

import static util.Constants.CONFIG;

public class Props {

	public static void main(String[] args) {

		Protocol protocol = null;
		Properties properties = new Properties();

		try {

			// Crea un flujo de entrada para el archivo
			FileInputStream in = new FileInputStream(new File(CONFIG));
			// Carga las claves y valores del archivo
			properties.load(in);
			// Obtiene el valor de la clave especificada
			String protocolClassName = properties.getProperty("server.protocol");
			String serverPort = properties.getProperty("server.port");
			// Crea una nueva instancia de la clase representada por el objeto Class
			protocol = (Protocol) Class.forName(protocolClassName).getDeclaredConstructor().newInstance();

			System.out.println("Server port: " + serverPort + "\nProtocol: " + protocol);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
