package game_test.gfx;

import java.awt.image.BufferedImage;

/**
 * Administra los frames de cada animacion.
 */

public class Animation {

	private final int speed;
	private int index;
	private long lastTime, timer;
	private final BufferedImage[] frames;

	public Animation(int speed, BufferedImage[] frames) {
		this.speed = speed;
		this.frames = frames;
		index = 0;
		timer = 0;
		lastTime = System.currentTimeMillis();
	}

	/**
	 * Actualiza el frame dependiendo de la velocidad. Si el temporizador alcanzo la velocidad especificada, entonces
	 * cambia de frame incrementando el indice. Si llego al ultimo indice, vuelve al primer frame.
	 */
	public void tick() {
		timer += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis(); // El player se vuelve loco sin esta linea xD
		if (timer > speed) {
			index++;
			timer = 0;
			if (index >= frames.length) index = 0;
		}
	}

	/**
	 * Obtiene el frame actual.
	 *
	 * @return el frame actual.
	 */
	public BufferedImage getCurrentFrame() {
		return frames[index];
	}

	/**
	 * Obtiene el primer frame.
	 *
	 * @return el primer frame.
	 */
	public BufferedImage getFirstFrame() {
		return frames[0];
	}

}
