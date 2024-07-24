package concurrency;

/**
 * A diferencia de la iteracion con for, en este ejemplos se utiliza un ciclo while para ejecutar y detener un
 * subproceso. Estos se hace utilizando una bandera (stopped) para indicar el estado de ejecucion. Mientras ningun
 * otro subproceso haya detenido la ejecucion, el subproceso actual sigue con su ejecucion hasta que se detenga.
 *
 * <p>A tener en cuenta...
 *
 * <p>La palabra clave <i>synchronized</i> detendra los subprocesos que ejecutan el metodo simultaneamente, pero si se
 * crea un segundo objeto nuevo, entonces dos subprocesos diferentes pueden llamar al metodo simultaneamente en cada una
 * de las instancias del objeto. Por lo tanto, synchronized solo funciona si varios subprocesos llaman al mismo objeto.
 *
 * <p>La forma nativa de detener un subproceso detendra un subproceso en un estado desconocido. Implemente un Runnable
 * que tenga una funcion de detencion en lugar de usar el metodo de detencion predeterminado. La funcion {@code stop()}
 * personalizada establece la variable stopped solicitada, luego el metodo {@code run()} puede seguir comprobando si se
 * ha solicitado o no una parada y terminar en sus propios terminos (guardando cualquier estado antes de detenerse).
 *
 * <p>Recursos:
 * <a href="https://www.youtube.com/watch?v=eQk5AWcTS8w">Detener subprocesos</a>
 *
 * @author Juan Debenedetti
 */

public class Stoppable {

	private static final int TIEMPO_BLOQUEADO = 1000;

	private static class Subproceso implements Runnable {

		private final Thread subproceso;
		private boolean stopped;

		public Subproceso(String name) {
			subproceso = new Thread(this, name);
		}

		@Override
		public void run() {
			System.out.println("Subproceso " + subproceso.getName() + " ejecutado");
			// Mientras el subproceso no este detenido
			while (!isStopped()) {
				sleep(TIEMPO_BLOQUEADO);
				System.out.println("...");
			}
			System.out.println("Subproceso " + subproceso.getName() + " detenido");
		}

		private synchronized void start() {
			subproceso.start();
		}

		public synchronized void stop() { // TODO Hace falta que lo sincronize?
			stopped = true;
		}

		private synchronized boolean isStopped() {
			return stopped;
		}

		private void sleep(long millis) {
			try {
				MyThread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {

		System.out.println("Subproceso " + MyThread.currentThread().getName() + " ejecutado");

		Subproceso A = new Subproceso("A");

		A.start();

		try {
			MyThread.sleep(5 * TIEMPO_BLOQUEADO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Deteniendo subproceso desde " + MyThread.currentThread().getName());
		A.stop();

	}

}