package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static util.Constants.*;

/**
 * Un canal es un nexo para operaciones de I/O que representa una conexión abierta a una entidad, como un dispositivo de
 * hardware, un archivo, un conector de red o un componente de programa que es capaz de realizar una o más operaciones
 * de I/O distintas, por ejemplo, leer o escribir.
 * 
 * Los canales representan conexiones a varias fuentes de I/O, como pipes, sockets, files, datagrams.
 * ° Operan con buffers y fuentes de I/O: mueve bloques de datos (lectura/escritura) dentro/fuera de buffers desde/hacia
 * fuentes de I/O.
 * ° Pueden ser de bloqueo/no-bloqueo, habilitan operaciones de I/O sin bloqueo.
 * 
 * En general, los canales están destinados a ser seguros para el acceso multiproceso.
 * 
 * Para este caso se crea una conexion con un archivo usando un canal y a travez de ese canal se llena un buffer (direct
 * o non-direct) con los bytes del archivo, pudiendo asi leer o escribir en el mismo de una manera mas eficiente.
 * 
 * Un canal de archivos se crea invocando el método open() definido por esta clase. También se puede obtener un canal de
 * archivo de un objeto FileInputStream, FileOutputStream o RandomAccessFile existente invocando el método getChannel de
 * ese objeto, que devuelve un canal de archivo que está conectado al mismo archivo subyacente. Cuando el canal de
 * archivo se obtiene de una secuencia existente o un archivo de acceso aleatorio, el estado del canal de archivo está
 * íntimamente conectado al del objeto cuyo método getChannel devolvió el canal.
 * 
 * @author Ru$o
 * 
 */

public class Channel_ {

	public static void main(String[] args) {

		FileChannel channel = null;
		RandomAccessFile file = null;

		try {

			/* En varios puntos, esta clase especifica que se requiere una instancia que esté "abierta para lectura",
			 * "abierta para escritura" o "abierta para lectura y escritura". Un canal obtenido a través del método getChannel de
			 * una instancia de FileInputStream estará abierto para lectura. Un canal obtenido a través del método getChannel de una
			 * instancia de FileOutputStream estará abierto para escritura. Finalmente, un canal obtenido a través del método
			 * getChannel de una instancia de RandomAccessFile estará abierto para lectura si la instancia fue creada con el modo
			 * "r" y estará abierto para lectura y escritura si la instancia fue creada con el modo "rw". */
			file = new RandomAccessFile(TEXT, "rw"); // Usa un archivo de texto de 10 bytes como ejemplo
			channel = file.getChannel(); // o usando el metodo open() de esta clase

			// Crea un buffer con una capacidad de 7 bytes para leer un archivo de 10 bytes
			ByteBuffer buf = ByteBuffer.allocate(7);

			/* El canal lee la secuencia de bytes del buffer y guarda la posicion actual de este. Despues de *escribir los bytes, la
			 * posicion queda establecida en el ultimo byte leido del buffer y mientras la posicion del buffer no llegue al final
			 * del archivo, este (el canal) sigue llenado el buffer con los datos del archivo.
			 * 
			 * *Es importante aclarar que primero se llena (escriben) ese buffer con los datos del archivo y despues el canal los
			 * lee, siendo esto, un proceso interno de java. El metodo read(buf) devuelve el numero de bytes leidos, posiblemente
			 * cero o -1 si el canal ha alcanzado el final del flujo. */
			while (channel.read(buf) > 0) { // o "!= -1"

				/* En la primera vuelta del bucle, la posicion del buffer quedo en la 7 porque el canal leyo hasta el ultimo byte del
				 * buffer ya que este ocupa 7 bytes y el archivo 10. Entonces para poder leer cada byte del buffer hay que voltearlo
				 * (flip), cambiandolo del modo escritura al modo lectura. */
				buf.flip(); // limit = position, position = 0

				// Mientras haya elementos en el buffer (elementos entre la posicion actual y el limite)
				while (buf.hasRemaining())
					/* Muestra el caracter que representa el byte devuelto por el get relativo. Esto quiere decir que devuelve el byte de la
					 * posicion actual del buffer e la incrementa. */
					System.out.println("posicion: " + buf.position() + " - limite: " + buf.limit() + " - caracter: " + (char) buf.get());

				/* Una vez que se hayan leido todos los bytes del buffer, lo "limpia" para rellenarlo con los bytes sobrantes del
				 * archivo (3) y asi poder leerlo de nuevo. En este caso cambia el buffer del modo lectura al modo escritura.
				 * Estableciendo el limite en la capacidad, le estamos diciendo que el buffer ya esta lleno. */
				buf.clear(); // position = 0, limit = capacity

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// TODO ¿Cerrando el canal, tambien se cierra el flujo del archivo?
				if (channel != null) channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
