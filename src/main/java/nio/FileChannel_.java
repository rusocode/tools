package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static util.Constants.*;

/**
 * Crea una conexion con un archivo usando un canal y a travez de ese canal se llena un buffer (directo o no-directo)
 * con los bytes del archivo, pudiendo asi leer o escribir en el mismo de una manera mas eficiente.
 * 
 * Un canal de archivos se crea invocando el método open() definidos por esta clase. También se puede obtener
 * un canal de archivo de un objeto FileInputStream, FileOutputStream o RandomAccessFile existente invocando el método
 * getChannel de ese objeto, que devuelve un canal de archivo que está conectado al mismo archivo subyacente. Cuando el
 * canal de archivo se obtiene de una secuencia existente o un archivo de acceso aleatorio, el estado del canal de
 * archivo está íntimamente conectado al del objeto cuyo método getChannel devolvió el canal.
 * 
 * Los canales representan conexiones a varias fuentes de I/O, como pipes, sockets, files, datagrams.
 * ° Operan con buffers y fuentes de I/O: mueve bloques de datos (lectura/escritura) dentro/fuera de buffers desde/hacia
 * fuentes de I/O.
 * ° Pueden ser de bloqueo/no-bloqueo, habilitan operaciones de I/O sin bloqueo.
 * 
 * -Buffer
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
 * @author Ru$o
 * 
 */

public class FileChannel_ {

	public static void main(String[] args) {

		RandomAccessFile raf = null;
		FileChannel channel = null;

		try {

			/* Crea un nexo con el archivo usando un canal para realizar operacion de I/O (rw).
			 * 
			 * En varios puntos, esta clase especifica que se requiere una instancia que esté "abierta para lectura",
			 * "abierta para escritura" o "abierta para lectura y escritura". Un canal obtenido a través del método getChannel de
			 * una instancia de FileInputStream estará abierto para lectura. Un canal obtenido a través del método getChannel de una
			 * instancia de FileOutputStream estará abierto para escritura. Finalmente, un canal obtenido a través del método
			 * getChannel de una instancia de RandomAccessFile estará abierto para lectura si la instancia fue creada con el modo
			 * "r" y estará abierto para lectura y escritura si la instancia fue creada con el modo "rw". */
			raf = new RandomAccessFile(TEXT, "rw"); // Uso un archivo de texto de 10 bytes como ejemplo
			channel = raf.getChannel(); // o usando el metodo open() de esta clase

			// Crea un buffer con una capacidad de 7 bytes para leer un archivo de 10 bytes
			ByteBuffer buf = ByteBuffer.allocate(7);

			/* Lee la secuencia de bytes del buffer desde el canal y guarda la posicion actual de este.
			 * Internamente se llena el buffer con los bytes del archivo y despues es leido desde el canal.
			 * La posicion queda establecida en el ultimo byte leido del buffer. */
			int position = channel.read(buf);

			// Mientras la posicion del buffer no llegue al final del archivo
			while (position != -1) {

				/* En la primera vuelta del bucle, la posicion del buffer quedo en la 7 porque el canal leyo hasta el ultimo byte del
				 * buffer ya que este ocupa 7 bytes y el archivo 10. Entonces para poder leer cada byte del buffer hay que voltearlo
				 * (flip), cambiandolo del modo escritura al modo lectura. */
				buf.flip(); // limit = position, position = 0;

				// Mientras haya elementos en el buffer (elementos entre la posicion actual y el limite)
				while (buf.hasRemaining())
					/* Muestra el caracter que representa el byte devuelto por el get relativo. Esto quiere decir que devuelve el byte de la
					 * posicion actual del buffer e la incrementa. */
					System.out.println("posicion: " + buf.position() + " / limite: " + buf.limit() + " / caracter: " + (char) buf.get());

				/* Una vez que se hayan leido todos los bytes del buffer, lo "limpia" para rellenarlo y asi poder leerlo de nuevo. En
				 * este caso cambia el buffer del modo lectura al modo escritura. */
				buf.clear(); // position = 0, limit = capacity; // TODO Usando flip() genera la misma salida

				/* Los bytes se leen comenzando en la posición actual del archivo de este canal y luego la posición del archivo se
				 * actualiza con el número de bytes realmente leídos. */
				// Rellena el buffer con los bytes sobrantes del archivo, en este caso sobraron 3 bytes
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
