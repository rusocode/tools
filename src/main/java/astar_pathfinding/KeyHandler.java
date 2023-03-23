package astar_pathfinding;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

	private final AStar aStar;

	public KeyHandler(AStar aStar) {
		this.aStar = aStar;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/* Cada vez que se presiona enter, el algoritmo evalua los nodos adyacentes y encuentra el mejor candidato hasta
		 * que alcance el objetivo. */
		if (e.getKeyCode() == KeyEvent.VK_ENTER) aStar.autoSearch();
	}

}
