package colecciones;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Las colas en java cumplen con la regla FIFO (First In First Out), primero en entrar primero en salir.
 * 
 * 
 * @author Ru$o
 * 
 */

public class Queue_ {

	public static void main(String[] args) {

		Queue<String> quemados = new LinkedList<>();

		// quemados.offer("jose");

		quemados.add("rulo");
		quemados.add("ruso");
		quemados.add("ale");
		quemados.add("manolo");

		// Muestra al primer quemado
		System.out.println(quemados.peek());

		// Elimina al primer quemado
		quemados.remove();

		Iterator<String> it = quemados.iterator();

		while (it.hasNext())
			System.out.println("Quemado: " + it.next());
		
		String s = quemados.poll();

		// El metodo poll() no lanza una excepcion si la cola esta vacia
		System.out.println(s);

		System.out.println(quemados);

	}

}
