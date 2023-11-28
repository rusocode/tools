package game_test.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import game_test.utils.Utils;

/**
 * Inicializa los recursos del juego solo una vez.
 *
 * <p>TODO Se podria usar un HashMap para referenciar a las diferentes imagenes en lugar de hacerlo de manera separada.
 * <p>TODO Utilizar constantes para los nombres de recursos
 * <p>TODO Crear sprites de x16 y escalar a x64 desde java, ahorrando recursos para la carga del SpriteSheet
 *
 * @author Juan
 */

public class Assets {

	public static Font goudyMedieval28, slkscr28;

	public static SpriteSheet tiles = new SpriteSheet(Utils.loadImage("game/textures/tiles.png"));
	public static SpriteSheet items = new SpriteSheet(Utils.loadImage("game/textures/items.png"));
	public static SpriteSheet player = new SpriteSheet(Utils.loadImage("game/textures/player 300x360.png"));
	public static SpriteSheet ui = new SpriteSheet(Utils.loadImage("game/textures/ui.png"));

	// Tiles
	public static BufferedImage dirt, grass, brick;

	// Items
	public static BufferedImage woodItem, rockItem;

	// Entities
	public static BufferedImage tree, rock;
	public static BufferedImage[] player_down, player_up, player_left, player_right;

	// UI
	public static BufferedImage[] btnStart;
	public static BufferedImage inventoryScreen;

	public static final List<BufferedImage> sprites = new ArrayList<>();

	/**
	 * Inicializa los sprites del SpriteSheet.
	 */
	public static void init() {

		goudyMedieval28 = Utils.loadFont("game/fonts/GoudyMedieval.ttf", 28);
		slkscr28 = Utils.loadFont("game/fonts/slkscr.ttf", 28);

		// Tiles
		grass = tiles.crop(0, 0, 64, 64);
		brick = tiles.crop(130, 0, 64, 64);

		// Items
		woodItem = items.crop(0, 0, 32, 32);
		rockItem = items.crop(32, 0, 32, 32);

		// Entidades estaticas
		tree = Utils.loadImage("game/textures/tree.png");
		rock = Utils.loadImage("game/textures/rock.png");

		// Entidades dinamicas
		initPlayer();

		// UI
		btnStart = new BufferedImage[2];
		btnStart[0] = ui.crop(0, 0, 62, ui.getHeight() / 2); // Letra blanca
		btnStart[1] = ui.crop(0, 30, 62, ui.getHeight() / 2); // Letra negra

		inventoryScreen = Utils.loadImage("game/textures/inventoryScreen.png");

	}

	/**
	 * Inicializa los sprites del SpriteSheet en un ArrayList.
	 *
	 * <p>Este metodo es util en caso de que el SpriteSheet sea muy grande.
	 *
	 * <p>FIXME Descartar los sprites transparentes
	 */
	public static void initTiles() {

		int tileSize = tiles.getWidth() / 32;

		// Agrega los sprites de izquierda a derecha como un SpriteSheet tradicional
		for (int y = 0; y < tiles.getHeight() / tileSize; y++) {
			for (int x = 0; x < tiles.getWidth() / tileSize; x++) {
				sprites.add(tiles.crop(x * tileSize, y * tileSize, tileSize, tileSize));
			}
		}
	}

	private static void initPlayer() {

		player_down = new BufferedImage[6];
		player_up = new BufferedImage[6];
		player_left = new BufferedImage[5];
		player_right = new BufferedImage[5];

		int player_width = player.getWidth() / player_down.length;
		int player_height = player.getHeight() / (player_left.length - 1);

		for (int y = 0; y < player.getHeight() / player_height; y++) {
			for (int x = 0; x < player.getWidth() / player_width; x++) {
				switch (y) { // Controla las filas
					case 0:
						player_down[x] = player.crop(x * player_width, 0, player_width, player_height);
						break;
					case 1:
						player_up[x] = player.crop(x * player_width, y * player_height, player_width, player_height);
						break;
					case 2:
						/* Los movimientos izquierdos y derechos solo tienen 5 frames, por lo tanto comprueba hasta el
						 * limite 5 para evitar un ArrayIndexOutOfBoundsException. */
						if (x < 5)
							player_left[x] = player.crop(x * player_width, y * player_height, player_width, player_height);
						break;
					case 3:
						if (x < 5)
							player_right[x] = player.crop(x * player_width, y * player_height, player_width, player_height);
						break;
				}
			}
		}

	}

}
