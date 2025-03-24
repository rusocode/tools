package com.punkipunk.game_test.utils;

/**
 * Aplica un timestep fijo a 60 ticks para que el juego se actualize a la misma velocidad en cualquier maquina
 * independientemente de los FPS.
 *
 * @author Juan Debenedetti
 */

public class Timestep {

	private int timer;
	private long startTime;
	private final double delta;
	private double elapsed, lag;

	public Timestep(final int ticks) {
		startTime = System.nanoTime();
		delta = 1e9 / ticks;
	}

	/**
	 * Comprueba si el tiempo transcurrido alcanzo el tiempo delta.
	 *
	 * @return true si el tiempo transcurrido alcanzo el tiempo delta.
	 */
	public boolean checkDelta() {
		long currentTime = System.nanoTime();
		elapsed = currentTime - startTime;
		lag += elapsed; // retraso
		timer += currentTime - startTime;
		startTime = currentTime;
		if (lag >= delta) {
			lag -= delta;
			return true;
		} else return false;
	}

	/**
	 * Comprueba si el timer alcanzo 1 segundo.
	 *
	 * @return true si el timer alcanzo 1 segundo.
	 */
	public boolean checkTimer() {
		return timer >= 1e9;
	}

	/**
	 * Resetea el timer a 0.
	 */
	public void resetTimer() {
		timer = 0;
	}


}

