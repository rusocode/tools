package com.punkipunk.game_test.entities.statics;

import com.punkipunk.game_test.Game;
import com.punkipunk.game_test.entities.EntityManager;
import com.punkipunk.game_test.gfx.Assets;
import com.punkipunk.game_test.entities.Item;
import com.punkipunk.game_test.worlds.World;

import java.awt.*;

public class Rock extends StaticEntity {

	private final EntityManager entityManager;

	public Rock(EntityManager entityManager, Game game, World world, float x, float y) {
		super(game, world, x, y, Assets.rock.getWidth(), Assets.rock.getHeight());

		this.entityManager = entityManager;
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.rock, (int) (x - camera.getxOffset()), (int) (y - camera.getyOffset()), width, height, null);
	}

	@Override
	public void die() {
		entityManager.addItem(new Item(game, world, x, y, Item.ITEM_WIDTH, Item.ITEM_HEIGHT, Assets.rockItem, "rock", 1));
	}
}
