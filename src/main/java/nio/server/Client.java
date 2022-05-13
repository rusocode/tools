package nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static util.Constants.*;

/**
 * Canal de cliente para crear conexiones con el servidor.
 *
 * @author Ru$o
 */

public class Client {

	public static void main(String[] args) throws IOException, InterruptedException {

		String host = "localhost";

		// Abre un canal de socket y lo conecta a una direccion remota, en este caso es la direccion local
		SocketChannel client = SocketChannel.open(new InetSocketAddress(host, SERVER_PORT));

		/* Configura el SocketChannel en modo sin bloqueo para llamar a los metodos connect(), read() y write() de forma
		 * asincronica. */
		client.configureBlocking(false);

		if (client.isConnected()) System.out.println("Conectado!");
		else System.out.println("El cliente no se pudo conectar!");

		ByteBuffer buf = ByteBuffer.allocate(8); // ByteBuffer buf = ByteBuffer.wrap("mensaje".getBytes());

		// Agrega 7 bytes al buffer
		buf.put("mensaje".getBytes());

		// Despues de agregar la cadena de bytes que representa el texto, quedaria disponible 1 byte en buffer
		System.out.println("Bytes disponibles = " + buf.remaining());

		// Da vuelta el buffer para que empiece a escribir desde la posicion 0 en el canal
		buf.flip(); // limit = position, position = 0;

		// Mientras haya bytes entre la posicion y el limite
		while (buf.hasRemaining())
			// Escribe el buffer en el canal del socket
			client.write(buf);

		client.close();

	}

}
