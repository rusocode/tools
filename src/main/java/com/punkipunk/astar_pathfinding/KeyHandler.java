package com.punkipunk.astar_pathfinding;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

	private final AStar aStar;

	public KeyHandler(AStar aStar) {
		this.aStar = aStar;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) aStar.autoSearch();
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) aStar.search();
	}

}
