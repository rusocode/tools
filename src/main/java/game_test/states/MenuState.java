package game_test.states;

import java.awt.*;

import game_test.Game;
import game_test.gfx.Assets;
import game_test.ui.*;

public class MenuState extends State {

	private final UIManager uiManager;

	public MenuState(Game game) {
		uiManager = new UIManager(game);
		// Establece el UIManager al MouseManager para poder interactuar con los componentes a travez del mouse
		game.getMouseManager().setUIManager(uiManager);
		uiManager.addComponent(new ImageButton(200, 200, 62, 30, Assets.btnStart, () -> StateManager.getInstance().setState(game.gameState)));
	}

	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		uiManager.render(g);
	}
}
