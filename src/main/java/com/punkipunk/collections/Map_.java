package com.punkipunk.collections;

import java.util.*;
import java.util.Map.Entry;

/**
 * La interfaz Map asocia claves a valores. Esta interfaz no puede contener claves duplicadas y; cada una de dichas
 * claves, solo puede tener asociado un valor como maximo.
 * 
 * Dentro de la interfaz Map existen varios tipos de implementaciones realizadas dentro de la plataforma Java. Vamos a
 * analizar cada una de ellas: HashMap, TreeMap y LinkedHashMap.
 * 
 * Ninguna de estas implementaciones son sincronizadas; es decir, no se garantiza el estado del Map si dos o mas hilos
 * acceden de forma concurrente al mismo. Esto se puede solucionar empleando una serie de metodos que actuan de wrapper
 * para dotar a estas colecciones de esta falta de sincronizacion:
 * 
 * Map map = Collections.synchronizedMap(new HashMap());
 * SortedMap mortedMap = Collections.synchronizedSortedMap(new TreeMap());
 * Map map = Collections.synchronizedMap(new LinkedHashMap());
 * 
 * El cuando usar una implementacion u otra de Map variara en funcion de la situacion en la que nos encontremos.
 * Generalmente, HashMap sera la implementacion que usemos en la mayoria de situaciones. HashMap es la implementacion
 * con mejor rendimiento (como se ha podido comprobar en el analisis de Set), pero en algunas ocasiones podemos decidir
 * renunciar a este rendimiento a favor de cierta funcionalidad como la ordenacion de sus elementos.
 * 
 * -La documentacion de ConcurrentModificationException dice:
 * "Tenga en cuenta que esta excepcion no siempre indica que un objeto ha sido modificado simultaneamente por un
 * subproceso diferente. Si un solo hilo emite una secuencia de invocaciones de metodos que viola el contrato de un
 * objeto, el objeto puede lanzar esta excepcion. Por ejemplo, si un hilo modifica una coleccion directamente mientras
 * esta iterando sobre la coleccion con un iterador a prueba de fallas, el iterador lanzara esta excepcion."
 * https://stackoverflow.com/questions/602636/why-is-a-concurrentmodificationexception-thrown-and-how-to-debug-it
 * 
 */

public class Map_ {

	private Map<String, Integer> map;

	public Map_() {
		map = new HashMap<>();
	}

	/** Agrega la clave y valor a la coleccion. */
	private void put(String k, Integer v) {
		map.put(k, v);
	}

	/** Muestra las claves de la coleccion. */
	private void getKeys() {
		System.out.print("Keys: ");
		for (String k : map.keySet())
			System.out.print(k + ", ");
	}

	/** Muestra los valores de la coleccion. */
	private void getValues() {
		System.out.print("Values: ");
		for (Integer v : map.values())
			System.out.print(v + ", ");
	}

	/** Muestra el conjunto de las asignaciones contenidas en la coleccion. */
	private void entrySet() {
		/* Map.Entry es la clase anidada de la interfaz Map. El metodo entrySet() devuelve una vista establecida de todas las
		 * entradas. La clase Entry nos permite almacenar e imprimir cada entrada de la vista utilizando los metodos getKey() y
		 * getValue(). */
		for (Entry<String, Integer> entry : map.entrySet())
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	}

	/**
	 * Arreglando la excepcion ConcurrentModificationException usando un Iterator generico.
	 * 
	 * Esta tecnica tiene la caracteristica de que es el unico metodo que permite remover entradas del mapa durante la
	 * iteracion usando iterator.remove(). Si intentas hacer esto con una iteracion for-each obtendras "resultados
	 * inpredecibles" de acuerdo con JavaDoc.
	 * 
	 * Desde el punto de vista del performance, esto es lo mismo que una iteracion for-each.
	 */
	private void remove(String k) {

		// Devuelve un iterador sobre los elementos del conjunto y se lo pasa a un Iterator
		Iterator<Entry<String, Integer>> it = map.entrySet().iterator();

		// Itera cada entrada y la alamacena en un Entry para poder manipular el conjuto
		while (it.hasNext()) {

			Entry<String, Integer> entry = it.next();

			if (entry.getKey().equals(k)) {
				it.remove();
				System.out.println("Se elimino a " + k);
			}

			// Causara un ConcurrentModificationException si se modifica la coleccion desde la misma y no desde el Iterador
			// if (item.getKey().equals(k)) map.remove(item.getKey());
		}
	}

	public static void main(String[] args) {

		Map_ map = new Map_();

		map.put("Juan", 27);
		// map.put("Rulo", 25);

		/* Estos metodos dan una leve ventaja en performance sobre la iteracion entrySet (cerca de un 10% mas rapidos) y son mas
		 * limpios. */
		// map.getKeys();
		// map.getValues();

		map.entrySet();
		map.remove("Juan");
		System.out.println("Coleccion actualizada...");
		map.entrySet();

	}

}
