package nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static util.Constants.*;

/**
 * Un canal seleccionable para crear conexiones.
 * 
 * @author Ru$o
 * 
 */

public class Client {

	public static void main(String[] args) throws IOException {

		String host = "localhost";

		// Abre un canal de socket y lo conecta a una direccion remota
		SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, SERVER_PORT));

		// Conecta el socket del canal
		// socketChannel.connect(new InetSocketAddress("localhost", 7666));

		/* Configura el SocketChannel en modo sin bloqueo para llamar a los metodos connect(), read() y write() de forma
		 * asincronica. */
		// socketChannel.configureBlocking(false);

		if (channel.isConnected()) System.out.println("Conectado!");
		else System.out.println("El cliente no se pudo conectar!");

		String texto = "mensaje";

		ByteBuffer buf = ByteBuffer.allocate(8);

		// Limpia el buffer para poder escribir en el
		// buf.clear(); // TODO Para que hace esto?

		// Agrega 7 bytes al buffer
		buf.put(texto.getBytes());

		// Despues de agregar la cadena de bytes que representa el texto, quedaria disponible 1 byte en el buffer
		System.out.println("Bytes disponibles = " + buf.remaining());

		// Da vuelta el buffer para que empiece a escribir desde la posicion 0 en el canal
		buf.flip(); // limit = position, position = 0;

		// Mientras haya bytes entre la posicion y el limite
		while (buf.hasRemaining())
			// Escribe el buffer en el canal del socket
			channel.write(buf);

		channel.close();

	}

}
