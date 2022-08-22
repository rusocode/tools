package _lab;

/**
 * Muestra la cantidad de ticks (ciclos de relojs) que puede ejecutar el procesador en 1 segundo.
 * <p> Recursos: <a href="https://en.wikipedia.org/wiki/Elapsed_real_time">Tiempo del reloj de pared</a>
 */

public class Ticks {

	public static void main(String[] args) {

		int c = 0;
		long lastTime = System.nanoTime();

		while (true) {
			c++;
			if (System.nanoTime() - lastTime > 1_000_000_000) {
				lastTime += 1_000_000_000;
				System.out.println(c + " ticks en un segundo!");
				c = 0;
			}
		}
	}

}
