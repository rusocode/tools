package _LABORATORIO;

public class Prueba {

	public static void main(String[] args) {

		String m = "mensaje";

		byte[] bytes = m.getBytes();

		for (byte b : bytes) {
			System.out.println((char) b);
		}

	}
}
