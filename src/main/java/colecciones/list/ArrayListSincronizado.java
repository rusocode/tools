package colecciones.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Formas de sincronizar una coleccion de tipo list en java:
 * 1. List list = Collections.synchronizedList(new ArrayList());
 * Esto devuelve una lista sincronizada (segura para subprocesos) respaldada por la lista especificada.
 * 2. Usando la clase CopyOnWriteArrayList que es una variante segura para subprocesos sobre ArrayList, en donde se
 * sincronizan los objetos de forma predetermianada.
 * 
 * ¿Por que tiene el nombre raro de CopyOnWriteArrayList?
 * Siempre que se realiza una operacion de modificacion en ArrayList, la matriz existente se copia internamente, la
 * operacion de modificacion se realiza en la nueva copia y luego se devuelve la nueva matriz. La matriz anterior se
 * descartara. De ahi el nombre, Copy on write (copiar al escribir). Las operaciones de modificacion incluyen add,
 * addAll, remove, removeAll, addIf, removeIf, subList, etc.
 * 
 * En conclusion es mucho mas eficiente usar la clase CopyOnWriteArrayList, ya que utiliza varios bloqueos para cada
 * subproceso y no uno solo como en el caso de la implementacion del metodo Collections.synchronizedList().
 * Tomando como ejemplo los baños quimicos de un evento al aire libre, podriamos decir que cada baño es un bloqueo
 * y el subproceso es la gente que esta en la fila para entrar a un baño. Si la fila es muy larga, entonces podria
 * ir al otro baño. Esto es lo que hace el paquete java.util.concurrent. Utiliza varios candados para distintos
 * subprocesos, haciendolo mucho mas eficiente y sincronizado.
 * Para mas detalles: https://www.youtube.com/watch?v=RoQvj3MJgNQ
 * 
 * Otras fuentes de informacion interesantes:
 * https://levelup.gitconnected.com/how-to-use-java-collections-safely-in-multi-threaded-environments-83c94be57ab8
 * https://stackoverflow.com/questions/1386275/why-is-java-vector-and-stack-class-considered-obsolete-or-deprecated
 * https://stackoverflow.com/questions/2986296/what-are-the-differences-between-arraylist-and-vector
 * 
 * @author Ru$o
 * 
 */

public class ArrayListSincronizado {

	public static void main(String[] args) {

		// 1.
		List<String> listaSincronizada = Collections.synchronizedList(new ArrayList<>());

		listaSincronizada.add("Java");
		listaSincronizada.add("C++");
		listaSincronizada.add("Python");

		// add, remove - No necesitan sincronizacion explicita

		/* Para obtener los valores de la lista, tenemos que usar la sincronizacion explicita.
		 * Esto se puede hacer sin ningun problema sin el bloque sincronizado, pero no con multiples subprocesos. */
		synchronized (listaSincronizada) {

			Iterator<String> it = listaSincronizada.iterator();

			while (it.hasNext())
				System.out.println(it.next());

		}

		// 2. Thread-safe/synchronized
		CopyOnWriteArrayList<String> listaSincronizada2 = new CopyOnWriteArrayList<>();
		listaSincronizada2.add("Juan");
		listaSincronizada2.add("Rulo");
		listaSincronizada2.add("Manolo");

		// No necesitamos una sincronizacion explicita para las operaciones: add, remove o fetch, etc.
		Iterator<String> it = listaSincronizada2.iterator();
		while (it.hasNext())
			System.out.println(it.next());

	}

}
