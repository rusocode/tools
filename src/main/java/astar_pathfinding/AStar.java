package astar_pathfinding;

import javax.swing.*;
import java.awt.*;

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

	public AStar() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.black);
		setLayout(new GridLayout(maxRow, maxCol));

		int row, col;

		// Places nodes
		for (row = 0; row < maxRow; row++) {
			for (col = 0; col < maxCol; col++) {
				node[row][col] = new Node(row, col);
				add(node[row][col]);
			}
		}

		// Set start and goal node
		setStartNode(6, 3);
		setGoalNode(3, 11);

		// Place solid nodes
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

		// Set cost
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
		int row, col;
		for (row = 0; row < maxRow; row++) {
			for (col = 0; col < maxCol; col++) {
				getCost(node[row][col]);
			}
		}
	}

	private void getCost(Node node) {
		// Get G Cost (the distance from the start node)
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance;

		// Get H Cost (the distance from the goal node)
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance;

		// Get F Cost (the total cost)
		node.fCost = node.gCost + node.hCost;

		// Display the cost on node
		if (node != startNode && node != goalNode)
			node.setText("<html>F: " + node.fCost + "<br>G: " + node.gCost + "</html>");

	}

}
