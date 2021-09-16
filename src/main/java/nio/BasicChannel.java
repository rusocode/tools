package nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

import static util.Constants.*;

public class BasicChannel {

	// Copia los datos de un canal a otro canal o de un archivo a otro archivo
	private static void copyData(ReadableByteChannel src, WritableByteChannel dest) {

		try {

			ByteBuffer buffer = ByteBuffer.allocate(20 * 1024);

			/**
			 * Lee una secuencia de bytes de este canal en el buffer dado.
			 * Se intenta leer hasta r bytes del canal, donde r es el numero de bytes que quedan en el buffer, es decir
			 * dst.remaining(), en el momento en que se invoca este metodo.
			 * 
			 * Supongamos que se lee una secuencia de bytes de longitud n, donde 0 <= n <= r. Esta secuencia de bytes se transferira
			 * al buffer de modo que el primer byte de la secuencia este en el indice p y el ultimo byte en el indice p + n - 1,
			 * donde p es la posicion del buffer en el momento en que se invoca este metodo. Al regresar, la posicion del buffer
			 * sera igual a p + n; su limite no habra cambiado.
			 * 
			 * Es posible que una operacion de lectura no llene el buffer y, de hecho, no lea ningun byte. Si lo hace o no depende
			 * de la naturaleza y el estado del canal. Un canal de socket en modo sin bloqueo, por ejemplo, no puede leer mas bytes
			 * de los que estan inmediatamente disponibles en el buffer de entrada del socket; De manera similar, un canal de
			 * archivo no puede leer mas bytes de los que quedan en el archivo. Sin embargo, se garantiza que si un canal esta en
			 * modo de bloqueo y hay al menos un byte restante en el buffer, este metodo se bloqueara hasta que se lea al menos un
			 * byte.
			 * 
			 * Este metodo puede invocarse en cualquier momento. Sin embargo, si otro subproceso ya ha iniciado una operacion de
			 * lectura en este canal, una invocacion de este metodo se bloqueara hasta que se complete la primera operacion.
			 * 
			 * @param buf - El buffer al que se transferiran los bytes.
			 * @return El numero de bytes leidos, posiblemente 0 o -1 si el canal ha alcanzado el final del flujo.
			 */
			while (src.read(buffer) != -1) {

				/* Voltea el buffer. El limite se establece en la posicion actual y luego la posicion se establece en cero. Si la marca
				 * esta definida, se descarta. */
				buffer.flip();

				// Mientras haya elementos en el buffer (indica si hay elementos entre la posicion actual y el limite)
				while (buffer.hasRemaining()) {

					/**
					 * Escribe una secuencia de bytes en este canal desde el buffer dado.
					 * Se intenta escribir hasta r bytes en el canal, donde r es el numero de bytes que quedan en el buffer, es decir,
					 * src.remaining(), en el momento en que se invoca este metodo.
					 * 
					 * Suponga que se escribe una secuencia de bytes de longitud n, donde 0 <= n <= r. Esta secuencia de bytes se
					 * transferira desde el buffer comenzando en el indice p, donde p es la posicion del buffer en el momento en que se
					 * invoca este metodo; el indice del ultimo byte escrito sera p + n - 1. Al regresar, la posicion del buffer sera igual
					 * a p + n; su limite no habra cambiado.
					 * 
					 * A menos que se especifique lo contrario, una operacion de escritura regresara solo despues de escribir todos los r
					 * bytes solicitados. Algunos tipos de canales, dependiendo de su estado, pueden escribir solo algunos de los bytes o
					 * posiblemente ninguno. Un canal de socket en modo sin bloqueo, por ejemplo, no puede escribir mas bytes de los que
					 * estan libres en el buffer de salida del socket.
					 * 
					 * Este metodo puede invocarse en cualquier momento. Sin embargo, si otro hilo ya ha iniciado una operacion de escritura
					 * en este canal, una invocacion de este metodo se bloqueara hasta que se complete la primera operacion.
					 * 
					 * @param buf - El buffer del que se recuperaran los bytes.
					 * @return El numero de bytes escritos, posiblemente cero.
					 */
					dest.write(buffer);
				}

				buffer.clear();

			}

		}

		catch (ClosedByInterruptException e) {
			System.err.println(
					"Otro canal interrumpio el hilo actual mientras la operacion de lectura estaba en progreso, cerrando asi el canal y estableciendo el estado de interrupcion del hilo actual!");
		} catch (AsynchronousCloseException e) {
			System.err.println("Otro hilo cerro este canal mientras la operacion de lectura estaba en progreso!");
		} catch (ClosedChannelException e) {
			System.err.println("El canal esta cerrado!");
		} catch (IllegalArgumentException e) {
			System.err.println("La capacidad especificada del buffer es un numero entero negativo!");
		} catch (NonReadableChannelException e) {
			System.err.println("Este canal no se abrio para lectura!");
		} catch (NonWritableChannelException e) {
			System.err.println("Este canal no se abrio para escritura!");
		} catch (IOException e) {
			System.err.println("Error de I/O!");
		} finally {
			try {
				if (src != null) src.close();
				if (dest != null) dest.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws IOException {

		FileInputStream input = new FileInputStream(TEXT); // Ruta del archivo de entrada
		/* Devuelve el canal del archivo asociado con este flujo de entrada.
		 * La posicion inicial del canal devuelto sera igual al numero de bytes leidos del archivo hasta ahora. La lectura de
		 * bytes de este flujo incrementara la posicion del canal. Cambiar la posicion del canal, ya sea explicitamente o
		 * leyendo, cambiara la posicion del archivo de esta secuencia. */
		ReadableByteChannel source = input.getChannel();

		FileOutputStream output = new FileOutputStream(getTextDir() + "copia_text.txt"); // Ruta del archivo de salida
		WritableByteChannel destination = output.getChannel();

		copyData(source, destination);

		/* Cierra los flujos del archivo y libera cualquier recurso del sistema asociado con el flujo.
		 * Si esta secuencia tiene un canal asociado, el canal tambien esta cerrado. */
		input.close();
		output.close();

	}

}
