package multiprocesos;

/**
 * Una condición de carrera es un problema de concurrencia que puede ocurrir dentro de una sección crítica. Una sección
 * crítica es una sección de código que es ejecutada por múltiples subprocesos y donde la secuencia de ejecución de los
 * subprocesos marca una diferencia en el resultado de la ejecución concurrente de la sección crítica.
 * 
 * Cuando el resultado de varios subprocesos que ejecutan una sección crítica puede diferir según la secuencia en la que
 * se ejecutan los subprocesos, se dice que la sección crítica contiene una condición de carrera. El término condición
 * de carrera proviene de la metáfora de que los hilos corren a través de la sección crítica y que el resultado de esa
 * carrera impacta el resultado de ejecutar la sección crítica.
 * 
 * Las condiciones de carrera pueden ocurrir cuando dos o más subprocesos leen y escriben la misma variable de acuerdo
 * con uno de estos dos patrones:
 * 
 * ° Leer-modificar-escribir
 * ° Verificar y luego actuar
 * 
 */

public class CondicionDeCarreraYSeccionCritica {

	protected int count;

	public static void main(String[] args) throws InterruptedException {

		CondicionDeCarreraYSeccionCritica principal = new CondicionDeCarreraYSeccionCritica();

		/* Los subprocesos A y B ejecutan el metodo add() en la misma instancia de la clase principal
		 * (CondicionDeCarreraYSeccionCritica). */
		Thread A = new Thread(new Hilo(principal, 3)); // Hilo A
		Thread B = new Thread(new Hilo(principal, 2)); // Hilo B

		A.start();
		B.start();

	}

	/* "Seccion critica" de lectura-modificacion-escritura que puede fallar si se ejecuta por varios subprocesos
	 * simultaneamente, siendo esto una condicion de carrera.
	 * 
	 * Para evitar que ocurran condiciones de carrera, debe asegurarse de que la seccion critica se ejecute como una
	 * instruccion atomica. Eso significa que una vez que un solo subproceso lo esta ejecutando, ningun otro subproceso
	 * puede ejecutarlo hasta que el primer subproceso haya abandonado la seccion critica.
	 * 
	 * Las condiciones de carrera pueden evitarse mediante una sincronizacion de subprocesos adecuada en las secciones
	 * criticas. */
	private void add(int value) {
		count += value;
	}

	private int getCount() {
		return count;
	}

	private static class Hilo implements Runnable {

		CondicionDeCarreraYSeccionCritica principal;
		int value;

		public Hilo(CondicionDeCarreraYSeccionCritica principal, int value) {
			this.principal = principal;
			this.value = value;
		}

		@Override
		public void run() {
			principal.add(value);
			System.out.println(principal.getCount());

		}

	}

}
