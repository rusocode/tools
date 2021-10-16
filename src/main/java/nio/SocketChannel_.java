package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Un canal seleccionable para crear conexiones.
 * 
 * @author Juan Debenedetti aka Ru$o
 * 
 */

public class SocketChannel_ {

	public static void main(String[] args) throws IOException {

		// Abre un canal para el socket de cliente
		SocketChannel clientChannel = SocketChannel.open();

		/* Configura el SocketChannel en modo sin bloqueo para llamar a los metodos connect(), read() y write() de forma
		 * asincronica. */
		// channel.configureBlocking(false);

		// Conecta el socket del canal
		clientChannel.connect(new InetSocketAddress("localhost", 7666));

		if (clientChannel.isConnected()) System.out.println("Conectado!");
		else System.out.println("El cliente no se pudo conectar!");

		String texto = "Rulo quemado";

		ByteBuffer buf = ByteBuffer.allocate(13);

		// Limpia el buffer para poder escribir en el
		buf.clear(); // position = 0, limit = capacity;
		// Agrega 12 bytes al buffer
		buf.put(texto.getBytes());

		// Despues de agregar la cadena de bytes que representa el texto, quedaria disponible 1 byte en el buffer
		System.out.println("Bytes disponibles = " + buf.remaining());

		// Da vuelta el buffer para que empiece a escribir desde la posicion 0 en el canal
		buf.flip(); // limit = position, position = 0;

		while (buf.hasRemaining())
			clientChannel.write(buf);

		clientChannel.close();

	}

}
