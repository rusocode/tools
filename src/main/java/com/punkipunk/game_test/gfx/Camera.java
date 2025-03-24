package com.punkipunk.game_test.gfx;

import com.punkipunk.game_test.Game;
import com.punkipunk.game_test.entities.Entity;
import com.punkipunk.game_test.tiles.Tile;
import com.punkipunk.game_test.worlds.World;

/**
 * La camara es el punto de vista del jugador en el juego.
 *
 * <p>Las camaras de punto fijo y de desplazamiento a menudo se agrupan como 2D. Los juegos de plataformas de mundo
 * abierto, usan una camara que sigue directamente al jugador, manteniendolo siempre en el centro de la pantalla.
 *
 * <p>Para este juego se utiliza el sistema de camara <a href="https://es-academic.com/dic.nsf/eswiki/924763"><i>Top-Down</i></a>,
 * tambien llamado "vista de pajaro" y se juega como si estuvieras mirando desde arriba hacia abajo. Este sistema es
 * parecido al <b>3/4</b>.
 *
 * <p>Para que la camara se centre en el jugador, se aplican los <i>desplazamientos</i>.
 *
 * <br><br>
 *
 * <h3>¿Que son los desplazamientos?</h3>
 * Los desplazamientos (offsets), son las coordenadas <b>x</b> e <b>y</b> de la esquina superior izquierda de la vista
 * de la camara, que nos dicen que tan lejos (restando o sumando) dibujamos algo (tile o entidad) de su posicion
 * original. Para el primer ejemplo, la vista tiene origen en {@code (0, 0)} de la pantalla. Ahora si la camara se mueve
 * hacia la derecha (+x), los tiles se desplazan hacia la izquierda restando la posicion original. Si la camara se mueve
 * hacia la izquierda (-x), los tiles se desplazan hacia la derecha sumando la posicion original. Esto mismo se aplica
 * para el eje y.
 *
 * <p>Es importante tener en cuenta que la multiplicacion de signos negativos da como resultado un signo positivo. Esto
 * quiere decir que si la camara se mueve hacia la izquierda (-x) en 1, entonces suma la posicion original de los tiles
 * en 1, ya que la instruccion {@code - (-1)} es igual a 1.
 *
 * <br><br>
 *
 * <h3>¿Como centrar la camara en la entidad?</h3>
 * Primero se calcula el centro de la pantalla restando la mitad del ancho de la ventana con la mitad del ancho de la
 * entidad, lo mismo para la altura. Ahora, el centro de la pantalla es la vista de la camara. Despues, a la posicion
 * del player se le resta el centro de la pantalla, obteniendo asi, un <i>"seguimiento" central de la camara en la entidad</i>.
 *
 * <p>Es necesario aplicar este desplazamiento tanto para el mundo como para la entidad, para que ambos mantengan un
 * "movimiento" sincronizado.
 *
 * <p>Nota: No confundir el centro de la pantalla con el centro del mundo.
 *
 * @author Juan Debenedetti
 */

public class Camera {

	private final Game game;
	private float xOffset, yOffset;

	// TODO No es al pedo pasar por parametro los desplazamientos? Ya que se calculan en esta clase
	public Camera(Game game, float xOffset, float yOffset) {
		this.game = game;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/**
	 * Calcula el desplazamiento para centrar la entidad en la pantalla. En otras palabras, centra la camara en la
	 * entidad.
	 *
	 * @param entity la entidad actual.
	 */
	public void centerOnEntity(Entity entity, World world) {
		float screenCenterX = (float) game.getWidth() / 2 - (float) entity.getWidth() / 2;
		float screenCenterY = (float) game.getHeight() / 2 - (float) entity.getHeight() / 2;
		xOffset = entity.getX() - screenCenterX; // 640 - 368 = 272
		yOffset = entity.getY() - screenCenterY; // 640 - 268 = 372
		//CleanCameraBounds(world);
	}

	/**
	 * Mueve la camara.
	 */
	public void move(float xAmt, float yAmt) {
		xOffset += xAmt;
		yOffset += yAmt;
		//checkBlankSpace();
	}

	/**
	 * Limita la vista de la camara a los bordes del mundo. Esto quiere decir que la vista solo llega hasta los limites
	 * derechos, izquierdos, superiores e inferiores del mundo, evitando asi, mostrar los tiles invisibles.
	 *
	 * <p>Si pasa los limites del mundo, entonces las coordenadas de desplazamiento se resetean. Esto va a hacer que la
	 * camara no "siga" al player.
	 *
	 * <p>En caso de que el mundo sea mas chico que la ventana, entonces lo centra en la pantalla.
	 */
	private void CleanCameraBounds(World world) {
		// Si el mundo es mas grande que la ventana
		if (world.getWidth() * Tile.TILE_WIDTH > game.getWidth() && world.getHeight() * Tile.TILE_HEIGHT > game.getHeight()) {
			/* Si la coordenda de la camara en x es menor a 0, entonces deja de "moverse" y tambien deja de centrar el
			 * player en la pantalla. */
			if (xOffset < 0) xOffset = 0;
			else if (xOffset > world.getWidth() * Tile.TILE_WIDTH - game.getWidth())
				xOffset = world.getWidth() * Tile.TILE_WIDTH - game.getWidth();
			if (yOffset < 0) yOffset = 0;
			else if (yOffset > world.getHeight() * Tile.TILE_HEIGHT - game.getHeight())
				yOffset = world.getHeight() * Tile.TILE_HEIGHT - game.getHeight();
		} else { // Si el mundo es mas chico que la ventana, entonces centra el mundo en la pantalla
			xOffset = (float) -(game.getWidth() - world.getWidth() * Tile.TILE_WIDTH) / 2;
			yOffset = (float) -(game.getHeight() - world.getHeight() * Tile.TILE_HEIGHT) / 2;
		}
	}

	public float getxOffset() {
		return xOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

}
