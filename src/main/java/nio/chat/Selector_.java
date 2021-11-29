package nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import static util.Constants.*;

/**
 * El Selector es un *multiplexor de objetos SelectableChannel encargado de seleccionar canales sin bloqueo.
 * 
 * Un canal seleccionable puede estar en modo de bloqueo o en modo sin bloqueo. En el modo de bloqueo, cada operacion de
 * I/O invocada en el canal se bloqueara hasta que se complete. En el modo sin bloqueo, una operacion de I/O nunca se
 * bloqueara y puede transferir menos bytes de los solicitados o posiblemente ningun byte. El modo de bloqueo de un
 * canal seleccionable puede determinarse invocando su metodo isBlocking().
 * 
 * Los canales seleccionables recien creados estan siempre en modo de bloqueo. El modo sin bloqueo es mas util junto con
 * la multiplexacion basada en selectores. Un canal debe colocarse en modo de no bloqueo antes de ser registrado con un
 * selector, y no puede volver al modo de bloqueo hasta que se haya cancelado su registro. En caso de registrar el
 * servidor con un selector que fue configurado con bloqueo, lanzara un IllegalBlockingModeException.
 * 
 * El Selector es un objeto utilizado para seleccionar un canal listo para comunicarse (para realizar una operacion).
 * Es decir que se consulta al Selector si hay algun canal que este listo para operar sin bloqueo. Tiene la posibilidad
 * de elegir un canal seleccionable que este listo para algunas operaciones de red, por ejemplo, conectar, aceptar, leer
 * y escribir.
 * 
 * Se dice que un canal que "dispara un evento" esta "listo" para ese evento. Por lo tanto, un canal que se ha
 * conectado correctamente a otro servidor esta "listo para conectarse". Un canal de socket de servidor que acepta una
 * conexion entrante esta listo para "aceptar". Un canal que tiene datos listos para ser leidos esta listo para "leer".
 * Un canal que esta listo para escribir datos en el, esta listo para "escribir".
 * 
 * Un canal puede registrarse como maximo una vez con cualquier selector en particular y estos canales seleccionables
 * son seguros para su uso por varios subprocesos simultaneos.
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

public class Selector_ extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JTextArea console;

	private Selector selector = null;
	private ServerSocketChannel server = null;

	public Selector_() {

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

			// Registra el canal con el selector y le pasa el tipo de evento en el que va a estar "listo"
			server.register(selector, SelectionKey.OP_ACCEPT); // Para mas de un evento: | SelectionKey.OP_READ
			if (server.isRegistered()) console.append("Se registro el servidor con el selector para aceptar conexiones!\n");

			// Ciclo del servidor
			/* Solo hay un hilo que maneja el servidor. Seria una pesadilla intentar sincronizar el bloqueo entre diferentes
			 * subprocesos. */
			while (true) {

				// FIXME ¿Por que sigue aceptando la misma conexion?

				console.append("Esperando una conexion en el puerto " + SERVER_PORT + "...\n");

				// Se bloquea hasta que al menos un canal este listo para los eventos para los que se registro
				selector.select(); // Seria como un server.accept()?

				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

				while (keys.hasNext()) {

					SelectionKey key = keys.next();

					if (key.isAcceptable()) console.append("Se acepto una conexion!\n");

					/* El Selector no elimina las instancias de SelectionKey del propio conjunto de keys seleccionadas. Tienes que hacer
					 * esto cuando hayas terminado de procesar el canal. La proxima vez que el canal este "listo", el selector la agregara
					 * al conjunto de keys seleccionadas nuevamente. */
					keys.remove();

				}

			}

		} catch (IOException e) {
			System.err.println("Error de I/O\n" + e.toString());
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error en el servidor", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} finally {
			try {
				server.close();
				// Cierra el Selector e invalida todas las instancias de SelectionKey registradas con este Selector
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
