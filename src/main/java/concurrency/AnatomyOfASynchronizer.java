package concurrency;

/**
 * Incluso si muchos sincronizadores (bloqueos, semáforos, cola de bloqueo, etc.) tienen una función diferente, a menudo
 * no son tan diferentes en su diseño interno. En otras palabras, constan de las mismas (o similares) partes básicas
 * internamente. Conocer estas partes básicas puede ser de gran ayuda a la hora de diseñar sincronizadores. Son estas
 * partes las que examina este texto más de cerca.
 * 
 * El propósito de la mayoría (si no de todos) los sincronizadores es proteger alguna área del código (sección crítica)
 * del acceso concurrente por subprocesos. Para hacer esto, a menudo se necesitan las siguientes partes en un
 * sincronizador:
 * 
 * 1. State
 * 2. Access Condition
 * 3. State Changes
 * 4. Notification Strategy
 * 5. Test and Set Method
 * 6. Set Method
 * 
 * No todos los sincronizadores tienen todas estas partes, y es posible que aquellos que las tengan no las tengan
 * exactamente como se describen aquí. Sin embargo, normalmente puede encontrar una o más de estas piezas.
 * 
 * Fuentes:
 * http://tutorials.jenkov.com/java-concurrency/anatomy-of-a-synchronizer.html
 * 
 * @author Ru$o
 * 
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
