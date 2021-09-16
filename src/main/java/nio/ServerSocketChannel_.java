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

/**
 * Un canal seleccionable para escuchar conexiones.
 * 
 * ¿Por que usar nio para hacer una aplicacion de chat... cuando simplemente podemos hacerlo usando la programacion de
 * socket facilmente?
 * 
 * Porque debido a que nio es sin bloqueo, no requiere subprocesos adicionales. Un chat basado en sockets requeriria
 * tantos subprocesos como usuarios, lo que agregaria una sobrecarga significativa, mientras que un chat nio siempre
 * necesitaria un solo subproceso, lo que lo haria mucho mas escalable, ya que la sobrecarga de subprocesos puede
 * volverse realmente significativa.
 * 
 * ...
 * No necesariamente. En medidas reales de Java en Linux, los multiprocesos de los diseños clasicos de I/O superan a NIO
 * en un 30% aproximadamente (ver "Libro de programacion de la red Java").
 * Java NIO supera a un diseño clasico de I/O solo si:
 * 1. Tienes una enorme cantidad de clientes > 20.000.
 * 2. El intervalo entre paquetes de datos para enviar es muy corto.
 * Conclusion: Necesita un servidor para > 20.000 clientes con comunicacion de alta frecuencia.
 * 
 * Fuente: https://gist.github.com/Botffy/3860641
 * 
 * @author Juan Debenedetti aka Ru$o
 * 
 */

public class ServerSocketChannel_ extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JTextArea console;

	public ServerSocketChannel_() {
		super("Servidor");
		setResizable(false);
		setSize(399, 430);
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

			// Abre un canal para el socket de servidor
			ServerSocketChannel serverChannel = ServerSocketChannel.open();

			// Recupera un socket de servidor asociado con este canal
			ServerSocket server = serverChannel.socket();

			/* Vincula el socket del canal a una direccion local y configura el socket para escuchar conexiones.
			 * Ahora el servidor puede recibir solicitudes de conexion que estaran en cola hasta que se acepten. */
			server.bind(new InetSocketAddress(7666));

			while (true) {

				console.append("Esperando una conexion...\n");

				SocketChannel socketChannel = serverChannel.accept();

				console.append("Se acepto la conexion!\n");

				/* Un ServerSocketChannel se puede configurar en modo sin bloqueo. En el modo sin bloqueo, el metodo accept() regresa
				 * inmediatamente y, por lo tanto, puede devolver un valor nulo, si no hubiera llegado una conexion entrante. Por lo
				 * tanto, debera verificar si el SocketChannel devuelto es nulo. */
				if (socketChannel != null) {

				}

			}

		} catch (IOException e) {
			System.err.println("Error de I/O\n" + e.toString());
		}
	}

	public static void main(String[] args) throws IOException {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new ServerSocketChannel_().setVisible(true);
	}

}
