package game_test.inventory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import game_test.Game;
import game_test.gfx.Assets;
import game_test.entities.Item;
import game_test.utils.Utils;

/**
 * Agrega los items desde la clse Item, actualiza el inventario desde el Player y lo renderiza desde la clase
 * EntityManager.
 */

public class Inventory {

	private final Game game;
	private boolean active;
	private final ArrayList<Item> inventoryItems;

	// Inventario, lista del centro y espacios verticales
	private final int invX = 64, invY = 48,
			invWidth = 512, invHeight = 384,
			invListCenterX = invX + 171,
			invListCenterY = invY + invHeight / 2 + 5,
			invListSpacing = 30;
	// Imagen de item
	private final int invImageX = 452, invImageY = 82,
			invImageWidth = 64, invImageHeight = 64;
	// Cantidad de items
	private final int invCountX = 484, invCountY = 172;
	// Selector de la lista
	private int selectedItem;

	public Inventory(Game game) {
		this.game = game;
		inventoryItems = new ArrayList<>();
	}

	public void tick() {
		// Si se presiono la tecla E, entonces activa el inventario
		if (game.getKeyManager().keyJustPressed(KeyEvent.VK_E)) active = !active;
		if (!active) return;

		if (game.getKeyManager().keyJustPressed(KeyEvent.VK_UP)) selectedItem--;
		if (game.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)) selectedItem++;
		if (selectedItem < 0) selectedItem = inventoryItems.size() - 1;
		else if (selectedItem >= inventoryItems.size()) selectedItem = 0;
	}

	public void render(Graphics g) {
		if (!active) return;

		g.drawImage(Assets.inventoryScreen, invX, invY, invWidth, invHeight, null);

		int length = inventoryItems.size();
		if (length == 0) return;

		// El inicio de la lista es -5, el centro es 0 y el limite 5
		for (int i = -5; i < 6; i++) {
			// Si el selector supera los limites del inventario
			if (selectedItem + i < 0 || selectedItem + i >= length) continue;
			if (i == 0)
				Utils.drawString(g, "> " + inventoryItems.get(selectedItem + i).getName() + " <", invListCenterX,
						invListCenterY, true, Color.YELLOW, Assets.slkscr28);
			else
				Utils.drawString(g, inventoryItems.get(selectedItem + i).getName(), invListCenterX,
						invListCenterY + i * invListSpacing, true, Color.WHITE, Assets.slkscr28);
		}

		Item item = inventoryItems.get(selectedItem);
		// Dibuja la imagen en el inventario
		g.drawImage(item.getTexture(), invImageX, invImageY, invImageWidth, invImageHeight, null);
		// Dibuja la cadena de la cantidad debajo de la imagen
		Utils.drawString(g, Integer.toString(item.getCount()), invCountX, invCountY, true, Color.WHITE, Assets.slkscr28);
	}

	/**
	 * Agrega el item al invetario.
	 *
	 * <p>Si el item ya existe en el inventario, entonces le suma la cantidad.
	 */
	public void addItem(Item item) {
		// Si el item ya esta en el invetario
		for (Item i : inventoryItems) {
			if (i.getId() == item.getId()) {
				i.setCount(i.getCount() + item.getCount());
				return;
			}
		}
		inventoryItems.add(item);
	}

	public boolean isActive() {
		return active;
	}

}
