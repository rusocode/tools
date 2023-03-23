package astar_pathfinding;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * El algoritmo A* evalua los costos y encuentra la mejor ruta hacia el objetivo.
 *
 * <p>Desde el nodo de inicio, evalua los nodos adyacentes asumiendo que en este caso, el npc solo se mueve en 4
 * direcciones. Esto significa que tenemos cuatro opciones como nuestro primer paso. Entonces evalua de los 4 nodos
 * adyacentes el que tenga el costo mas bajo (F y G) y lo configura como verificado. Este proceso se repite verificando
 * los nodos adyacentes con menor costo a los nodos anteriormente verificados hasta llegar al objetivo.
 *
 * <p>En caso de que la ruta se tope con nodos solidos, busca el proximo nodo con menor costo entre los nodos adyacentes
 * verificados.
 */

public class AStar extends JPanel {

	// Screen settings
	final int maxRow = 10;
	final int maxCol = 15;
	final int nodeSize = 70;
	final int screenWidth = nodeSize * maxCol;
	final int screenHeight = nodeSize * maxRow;

	// Node
	Node[][] node = new Node[maxRow][maxCol];
	Node startNode, goalNode, currentNode;
	ArrayList<Node> openList = new ArrayList<>();

	// Others
	boolean goalReached;
	// Cuenta los pasos en cada ciclo hasta un limite para evitar que se congele el juego en caso de que el npc este encerrado en nodos solidos
	int step;

	public AStar() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.black);
		setLayout(new GridLayout(maxRow, maxCol));
		addKeyListener(new KeyHandler(this));
		setFocusable(true);

		// Crea los nodos y los agrega al panel
		for (int row = 0; row < maxRow; row++) {
			for (int col = 0; col < maxCol; col++) {
				node[row][col] = new Node(row, col);
				add(node[row][col]);
			}
		}

		// Establece el nodo de inicio y el nodo objetivo
		setStartNode(6, 3);
		setGoalNode(3, 11);

		// Establece los nodos solidos
		setSolidNode(2, 10);
		setSolidNode(3, 10);
		setSolidNode(4, 10);
		setSolidNode(5, 10);
		setSolidNode(6, 10);
		setSolidNode(7, 10);
		setSolidNode(2, 6);
		setSolidNode(2, 7);
		setSolidNode(2, 8);
		setSolidNode(2, 9);
		setSolidNode(7, 11);
		setSolidNode(7, 12);
		setSolidNode(1, 6);

		// Establece los costos a cada nodo
		setCostOnNodes();

	}

	private void setStartNode(int row, int col) {
		node[row][col].setAsStart();
		startNode = node[row][col];
		currentNode = startNode;
	}

	private void setGoalNode(int row, int col) {
		node[row][col].setAsGoal();
		goalNode = node[row][col];
	}

	private void setSolidNode(int row, int col) {
		node[row][col].setAsSolid();
	}

	private void setCostOnNodes() {
		for (int row = 0; row < maxRow; row++)
			for (int col = 0; col < maxCol; col++)
				getCost(node[row][col]);
	}

	private void getCost(Node node) {
		// Obtiene el G Cost (distancia desde el nodo inicio)
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance;

		// Obtiene el H Cost (distancia desde el nodo objetivo)
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance;

		// Obtiene el F Cost (el costo total)
		node.fCost = node.gCost + node.hCost;

		// Muestra el costo en cada nodo
		if (node != startNode && node != goalNode)
			node.setText("<html>F: " + node.fCost + "<br>G: " + node.gCost + "</html>");

	}

	public void search() {
		if (!goalReached) {
			int row = currentNode.row;
			int col = currentNode.col;

			System.out.println(openList.size());

			currentNode.setAsChecked();
			openList.remove(currentNode);

			if (row - 1 >= 0) openNode(node[row - 1][col]);
			if (col - 1 >= 0) openNode(node[row][col - 1]);
			if (row + 1 < maxRow) openNode(node[row + 1][col]);
			if (col + 1 < maxCol) openNode(node[row][col + 1]);

			// Encuentra el mejor nodo
			int bestNodeIndex = 0;
			int bestNodefCost = 999;

			for (int i = 0; i < openList.size(); i++) {
				if (openList.get(i).fCost < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = openList.get(i).fCost;
				} else if (openList.get(i).fCost == bestNodefCost)
					if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) bestNodeIndex = i;
			}

			currentNode = openList.get(bestNodeIndex);

			if (currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}

		}
	}

	public void autoSearch() {
		// Mientras no haya llegado al objetivo y la cantidad de pasos no supere el limite
		while (!goalReached && step < 300) {
			int row = currentNode.row;
			int col = currentNode.col;

			/* Configura el nodo actual como marcado y lo elimina de la lista abierta. Esto significa que ya lo hemos
			 * verificado como mejor candidato para la ruta, por lo tanto ya no es necesario evaluarlo. */
			currentNode.setAsChecked();
			openList.remove(currentNode);

			// Abre los nodos adyacentes al actual siempre y cuando se eviten abrir los nodos ubicados a los extremos
			if (row - 1 >= 0) openNode(node[row - 1][col]);
			if (col - 1 >= 0) openNode(node[row][col - 1]);
			if (row + 1 < maxRow) openNode(node[row + 1][col]);
			if (col + 1 < maxCol) openNode(node[row][col + 1]);

			int bestNodeIndex = 0;
			int bestNodefCost = 999;

			// Compara los nodos de la lista abierta y verifica que nodo tiene el mejor F Cost o G Cost
			for (int i = 0; i < openList.size(); i++) {
				// Verifica si el F Cost es mas bajo que el F Cost del mejor nodo actual
				if (openList.get(i).fCost < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = openList.get(i).fCost;
				}
				// Si el F Cost es igual al F Cost del mejor nodo actual, usa el G Cost como indice
				else if (openList.get(i).fCost == bestNodefCost)
					if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) bestNodeIndex = i;
			}

			// Obtiene el siguiente mejor nodo
			currentNode = openList.get(bestNodeIndex);

			if (currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
		}

		step++;

	}

	private void openNode(Node node) {
		if (!node.open && !node.checked && !node.solid) {
			node.setAsOpen();
			// Configura el nodo actual como su padre para verificar los proximos nodos adyacentes a este
			node.parent = currentNode;
			openList.add(node);
		}
	}

	/**
	 * Retrocede hasta el nodo de inicio con el objetivo de marcar la mejor ruta.
	 */
	private void trackThePath() {
		Node current = goalNode;
		while (current != startNode) {
			current = current.parent;
			if (current != startNode) current.setAsPath();
		}
	}

}
