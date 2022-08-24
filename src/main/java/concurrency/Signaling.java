package concurrency;

/**
 * El proposito de la señalizacion de subprocesos es permitir que los subprocesos se envien señales entre si. Ademas, la
 * señalizacion de subprocesos permite que los subprocesos esperen señales de otros subprocesos. Por ejemplo, un
 * subproceso B podria esperar una señal del subproceso A que indique que los datos estan listos para ser procesados.
 *
 * <p>Recursos:
 * <a href="https://jenkov.com/tutorials/java-concurrency/thread-signaling.html">Thread Signaling</a>
 */

public class Signaling {

	/**
	 * -wait(), notify() and notifyAll()
	 * Java tiene un mecanismo de espera incorporado que permite que los subprocesos se vuelvan inactivos mientras esperan
	 * señales. La clase java.lang.Object define tres metodos, wait(), notify() y notifyAll(), para facilitar esto.
	 * <p>
	 * Un subproceso que llama a wait() en cualquier objeto se vuelve inactivo hasta que otro subproceso llama a notify() en
	 * ese objeto. Para llamar a wait() o notify(), el subproceso de llamada primero debe obtener el bloqueo en ese objeto.
	 * En otras palabras, el subproceso de llamada debe llamar a wait() o notify() desde dentro de un bloque sincronizado.
	 */

	private static class MonitorObject {

	}

	private static class MyWaitNotify {
		final MonitorObject myMonitorObject = new MonitorObject();

		public void doWait() {
			synchronized (myMonitorObject) {
				try {
					myMonitorObject.wait();
				} catch (InterruptedException e) {

				}
			}
		}

		public void doNotify() {
			synchronized (myMonitorObject) {
				myMonitorObject.notify();
			}
		}
	}

	/* -wait(), notify() and notifyAll()
	 * El hilo de espera llamaria a doWait(), y el hilo de notificacion llamaria a doNotify(). Cuando un subproceso llama a
	 * notify() en un objeto, uno de los subprocesos que espera en ese objeto se despierta y se le permite ejecutar. Tambien
	 * hay un metodo de notifyAll() que activara todos los subprocesos que esperan en un objeto determinado.
	 *
	 * Como puede ver, tanto el hilo de espera como el de notificacion llaman a wait() y notify() desde dentro de un bloque
	 * sincronizado. ¡Esto es obligatorio! Un subproceso no puede llamar a wait(), notify() o notifyAll() sin mantener el
	 * bloqueo en el objeto al que se llama el metodo. Si lo hace, se lanza una IllegalMonitorStateException.
	 *
	 * ¿Pero, como es esto posible? ¿El subproceso en espera no mantendria el bloqueo en el objeto del monitor (myMonitorObject)
	 * siempre que se este ejecutando dentro de un bloque sincronizado? ¿El subproceso en espera no bloqueara el subproceso de
	 * notificacion para que nunca ingrese al bloque sincronizado en doNotify()? La respuesta es no. Una vez que un subproceso
	 * llama a wait(), libera el bloqueo que mantiene en el objeto del monitor. Esto permite que otros subprocesos tambien
	 * llamen a wait() o notify(), ya que estos metodos deben llamarse desde dentro de un bloque sincronizado.
	 *
	 * Una vez que se despierta un subproceso, no puede salir de la llamada a wait() hasta que el subproceso que llama a
	 * notify() haya dejado su bloque sincronizado. En otras palabras: el subproceso despertado debe volver a obtener el
	 * bloqueo en el objeto del monitor antes de que pueda salir de la llamada de wait(), porque la llamada de espera esta
	 * anidada dentro de un bloque sincronizado. Si se activan varios subprocesos mediante el metodo de notifyAll(), solo
	 * un subproceso activado a la vez puede salir del metodo wait(), ya que cada subproceso debe obtener el bloqueo en
	 * el objeto del monitor antes de salir de wait(). */

	/* -Señales perdidas
	 * Los metodos notify() y notifyAll() no guardan las llamadas al metodo en caso de que no haya subprocesos esperando
	 * cuando se les llama. Entonces, la señal de notificacion simplemente se pierde. Por lo tanto, si un subproceso llama
	 * a notify() antes de que el subproceso para señalar haya llamado a wait(), el subproceso en espera perdera la señal.
	 * Esto puede o no ser un problema, pero en algunos casos esto puede resultar en que el subproceso en espera espere
	 * para siempre, sin despertarse nunca, porque se perdio la señal para despertar.
	 *
	 * Para evitar perder señales, deben almacenarse dentro de la clase de señal. En el ejemplo de MyWaitNotify, la señal
	 * de notificacion debe almacenarse en una variable miembro dentro de la instancia de MyWaitNotify. Aqui hay una
	 * version modificada de MyWaitNotify que hace esto: */

	private static class MyWaitNotify2 {

		final MonitorObject myMonitorObject = new MonitorObject();
		boolean wasSignalled;

		public void doWait() {
			synchronized (myMonitorObject) {
				if (!wasSignalled) {
					try {
						myMonitorObject.wait();
					} catch (InterruptedException e) {
					}
				}
				// Limpia la señal y continua con la ejecucion
				wasSignalled = false;
			}
		}

		public void doNotify() {
			synchronized (myMonitorObject) {
				wasSignalled = true;
				myMonitorObject.notify();
			}
		}
	}

	/* -Señales perdidas
	 * Observe como el metodo doNotify() ahora establece la variable wasSignalled en verdadero antes de llamar a notify().
	 * Ademas, observe como el metodo doWait() ahora verifica la variable wasSignalled antes de llamar a wait(). De hecho,
	 * solo llama a wait() si no se recibio ninguna señal entre la llamada anterior a doWait() y esta. */

	/* -Activaciones espurias
	 * Por razones inexplicables, es posible que los subprocesos se despierten incluso si no se ha llamado a notify() y
	 * notifyAll(). Esto se conoce como despertares espurios sin ningun motivo.
	 *
	 * Si se produce una activacion falsa en el metodo doWait() de la clase MyWaitNofity2, el subproceso en espera puede
	 * continuar procesando sin haber recibido una señal adecuada para hacerlo. Esto podria causar serios problemas en su
	 * aplicacion.
	 *
	 * Para protegerse contra despertares espurios, la variable miembro de la señal se verifica dentro de un ciclo while
	 * en lugar de dentro de una declaracion if. Este bucle while tambien se denomina spin lock. El subproceso despertado
	 * gira hasta que la condicion en el bloqueo de giro (bucle while) se vuelve falsa. Aqui hay una version modificada
	 * de MyWaitNotify2 que muestra esto: */

	private static class MyWaitNotify3 {

		final MonitorObject myMonitorObject = new MonitorObject();
		boolean wasSignalled;

		public void doWait() {
			synchronized (myMonitorObject) {
				while (!wasSignalled) {
					try {
						myMonitorObject.wait();
					} catch (InterruptedException e) {
					}
				}
				// Limpia la señal y continua con la ejecucion
				wasSignalled = false;
			}
		}

		public void doNotify() {
			synchronized (myMonitorObject) {
				wasSignalled = true;
				myMonitorObject.notify();
			}
		}
	}

	/* Observe como la llamada wait() ahora esta anidada dentro de un bucle while en lugar de una declaracion if. Si el
	 * subproceso en espera se despierta sin haber recibido una señal, el miembro wasSignalled seguira siendo falso y
	 * el ciclo while se ejecutara una vez mas, lo que hara que el subproceso despierto vuelva a esperar. */

}
