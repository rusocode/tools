package game_test.input;

import game_test.ui.UIManager;

import java.awt.event.*;

/**
 * Maneja las entradas del mouse.
 * <p>
 * TODO Se podria aplicar una especie de teletransportacion.
 */

public class MouseManager extends MouseAdapter {

	private UIManager uiManager;

	private boolean leftPressed, rightPressed;
	private int mouseX, mouseY;

	public MouseManager() {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) leftPressed = true;
		if (e.getButton() == MouseEvent.BUTTON3) rightPressed = true; // En una laptop el boton derecho es el BUTTON2
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) leftPressed = false;
		if (e.getButton() == MouseEvent.BUTTON3) rightPressed = false;
		if (uiManager != null) uiManager.onMouseRelease();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (uiManager != null) uiManager.onMouseMove(e);
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setUIManager(UIManager uiManager) {
		this.uiManager = uiManager;
	}

}