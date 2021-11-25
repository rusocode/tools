package nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import static util.Constants.*;

/**
 * Un Selector es un *multiplexor de objetos SelectableChannel. En este caso se encarga de escuchar conexiones SIN
 * bloqueo.
 * 
 * El Selector es un objeto utilizado para seleccionar un canal listo para comunicarse (para realizar una operacion).
 * Es decir que se consulta al Selector si hay algun canal que este listo para operar sin bloqueo. Tiene la posibilidad
 * de elegir un canal seleccionable que este listo para algunas operaciones de red, por ejemplo, conectar, aceptar, leer
 * y escribir.
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
 * Cada registro del canal esta representado por un SelectionKey. Un selector funciona con un conjunto (set) de
 * SelectionKey. SelectionKey es un token que representa el registro de un canal con un selector. El selector mantiene
 * tres set de keys:
 * - "Key set" contiene las keys con los canales registrados; se obtienen usando el metodo keys().
 * - "Selected-key set" contiene las keys listas para al menos una de las operaciones; se obtienen usando el metodo
 * selectedKeys().
 * - "Cancelled-key set" contiene las keys canceladas cuyos canales aun no se han cancelado.
 * Los tres conjuntos están vacíos en un selector recién creado.
 * 
 * Cuando registras un canal con un selector, el metodo register() devuelve un objeto de tipo SelectionKey que
 * representa el registro de los canales con ese selector. Este objeto contiene algunas propiedades interesantes:
 * El interest set
 * El ready set
 * El Channel
 * El Selector
 * Y un objeto adjunto (opcional)
 * 
 * _______________________________
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

public class Selector_ extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JTextArea console;

	private Selector selector = null;
	private ServerSocketChannel server = null;

	public Selector_() {

		super("Servidor");
		setResizable(false);
		setSize(600, 430);
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
			if (!server.isBlocking()) console.append("Se configuro el servidor en modo sin bloqueo\n");

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
			if (server.isRegistered()) console.append("Se registro el servidor con el selector para aceptar conexiones!\n");

			/* Despues de registrar el canal del servidor para que acepte conexiones por medio de un selector, va a ponerse a la
			 * escucha. Ahora cuando un cliente se conecte al servidor por medio del canal, va a mostrar un mensaje. */
			while (true) {

				console.append("Esperando una conexion en el puerto " + SERVER_PORT + "...\n");

				/* Selecciona un conjunto de keys cuyos canales correspondientes estan listos para operaciones de I/O.
				 * Este metodo realiza una operacion de seleccion de bloqueo (es decir que se bloquea). Solo regresa despues de que se
				 * selecciona al menos un canal, se invoca el metodo wakeup() de este selector o se interrumpe el hilo actual, lo que
				 * ocurra primero. */
				selector.select(); // Seria como un server.accept()?

				// Itera las keys seleccionadas
				for (SelectionKey key : selector.selectedKeys()) {

					/* Verifica si la key es valida. Una key es valida en el momento de la creacion y permanece asi hasta que se cancela,
					 * se cierra su canal o se cierra su selector. */
					if (key.isValid()) {

						if (key.isAcceptable()) console.append("Se acepto una conexion!\n");
						if (key.isConnectable()) console.append("Se establecio una conexion con un servidor remoto!\n");
						if (key.isReadable()) console.append("Un canal esta listo para leer!\n");
						if (key.isWritable()) console.append("Un canal esta listo para escribir!\n");

					} else console.append("La key no es valida!\n");

					/* Solicita que se cancele el registro del canal de esta key con su selector. Al regresar, la key no sera valida y
					 * se habra agregado al conjunto de keys canceladas de su selector. La key se quitara de todos los conjuntos de
					 * keys del selector durante la siguiente operacion de seleccion. Esto evita que se repita el mensaje que representa esa
					 * key en cada vuelta del bucle.
					 * 
					 * ¿Pero es posible seguir aceptando conexiones despues de que se cancelo esa key? */
					key.cancel();

				}

			}

			// Itera las claves registradas en el selector
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
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error en el servidor", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
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

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Selector_().setVisible(true);
	}

}
