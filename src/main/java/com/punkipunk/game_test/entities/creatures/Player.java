package com.punkipunk.game_test.entities.creatures;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.punkipunk.game_test.Game;
import com.punkipunk.game_test.entities.Entity;
import com.punkipunk.game_test.gfx.*;
import com.punkipunk.game_test.inventory.Inventory;
import com.punkipunk.game_test.utils.Timer;
import com.punkipunk.game_test.worlds.World;

/**
 * Dibuja el player en el centro de la pantalla mientras se mantiene en movimiento, aunque en realidad lo que se mueve
 * es el mundo. El player solo se mantiene quieto en el centro de la pantalla del jugador.
 *
 * <p>Para ubicar el player en el centro de la pantalla, se resta o suma el desplazamiento central de la pantalla a la
 * posicion actual del player. Para la <i>primera</i> posicion de <b>x</b> se hace {@code 640-272}, y para <b>y</b>
 * {@code 640-372}. Este calculo desplaza el player al centro de la pantalla, moviendolo 272 pixeles a la izquierda y
 * 372 pixeles arriba, quedando en la posicion {@code x=368, y=268}.
 *
 * <p>TODO Se podria implementar una maquina de estados para la administracion de animaciones.
 */

public class Player extends Creature {

	private final Timer attackTimer;
	private final Animation down, up, left, right;
	private BufferedImage currentFrame;
	private final Inventory inventory;

	// Coordenadas del cuadro de ataque
	private int tx, ty;

	public Player(Game game, World world, float x, float y) {
		super(game, world, x, y, Assets.player.getWidth() / 6, Assets.player.getHeight() / 4);

		attackTimer = new Timer(1000); // TODO Obtener el valor del timer desde la clase Constants

		// TODO Error si son numeros impares?
		rectangle.width = 18;
		rectangle.height = 44;
		rectangle.x = 16;
		rectangle.y = 40;

		int animationSpeed = 90;
		down = new Animation(animationSpeed, Assets.player_down);
		up = new Animation(animationSpeed, Assets.player_up);
		left = new Animation(animationSpeed, Assets.player_left);
		right = new Animation(animationSpeed, Assets.player_right);
		currentFrame = down.getFirstFrame(); // Frame inicial

		inventory = new Inventory(game);
	}

	/**
	 * Actualiza la posicion del player.
	 */
	@Override
	public void tick() {
		down.tick();
		up.tick();
		left.tick();
		right.tick();
		getInput();
		move();
		camera.centerOnEntity(this, world);
		checkAttacks();
		inventory.tick();
	}

	/**
	 * Dibuja el player aplicando el desplazamiento.
	 *
	 * @param g pincel.
	 */
	@Override
	public void render(Graphics g) {
		g.drawImage(getCurrentAnimationFrame(), (int) (x - camera.getxOffset()), (int) (y - camera.getyOffset()), width, height, null);
		g.setColor(Color.blue);
		// g.fillRect((int) (x + rectangle.x - handler.getCamera().getxOffset()), (int) (y + rectangle.y - handler.getCamera().getyOffset()), rectangle.width, rectangle.height);
		g.setColor(Color.red);
		// g.fillRect((int) (tx - handler.getCamera().getxOffset()), (int) (ty - handler.getCamera().getyOffset()), 20, 20);
	}

	/**
	 * TODO Aplicar el patron Observer para representar el inventario desde otra clase.
	 * <a href="https://www.tutorialspoint.com/design_pattern/observer_pattern.htm">Observer Pattern</a>
	 */
	public void postRender(Graphics g) {
		inventory.render(g);
	}

	@Override
	public void die() {
		System.out.println("You die!");
	}

	private void checkAttacks() {

		// No puede atacar si no se completo el cooldown de ataque
		if (attackTimer.checkCooldown()) return;
		// No puede atacar si el inventario esta activado
		if (inventory.isActive()) return;

		// Balance de colision para obtener las coordenadas del cuadro de colision para nuestro jugador
		Rectangle cb = getCollisionBounds(0, 0);

		// Crea un rectangulo de ataque de 20x20
		Rectangle ar = new Rectangle();
		int arSize = 20;
		ar.width = arSize;
		ar.height = arSize;

		// Calcula las coordenadas dependiendo de la direccion de ataque
		if (keyManager.aUp && currentFrame == up.getFirstFrame()) {
			ar.x = cb.x + cb.width / 2 - arSize / 2;
			ar.y = cb.y - arSize;
		} else if (keyManager.aDown && currentFrame == down.getFirstFrame()) {
			ar.x = cb.x + cb.width / 2 - arSize / 2;
			ar.y = cb.y + cb.height;
		} else if (keyManager.aLeft && currentFrame == left.getFirstFrame()) {
			ar.x = cb.x - arSize;
			ar.y = cb.y + cb.height / 2 - arSize / 2;
		} else if (keyManager.aRight && currentFrame == right.getFirstFrame()) {
			ar.x = cb.x + cb.width;
			ar.y = cb.y + cb.height / 2 - arSize / 2;
		} else return; // Sale en caso de no atacar ninguna entidad y evita ejecutar el siguiente codigo

		tx = ar.x;
		ty = ar.y;

		attackTimer.resetTimer();

		// Itera cada entidad del mundo para comprobar con cual colisiono el rectangulo de ataque
		for (Entity e : world.getEntityManager().getEntities()) {
			if (e.equals(this)) continue; // Evita atacarse asi mismo
			// Si el cuadro de la entidad intersecciona con el cuadro de ataque
			if (e.getCollisionBounds(0, 0).intersects(ar)) {
				e.hurt(5);
				return;
			}
		}

	}

	/**
	 * Obtiene el tipo de entrada en teclado y en base a eso, actualiza el movimiento de x o y.
	 */
	private void getInput() {
		// El player no puede moverse si el inventario esta activado
		if (inventory.isActive()) return;

		xMove = 0;
		yMove = 0;

		// Forma orientada a objetos en la que ahora puede hacer que diferentes criaturas se muevan a diferentes velocidades
		if (keyManager.up) {
			yMove = -speed;
			return;
		}
		if (keyManager.down) {
			yMove = speed;
			return;
		}
		if (keyManager.left) {
			xMove = -speed;
			return;
		}
		if (keyManager.right) xMove = speed;
	}

	/**
	 * Obtiene el frame de animacion actual dependiendo del movimiento (abajo, arriba, izquierda o derecha). En caso de
	 * que el player este detenido, devuelve el frame con el ultimo movimiento realizado. Por defecto, empieza con el
	 * frame hacia abajo.
	 *
	 * <p>Si el player colisiona con una entidad o un tile solido, entonces devuelve el primer frame en la direccion
	 * especificada y no genera animacion alguna.
	 *
	 * @return el frame de animacion actual.
	 */
	private BufferedImage getCurrentAnimationFrame() {
		if (yMove > 0) {
			currentFrame = down.getFirstFrame();
			return colisionEntity || colisionTile ? down.getFirstFrame() : down.getCurrentFrame();
		} else if (yMove < 0) {
			currentFrame = up.getFirstFrame();
			return colisionEntity || colisionTile ? up.getFirstFrame() : up.getCurrentFrame();
		} else if (xMove < 0) {
			currentFrame = left.getFirstFrame();
			return colisionEntity || colisionTile ? left.getFirstFrame() : left.getCurrentFrame();
		} else if (xMove > 0) {
			currentFrame = right.getFirstFrame();
			return colisionEntity || colisionTile ? right.getFirstFrame() : right.getCurrentFrame();
		}
		return currentFrame;
	}

	public Inventory getInventory() {
		return inventory;
	}

}
