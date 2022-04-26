package concurrency;

/**
 * 
 * https://www.javatpoint.com/mvc-architecture-in-java
 */

public class RaceConditionDemo {

	public static void main(String args[]) {

		Counter2 counter = new Counter2();

		Thread t1 = new Thread(counter, "Thread-1");
		Thread t2 = new Thread(counter, "Thread-2");
		Thread t3 = new Thread(counter, "Thread-3");

		t1.start();
		t2.start();
		t3.start();
	}

}

class Counter2 implements Runnable {

	private int c;

	public void increment() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c++;
	}

	public void decrement() {
		c--;
	}

	public int getValue() {
		return c;
	}

	@Override
	public void run() {

		increment();
		System.out.println("Valor de " + Thread.currentThread().getName() + " despues del incremento " + this.getValue());

		decrement();
		System.out.println("Valor de " + Thread.currentThread().getName() + " al final " + this.getValue());

	}
}
