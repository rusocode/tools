package nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import static util.Constants.*;

/**
 * El Selector es un *multiplexor de objetos SelectableChannel encargado de seleccionar canales sin bloqueo y determinar
 * qué canales están listos para, por ejemplo, leer o escribir. De esta manera, un solo hilo puede administrar múltiples
 * canales y, por lo tanto, múltiples conexiones de red.
 * 
 * ¿Por que utilizar un selector?
 * La ventaja de usar un solo hilo para manejar múltiples canales es que necesita menos hilos para manejar los canales.
 * En realidad, puede usar un solo hilo para manejar todos sus canales. Cambiar entre subprocesos es costoso para un
 * sistema operativo, y cada subproceso también consume algunos recursos (memoria) en el sistema operativo. Por lo
 * tanto, cuantos menos subprocesos uses, mejor.
 * 
 * Un canal seleccionable puede estar en modo de bloqueo o en modo sin bloqueo. En el modo de bloqueo, cada operacion de
 * I/O invocada en el canal se bloqueara hasta que se complete. En el modo sin bloqueo, una operacion de I/O nunca se
 * bloqueara y puede transferir menos bytes de los solicitados o posiblemente ningun byte. El modo de bloqueo de un
 * canal seleccionable puede determinarse invocando su metodo isBlocking(). Los canales seleccionables recien creados
 * estan siempre en modo de bloqueo. El modo sin bloqueo es mas util junto con la multiplexacion basada en selectores.
 * Un canal debe colocarse en modo de no bloqueo antes de ser registrado con un selector, y no puede volver al modo de
 * bloqueo hasta que se haya cancelado su registro. En caso de registrar el servidor con un selector que fue configurado
 * con bloqueo, lanzara un IllegalBlockingModeException.
 * 
 * Un canal puede registrarse como maximo una vez con cualquier selector en particular y ademas, estos canales
 * seleccionables son seguros para su uso por varios subprocesos simultaneos.
 * 
 * Se dice que un canal que "dispara un evento" esta "listo" para ese evento. Por lo tanto, un canal que se ha
 * conectado correctamente a otro servidor esta "listo para conectarse". Un canal de socket de servidor que acepta una
 * conexion entrante esta listo para "aceptar". Un canal que tiene datos listos para ser leidos esta listo para "leer".
 * Un canal que esta listo para escribir datos en el, esta listo para "escribir".
 * 
 * El Selector incluye los siguientes canales seleccionables:
 * -ServerSocketChannel
 * -SocketChannel
 * -DatagramChannel
 * -Pipe.SinkChannel
 * -Pipe.SourceChannel
 * El canal de FileChannel no se puede usar con un Selector ya que el canal no se puede cambiar al modo sin bloqueo, sin
 * embargo, los canales de socket si.
 * 
 * Cada registro del canal esta representado por un SelectionKey. Un selector funciona con un conjunto (set) de
 * SelectionKey. SelectionKey es un token que representa el registro de un canal con un selector. El selector mantiene
 * tres set de keys:
 * -"Key set" contiene las keys con los canales registrados; se obtienen usando el metodo keys().
 * -"Selected-key set" contiene las keys listas para al menos una de las operaciones; se obtienen usando el metodo
 * selectedKeys().
 * -"Cancelled-key set" contiene las keys canceladas cuyos canales aun no se han cancelado.
 * Los tres conjuntos están vacíos en un selector recién creado.
 * 
 * Cuando registras un canal con un selector, el metodo register() devuelve un objeto de tipo SelectionKey que
 * representa el registro de los canales con ese selector. Este objeto contiene algunas propiedades interesantes:
 * -El interest set
 * -El ready set
 * -El Channel
 * -El Selector
 * -Y un objeto adjunto (opcional)
 * 
 * *Los multiplexores son circuitos combinacionales con varias entradas y una unica salida de datos. Estan dotados de
 * entradas de control capaces de seleccionar una, y solo una, de las entradas de datos para permitir su transmision
 * desde la entrada seleccionada hacia dicha salida.
 * 
 * Recursos y fuentes
 * http://tutorials.jenkov.com/java-nio/selectors.html
 * https://docs.oracle.com/javase/8/docs/api/java/nio/channels/SelectableChannel.html
 * 
 * @author Ru$o
 * 
 */

