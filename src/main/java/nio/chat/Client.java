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

		/* Abre un canal de socket y lo conecta a una direccion remota.
		 * 
		 * Pasandole un objeto InetSocketAddress se ahorra de conectar el socket usando el metodo connect()
		 * channel.connect(new InetSocketAddress(host, CLIENT_PORT)); */
		SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, CLIENT_PORT)); // Uso el puerto del cliente o del servidor?

		/* Configura el SocketChannel en modo sin bloqueo para llamar a los metodos connect(), read() y write() de forma
		 * asincronica. */
		// socketChannel.configureBlocking(false);

		if (channel.isConnected()) System.out.println("Conectado!");
		else System.out.println("El cliente no se pudo conectar!");

		ByteBuffer buf = ByteBuffer.allocate(8);

		buf.put("mensaje".getBytes());

		// Limpia el buffer para poder escribir en el
		// buf.clear(); // TODO Para que hace esto?

		// Agrega 7 bytes al buffer

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
