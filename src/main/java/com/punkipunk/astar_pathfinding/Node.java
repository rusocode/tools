package com.punkipunk.astar_pathfinding;

import javax.swing.*;
import java.awt.*;

/**
 * Los nodos representan los Tiles en el caso de un juego 2D.
 *
 * <p>Para evaluar el camino mas corto, el algoritmo A* usa tres numeros y se llaman G Cost, H Cost y F Cost. El G Cost
 * significa la distancia entre la posicion inicial y la posicion actual. El H Cost significa la posicion actual y el
 * objetivo. El F Cost es la suma de G Cost y H Cost.
 *
 * <p>Por lo tanto, cada nodo tiene estos tres costos y el algoritmo evalua estos costos para descubrir que nodo es el
 * mas prometedor (el costo mas bajo) para alcanzar el objetivo. Entre estos tres costos, el F Cost es el mas importante
 * ya que indica el costo total.
 */

public class Node extends JButton {

	Node parent;
	int row, col;
	int gCost, hCost, fCost;
	boolean start, goal, solid;
	boolean open, checked;

	public Node(int row, int col) {
		this.row = row;
		this.col = col;
		setBackground(Color.white);
		setForeground(Color.black);
	}

	/**
	 * Establece el nodo como inicial.
	 */
	public void setAsStart() {
		setBackground(Color.blue);
		setForeground(Color.white);
		setText("Start");
		start = true;
	}

	/**
	 * Establece el nodo como objetivo.
	 */
	public void setAsGoal() {
		setBackground(Color.yellow);
		setForeground(Color.black);
		setText("Goal");
		goal = true;
	}

	/**
	 * Establece el nodo como solido.
	 */
	public void setAsSolid() {
		setBackground(Color.black);
		solid = true;
	}

	/**
	 * Establece el nodo como abierto.
	 */
	public void setAsOpen() {
		open = true;
	}

	/**
	 * Establece el nodo como verificado.
	 */
	public void setAsChecked() {
		if (!start && !goal) {
			setBackground(Color.orange);
			setForeground(Color.black);
		}
		checked = true;
	}

}
