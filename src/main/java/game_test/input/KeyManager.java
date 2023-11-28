package game_test.input;

import java.awt.event.*;

/**
 * Maneja las entradas del teclado.
 */

public class KeyManager implements KeyListener {

	private boolean[] keys, justPressed, cantPress;
	public boolean up, down, left, right;
	public boolean aUp, aDown, aLeft, aRight;

	public KeyManager() {
		keys = new boolean[256];
		justPressed = new boolean[256];
		cantPress = new boolean[256];
	}

	/**
	 * Actualiza las teclas presionadas y soltadas.
	 */
	public void tick() {

		/* Itera cada codigo de tecla posible almacenado dentro de estos arreglos, asegurando de que solo se pueda
		 * presionar una tecla. */
		for (int i = 0; i < keys.length; i++) {
			// Si no puede presionar y si ya no se presiono
			if (cantPress[i] && !keys[i]) cantPress[i] = false;
			else if (justPressed[i]) { // Si se presiono
				cantPress[i] = true;
				justPressed[i] = false;
			}
			// Si puede presionar y si ya se presiono (la tecla E por ejemplo)
			if (!cantPress[i] && keys[i]) justPressed[i] = true; // Aca entra por primera vez
		}

		// Si se presiono la tecla E
		// if (keyJustPressed(KeyEvent.VK_E)) System.out.println("Se acaba de presionar la tecla E");

		// Teclas de movimiento
		up = keys[KeyEvent.VK_W]; // Para multiples keys "| keys[KeyEvent.VK_UP];"
		down = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];

		// Teclas de ataque
		aUp = keys[KeyEvent.VK_UP];
		aDown = keys[KeyEvent.VK_DOWN];
		aLeft = keys[KeyEvent.VK_LEFT];
		aRight = keys[KeyEvent.VK_RIGHT];
	}

	/**
	 * Cuando se presiona una tecla.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// Evita cualquier tipo de error
		if (e.getKeyCode() < 0 || e.getKeyCode() >= keys.length) return;
		keys[e.getKeyCode()] = true;
	}

	/**
	 * Cuando se suelta una tecla.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < 0 || e.getKeyCode() >= keys.length) return;
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public boolean keyJustPressed(int keyCode) {
		if (keyCode < 0 || keyCode >= keys.length) return false;
		return justPressed[keyCode];
	}

}