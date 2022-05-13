package nio;

import java.nio.ByteBuffer;

/**
 * IMPORTANTE: ver la clase Buffer explicada en el paquete io para entender mejor su funcionamiento.
 *
 * Un buffer en NIO es un contenedor para una cantidad fija de datos de un tipo primitivo especifico que se puede
 * escribir/leer en/desde un canal. A diferencia de la API IO que se maneja con bytes individuales, los buffers se
 * manejan con porciones de bytes.
 * Aparte de su contenido, las propiedades esenciales de un búffer son su capacidad, límite y posición:
 * ° La capacidad de un búffer es el número de elementos que puede contener. La capacidad de un búfer nunca es negativa
 * y nunca cambia.
 * ° El límite de un búffer es el índice del primer elemento que no debe leerse ni escribirse. El límite de un búffer
 * nunca es negativo y nunca es mayor que su capacidad.
 * ° La posición de un búffer es el índice del siguiente elemento a leer o escribir. La posición de un búffer nunca es
 * negativa y nunca supera su límite.
 *
 * Hay una subclase de esta clase para cada tipo primitivo no booleano.
 *
 * Un búffer recién creado siempre tiene una posición de cero y una marca que no está definida. El límite inicial puede
 * ser cero o puede ser algún otro valor que dependa del tipo de búffer y de la forma en que se construye. Cada elemento
 * de un búffer recién asignado se inicializa a cero.
 *
 * El siguiente invariante es válido para los valores de marca, posición, límite y capacidad:
 * 0 <= marca <= posición <= límite <= capacidad
 *
 * Los búfferes de bytes se pueden crear con el metodo allocate(), que asigna espacio para el contenido del búffer, o
 * encapsulando una matriz de bytes existente en un búffer.
 *
 * Existen los buffer directos y no directos. Los directos actuan directamente sobre el sistema operativo y se suponen
 * que son mas rapidos, y los no directos primero pasan por la memoria del JVM usando un buffer intermedio. ¿Entonces
 * por que no usar los buffers directos si se supone que son mas rapidos? Esto se debe a que se necesita mas tiempo
 * para crear un buffer directo.
 *
 * Para este caso se utiliza la clase ByteBuffer como ejemplo.
 *
 * Recursos
 * API JavaSE 8: <a href="https://docs.oracle.com/javase/8/docs/api/">...</a>
 * Grafico: <a href="https://github.com/rusocode/utilidades/blob/master/src/main/resources/tips/NIO%20byte%20buffer.PNG">...</a>
 *
 * @author Ruso
 */

public class Buffer_ {

	public static void main(String[] args) {

		// long startTime = System.nanoTime();

		// Crea un array de bytes con 5 elementos
		byte[] arr = {1, 5, 8, 50, 55};

		// Crea un buffer de bytes con la capacidad de 5 bytes (lo que ocupa el array creado)
		ByteBuffer buf = ByteBuffer.allocate(arr.length); // Buffer non-direct (buffer no directo)

		/* Coloca los bytes en el buffer. Ahora el buffer va a estar respaldado por el array de bytes.
		 * En caso de que se supere la capacidad del buffer, lanza un BufferOverflowException. */
		buf.put(arr);

		/* Cambia el buffer a modo lectura ya que la posicion quedo en el ultimo byte del buffer al agregar los bytes.
		 * EL metodo flip() lo que hace es volver la posicion a 0 y establecer el limite en la ultima posicion. */
		buf.flip();

		// Mientras haya elementos entre la posicion y el limite
		while (buf.hasRemaining())
			// Muestra el byte de la posicion y la incrementa, esto es lo que se conoce como get relativo
			System.out.print(buf.get() + " ");

		// long endTime = System.nanoTime();
		// System.out.println("\nDuracion: " + (endTime - startTime) / 1e6 + " ms");

	}
}
