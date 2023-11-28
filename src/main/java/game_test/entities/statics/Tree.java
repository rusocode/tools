package game_test.entities.statics;

import java.awt.*;
import java.util.ArrayList;

import game_test.Game;
import game_test.entities.EntityManager;
import game_test.gfx.Assets;
import game_test.entities.Item;
import game_test.worlds.World;

public class Tree extends StaticEntity {

	private final EntityManager entityManager;

	public Tree(EntityManager entityManager, Game game, World world, float x, float y) {
		super(game, world, x, y, Assets.tree.getWidth(), Assets.tree.getHeight());
		rectangle.width = 85;
		rectangle.height = 7;
		int centerX = (Assets.tree.getWidth() / 2) - (rectangle.width / 2);
		int centerY = (Assets.tree.getHeight() / 2) - (rectangle.height / 2);
		rectangle.x = centerX - 2;
		rectangle.y = centerY + 110;
		// health = 20;

		this.entityManager = entityManager;
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.tree, (int) (x - camera.getxOffset()), (int) (y - camera.getyOffset()), width, height, null);
		g.setColor(Color.yellow);
		// g.fillRect((int) (x + rectangle.x - handler.getCamera().getxOffset()), (int) (y + rectangle.y - handler.getCamera().getyOffset()), rectangle.width, rectangle.height);
	}

	/**
	 * Crea un nuevo item de madera en la posicion x e y del mundo.
	 */
	@Override
	public void die() {
		entityManager.addItem(new Item(game, world, (int) x + rectangle.x + rectangle.width / 2f, (int) y + rectangle.y, Item.ITEM_WIDTH, Item.ITEM_HEIGHT, Assets.woodItem, "wood", 0));
	}

}