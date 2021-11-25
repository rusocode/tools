package nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import static util.Constants.*;

/**
 * Canal seleccionable para escuchar conexiones SIN bloqueo.
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
 * - "Selected-key set" contiene las keys listas para al menos una de las operaciones;
 * - "Cancelled-key set" contiene las keys canceladas cuyos canales aun no se han cancelado.
 * 
 * Cuando registras un canal con un selector, el metodo register() devuelve un objeto de tipo SelectionKey que
 * representa el registro de los canales con ese selector. Este objeto contiene algunas propiedades interesantes:
 * El interest set
 * El ready set
 * El Channel
 * El Selector
 * Y un objeto adjunto (opcional)
 *
 * Recursos y fuentes
 * http://tutorials.jenkov.com/java-nio/selectors.html
 * https://docs.oracle.com/javase/8/docs/api/java/nio/channels/SelectableChannel.html
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
		ServerSocketChannel server = null;

		try {

			// Abre el selector
			selector = Selector.open();
			if (selector.isOpen()) System.out.println("Se abrio el canal");

			/* Abre el servidor. Ahora mismo el socket del servidor no esta vinculado; debe estar vinculado a una direccion
			 * especifica a traves de uno de los metodos de vinculacion de su socket antes de que se puedan aceptar las
			 * conexiones. */
			server = ServerSocketChannel.open();
			if (server.isOpen()) System.out.println("Se abrio el servidor");

			/* Recupera un socket de servidor asociado con este canal y lo vincula a la diereccion local con el puerto TCP
			 * especificado. */
			server.socket().bind(new InetSocketAddress(SERVER_PORT));

			/* Configura el canal del servidor en modo sin bloqueo.
			 * 
			 * Un canal seleccionable esta en modo de bloqueo o en modo sin bloqueo. En el modo de bloqueo, cada operacion de I/O
			 * invocada en el canal se bloqueara hasta que se complete. En el modo sin bloqueo, una operacion de I/O nunca se
			 * bloqueara y puede transferir menos bytes de los solicitados o posiblemente ningun byte. El modo de bloqueo de un
			 * canal seleccionable puede determinarse invocando su metodo isBlocking().
			 * 
			 * Los canales seleccionables recien creados estan siempre en modo de bloqueo. El modo sin bloqueo es mas util junto con
			 * la multiplexacion basada en selectores. Un canal debe colocarse en modo de no bloqueo antes de ser registrado con un
			 * selector, y no puede volver al modo de bloqueo hasta que se haya cancelado su registro. En caso de registrar el
			 * servidor con un selector que fue configurado con bloqueo, lanzara un IllegalBlockingModeException. */
			server.configureBlocking(false);
			if (!server.isBlocking()) System.out.println("Se configuro el servidor en modo sin bloqueo");
			else System.out.println("El servidor esta en modo con bloqueo por defecto");

			/* Registra el servidor con el selector para aceptar conexiones en donde se usa un objeto SelectionKey que representa el
			 * registro del canal con el selector.
			 *
			 * No se puede cancelar el registro de un canal directamente; en cambio, la key que representa su registro debe
			 * cancelarse. La cancelacion de una key solicita que el canal se cancele durante la siguiente operacion de seleccion
			 * del selector. Una key puede cancelarse explicitamente invocando el metodo cancel(). Todas las keys de un canal se
			 * cancelan implicitamente cuando el canal se cierra, ya sea invocando el metodo close() o interrumpiendo un hilo
			 * bloqueado en una operacion de I/O en el canal.
			 * 
			 * Si el selector en si esta cerrado, el canal se cancelara y la key que representa su registro se invalidara sin mas
			 * demora.
			 * 
			 * Un canal puede registrarse como maximo una vez con cualquier selector en particular.
			 * 
			 * Los canales seleccionables son seguros para su uso por varios subprocesos simultaneos. */
			server.register(selector, SelectionKey.OP_ACCEPT);
			if (server.isRegistered()) System.out.println("Se registro el canal del servidor con el selector especificado para aceptar conexiones!");

			/* Despues de registrar el canal del servidor para que acepte conexiones por medio de un selector, va a ponerse a la
			 * escucha. Ahora cuando un cliente se conecte al servidor por medio del canal, va a mostrar un mensaje. */
			while (true) {

				// Bloqueo de llamadas, pero solo una para todo
				selector.select();

				// El metodo selectedKeys() del selector abierto, devuelve un conjunto de claves para determinar un evento
				for (SelectionKey key : selector.selectedKeys()) {

					if (key.isValid()) {

						if (key.isAcceptable()) System.out.println("Una conexion fue aceptada por ServerSocketChannel");
						if (key.isConnectable()) System.out.println("Se establecio una conexion con un servidor remoto");
						if (key.isReadable()) System.out.println("Un canal esta listo para leer");
						if (key.isWritable()) System.out.println("Un canal esta listo para escribir");

					}
				}

			}

//			// Itera las claves registradas en el selector
//			while (true) {
//
//				// Obtiene la cantidad de canales que estan listos
//				int readyChannels = selector.selectNow();
//
//				if (readyChannels == 0) continue;
//
//				/* Una vez que haya llamado a uno de los metodos select() y su valor de retorno haya indicado que uno o mas canales
//				 * estan listos, puede acceder a los canales usando el metodo selectedKeys(). */
//
//				// Devuelve el selected-key set del selector y se lo asigna al conjunto selectedKeys del tipo SelectionKey
//				Set<SelectionKey> selectedKeys = selector.selectedKeys();
//
//				// Devuelve un iterador sobre los elementos del conjunto SelectionKey
//				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
//
//				// Mientras haya elementos
//				while (keyIterator.hasNext()) {
//
//					// Devuelve el key del iterador
//					key = keyIterator.next();
//
//					if (key.isAcceptable()) System.out.println("Una conexion fue aceptada por ServerSocketChannel");
//					else if (key.isConnectable()) System.out.println("Se establecio una conexion con un servidor remoto");
//					else if (key.isReadable()) System.out.println("Un canal esta listo para leer");
//					else if (key.isWritable()) System.out.println("Un canal esta listo para escribir");
//
//					/* El Selector no elimina las instancias de SelectionKey del propio conjunto de keys seleccionadas. Tienes que hacer
//					 * esto cuando hayas terminado de procesar el canal. La proxima vez que el canal este "listo", el selector lo agregara
//					 * nuevamente al conjunto de claves seleccionadas. */
//					keyIterator.remove();
//				}
//
//			}

		} catch (IOException e) {
			System.err.println("Error de I/O\n" + e.toString());
		} finally {
			try {
				server.close();
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
