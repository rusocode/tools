package _LABORATORIO;

public class Prueba {

	public static void main(String[] args) {

		long startTime = System.nanoTime();

		int a = 0, b = 1, c = 1, d = 0, e = 1, f = 1;

		a = b + c;
		d = e + f;

		System.out.println(a);
		System.out.println(d);

		long endTime = System.nanoTime();
		System.out.println("\nDuracion: " + (endTime - startTime) / 1e6 + " ms");

	}

}
