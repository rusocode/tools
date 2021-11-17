package nio;

import java.nio.ByteBuffer;

/**
 * Un Buffer en NIO es un contenedor para una cantidad fija de datos de un tipo primitivo especifico usado por canales.
 * ° Contenido, datos.
 * ° Capacidad, tamaño del buffer; Se establece cuando se crea el buffer y no puede ser cambiado.
 * ° Limite, el indice del primer elemento que no debe leerse ni escribirse; limite < capacidad.
 * ° Posicion, el indice del siguiente elemento a leer o escribir.
 * ° Marca, el indice al que se restablecera la posicion cuando se invoque el metodo reset().
 * 
 * Un búffer recién creado siempre tiene una posición de cero y una marca que no está definida. El límite inicial puede
 * ser cero o puede ser algún otro valor que dependa del tipo de búffer y de la forma en que se construye. Cada elemento
 * de un búffer recién asignado se inicializa a cero.
 * 
 * El siguiente invariante es válido para los valores de marca, posición, límite y capacidad:
 * 0 <= marca <= posición <= límite <= capacidad
 * 
 * Para este ejemplo se utiliza un ByteBuffer.
 * 
 * Ejemplos de ByteBuffer: https://www.geeksforgeeks.org/bytebuffer-flip-methods-in-java-with-examples/
 * 
 * @author Ru$o
 * 
 */

public class ByteBuffer_ {

	public static void main(String[] args) {

		// Crea un array de bytes con 5 elementos
		byte[] arr = { 1, 5, 8, 50, 55 };

		// Crea un buffer de bytes con la capacidad de 5 bytes (lo que ocupa el array creado)
		ByteBuffer buf = ByteBuffer.allocate(arr.length);

		/* Coloca los bytes en el buffer. Ahora el buffer va a estar respaldado por el array de bytes.
		 * En caso de que se supere la capacidad del buffer, lanzaria un BufferOverflowException. */
		buf.put(arr);

		/* Cambia el buffer a modo lectura ya que la posicion quedo en el ultimo byte del buffer al agregar los bytes.
		 * EL metodo flip() lo que hace es volver la posicion a 0 y establecer el limite en la ultima posicion. */
		buf.flip();

		// Mientras haya elementos entre la posicion y el limite
		while (buf.hasRemaining())
			// Muestra el byte de la posicion y la incrementa, esto es lo que se conoce como get relativo
			System.out.print(buf.get() + " ");

	}
}
