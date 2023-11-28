package game_test.entities.statics;

import game_test.Game;
import game_test.entities.Entity;
import game_test.worlds.World;

/**
 * Aca se crean todas las entidades estaticas como arboles o rocas.
 */

public abstract class StaticEntity extends Entity {

	public StaticEntity(Game game, World world, float x, float y, int width, int height) {
		super(game, world, x, y, width, height);
	}

}
