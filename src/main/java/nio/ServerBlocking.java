package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import static utils.Constants.*;

/**
 * Canal de servidor TCP para escuchar conexiones CON bloqueo. Se puede configurar sin bloqueo con
 * configureBlocking(false), haciendo posible su uso con un Selector.
 * <p>
 * Para comunicaciones UDP se usa el canal de DatagramChannel.
 * <p>
 * ¿Por que usar NIO para hacer una conexion de sockets cuando tambien se puede usar el flujo I/O clasico?
 * Porque debido a que nio es sin bloqueo, no requiere subprocesos adicionales. Un chat basado en sockets requeriria
 * tantos subprocesos como usuarios, lo que agregaria una sobrecarga significativa, mientras que un chat nio siempre
 * necesitaria un solo subproceso, lo que lo haria mucho mas escalable, ya que la sobrecarga de subprocesos puede
 * volverse realmente significativa.
 * <p>
 * ...
 * No necesariamente. En medidas reales de Java en Linux, los multiprocesos de los diseños clasicos de I/O superan a NIO
 * en un 30% aproximadamente (ver "Libro de programacion de la red Java").
 * Java NIO supera a un diseño clasico de I/O solo si:
 * 1. Tienes una enorme cantidad de clientes > 20.000.
 * 2. El intervalo entre paquetes de datos para enviar es muy corto.
 * Conclusion: Necesita un servidor para > 20.000 clientes con comunicacion de alta frecuencia.
 * <p>
 * Fuente: <a href="https://gist.github.com/Botffy/3860641">...</a>
 *
 * @author Ruso
 */

public class ServerBlocking extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JTextArea console;

	public ServerBlocking() {
		super("Servidor");
		setResizable(false);
		setSize(399, 430);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();

		new Thread(this).start();
	}

	private void initialize() {

		JPanel panel = new JPanel();
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

			// Abre el servidor
			ServerSocketChannel server = ServerSocketChannel.open();

			/* Usar el modo sin bloqueo sin un selector no tendria sentido. Para eso se creo la clase Selector, para gestionar las
			 * conexiones que van entrando sin bloqueo. Esto ahorra muchos subprocesos utilizando un solo Thread para las
			 * conexiones. */
			// server.configureBlocking(false);

			/* Recupera un socket de servidor asociado con este canal y lo vincula a una direccion local para escuchar conexiones.
			 * Ahora el servidor puede recibir solicitudes de conexion que estaran en cola hasta que se acepten. */
			ServerSocket socket = server.socket();
			socket.bind(new InetSocketAddress(SERVER_PORT));

			while (true) {

				console.append("Esperando una conexion en el puerto " + socket.getLocalPort() + "...\n");

				SocketChannel socketChannel = server.accept();

				console.append("El cliente " + socketChannel.getRemoteAddress() + " se conecto!\n");

				/* Un ServerSocketChannel se puede configurar en modo sin bloqueo. En el modo sin bloqueo, el metodo accept() regresa
				 * inmediatamente y, por lo tanto, puede devolver un valor nulo, si no hubiera llegado una conexion entrante. Por lo
				 * tanto, debera verificar si el SocketChannel devuelto es nulo. */
				if (socketChannel != null) {
					// console.append("El cliente " + socketChannel.getRemoteAddress() + " se conecto!\n");
				}

			}

		} catch (IOException e) {
			System.err.println("Error de I/O\n" + e);
		}
	}

	public static void main(String[] args) throws IOException {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new ServerBlocking().setVisible(true);
	}

}
