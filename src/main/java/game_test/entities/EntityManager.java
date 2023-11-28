package game_test.entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import game_test.Game;
import game_test.entities.creatures.Player;
import game_test.worlds.World;

public class EntityManager {

	private final Game game;
	private final World world;
	private final Player player;
	private final List<Entity> entities;

	public EntityManager(Game game, World world, Player player) {
		this.game = game;
		this.world = world;
		this.player = player;
		entities = new ArrayList<>();
		addEntity(player);
	}

	public void tick() {

		// Itera las entidades usando un Iterator para poder eliminarlas al mismo tiempo
		/* Iterator<Entity> it = entities.iterator();

		while (it.hasNext()) {
			Entity entity = it.next();
			entity.tick();
			// Elimina de manera segura y adecuada mientras permite iterar la lista sin omitir otras entidades
			if (!entity.isActive()) it.remove();
			if (entity.pickedUp) it.remove();
		} */

		/* Iterar sobre una matriz en lugar de una coleccion: esto puede funcionar bien con listas de tamaño pequeño,
		 * pero puede degradar el rendimiento de las mas grandes. https://rollbar.com/blog/java-concurrentmodificationexception/ */
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
			if (entities.get(i).isDead()) entities.remove(entities.get(i));
			if (entities.get(i).isPickedUp()) entities.remove(entities.get(i));
		}

		/* Si la coordenada Y del player es menor a la coordenada Y de la entidad (arbol), entonces se dibuja por encima
		 * de esta (por encima del tronco par ser mas especificos), es decir que ordena primero al objeto Player en la
		 * lista. En caso contrario, ordena primero al objeto Tree (el player se dibuja por debajo de las ramas). */
		entities.sort((Entity a, Entity b) -> (a.getY() + a.getHeight() < b.getY() + b.getHeight() ? -1 : 0));
	}

	/**
	 * Renderiza primero las entidades, despues el inventario.
	 */
	public void render(Graphics g) {
		entities.forEach(entity -> entity.render(g));
		player.postRender(g);
	}

	/**
	 * Agrega la entidad a la lista de entidades.
	 *
	 * @param entity la entidad.
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	/**
	 * Agrega el item a la lista de entidades.
	 *
	 * @param item el item.
	 */
	public void addItem(Item item) {
		item.setGame(game);
		item.setWorld(world);
		entities.add(item);
	}

	public Player getPlayer() {
		return player;
	}

	public List<Entity> getEntities() {
		return entities;
	}

}
