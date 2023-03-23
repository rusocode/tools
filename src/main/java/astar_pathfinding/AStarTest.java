package astar_pathfinding;

import javax.swing.*;

public class AStarTest extends JFrame {

	public AStarTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		add(new AStar());

		pack();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new AStarTest().setVisible(true);
	}


}
