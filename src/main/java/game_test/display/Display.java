package game_test.display;

import javax.swing.*;
import java.awt.*;

/**
 * Un componente Canvas representa un area rectangular en blanco de la pantalla en la que la aplicacion puede dibujar o
 * desde la cual la aplicacion puede atrapar eventos de entrada del usuario.
 *
 * <p>Su origen de coordenadas comienza en la esquina superior izquierda.
 */

public class Display extends JFrame {

	private Canvas canvas;

	private final String title;
	private final int width, height;
	public static final int SCALE = 3;

	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		createDisplay();
	}

	private void createDisplay() {

		setTitle(title);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);
		setLocationRelativeTo(null);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);

		add(canvas);
		// pack(); // TODO Hace falta?

		setVisible(true);

	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return this;
	}

}
