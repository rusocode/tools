package nio;

import java.nio.ByteBuffer;

/**
 * Un buffer en NIO es un contenedor para una cantidad fija de datos de un tipo primitivo especifico usado por canales.
 * Aparte de su contenido, las propiedades esenciales de un búffer son su capacidad, límite y posición:
 * ° La capacidad de un búffer es el número de elementos que contiene. La capacidad de un búfer nunca es negativa y
 * nunca cambia.
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
 * Para este caso se utiliza la clase ByteBuffer como ejemplo.
 * 
 * Recursos
 * file:///C:/Users/juand/Documents/Eclipse%20-%20Proyectos/utilidades/src/main/resources/tips/NIO%20byte%20buffer.PNG
 * https://www.geeksforgeeks.org/bytebuffer-flip-methods-in-java-with-examples/
 * 
 * @author Ru$o
 * 
 */

public class Buffer {

	public static void main(String[] args) {

		// Crea un array de bytes con 5 elementos
		byte[] arr = { 1, 5, 8, 50, 55 };

		// Crea un buffer de bytes con la capacidad de 5 bytes (lo que ocupa el array creado)
		ByteBuffer buf = ByteBuffer.allocate(arr.length);

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

	}
}