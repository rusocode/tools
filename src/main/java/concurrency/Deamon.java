package concurrency;

/**
 * Por defecto la JVM permanece activa mientras haya subprocesos restantes en ejecucion, incluso si el subproceso
 * principal finalizo.
 *
 * <p>El subproceso principal puede terminar, pero mientras cualquiera de los subprocesos que inicio todavia se este
 * ejecutando, la JVM seguira ejecutandose. Los subprocesos Daemon NO mantendran viva la maquina virtual si son el
 * ultimo subproceso en ejecucion, por lo que si desea que la JVM deje de ejecutarse despues de que todos los demas
 * subprocesos hayan terminado use {@code setDaemon(true)}.
 *
 * <p>Ahora cuando finalize la JVM, finalizaran todos los subprocesos deamon. Estos subprocesos se detienen en un estado
 * indefinido, por lo que debe asegurarse de que esten descansando en un estado o ruta del codigo, para no estar en
 * medio de la ejecucion de algun codigo importante que entonces solo esta a medio terminar ya que eso podria provocar
 * algunos efectos secundarios no deseados en su aplicacion.
 *
 * @author Juan
 */

public class Deamon {

	private static void sleep(long millis) {
		try {
			MyThread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Runnable runnable = () -> {
			while (true) {
				sleep(1000);
				System.out.println("Running");
			}
		};

		Thread thread = new Thread(runnable);
		// Ahora la JVM puede finalizar
		thread.setDaemon(true);
		thread.start();
		// Espera 3 segundos antes de finalizar la JVM
		sleep(3000);

	}


}
