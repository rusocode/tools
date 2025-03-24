package com.punkipunk.game_test.entities.creatures;

import com.punkipunk.game_test.Game;
import com.punkipunk.game_test.entities.Entity;
import com.punkipunk.game_test.tiles.Tile;
import com.punkipunk.game_test.worlds.World;

public abstract class Creature extends Entity {

	public static final float DEFAULT_SPEED = 3.0f;
	protected float speed;
	protected float xMove, yMove;

	public Creature(Game game, World world, float x, float y, int width, int height) {
		super(game, world, x, y, width, height);
		speed = DEFAULT_SPEED;
		xMove = 0;
		yMove = 0;
	}

	/**
	 * Actualiza la posicion de la entidad si no hay colision con alguna entidad.
	 */
	public void move() {
		if (!checkEntityCollisions(xMove, 0f)) {
			moveX();
			colisionEntity = false;
		}
		if (!checkEntityCollisions(0f, yMove)) {
			moveY();
			colisionEntity = false;
		}
		// Si hay colision en x o y
		if (checkEntityCollisions(xMove, 0f) || checkEntityCollisions(0f, yMove)) colisionEntity = true;
	}

	/**
	 * Mueve la posicion de x.
	 */
	private void moveX() {
		int tx;
		if (xMove > 0) { // Derecha
			tx = (int) Math.floor((x + xMove + rectangle.x + rectangle.width) / Tile.TILE_WIDTH);
			// Si no hay colision con el tile de la derecha
			if (!collisionWithTile(tx, (int) Math.floor((y + rectangle.y) / Tile.TILE_HEIGHT)) && !collisionWithTile(tx, (int) Math.floor((y + rectangle.y + rectangle.height) / Tile.TILE_HEIGHT))) {
				x += xMove;
				colisionTile = false;
			} else {
				x = tx * Tile.TILE_WIDTH - rectangle.x - rectangle.width - 1;
				colisionTile = true;
			}
		} else if (xMove < 0) { // Izquierda

			/* Calcula la posicion x del cuerpo del player sumando la posicion actual en x, el movimiento y el
			 * desplazamiento x del cuerpo para que la colision solo se genere con el limite izquierdo del cuerpo y no
			 * con el borde izquierdo de la imagen. Luego, la suma se divide por el ancho del tile para obtener la
			 * posicion en terminos de tiles. Despues, la posicion se redondea hacia abajo utilizando la funcion floor()
			 * en caso de que el resultado de la posicion sea un decimal. Ademas, esto tambien se hace en caso de que si
			 * el jugador pasa los limites del mundo, por ejemplo, si pasa el limite izquierdo, entonces el primer tile
			 * despues del limite izquierdo del mundo es el -1, pero sin redondear hacia abajo se sigue "quedando" en el
			 * 0 ya que un valor como -0,234 casteado a entero da 0, siendo este un valor erroneo. Por lo tanto, la
			 * solucion del metodo floor() redondea -0,234 a -1.0. Por ultimo, se castea el valor double -1.0 a entero
			 * quedando x en -1. Si la posicion x del cuerpo esta en un tile solido, no actualiza la posicion de x. Si
			 * hay colision, entonces alinea el limite izquierdo del cuerpo con el borde derecho del tile ("retrocede"),
			 * generando asi, una colision izquierda perfecta. La colision izquierda perfecta se calcula sumando la
			 * posicion x del tile solido mas el ancho del tile. Al ancho del tile se le resta el desplazamiento x del
			 * cuerpo para que quede en una colision izquierda perfecta. */
			tx = (int) Math.floor((x + xMove + rectangle.x) / Tile.TILE_WIDTH); // Posicion temporal

			// Si no hay colision con el tile de la izquierda
			if ( /* esquina superior izquierda */ !collisionWithTile(tx, (int) Math.floor((y + rectangle.y) / Tile.TILE_HEIGHT)) &&
					/* esquina inferior izquierda */ !collisionWithTile(tx, (int) Math.floor((y + rectangle.y + rectangle.height) / Tile.TILE_HEIGHT))) {
				x += xMove;
				colisionTile = false;
			} else {
				x = tx * Tile.TILE_WIDTH + Tile.TILE_WIDTH - rectangle.x;
				colisionTile = true;
			}
		}
	}

	/**
	 * Mueve la posicion de y.
	 */
	private void moveY() {
		int ty;
		if (yMove > 0) { // Abajo
			ty = (int) Math.floor((y + yMove + rectangle.y + rectangle.height) / Tile.TILE_HEIGHT);
			// Si no hay colision con el tile de abajo
			if (!collisionWithTile((int) Math.floor((x + rectangle.x) / Tile.TILE_WIDTH), ty) && !collisionWithTile((int) Math.floor((x + rectangle.x + rectangle.width) / Tile.TILE_WIDTH), ty)) {
				y += yMove;
				colisionTile = false;
			} else {
				y = ty * Tile.TILE_HEIGHT - rectangle.y - rectangle.height - 1;
				colisionTile = true;
			}
		} else if (yMove < 0) { // Arriba
			ty = (int) Math.floor((y + yMove + rectangle.y) / Tile.TILE_HEIGHT);
			// Si no hay colision con el tile de arriba
			if (!collisionWithTile((int) Math.floor((x + rectangle.x) / Tile.TILE_WIDTH), ty) && !collisionWithTile((int) Math.floor((x + rectangle.x + rectangle.width) / Tile.TILE_WIDTH), ty)) {
				y += yMove;
				colisionTile = false;
			} else {
				y = ty * Tile.TILE_HEIGHT + Tile.TILE_HEIGHT - rectangle.y;
				colisionTile = true;
			}
		}
	}

	/**
	 * Comprueba si el tile de la posicion especificada es solido.
	 *
	 * @param x posicion del tile en el eje x del mundo.
	 * @param y posicion del tile en el eje y del mundo.
	 * @return true si el tile de la posicion especificada es solido.
	 */
	private boolean collisionWithTile(int x, int y) {
		return world.getTile(x, y).isSolid();
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getxMove() {
		return xMove;
	}

	public void setxMove(float xMove) {
		this.xMove = xMove;
	}

	public float getyMove() {
		return yMove;
	}

	public void setyMove(float yMove) {
		this.yMove = yMove;
	}

}