package com.punkipunk.concurrency;

/**
 * Para hacer que el subproceso principal espere a otro subproceso, tiene que llamar al metodo {@code join()}. Ahora el
 * subproceso principal se bloqueara hasta que el subproceso actual finalice.
 *
 * <p>En caso contrario de utilizar el metodo {@code join()}, se tendria que bloquear al subproceso principal llamando a
 * la funcion {@code sleep()} para indicar cuanto tiempo permanecera bloqueada hasta que finalize el otro subproceso.
 */

public class Join {

	private static void sleep(long millis) {
		try {
			MyThread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Runnable runnable = () -> {
			for (int i = 0; i < 3; i++) {
				sleep(1000);
				System.out.println("Running");
			}
		};

		Thread thread = new Thread(runnable);
		// thread.setDaemon(true); // TODO Hace falta marcar el subproceso como deamon cuando se utiliza el metodo join()?
		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
