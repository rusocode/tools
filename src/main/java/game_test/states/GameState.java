package game_test.states;

import java.awt.*;

import game_test.Game;
import game_test.worlds.World;

public class GameState extends State {

	private final World world;

	public GameState(Game game) {
		world = new World(game);
	}

	@Override
	public void tick() {
		world.tick();
	}

	@Override
	public void render(Graphics g) {
		world.render(g);
	}

}
