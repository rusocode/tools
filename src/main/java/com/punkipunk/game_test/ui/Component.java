package com.punkipunk.game_test.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * Caracteristicas y funciones principales del componente.
 *
 * <p>TODO Se podria crear o usar una libreria externa para la UI.
 */

public abstract class Component {

	protected float x, y;
	protected int width, height;
	protected Rectangle rectangle;
	protected boolean over;

	public Component(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rectangle = new Rectangle((int) x, (int) y, width, height);
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public abstract void onClick();

	/**
	 * Permite hacer click solo si el mouse esta sobre el componente.
	 */
	public void onMouseRelease() {
		if (over) onClick();
	}

	/**
	 * Comprueba si el mouse esta sobre el componente.
	 *
	 * @param e evento del mouse.
	 */
	public void onMouseMove(MouseEvent e) {
		over = rectangle.contains(e.getX(), e.getY());
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

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

}