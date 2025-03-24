package com.punkipunk.game_test.tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.punkipunk.game_test.gfx.Assets;

/**
 * Al ver un juego tipico de estrategia o aventura en 2D, muchos se preguntan como poder almacenar escenarios de tan
 * grandes proporciones sin pedir, como requerimientos minimos, cantidades ingentes de memoria RAM. La tecnica de los
 * "tiles" permite construir escenarios utilizando pequeñas porciones de distintas texturas que, poniendolas de forma
 * adyacente (unas al lado de otras), encajan a la perfeccion dando la sensacion de que todo lo que vemos es un grafico
 * de una sola pieza. Asi, los tiles permiten construir escenarios o mapas mediante la utilizacion inteligente de
 * pequeñas porciones graficas que se identifican por sus diferentes texturas. Entonces, podemos crear un escenario de
 * grandes proporciones que simule un prado mediante la union de tiles que tengan la misma textura. En este caso, la
 * textura deberia de simular ese color propio de un terreno verde. La utilizacion de tiles ha sido muy provechosa
 * para la creacion de escenarios en juegos de estrategia o aventuras "a vista de pajaro" que permiten a los
 * programadores disponer de mapas fabricados a partir de matrices que toman diferentes valores dependiendo del tile que
 * se representa en pantalla.
 *
 * <p>La implementacion principal para representar los tiles, consiste en disponerlos de tal forma que simulen un
 * escenario visto desde arriba, tal como se aplica aqui.
 *
 * <p>Esta clase se encarga de definir los diferentes tipos de tiles que existen, almacenandolos en una matriz. Cada
 * tile se identifica por su textura e id.
 *
 * <p>Un tip de eficiencia, es usar los tiles en 64x64, ya que al agrandar el tamaño se van a renderizar menos recursos
 * durante el juego.
 *
 * <p><i>Importante: Esta clase se carga en memoria por primera vez cuando se accede al campo estatico {@link Tile#tiles}
 * desde la linea {@code Tile tile = Tile.tiles[tiles[x][y]];} en la clase {@link game_test.worlds.World#World World}.</i>
 *
 * <p>Recursos:
 * <a href="http://macedoniamagazine.frodrig.com/epja1.htm">Introduccion a la tecnica de los "tiles"</a>
 *
 * @author Juan
 */

public class Tile {

	/* El beneficio de usar una matriz de Tile, es que solo se crean los tiles (grass, dirt, stone, etc.) una vez y se
	 * reutilizan con la matriz. */
	public static Tile[] tiles = new Tile[256];
	public static Tile dirtTile = new Tile(Assets.dirt, 0, false);
	public static Tile grassTile = new Tile(Assets.grass, 1, false);
	public static Tile brickTile = new Tile(Assets.brick, 2, false);
	public static Tile invisibleTile = new Tile(null, 255, false);

	public static final int TILE_WIDTH = 64, TILE_HEIGHT = 64; // TODO Mover a clase de constantes
	public final BufferedImage texture;
	private final int id;
	private final boolean solid;

	public Tile(BufferedImage texture, int id, boolean solid) {
		this.texture = texture;
		this.id = id;
		this.solid = solid;
		tiles[id] = this;
	}

	/**
	 * Tiles animados.
	 */
	public void tick() {

	}

	/**
	 * Dibuja el tile.
	 *
	 * @param g pincel.
	 * @param x posicion del tile en el eje x.
	 * @param y posicion del tile en el eje y.
	 */
	public void render(Graphics g, int x, int y, int width, int height) {
		g.drawImage(texture, x, y, width, height, null);
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public int getId() {
		return id;
	}

	/**
	 * Verifica si el tile es solido.
	 *
	 * @return false si el tile no es solido.
	 */
	public boolean isSolid() {
		return solid;
	}

}
