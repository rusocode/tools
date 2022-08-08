package _lab;

import java.util.ArrayList;

/**
 * A partir de la plataforma Java 2 v1.2, esta clase se actualizo para implementar la interfaz List, convirtiendola en
 * miembro de Java Collections Framework. A diferencia de las implementaciones de la nueva coleccion, Vector esta
 * sincronizado. Si no se necesita una implementacion segura para subprocesos, se recomienda utilizar ArrayList en lugar
 * de Vector.
 * 
 * Aunque esto es cierto, la clase Vector actualmente esta obsolota y en desuso.
 * https://stackoverflow.com/questions/1386275/why-is-java-vector-and-stack-class-considered-obsolete-or-deprecated
 * 
 */

public class Vector_ implements Runnable {

	static ArrayList<Thread> arrayList = new ArrayList<>();

	public Vector_() throws InterruptedException {
		Thread h1 = new Thread(this, "Hilo 1");
		Thread h2 = new Thread(this, "Hilo 2");

		h1.start();
		arrayList.add(h1);
		// h1.join();

		h2.start();

		arrayList.add(h2);

	}

	@Override
	public void run() {
		System.out.println("Se inicio el " + Thread.currentThread().getName());
	}

	public static void main(String[] args) throws InterruptedException {

		new Vector_();

		// Thread.sleep(1000);

		for (Thread a : arrayList) {
			System.out.println(a.getName());
		}

	}

}