public class ServerNonBlocking extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JTextArea console;

	private Selector selector = null;
	private ServerSocketChannel server = null;

	public ServerNonBlocking() {

		super("Servidor");
		setResizable(false);
		setSize(550, 430);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();

		new Thread(this).start();
	}

	private void initialize() {

		panel = new JPanel();
		panel.setLayout(new MigLayout("fill", "", "[grow][]")); // columna/fila

		console = new JTextArea();
		console.setEditable(false);
		panel.add(console, "grow, wrap");

		add(panel);
		setLocationRelativeTo(null);

	}

	@Override
	public void run() {

		try {

			// Abre el selector
			selector = Selector.open();
			if (selector.isOpen()) console.append("Se abrio el selector\n");

			/* Abre el servidor. Ahora mismo el socket del servidor no esta vinculado; debe estar vinculado a una direccion
			 * especifica a traves de uno de los metodos de vinculacion de su socket antes de que se puedan aceptar las
			 * conexiones. */
			server = ServerSocketChannel.open();
			if (server.isOpen()) console.append("Se abrio el servidor\n");

			// Vincula el servidor a la diereccion local y puerto TCP
			server.bind(new InetSocketAddress(SERVER_PORT));

			// Configura el canal del servidor en modo sin bloqueo
			server.configureBlocking(false);
			if (!server.isBlocking()) console.append("Se configuro el servidor en modo sin bloqueo\n");

			// Registra el canal del servidor con el selector y le asigna una clave
			server.register(selector, SelectionKey.OP_ACCEPT); // Para mas de un evento: | SelectionKey.OP_READ
			if (server.isRegistered()) console.append("Se registro el servidor con el selector para aceptar conexiones!\n");

			while (true) {

				/* FIXME Se sigue ejecutando el bucle cuando solo hay una conexion para aceptar. Una posible solucion seria cancelar la
				 * clave una vez utilizada. */

				console.append("Esperando la operacion de seleccion en el puerto " + SERVER_PORT + "...\n");

				// Bloquea el servidor hasta que un canal este listo
				selector.select();

				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

				while (keys.hasNext()) {

					SelectionKey key = keys.next();

					/* El Selector no elimina las instancias de SelectionKey del propio conjunto de keys seleccionadas. Tienes que hacer
					 * esto cuando hayas terminado de procesar el canal. La proxima vez que el canal este "listo", el selector la agregara
					 * al conjunto de keys seleccionadas nuevamente. */
					keys.remove();

					// Comprueba si el canal de esta clave esta listo para aceptar una nueva conexion
					if (key.isAcceptable()) {

						/* TODO Creo que no hace falta obtener el canal desde la key ya que es lo mismo que usar la instancia server de esta
						 * clase. */
						// ServerSocketChannel server = (ServerSocketChannel) key.channel();

						// Acepta la conexion del cliente
						SocketChannel client = server.accept();

						/* En el modo sin bloqueo, el metodo accept() regresa inmediatamente y puede devolver un valor nulo si no hubiera
						 * llegado una conexion entrante. Por lo tanto, verifica si el SocketChannel devuelto es nulo. */
						if (client != null && client.isConnected()) {

							console.append(client.getRemoteAddress() + " se conecto!\n");

							client.configureBlocking(false);

							// Registra el canal del cliente y le asigna una clave
							client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

						} else console.append("El cliente no se pudo conectar!\n");

					}

					// Comprueba si el canal de esta clave esta listo para leer
					else if (key.isReadable()) {

						// Obtiene el canal y el buffer de la clave registrada en la conexion del cliente
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer buf = (ByteBuffer) key.attachment();

						// Mientras no se haya leido todo el buffer
						while (client.read(buf) > 0) {

							// Voltea el buf para poder leerlo
							buf.flip();

							// Mientras haya bytes entre la posicion y el limite del buffer, muestra la representacion del byte en caracter
							while (buf.hasRemaining())
								console.append("" + (char) buf.get());

							console.append("\n");

							// "Limpia" el buffer para rellenarlo con los bytes sobrantes del canal
							buf.clear();

						}

						// ?
						key.interestOps(SelectionKey.OP_READ);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error de I/O\n" + e.toString(), "Error en el servidor", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error en el servidor", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} finally {
			try {
				server.close();
				selector.close(); // Cierra el Selector e invalida todas las instancias de SelectionKey registradas con este Selector
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error al cerrar la conexion\n" + e.toString(), "Error en el servidor",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public static void main(String[] args) throws IOException {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new ServerNonBlocking().setVisible(true);
	}

}
