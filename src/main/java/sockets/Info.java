package sockets;

import javax.swing.JOptionPane;

public class Info {

	public static void showClientMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error en el cliente", JOptionPane.ERROR_MESSAGE);
	}

	public static void showServerMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error en el servidor", JOptionPane.ERROR_MESSAGE);
	}

}
