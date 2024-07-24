package concurrency;

import java.awt.geom.*;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Game {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al establecer el LookAndFeel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		new Marco().setVisible(true);
	}

}

class Rectangle {

	static int tiempo_bloqueado = 6;

	// Tamaño del rectangulo
	private static final int RECTANGLE_WIDTH = 15;
	private static final int RECTANGLE_HEIGHT = 15;
	// Coordenadas
	private double x = 0, y = 0; // posicion absoluta
	private double dx = 1, dy = 1; // deltas

	public void mover(Rectangle2D limites) {

		// FIXME Por que el ancho de la lamina es de 284? Si cree una ventana de 300 de ancho

		x += dx;
		y += dy;

		// Invierte x e y (-1) en caso de que el rectangulo toque los limites de la lamina
		if (x <= 0) dx = -dx;
		if (x >= limites.getMaxX() - RECTANGLE_WIDTH) dx = -dx;
		if (y <= 0) dy = -dy;
		if (y >= limites.getMaxY() - RECTANGLE_HEIGHT) dy = -dy;

	}

	// Crea e inicializa un Rectangle2D a partir de las coordenadas y el tamaño especificado
	public Rectangle2D getShape() {
		return new Rectangle2D.Double(x, y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
	}

}

class Hilo implements Runnable {

	private Rectangle rectangle;
	private Component component;

	public Hilo(Rectangle rectangle, Component component) {
		this.rectangle = rectangle;
		this.component = component;
	}

	@Override
	public void run() {

		System.out.println(MyThread.currentThread().getName() + " >> en ejecucion!");

		/* Mientras el hilo del objeto no se haya interrumpido.
		 * Diferencia entre isInterrupted() y interrupted():
		 * https://stackoverflow.com/questions/1904072/java-difference-in-usage-between-thread-interrupted-and-thread-
		 * isinterrupted
		 * 
		 * Diferencia entre interrupt, interrupted y isInterrupted
		 * https://www.programmersought.com/article/7473981424/#:~:text=3.,true%20for%20the%20first%20time. */
		while (!MyThread.currentThread().isInterrupted()) { // No borra su estado, pero con la llamada estatica si (Thread.interrupted)
			// Dibuja el cuadrado llamando al metodo paintComponent() de la clase Lamina
			// component.paint(component.getGraphics());
			/* La desaparacion de los botones ocurre porque los componentes swing se estan ejecutando en el mismo hilo que el run de
			 * tu thread. Es decir, que para que los componentes de swing sean estables, debes crear ese thread como un nuevo objeto
			 * en otra clase. Esto se debe a que los componentes de swing llevan un "tipo" de thread interno que fluctua si existe
			 * un Thread contenido (run() dentro de la misma clase).
			 *
			 * Tambien se puede usar el metodo repaint() para solucionar el problema del parpadeo. El repaint hace un refresco de
			 * pantalla, el paint lo pinta todo de nuevo componente a componente. */
			component.repaint();

			try {
				/* Bloquea el hilo durante una cantidad determinada de tiempo.
				 * Cuando se intenta interrumpir un hilo bloqueado, lanza una excepcion de tipo InterruptedException. */
				MyThread.sleep(Rectangle.tiempo_bloqueado);
			} catch (InterruptedException e) {
				MyThread.currentThread().interrupt();
				System.out.println(
						MyThread.currentThread().getName() + " >> " + (MyThread.currentThread().isInterrupted() ? "terminado!" : "en ejecucion!"));
			}

			// Cambia los valores de x e y, en otras palabras... mueve el cuadrado
			rectangle.mover(component.getBounds());

		}

	}

}

class Marco extends JFrame {

	private Lamina lamina;

	private JButton btnArranca1, btnArranca2, btnDetener1, btnDetener2;
	private JButton btnRelentizar;

	Thread h1, h2;

	public Marco() {

		setTitle("Blocks");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 300);
		setLocationRelativeTo(null);

		initialize();

	}

	private void initialize() {

		lamina = new Lamina();

		add(lamina, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel();

		btnArranca1 = new JButton("Arranca 1");
		btnArranca1.setFocusable(false);
		btnArranca1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start(e);
			}
		});
		panelBotones.add(btnArranca1);

		btnArranca2 = new JButton("Arranca 2");
		btnArranca2.setFocusable(false);
		btnArranca2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start(e);
			}
		});
		panelBotones.add(btnArranca2);

		btnDetener1 = new JButton("Detener 1");
		btnDetener1.setFocusable(false);
		btnDetener1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				interrupt(e);
			}
		});
		panelBotones.add(btnDetener1);

		btnDetener2 = new JButton("Detener 2");
		btnDetener2.setFocusable(false);
		btnDetener2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				interrupt(e);
			}
		});
		panelBotones.add(btnDetener2);

		btnRelentizar = new JButton("Relentizar");
		btnRelentizar.setFocusable(false);
		btnRelentizar.addActionListener(new ActionListener() {

			boolean bandera = false;

			@Override
			public void actionPerformed(ActionEvent evt) {

				if (btnRelentizar.getText().equals("Relentizar")) {
					Rectangle.tiempo_bloqueado = 50;
					btnRelentizar.setText("Acelerar");
					bandera = false;
				}

				if (btnRelentizar.getText().equals("Acelerar") && bandera == true) {
					Rectangle.tiempo_bloqueado = 6;
					btnRelentizar.setText("Relentizar");
				}

				bandera = true;

			}

		});
		panelBotones.add(btnRelentizar);

		crearBoton(panelBotones, "Limpiar", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				lamina.clear();
			}
		});

		add(panelBotones, BorderLayout.SOUTH);
	}

	private void crearBoton(Container c, String titulo, ActionListener oyente) {
		JButton boton = new JButton(titulo);
		boton.setFocusable(false);
		boton.addActionListener(oyente);
		c.add(boton);
	}

	public void start(ActionEvent e) {

		Rectangle rectangle = new Rectangle();

		lamina.add(rectangle);

		if (e.getSource().equals(btnArranca1)) {
			// Crea un hilo para el rectangulo
			h1 = new Thread(new Hilo(rectangle, lamina));
			// Inicia el hilo
			h1.start();
		}

		if (e.getSource().equals(btnArranca2)) {
			h2 = new Thread(new Hilo(rectangle, lamina));
			h2.start();
		}

	}

	public void interrupt(ActionEvent e) {

		if (e.getSource().equals(btnDetener1) && h1 != null)
			/* Si este hilo esta bloqueado en una invocacion de los metodos wait(), join() o sleep(), entonces su estado de
			 * interrupcion se borrara y recibira una InterruptedException. */
			h1.interrupt();

		if (e.getSource().equals(btnDetener2) && h2 != null) h2.interrupt();

	}

}

class Lamina extends JPanel {

	private static final long serialVersionUID = -7436842110508511195L;

	private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

	// Agrega el rectangulo al ArrayList
	public void add(Rectangle rectangle) {
		rectangles.add(rectangle);
	}

	public void clear() {
		rectangles.clear();
		// Limpia los rectangulos detenidos
		for (int i = 0; i < rectangles.size(); i++)
			rectangles.set(i, null);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// Controla cada rectangulo del ArrayList
		for (Rectangle rectangle : rectangles)
			/* Rellena el interior de una forma utilizando la configuracion del contexto Graphics2D. Los atributos de renderizado
			 * aplicados incluyen Clip, Transform, Paint y Composite. */
			g2.fill(rectangle.getShape());

	}

}
