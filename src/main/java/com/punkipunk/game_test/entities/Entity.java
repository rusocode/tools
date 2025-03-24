package com.punkipunk.game_test.entities;

import java.awt.*;

import com.punkipunk.game_test.Game;
import com.punkipunk.game_test.gfx.Camera;
import com.punkipunk.game_test.input.KeyManager;
import com.punkipunk.game_test.worlds.World;

/**
 * La entidad es cualquier cosa que NO sea un tile, como por ejemplo: item, player o una roca.
 */

public abstract class Entity {

	protected Game game;
	protected World world;
	protected Camera camera;
	protected KeyManager keyManager;

	public static final int DEFAULT_HEALTH = 10;
	protected float x, y;
	protected int width, height;
	protected Rectangle rectangle; // Rectangulo que se usa como delimitador para detectar colisiones contra entidades y tiles solidos
	protected int health;
	protected boolean dead, pickedUp, colisionEntity, colisionTile;

	public Entity(Game game, World world, float x, float y, int width, int height) {
		this.game = game;
		this.world = world;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rectangle = new Rectangle(0, 0, width, height);
		camera = game.getCamera();
		keyManager = game.getKeyManager();
		health = DEFAULT_HEALTH;
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public abstract void die();

	/**
	 * Daña a la entidad y si la vida es menor o igual a 0, muere.
	 *
	 * @param amt cantidad de daño.
	 */
	public void hurt(int amt) {
		health -= amt;
		if (health <= 0) {
			dead = true;
			die();
		}
	}

	/**
	 * Itera cada entidad y determina si su area delimitadora colisiona con el area delimitadora de la otra entidad. Dos
	 * rectangulos se intersecan si su interseccion no esta vacia.
	 *
	 * <p>TODO Se podria implementar un QuadTree en caso de que haya muchas entidades.
	 *
	 * @param xOffset el desplazamiento de la entidad en el eje x.
	 * @param yOffset el desplazamiento de la entidad en el eje y.
	 * @return true si colisiona con esta entidad.
	 */
	public boolean checkEntityCollisions(float xOffset, float yOffset) {
		for (Entity e : world.getEntityManager().getEntities()) {
			// Ignora la colision contra si mismo y contra los items
			if (e.equals(this) || e instanceof Item) continue;
			if (e.getCollisionBounds(0, 0).intersects(getCollisionBounds(xOffset, yOffset))) return true;
		}
		return false;
	}

	/**
	 * Obtiene el area delimitadora de la entidad.
	 *
	 * @param xOffset el desplazamiento de la entidad en el eje x.
	 * @param yOffset el desplazamiento de la entidad en el eje y.
	 * @return el area delimitadora de la entidad.
	 */
	public Rectangle getCollisionBounds(float xOffset, float yOffset) {
		return new Rectangle((int) (x + rectangle.x + xOffset), (int) (y + rectangle.y + yOffset), rectangle.width, rectangle.height);
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isDead() {
		return dead;
	}

	public boolean isPickedUp() {
		return pickedUp;
	}

	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}

}
