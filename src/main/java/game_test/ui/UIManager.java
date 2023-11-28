package game_test.ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import game_test.Game;

/**
 * Tiene la misma funcionalidad que la clase EntityManager.
 */

public class UIManager {

	private Game game;
	private ArrayList<Component> components;

	// TODO Hace falta usar un handler aca?
	public UIManager(Game game) {
		this.game = game;
		components = new ArrayList<>();
	}

	public void tick() {
		for (Component component : components) component.tick();
	}

	public void render(Graphics g) {
		for (Component component : components) component.render(g);
	}

	public void onMouseRelease() {
		for (Component component : components) component.onMouseRelease();
	}

	public void onMouseMove(MouseEvent e) {
		for (Component component : components) component.onMouseMove(e);
	}

	public void addComponent(Component component) {
		components.add(component);
	}

	public void removeComponent(Component component) {
		components.remove(component);
	}


	public ArrayList<Component> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<Component> components) {
		this.components = components;
	}

}
