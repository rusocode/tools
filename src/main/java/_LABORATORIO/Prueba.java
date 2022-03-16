package _LABORATORIO;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Prueba {

	static boolean bandera = true;

	public static void main(String[] args) throws UnknownHostException {
		salir();
	}

	static void salir() {
		try {

			if (bandera) return;
			
			System.out.println("asd");
			
		} catch (Exception e) {

		} finally {
			System.out.println("asd");
		}
	}

}