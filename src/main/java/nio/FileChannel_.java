package nio;

import static util.Constants.TEXT;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Crea una conexion con un archivo usando un canal y a travez de ese canal se llena un buffer con los bytes del
 * archivo, pudiendo asi leer o escribir en el mismo de una manera mas eficiente.
 * 
 * Una cosa interesante de los canales, es que no tienen la opcion de hacer I/O nativas al archivo. Esto obliga a
 * implementar un buffer haciendo el proceso mas eficiente, ademas de ser seguros para el acceso multiproceso.
 * 
 * Un Buffer en NIO es un contenedor para una cantidad fija de datos de un tipo primitivo especifico usado por canales.
 * - Contenido, datos.
 * - Capacidad, tamaño del buffer; Se establece cuando se crea el buffer y no puede ser cambiado.
 * - Limite, el indice del primer elemento que no debe leerse ni escribirse; limite < capacidad.
 * - Posicion, el indice del siguiente elemento a leer o escribir.
 * - Marca, el indice al que se restablecera la posicion cuando se invoque el metodo reset().
 * 
 * Los canales representan conexiones a varias fuentes de I/O, como pipes, sockets, files, datagrams.
 * - Operan con buffers y fuentes de I/O: mueve bloques de datos (lectura/escritura) dentro/fuera de buffers desde/hacia
 * fuentes de I/O.
 * - Pueden ser de bloqueo/no-bloqueo, habilitan operaciones de I/O sin bloqueo.
 * 
 * @author Juan Debenedetti aka Ru$o
 * 
 */

public class FileChannel_ {

	public static void main(String[] args) {

		RandomAccessFile raf = null;
		FileChannel channel = null;

		try {

			// Crea un nexo con el archivo usando un canal para realizar operacion de I/O (rw)
			raf = new RandomAccessFile(TEXT, "rw");
			channel = raf.getChannel();

			// Crea un buffer con una capacidad de 10 bytes
			ByteBuffer buf = ByteBuffer.allocate(10);

			/* Lee una secuencia de bytes del buffer desde el canal y guarda la posicion actual de este.
			 * Internamente se llena el buffer con los bytes del archivo y despues es leido desde el canal. */
			int position = channel.read(buf);

			// Mientras la posicion del buffer no llegue al final
			while (position != -1) {

				/* Ahora mismo, la posicion del buffer esta en la 10 porque el canal leyo hasta el ultimo byte del buffer
				 * ya que el archivo "text.txt" ocupa 10 bytes. Entonces para poder leer cada byte del buffer hay que voltearlo
				 * (flip). Esto cambia el buffer del modo escritura al modo lectura. */
				buf.flip(); // limit = position, position = 0;

				// Mientras haya elementos en el buffer (elementos entre la posicion actual y el limite)
				while (buf.hasRemaining())
					/* Muestra el caracter que representa el byte devuelto por el get relativo. Esto quiere decir que devuelve el byte de la
					 * posicion actual del buffer e la incrementa. */
					System.out.print((char) buf.get());

				/* Una vez que se hayan leido todos los datos, borra el buffer para escribir nuevamente. Es decir que "limpia" el buffer
				 * para rellenarlo y asi poder leerlo de nuevo. Es este caso cambia el buffer del modo lectura al modo escritura. */
				buf.clear(); // position = 0, limit = capacity;

				/* Ahora al rellenar el buffer y leer la secuencia de bytes, la posicion llega al final del archivo (-1) y no a 10, ya
				 * que el limite se establecio en la capacidad. */
				position = channel.read(buf);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (channel != null) channel.close(); // Creo que esto tambien cierra el raf
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
