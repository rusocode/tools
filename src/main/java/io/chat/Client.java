package io.chat;

import java.net.*;
import java.util.Map;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import static util.Constants.*;

/**
 * Clase encargada de conectarse con un servidor usando el protocolo TCP.
 * Tambien esta clase recibe conexiones por parte de otros clientes, por lo tanto necesita estar a la escucha.
 * 
 * Si el servidor este alojado en la misma maquina que el cliente, se utilizaria 127.0.0.1 o localhost como ip local.
 * En caso de que el servidor este en una maquina diferente a la del cliente pero en la misma red (lan), habria que
 * utilizar la ip remota.
 * 
 * FIXME ¿Por que no me puedo manejar con la misma conexion? Antes de mandar un mensaje tengo que crear un nuevo
 * Socket... osea no entiendo la logica, si con esa sola conexion ya tendria que comunicarme.
 * Porque el paquete IO crea una nueva conexion cada vez que se conecta. En cambio para manejar la misma conexion se usa
 * el paquete NIO.
 * 
 * @author Ru$o
 * 
 */

public class Client extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JLabel lblNick, lblServidor;
	private JTextField txtNick, txtServer, txtMessage;
	private JButton btnConnect;
	private JComboBox<String> combo;
	private JTextArea console;
	private JScrollPane scroll;

	private ServerSocket server;
	private Socket socketOut, socketIn;
	private ObjectInputStream streamIn;
	private ObjectOutputStream streamOut;
	private Packet packet;

	private Map<String, String> map;

	public Client() {

		super("Cliente");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();

		new Thread(this).start();

	}

	private void initialize() {

		panel = new JPanel();
		panel.setLayout(new MigLayout());

		lblNick = new JLabel();
		lblNick.setText("Nick");
		panel.add(lblNick);

		txtNick = new JTextField();
		txtNick.addKeyListener(new Listener());
		panel.add(txtNick, "w 100!");

		lblServidor = new JLabel("Servidor");
		panel.add(lblServidor);

		txtServer = new JTextField("localhost");
		txtServer.addKeyListener(new Listener());
		panel.add(txtServer, "w 100!");

		btnConnect = new JButton("Conectar");
		btnConnect.addActionListener(new Listener());
		btnConnect.setFocusable(false);
		btnConnect.setEnabled(false);
		/* Le paso un ancho de 93px (lo que ocupa "Desconectar") para que cuando se cambie el texto a "Desconectar" no se vea el
		 * agrandamiento de la ventana, ya que "Conectar" ocupa 77px. */
		panel.add(btnConnect, "w 93!, wrap");

		combo = new JComboBox<String>();
		combo.addActionListener(new Listener());
		combo.setFocusable(false);
		combo.setEnabled(false);
		panel.add(combo, "span, grow, wrap");

		console = new JTextArea();
		console.setEditable(false);
		scroll = new JScrollPane(console);
		panel.add(scroll, "h 300!, span, grow, wrap");

		txtMessage = new JTextField();
		txtMessage.addKeyListener(new Listener());
		txtMessage.setEnabled(false);
		panel.add(txtMessage, "span, grow");

		getContentPane().add(panel);

		pack();
		setLocationRelativeTo(null);

	}

	private class Listener extends KeyAdapter implements ActionListener {

		// Sirve para comprobar el estado del boton
		private boolean flag;

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == btnConnect) connect();
		}

		public void keyReleased(KeyEvent evt) {
			if (evt.getSource() == txtMessage && evt.getKeyChar() == KeyEvent.VK_ENTER) sendMessage();
			if (evt.getSource() == txtNick || evt.getSource() == txtServer) enableBTN();
		}

		/** Crea un puente virtual con el servidor en caso de que el cliente se conecte o desconecte. */
		private void connect() {

			try {

				socketOut = new Socket(txtServer.getText(), SERVER_PORT);
				streamOut = new ObjectOutputStream(socketOut.getOutputStream());
				packet = new Packet();
				packet.setNick(txtNick.getText());

				/* Si el cliente se conecto, envia el paquete con el tipo de conexion, deshabilita los campos de texto (para evitar que
				 * se modifiquen mientras este conectado), cambia el nombre del boton a "Desconectar", habilita el combo y habilita el
				 * campo de mensajes y le pasa el foco. */
				if (btnConnect.getText().equals("Conectar")) {

					packet.setConnected(true);
					streamOut.writeObject(packet);

					txtNick.setEnabled(false);
					txtServer.setEnabled(false);
					btnConnect.setText("Desconectar");
					combo.setEnabled(true);
					txtMessage.setEnabled(true);
					txtMessage.requestFocus();

					flag = false;

				}

				/* Si el cliente se desconecto, envia el paquete con el tipo de conexion, cierra la conexion, habilita los campos de
				 * texto, cambia el nombre del boton a "Conectar", limpia el combo y lo habilita, limpia la consola y limpia el campo de
				 * mensajes y lo habilita. */
				if (btnConnect.getText().equals("Desconectar") && flag == true) {

					// Obviamente envia el paquete antes de cerrar la conexion para avisarle al servidor que se desconecto
					packet.setConnected(false);
					streamOut.writeObject(packet);

					if (socketOut != null) socketOut.close();

					if (socketOut.isClosed()) {

						txtNick.setEnabled(true);
						txtServer.setEnabled(true);
						btnConnect.setText("Conectar");
						combo.removeAllItems();
						combo.setEnabled(false);
						console.setText(null);
						txtMessage.setText("");
						txtMessage.setEnabled(false);

					}

				}

				flag = true;

			} catch (UnknownHostException e) {
				Info.showClientMessage("No se pudo determinar la direccion IP del host\n" + e.toString());
			} catch (IOException e) {
				Info.showClientMessage("Error de I/O\n" + e.toString());
			}

		}

		private void enableBTN() {
			if (txtNick != null && txtServer != null) {
				if (!txtNick.getText().isEmpty() && !txtServer.getText().isEmpty()) btnConnect.setEnabled(true);
				else btnConnect.setEnabled(false);
			}
		}

		private void sendMessage() {

			try {

				// Si el mensaje no esta vacio...
				if (!txtMessage.getText().isEmpty()) {

					console.append(txtNick.getText() + ": " + txtMessage.getText() + "\n");

					socketOut = new Socket(txtServer.getText(), SERVER_PORT);
					streamOut = new ObjectOutputStream(socketOut.getOutputStream());
					packet = new Packet();
					packet.setConnected(true);
					packet.setNick(txtNick.getText());
					packet.setMessage(txtMessage.getText());
					packet.setIp(map.get(combo.getSelectedItem().toString()));
					streamOut.writeObject(packet);

					txtMessage.setText("");

					// Desplaza el scroll hacia abajo cuando hay mucho texto
					console.setCaretPosition(console.getDocument().getLength());

				}

			} catch (UnknownHostException e) {
				Info.showClientMessage("No se pudo determinar la direccion IP del host\n" + e.toString());
			} catch (IOException e) {
				Info.showClientMessage("Error de I/O\n" + e.toString());
			} finally {
				try {
					if (socketOut != null) socketOut.close();
				} catch (IOException e) {
					Info.showClientMessage("Error al cerrar la conexion\n" + e.toString());
				}
			}

		}

	}

	@Override
	public void run() {

		try {

			// Crea un servidor desde el cliente para poder recibir conexiones de otros clientes
			server = new ServerSocket(CLIENT_PORT);

			while (true) {

				socketIn = server.accept();
				streamIn = new ObjectInputStream(socketIn.getInputStream());
				packet = (Packet) streamIn.readObject();

				// Si recibio un mensaje...
				if (packet.getMessage() != null) console.append(packet.getNick() + ": " + packet.getMessage() + "\n");
				else { // Si un cliente se conecto o desconecto...

					// Actualiza los nicks del combo
					combo.removeAllItems();
					map = packet.getMap();
					for (String nick : map.keySet())
						combo.addItem(nick);
				}

				if (socketIn != null) socketIn.close();

			}

		} catch (SocketException e) {
			Info.showClientMessage("Ya tiene otro proceso vinculado al mismo puerto");
			System.exit(0);
		} catch (IOException e) {
			Info.showClientMessage("Error de I/O\n" + e.toString());
		} catch (IllegalArgumentException e) {
			Info.showClientMessage("El puerto esta fuera del rango especificado de valores de puerto validos, que estan entre 0 y 65535, inclusive\n"
					+ e.toString());
		} catch (ClassNotFoundException e) {
			Info.showClientMessage("No se pudo encontrar la clase del objeto serializado\n" + e.toString());
		} finally {
			try {
				if (socketIn != null) socketIn.close();
			} catch (IOException e) {
				Info.showClientMessage("Error al cerrar la conexion\n" + e.toString());
			}

		}

	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			Info.showClientMessage("Error al establecer el LookAndFeel: " + e.getMessage());
		}
		new Client().setVisible(true);
	}

}
