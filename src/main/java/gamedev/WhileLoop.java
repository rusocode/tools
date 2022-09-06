package gamedev;

/**
 * En el cilo while, la CPU va a tratar de cumplir el ciclo a la mayor velocidad posible, aunque esa velocidad puede ser
 * afectada por algunos factores principales.
 *
 * <p>La cantidad de veces que se ejecute el ciclo while en un segundo depende del codigo maquina en el que se compile
 * el codigo fuente, del porcentaje de uso del CPU que este ocurriendo durante el ciclo, de la velocidad del procesador
 * y de como se ejecute el ciclo.
 *
 * <p>En caso de querer regular la velocidad del ciclo, se usaria un {@code Thread.sleep()} especificando un periodo muy
 * corto de tiempo en milisegundos (por ejemplo, 2ms).
 *
 * <p>El ciclo se ejecuta mucho mas rapido desde el metodo {@link WhileLoop#whileLoopPerSec2()} ya que el uso de
 * subprocesos es costoso y requieren mas tiempo.
 *
 * <p>Nota: Nunca confie en la velocidad del ciclo para el tiempo. Si necesita cronometraje, use temporizadores
 * (timer).
 *
 * <p>Recursos:
 * <a href="https://www.quora.com/What-is-the-amount-of-times-that-a-while-loop-runs-per-second-in-C">¿Cual es la cantidad de veces que se ejecuta un ciclo while() por segundo en C#?</a>
 *
 * @author Juan Debenedetti
 */

public class WhileLoop implements Runnable {

	private int c;
	private boolean stopped;

	@Override
	public void run() {
		while (!isStopped()) c++;
	}

	private synchronized void stop() {
		stopped = true;
	}

	private synchronized boolean isStopped() {
		return stopped;
	}

	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Muestra la cantidad de veces que se ejecuta el ciclo while por segundo usando un subproceso.
	 */
	private void whileLoopPerSec1() {
		new Thread(this).start();
		sleep();
		System.out.println(c + " ejecuciones en un segundo!");
		stop();
	}

	/**
	 * Muestra la cantidad de veces que se ejecuta el ciclo while por segundo usando {@code currentTimeMillis()} para
	 * condicionar el tiempo cada 1 segundo.
	 *
	 * <p>Nota: El uso de {@code nanoTime()} es mas costoso (mas de 100 relojes de CPU) pero mas preciso, por lo tanto
	 * el ciclo hara menos ejecuciones.
	 */
	private static void whileLoopPerSec2() {
		int c = 0;
		long startTime = System.currentTimeMillis();
		while (true) {
			c++;
			if (System.currentTimeMillis() - startTime >= 1000) {
				startTime += 1000;
				System.out.println(c + " ejecuciones en un segundo!");
				c = 0;
			}
		}
	}

	public static void main(String[] args) {
		// new WhileLoop().whileLoopPerSec1();
		whileLoopPerSec2();
	}

}
