package game_test.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

import game_test.Game;
import game_test.worlds.World;

/**
 * Un item puede tener dos estados:
 * <ul>
 * 	<li>Tirado en el suelo
 * 	<li>En el invenatario
 * </ul>
 * <p>
 * Si el item esta en el inventario, entonces se representa en un lugar especifico de la pantalla dentro del espacio del
 * inventario. Para ese caso, se usa el metodo {@code render(Graphics g, int x, int y)} con los tres parametros. Pero
 * los items tambien pueden estar en el suelo del mundo, por lo que necesita una posicion x e y, y para eso usa el
 * metodo {@code render(Graphics g)} sin los parametros x e y.
 */

public class Item extends Entity {

	public static final int ITEM_WIDTH = 32, ITEM_HEIGHT = 32;
	private Game game;
	private World world;
	private final BufferedImage texture;
	private final String name;
	private final int id;
	private int count; // TODO Si quisieramos que los items se desplacen por el suelo, entonces las coordenadas se declararian como float
	private final Rectangle rectangle;

	public Item(Game game, World world, float x, float y, int width, int height, BufferedImage texture, String name, int id) {
		super(game, world, x, y, width, height);
		this.texture = texture;
		this.name = name;
		this.id = id;
		count = 1;
		rectangle = new Rectangle((int) x, (int) y, ITEM_WIDTH, ITEM_HEIGHT);
	}

	@Override
	public void tick() {
		/* Si el rectangulo delimitador del player colisiona con el rectangulo delimitador del item, entonces lo hace
		 * agarrable y lo agrega al inventario. */
		if (world.getEntityManager().getPlayer().getCollisionBounds(0f, 0f).intersects(rectangle)) {
			setPickedUp(true);
			world.getEntityManager().getPlayer().getInventory().addItem(this);
		}
	}

	@Override
	public void die() {

	}

	/**
	 * Renderiza el item en el suelo.
	 */
	@Override
	public void render(Graphics g) {
		render(g, (int) (x - game.getCamera().getxOffset()), (int) (y - game.getCamera().getyOffset()));
		g.setColor(Color.yellow);
		// g.fillRect((int) (x + rectangle.x - game.getCamera().getxOffset()), (int) (y + rectangle.y - game.getCamera().getyOffset()), rectangle.width, rectangle.height);
	}

	/**
	 * Renderiza el item en el inventario.
	 */
	public void render(Graphics g, int x, int y) {
		g.drawImage(texture, x, y, ITEM_WIDTH, ITEM_HEIGHT, null);
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}