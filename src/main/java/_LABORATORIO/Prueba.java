package _LABORATORIO;

public class Prueba {

	public static void main(String[] args) throws InterruptedException {

		long inicio = System.nanoTime();

		Thread.sleep(500);

		long fin = System.nanoTime();

		System.out.println((fin - inicio) / 1e6 + " ms");

	}
}
