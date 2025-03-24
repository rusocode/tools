package com.punkipunk.game_test.gfx;

import java.awt.image.BufferedImage;

/**
 * La hoja de sprites (sprite sheet en ingles, tambien llamada <i>atlas de texturas</i>) es una imagen que contiene
 * varias imagenes mas pequeñas (sprites) y/o animaciones, generalmente agrupadas para reducir las dimensiones generales.
 * Un atlas puede consistir en imagenes de tamaño uniforme o imagenes de diferentes dimensiones. Para seleccionar una
 * imagen del atlas actual, se utilizan las coordenadas de textura personalizadas. Por ejemplo, para cargar la primera
 * imagen (tree), se utilizan las coordenadas {@code x=0, y=0}, especificando tambien, el ancho y alto de esa region
 * rectangular.
 *
 * <p>Para este tipo de juegos en el que se utilizan con frecuencia muchas texturas pequeñas, suele ser mas eficaz
 * meterlas en una hoja de spritres para que la GPU las trate como una sola unidad. Esto reduce tanto la sobrecarga de
 * E/S del disco como la sobrecarga de un cambio de contexto al aumentar la localidad de la memoria. Es posible que se
 * necesite una alineacion cuidadosa para evitar el sangrado entre las imagenes.
 *
 * <p>El formato mas completo para imagenes es el PNG. Este formato de imagen utiliza el modelo de color RGB que permite
 * manejar varias profundidades de color, desde el blanco y negro (1 bit por pixel), hasta mas de 16 millones de colores
 * con varios grados de transparencia (32 bit por pixel).
 *
 * <p><b>Consejo</b>
 * <br>
 * Se puede ahorrar un poco de memoria utilizando transparencia para el espacio sobrante del sprite sheet que usando
 * cualquier otro color. Y para la alineacion, el negro es una buena eleccion ya que ocupa menos espacio en memoria
 * (porque sus valores rgb estan en 0) que los colores como el morado (D67FFF) o el rosa (F714FF).
 *
 * <p>Recursos:
 * <a href="https://www.codeandweb.com/what-is-a-sprite-sheet">What is a sprite sheet?</a>
 * <a href="https://www.soyunignorante.es/el-formato-png/#:~:text=Por%20ello%2C%20trabaja%20%C3%BAnicamente%20con,(32%20bit%20por%20p%C3%ADxel)">Conociendo el formato PNG</a>
 * <a href="http://cv.uoc.edu/UOC/a/moduls/90/90_574b/web/main/m6/c2/12.html">Los colores de una imagen en pantalla</a>
 */

public class SpriteSheet {

	private final BufferedImage image;

	public SpriteSheet(BufferedImage sheet) {
		this.image = sheet;
	}

	/**
	 * Devuelve una subimagen definida por una region rectangular especificada. La BufferedImage devuelta comparte la
	 * misma matriz de datos que la imagen original.
	 *
	 * @param x      la coordenada X de la esquina superior izquierda de la region rectangular especificada.
	 * @param y      la coordenada Y de la esquina superior izquierda de la region rectangular especificada.
	 * @param width  el ancho de la region rectangular especificada.
	 * @param height la altura de la region rectangular especificada.
	 * @return una BufferedImage que es la subimagen de esta BufferedImage.
	 */
	public BufferedImage crop(int x, int y, int width, int height) {
		return image.getSubimage(x, y, width, height);
	}

	/**
	 * Metodo de prueba.
	 */
	public int[] getRGB(int x, int y, int width, int height) {
		int[] rgbArray = new int[width * height];
		return image.getRGB(x, y, width, height, rgbArray, 0, width);
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

}
