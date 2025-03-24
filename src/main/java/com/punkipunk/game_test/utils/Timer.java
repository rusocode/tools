package com.punkipunk.game_test.utils;

/**
 * El cooldown deshabilita la posibilidad de realizar una accion en un determinado tiempo.
 *
 * <p>TODO Habria algun problema si declaro los metodos como estaticos?
 */

public class Timer {

	private long timer, startTime;
	private final long cooldown;

	public Timer(final long cooldown) {
		this.cooldown = cooldown;
		// timer = cooldown; // TODO Es necesario?
	}

	/**
	 * Comprueba si el timer completo el cooldown.
	 *
	 * @return true si el timer es menor al cooldown.
	 */
	public boolean checkCooldown() {
		timer += System.currentTimeMillis() - startTime;
		startTime = System.currentTimeMillis();
		return timer < cooldown;
	}

	/**
	 * Resetea el timer a 0.
	 */
	public void resetTimer() {
		timer = 0;
	}

}
