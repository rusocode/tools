package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Un canal seleccionable para escuchar conexiones SIN bloqueo.
 * 
 * El Selector es un objeto utilizado para seleccionar un canal listo para comunicarse (para realizar una operacion).
 * Es decir que se consulta al Selector si hay algun canal que este listo para operar sin bloqueo.
 * Tiene la posibilidad de elegir un canal seleccionable que este listo para algunas operaciones de red, por ejemplo,
 * conectar, aceptar, leer y escribir.
 * 
 * El canal debe estar en modo sin bloqueo para ser utilizado con un selector. Esto significa que no puede usar
 * FileChannel con un Selector ya que FileChannel no se puede cambiar al modo sin bloqueo. Sin embargo, los canales
 * de socket si.
 * 
 * Se dice que un canal que "dispara un evento" esta "listo" para ese evento. Por lo tanto, un canal que se ha
 * conectado correctamente a otro servidor esta "listo para conectarse". Un canal de socket de servidor que acepta una
 * conexion entrante esta listo para "aceptar". Un canal que tiene datos listos para ser leidos esta listo para "leer".
 * Un canal que esta listo para escribir datos en el, esta listo para "escribir".
 * 
 * El Selector incluye canales seleccionables:
 * - DatagramChannel
 * - Pipe.SinkChannel
 * - Pipe.SourceChannel
 * - ServerSocketChannel
 * - SocketChannel
 * 
 * Cada registro del canal esta representado por un SelectionKey.
 * Un selector funciona con un conjunto (set) de SelectionKey.
 * SelectionKey es un token que representa el registro de un canal con un selector.
 * El selector mantiene tres set de keys:
 * - "Key set" contiene las keys con los canales registrados;
 * - "Selected-key set" contiene las keys con canales listos para al menos una de las operaciones;
 * - "Cancelled-key set" contiene keys canceladas cuyos canales aun no se han cancelado.
 * 
 * Cuando registras un canal con un selector, el metodo register() devuelve un objeto de tipo SelectionKey que
 * representa el registro de los canales con ese selector. Este objeto contiene algunas propiedades interesantes:
 * El interest set
 * El ready set
 * El Channel
 * El Selector
 * Y un objeto adjunto (opcional)
 *
 * Fuente: http://tutorials.jenkov.com/java-nio/selectors.html
 * 
 * @author Ru$o
 * 
 */

public class Selector_ implements Runnable {

	public Selector_() {
		new Thread(this).start();
	}

	@Override
	public void run() {

		Selector selector = null;

		try {

			// Abre el selector
			selector = Selector.open();

			ServerSocketChannel server = ServerSocketChannel.open();

			// Configura el canal en modo sin bloqueo
			server.configureBlocking(false);

			server.socket().bind(new InetSocketAddress(7666));

			// Registra el selector para aceptar conexiones
			SelectionKey key = server.register(selector, SelectionKey.OP_ACCEPT);

			// Itera las claves registradas en el selector
			while (true) {

				// Obtiene la cantidad de canales que estan listos
				int readyChannels = selector.selectNow();

				if (readyChannels == 0) continue;

				/* Una vez que haya llamado a uno de los metodos select() y su valor de retorno haya indicado que uno o mas canales
				 * estan listos, puede acceder a los canales usando el metodo selectedKeys(). */

				// Devuelve el selected-key set del selector y se lo asigna al conjunto selectedKeys del tipo SelectionKey
				Set<SelectionKey> selectedKeys = selector.selectedKeys();

				// Devuelve un iterador sobre los elementos del conjunto SelectionKey
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

				// Mientras haya elementos
				while (keyIterator.hasNext()) {

					// Devuelve el key del iterador
					key = keyIterator.next();

					if (key.isAcceptable()) System.out.println("Una conexion fue aceptada por ServerSocketChannel");
					else if (key.isConnectable()) System.out.println("Se establecio una conexion con un servidor remoto");
					else if (key.isReadable()) System.out.println("Un canal esta listo para leer");
					else if (key.isWritable()) System.out.println("Un canal esta listo para escribir");

					/* El Selector no elimina las instancias de SelectionKey del propio conjunto de keys seleccionadas. Tienes que hacer
					 * esto cuando hayas terminado de procesar el canal. La proxima vez que el canal este "listo", el selector lo agregara
					 * nuevamente al conjunto de claves seleccionadas. */
					keyIterator.remove();
				}

			}

		} catch (IOException e) {
			System.err.println("Error de I/O\n" + e.toString());
		} finally {
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws IOException {
		new Selector_();
	}

}
