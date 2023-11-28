package game_test;

import javax.swing.*;
import java.awt.*;

public class Launcher {

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		// https://stackoverflow.com/questions/144892/how-to-center-a-window-in-java
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		new Game("", 800, 600).start(); // screenSize.width / 2

	}

}
