package game_test.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

/**
 * Clase de utilidades en donde carga los recursos del classpath.
 *
 * <p>TODO Implementar JOptionPane para cada mensaje.
 */

public class Utils {

	private Utils() {

	}

	/**
	 * Carga la imagen utilizando la ruta especificada.
	 *
	 * @param path la ruta del recurso.
	 * @return una BufferedImage que contiene el contenido decodificado de la entrada.
	 */
	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path)));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	/**
	 * Carga el archivo utilizando la ruta especificada.
	 *
	 * @param path la ruta del recurso.
	 * @return el archivo como cadena.
	 */
	public static String loadFileAsString(String path) {

		StringBuilder builder = new StringBuilder();
		BufferedReader buffer = null;

		try {

			buffer = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path))));

			String line;

			// Mientras el archivo no llegue al final de linea...
			while ((line = buffer.readLine()) != null)
				builder.append(line + "\n");

		} catch (NullPointerException e) {
			System.err.println("No se encontro el archivo especificado");
		} catch (IOException e) {
			System.err.println("Error de I/O: " + e.getMessage());
		} finally {
			try {
				if (buffer != null) buffer.close();
			} catch (IOException e) {
				System.err.println("Error al cerrar el flujo de datos: " + e.getMessage());
			}
		}
		return builder.toString();
	}

	/**
	 * Carga la fuente utilizando la ruta especificada.
	 *
	 * @param path la ruta del recurso.
	 * @param size el tamaño de la fuente.
	 * @return un objeto Font con el tipo de fuente especificado.
	 */
	public static Font loadFont(String path, float size) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(path))).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	/**
	 * Dibuja una cadena de texto en pantalla.
	 *
	 * @param g      el pincel.
	 * @param text   la cadena.
	 * @param xPos   la posicion en x.
	 * @param yPos   la posicion en y.
	 * @param center si la cadena debe ir centrada o no.
	 * @param c      el color de la cadena.
	 * @param font   el tipo de fuente.
	 */
	public static void drawString(Graphics g, String text, int xPos, int yPos, boolean center, Color c, Font font) {
		g.setColor(c);
		g.setFont(font);
		int x = xPos;
		int y = yPos;
		if (center) {
			FontMetrics fm = g.getFontMetrics(font);
			x = xPos - fm.stringWidth(text) / 2;
			y = (yPos - fm.getHeight() / 2) + fm.getAscent();
		}
		g.drawString(text, x, y);
	}

	/**
	 * Convierte el caracter numerico que representa el tile del mapa en un entero.
	 *
	 * @param number caracter numerico.
	 * @return el caracter numerico convertido a entero.
	 */
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
