package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class Dialogo extends JFrame {

	private JPanel lamina_cuadricula, lamina_mostrar;

	private Caja caja_tipo, caja_tipo_mensaje, caja_mensaje, caja_tipo_opcion, caja_opcion, caja_entrada;

	private String[] tipo = { "Mensaje", "Confirmar", "Opcion", "Entrada" };
	private String[] tipo_mensaje = { "ERROR_MESSAGE", "INFORMATION_MESSAGE", "WARNING_MESSAGE", "QUESTION_MESSAGE", "PLAIN_MESSAGE" };
	private String[] mensaje = { "Cadena", "Icono", "Componente", "Otros", "Object[]" };
	private String[] tipo_opcion = { "DEFAULT_OPTION", "YES_NO_OPTION", "YES_NO_CANCEL_OPTION", "OK_CANCEL_OPTION" };
	private String[] opcion = { "String[]", "Icon[]", "Object[]" };
	private String[] entrada = { "Campo de texto", "Combo" };

	private JButton btnMostrar;

	private Object[] objetos_mensaje;

	public Dialogo() {

		setFrameProperties();

	}

	public static void main(String[] args) {
		new Dialogo().setVisible(true);
	}

	private void setFrameProperties() {

		setTitle("Prueba de dialogos");
		setLayout(new BorderLayout());
		setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {

			e.printStackTrace();
		}

		makeGUI();

	}

	private void makeGUI() {

		// Crea la lamina cuadricula.
		lamina_cuadricula = new JPanel();
		lamina_cuadricula.setLayout(new GridLayout(2, 3));

		// Crea las cajas con sus caracteristicas.
		caja_tipo = new Caja("Tipo", tipo);
		caja_tipo_mensaje = new Caja("Tipo de mensaje", tipo_mensaje);
		caja_mensaje = new Caja("Mensaje", mensaje);
		caja_tipo_opcion = new Caja("Confirmar", tipo_opcion);
		caja_opcion = new Caja("Opcion", opcion);
		caja_entrada = new Caja("Entrada", entrada);

		// Agrega las cajas a la lamina.
		lamina_cuadricula.add(caja_tipo);
		lamina_cuadricula.add(caja_tipo_mensaje);
		lamina_cuadricula.add(caja_mensaje);
		lamina_cuadricula.add(caja_tipo_opcion);
		lamina_cuadricula.add(caja_opcion);
		lamina_cuadricula.add(caja_entrada);

		// Crea la lamina con el boton.
		lamina_mostrar = new JPanel();
		btnMostrar = new JButton("Mostrar");
		btnMostrar.addActionListener(new AccionMostrar());
		lamina_mostrar.add(btnMostrar);

		// Agrega las laminas al marco.
		add(lamina_cuadricula, BorderLayout.CENTER);
		add(lamina_mostrar, BorderLayout.SOUTH);
	}

	private class AccionMostrar implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// Almacena en la variable radio de tipo String el texto del radio seleccionado en la caja_tipo.
			String radio = caja_tipo.dameSeleccion();

			/* Muestra los diferentes dialogos dependiendo del radio seleccionado en caja_tipo.
			 * parentComponent: determina el marco en el que se muestra el dialogo; si es nulo, o si el componente principal no
			 * tiene
			 * marco, se usa un marco predeterminado.
			 * message: el objeto a mostrar.
			 * title: la cadena de titulo para el dialogo.
			 * optionType: un entero que designa las opciones disponibles en el cuadro de dialogo: DEFAULT_OPTION, YES_NO_OPTION,
			 * YES_NO_CANCEL_OPTION o OK_CANCEL_OPTION.
			 * messageType: un entero que designa el tipo de mensaje que es; se utiliza principalmente para determinar el icono
			 * desde
			 * el aspecto y conexion conectables: ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE o
			 * PLAIN_MESSAGE. */
			if (radio.equals("Mensaje")) JOptionPane.showMessageDialog(Dialogo.this, getMensaje(), "Titulo", getTipo(caja_tipo_mensaje));
			if (radio.equals("Confirmar"))
				JOptionPane.showConfirmDialog(Dialogo.this, getMensaje(), "Titulo", getTipo(caja_tipo_opcion), getTipo(caja_tipo_mensaje));
			if (radio.equals("Opcion")) JOptionPane.showOptionDialog(Dialogo.this, getMensaje(), "Titulo", 1, getTipo(caja_tipo_mensaje), null,
					getOpciones(caja_opcion), null);
			if (radio.equals("Entrada")) {
				if (caja_entrada.dameSeleccion().equals("Campo de texto"))
					JOptionPane.showInputDialog(Dialogo.this, getMensaje(), "Titulo", getTipo(caja_tipo_mensaje));
				// Crea un dialogo con una matriz de objetos que da las posibles selecciones.
				if (caja_entrada.dameSeleccion().equals("Combo")) JOptionPane.showInputDialog(Dialogo.this, getMensaje(), "Titulo",
						getTipo(caja_tipo_mensaje), null, new String[] { "Amarillo", "Azul", "Rojo" }, "Azul");
			}

		}

	}

	private Object getMensaje() {

		// Tipos de mensajes.
		String cadena_mensaje = "Mensaje";
		Icon icono_mensaje = new ImageIcon("src/img/azul.png"); // ImageIcon implementa la interfaz Icon.
		// C:\\Users\\Juan\\Documents\\Eclipse\\Aplicaciones graficas\\src\\img\\azul.png <-- Ruta local.

		// JPanel hereda de Component. Es decir, JPanel es un Component, y Component puede ser un JPanel u otro componente.
		Component componente_mensaje = new JPanel() {

			public void paintComponent(Graphics g) {

				super.paintComponent(g);

				Graphics2D g2 = (Graphics2D) g;

				// Crea un rectangulo en la posicion x=0;y=0, con el ancho y alto de la propia lamina.
				Rectangle2D rectangulo = new Rectangle2D.Double(0, 0, getWidth(), getHeight());

				// Pinta el rectangulo de verde.
				g2.setPaint(Color.GREEN);
				// Rellena el interior de una Figura/Forma/Shape (rectangulo) utilizando la configuracion (Color, etc.) del contexto
				// Graphics2D.
				g2.fill(rectangulo);

			}
		};
		Object objeto_mensaje = new Date();
		objetos_mensaje = new Object[] { cadena_mensaje, icono_mensaje, componente_mensaje, objeto_mensaje };

		// Determina que radio se selecciono.
		String radio = caja_mensaje.dameSeleccion();

		if (radio.equals("Cadena")) return cadena_mensaje;
		if (radio.equals("Icono")) return icono_mensaje;
		if (radio.equals("Componente")) return componente_mensaje;
		if (radio.equals("Otros")) return objeto_mensaje;
		if (radio.equals("Object[]")) return objetos_mensaje;

		return null;

	}

	private int getTipo(Caja caja) {

		String radio = caja.dameSeleccion();

		// Devuelve el tipo de mensaje y el tipo de opcion.
		if (radio.equals("PLAIN_MESSAGE") || radio.equals("DEFAULT_OPTION")) return -1;
		if (radio.equals("ERROR_MESSAGE") || radio.equals("YES_NO_OPTION")) return 0;
		if (radio.equals("INFORMATION_MESSAGE") || radio.equals("YES_NO_CANCEL_OPTION")) return 1;
		if (radio.equals("WARNING_MESSAGE") || radio.equals("OK_CANCEL_OPTION")) return 2;
		if (radio.equals("QUESTION_MESSAGE")) return 3;

		return -1;

	}

	private Object[] getOpciones(Caja caja) {

		String radio = caja.dameSeleccion();

		if (radio.equals("String[]")) return new String[] { "Amarillo", "Azul", "Rojo" };
		if (radio.equals("Icon[]"))
			return new Object[] { new ImageIcon("src/img/amarillo.png"), new ImageIcon("src/img/azul.png"), new ImageIcon("src/img/rojo.png") };
		if (radio.equals("Object[]")) return objetos_mensaje;

		return null;

	}

}

class Caja extends JPanel {

	// Crea un grupo para cada radio.
	ButtonGroup grupo = new ButtonGroup();

	public Caja(String titulo, String[] opciones) {

		// Crea un borde simple con titulo para la caja.
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), titulo));

		// setBorder(new TitledBorder(titulo));

		// Crea la caja y ordena verticalmente los componentes que hay dentro de esta.
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for (int i = 0; i < opciones.length; i++) {

			// Crea un radio con el titulo de la posicion i.
			JRadioButton radio = new JRadioButton(opciones[i]);

			// Estable para cada boton la accion de comando que corresponde al texto del boton.
			radio.setActionCommand(opciones[i]);

			// Selecciona el primer radio.
			radio.setSelected(i == 0);

			// Agrupa los radio.
			grupo.add(radio);

			// Agrega el radio a la lamina.
			add(radio);

		}

	}

	// Metodo con alcance de paquete. Quiero que sea publico para mi paquete y no para otros.
	String dameSeleccion() {

		// ButtonModel boton = grupo.getSelection();
		// String texto = boton.getActionCommand();
		//
		// return texto;

		// Devuelve el texto del boton seleccionado de la caja especificada.
		return grupo.getSelection().getActionCommand();

	}

}