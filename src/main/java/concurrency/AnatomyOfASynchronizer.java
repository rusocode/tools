package concurrency;

/**
 * Incluso si muchos sincronizadores (bloqueos, semaforos, cola de bloqueo, etc.) tienen una funcion diferente, a menudo
 * no son tan diferentes en su diseño interno. En otras palabras, constan de las mismas (o similares) partes basicas
 * internamente. Conocer estas partes basicas puede ser de gran ayuda a la hora de diseñar sincronizadores. Son estas
 * partes las que examina este texto mas de cerca.
 * <p>
 * El proposito de la mayoria (si no de todos) los sincronizadores es proteger alguna area del codigo (seccion critica)
 * del acceso concurrente por subprocesos. Para hacer esto, a menudo se necesitan las siguientes partes en un
 * sincronizador:
 * <ol>
 * 		<li>State</li>
 *  	<li>Access Condition</li>
 * 		<li>State Changes</li>
 * 		<li>Notification Strategy</li>
 * 		<li>Test and Set Method</li>
 * 		<li>Set Method</li>
 * </ol>
 * No todos los sincronizadores tienen todas estas partes, y es posible que aquellos que las tengan no las tengan
 * exactamente como se describen aqui. Sin embargo, normalmente puede encontrar una o mas de estas piezas.
 * <br><br>
 * Fuentes:
 * <a href="http://tutorials.jenkov.com/java-concurrency/anatomy-of-a-synchronizer.html">Anatomy of a Synchronizer</a>
 *
 * @author Ruso
 */

public class AnatomyOfASynchronizer {

	// State
	private boolean isLocked = false;

	public synchronized void lock() throws InterruptedException {

		// Access condition
		while (isLocked) {
			// Wait strategy - relacionado con la estrategia de notificacion
			wait();
		}

		// State change
		isLocked = true;
	}

	// Set method
	public synchronized void unlock() {
		// State change
		isLocked = false;
		// Notification strategy
		notify();
	}

}
