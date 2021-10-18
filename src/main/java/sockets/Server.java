package sockets;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import static sockets.Constants.*;

/**
 * Clase encargada de gestionar las conexiones que entran y salen.
 * 
 * TODO Cambiar el mensaje de envio hacia una ip a un mensaje de envio hacia un nick.
 * 
 * @author Ru$o
 * 
 */

public class Server extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JTextArea console;
	private JScrollPane scroll;
	private JButton btnClean;

	// Servidor
	private ServerSocket server;
	// Cliente origen y destino
	private Socket socketOut, socketIn;
	// Flujos de entrada y salida
	private ObjectInputStream streamIn;
	private ObjectOutputStream streamOut;
	// Paquete con la informacion del cliente
	private Packet packet;
	// Coleccion de tipo HashMap para guardar pares de claves y valores (nick/ip)
	private Map<String, String> map = new HashMap<>();

	public Server() {

		super("Servidor");
		setResizable(false);
		setSize(399, 430);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();

		// Crea un hilo para el servidor y lo inicia
		new Thread(this).start();

	}

	private void initialize() {
		panel = new JPanel();
		panel.setLayout(new MigLayout("fill", "", "[grow][]")); // columna/fila

		console = new JTextArea();
		console.setEditable(false);
		scroll = new JScrollPane(console);
		panel.add(scroll, "grow, wrap");

		btnClean = new JButton("Limpiar consola");
		btnClean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				console.setText(null);
			}
		});
		btnClean.setFocusable(false);
		panel.add(btnClean, "alignx right");

		add(panel);
		setLocationRelativeTo(null);

	}

	@Override
	public void run() {

		try {

			/* Crea un servidor abriendo el puerto especificado, es decir que crea como un "enchufe" (socket en ingles) para recibir
			 * conexiones. */
			server = new ServerSocket(SERVER_PORT);

			// Constantemente a la escucha...
			while (true) {

				console.append("Esperando una conexion en el puerto " + server.getLocalPort() + "...\n");

				console.setCaretPosition(console.getDocument().getLength());

				/* Bloquea el metodo hasta que se establezca una conexion, es decir que hasta que no se acepte una conexion, el flujo
				 * del programa (la siguiente linea) no se ejecuta. */
				socketIn = server.accept();

				// Crea un flujo de entrada para recibir paquetes de la conexion
				streamIn = new ObjectInputStream(socketIn.getInputStream());

				// Deserializa del flujo el objeto y lo castea a Packet para poder usarlo
				packet = (Packet) streamIn.readObject();

				/* ¿Por que vuelve a verificar si el cliente se conecto? Si se supone que el servidor ya acepto una conexion. Porque el
				 * cliente se puede conectar o desconectar, es decir que el servidor no puede comprobar si se desconecto, por lo tanto
				 * necesita aceptar una conexion y ver el estado de la conexion por parte de ese cliente, es decir si es true
				 * (conectado) o false (desconectado). */
				if (packet.isConnected()) { // Si se conecto...

					console.append("El cliente " + socketIn.getInetAddress() + ":" + socketIn.getPort() + " se conecto!\n");

					// Si envio un mensaje...
					if (packet.getMessage() != null) {

						// Agrega a la consola los datos del paquete recibido
						console.append(packet.getNick() + ": " + packet.getMessage() + " >> " + packet.getIp() + "\n");
						// Crea un puente virtual con el cliente especificado
						socketOut = new Socket(packet.getIp(), CLIENT_PORT);
						// Crea un flujo de salida para enviar paquetes
						streamOut = new ObjectOutputStream(socketOut.getOutputStream());
						// Escribe el paquete serializado en el flujo
						streamOut.writeObject(packet);

					} else { // Si solo se conecto...

						// Agrega a la coleccion el nick asociado con la ip
						map.put(packet.getNick(), socketIn.getInetAddress().getHostAddress());

						// Agrega la coleccion al paquete
						packet.setMap(map);

						// Le avisa a los otros clientes de la nueva conexion
						for (String ip : map.values()) {
							socketOut = new Socket(ip, CLIENT_PORT);
							streamOut = new ObjectOutputStream(socketOut.getOutputStream());
							streamOut.writeObject(packet);
						}

					}

				} else { // Si se desconecto...

					console.append("El cliente " + socketIn.getInetAddress() + ":" + socketIn.getPort() + " se desconecto!\n");

					/* Devuelve un iterador sobre los elementos del conjunto y se lo pasa a un Iterator para poder borrar un item de la
					 * coleccion sin que lance un ConcurrentModificationException. */
					Iterator<Entry<String, String>> it = map.entrySet().iterator();

					while (it.hasNext()) {
						// Itera cada entrada y la alamacena en un Entry para poder manipular el conjuto
						Entry<String, String> entry = it.next();
						// Si el nick de la coleccion es igual al nick del paquete, lo elimina de la coleccion
						if (entry.getKey().equals(packet.getNick())) it.remove();
					}

					packet.setMap(map);

					for (String ip : map.values()) {
						socketOut = new Socket(ip, CLIENT_PORT);
						streamOut = new ObjectOutputStream(socketOut.getOutputStream());
						streamOut.writeObject(packet);
					}

				}

				// Cierra las conexiones (al cerrar el socket tambien se cierran los flujos)
				if (socketIn != null) socketIn.close();
				if (socketOut != null) socketOut.close();
			}

		} catch (SocketException e) {
			Info.showServerMessage("Ya tiene otro proceso vinculado al mismo puerto");
			System.exit(0); // Cierra la aplicacion en caso de que se abra un servidor en el mismo puerto
		} catch (IOException e) {
			Info.showServerMessage("Error de I/O\n" + e.toString());
		} catch (IllegalArgumentException e) {
			Info.showServerMessage("El puerto esta fuera del rango especificado de valores de puerto validos, que estan entre 0 y 65535, inclusive\n"
					+ e.toString());
		} catch (ClassNotFoundException e) {
			Info.showServerMessage("No se pudo encontrar la clase del objeto serializado\n" + e.toString());
		} finally {
			try {
				// Cierra las conexiones en finally en caso de que se produzca una excepcion
				if (socketIn != null) socketIn.close();
				if (socketOut != null) socketOut.close();
			} catch (IOException e) {
				Info.showServerMessage("Error al cerrar la conexion\n" + e.toString());
			}

		}

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			Info.showServerMessage("Error al establecer el LookAndFeel: " + e.getMessage());
		}

		new Server().setVisible(true);

	}

}
