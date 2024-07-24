package concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * La logica de este programa es que el banco debe tener una cantidad fija de dinero, por lo tanto
 * se deben sincronizar las cuentas para que ese total se mantega fijo y no se pierda dinero en
 * las transferencias.
 * 
 * Explicacion de la programacion concurrente:
 * https://www.youtube.com/watch?v=SajW49zuzXg&list=PLU8oAlHdN5BktAXdEVCLUYzvDyqRQJ2lk&index=175
 * En conclusion, si se da el caso de que varios hilos accedan a la misma cuenta para hacer una transferencia, entonces
 * se volatizaria (perder) una parte del dinero. Esto es lo que sucede si los hilos no se sincronizan.
 * Una posible solucion seria utilizando el metodo join(), pero se complicaria ya que en este caso son muchos los hilos
 * en ejecucion.
 * Aplicando la clase ReentrantLock se solucionaria el problema de sincronizacion, o utilizando la palabra synchronized
 * junto a los metodos wait() y notifyAll() tambien, siendo esta ultima una opcion mas sencilla.
 * 
 * @author Juan Debenedetti aka Ru$o
 * 
 */

public class Banco {

	private final double[] CUENTAS = new double[5];
	private ReentrantLock lock = new ReentrantLock();
	private Condition saldoSuficiente;

	public Banco() {
		for (int i = 0; i < CUENTAS.length; i++)
			CUENTAS[i] = 2000;

		saldoSuficiente = lock.newCondition();

	}

	private void transferir(int origen, int destino, double cantidad) throws InterruptedException {

		// Adquiere el bloqueo para el hilo actual
		lock.lock();

		try {

			if (origen == destino) {
				System.err.println("La cuenta de origen es la misma que la de destino!");
				return;
			}

			/* Mientras el saldo de la cuenta origen sea menor a la cantidad especificada, entonces pone a la espera al hilo
			 * hasta que otro hilo lo despierte, evitando asi, que queden hilos muertos (return).
			 * Por ejemplo, tenemos la cuenta 25 con un saldo de 100 y decide hacer una trasnferencia de 500 a la cuenta 10, esto no
			 * seria posible. Entonces con la interfaz Condition, se establece una condicion para este caso, poniendo a la espera
			 * al hilo 25 de que otro hilo, por ejemplo el 7, haga una trasferencia de 1000 al hilo 25. El hilo 25 ya
			 * tendria un total de 1100, por lo tanto como la condicion se cumple, el hilo 7 informa al hilo 25 que ya
			 * puede realizar la transferencia.
			 * 
			 * Es importante utilizar un bucle while para comprobar si se cumple o no la condicion luego de que otro hilo
			 * haya despertado a los que estaban en espera. Es decir, que vuelve a comprobar si la condicion es verdadera o no para
			 * evitar problemas. */
			while (CUENTAS[origen] < cantidad) {
				System.err.printf("%s bloqueado por que tiene un saldo de %.2f$ y se necesitan %.2f$!%n", MyThread.currentThread().getName(),
						CUENTAS[origen], cantidad);
				saldoSuficiente.await(); // Hace que el hilo actual espere hasta que sea señalado por otro
			}

			// Muestra el hilo actual
			System.out.println("[" + MyThread.currentThread().getName() + "]");

			CUENTAS[origen] -= cantidad;
			CUENTAS[destino] += cantidad;

			System.out.printf("Se transfirio %.2f$ de la cuenta %d a la %d!%n", cantidad, origen, destino);
			System.out.printf("Saldo en la cuenta %d: %.2f$%n", origen, CUENTAS[origen]);
			System.out.printf("Saldo en la cuenta %d: %.2f$%n", destino, CUENTAS[destino]);
			System.out.printf("Saldo total: %.2f$%n", getSaldoTotal());

			// Notifica a todos los hilos en espera a que ya puedan realizar la transferencia
			saldoSuficiente.signalAll();

		} finally {
			// Libera el bloqueo una vez terminada la ejecucion, dependiendo o no de algun fallo
			lock.unlock();
		}
	}

	/* La diferencia con la clase ReentrantLock, es que synchronized solo utiliza una condicion para los hilos. */
	private synchronized void transferir2(int origen, int destino, double cantidad) throws InterruptedException {

		if (origen == destino) {
			System.err.println("La cuenta de origen es la misma que la de destino!");
			return;
		}

		while (CUENTAS[origen] < cantidad) {
			System.err.printf("%s bloqueado por que tiene un saldo de %.2f$ y se necesitan %.2f$!%n", MyThread.currentThread().getName(),
					CUENTAS[origen], cantidad);
			wait();
		}

		System.out.println("[" + MyThread.currentThread().getName() + "]");

		CUENTAS[origen] -= cantidad;
		CUENTAS[destino] += cantidad;

		System.out.printf("Se transfirio %.2f$ de la cuenta %d a la %d!%n", cantidad, origen, destino);
		System.out.printf("Saldo en la cuenta %d: %.2f$%n", origen, CUENTAS[origen]);
		System.out.printf("Saldo en la cuenta %d: %.2f$%n", destino, CUENTAS[destino]);
		System.out.printf("Saldo total: %.2f$%n", getSaldoTotal());

		notifyAll();

	}

	private double getSaldoTotal() {
		double total = 0;
		for (double cuenta : CUENTAS)
			total += cuenta;
		return total;
	}

	private static class Hilo implements Runnable {

		Banco banco;
		int origen;
		double cantidadMax;

		public Hilo(Banco banco, int origen, double cantidadMax) {
			this.banco = banco;
			this.origen = origen;
			this.cantidadMax = cantidadMax;
		}

		@Override
		public void run() {
			while (true) {
				int destino = (int) (banco.CUENTAS.length * Math.random());
				double cantidad = cantidadMax * Math.random();
				try {
					banco.transferir2(origen, destino, cantidad);
					MyThread.sleep(250);
				} catch (InterruptedException e) {
				}
			}
		}

	}

	public static void main(String[] args) {

		Banco banco = new Banco();

		// Crea un hilo para cada cuenta usando la clase Hilo
		for (int i = 0; i < banco.CUENTAS.length; i++)
			new Thread(new Hilo(banco, i, 2000)).start();

	}

}
