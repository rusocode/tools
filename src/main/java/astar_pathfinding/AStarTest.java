package astar_pathfinding;

import javax.swing.*;

public class AStarTest extends JFrame {

	public AStarTest() throws Exception {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		add(new AStar());

		pack();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) throws Exception {
		new AStarTest().setVisible(true);
	}


}
