package com.punkipunk.game_test.worlds;

import java.awt.*;

import com.punkipunk.game_test.Game;
import com.punkipunk.game_test.entities.EntityManager;
import com.punkipunk.game_test.entities.creatures.Player;
import com.punkipunk.game_test.entities.statics.*;
import com.punkipunk.game_test.gfx.Camera;
import com.punkipunk.game_test.tiles.Tile;
import com.punkipunk.game_test.utils.Utils;

/**
 * El mundo esta compuesto por {@link Tile tiles}, entidades estaticas (arboles, rocas, etc.), entidades dinamicas
 * (player, enemigos, etc.) y items. Cada entidad es administrada por la clase {@link EntityManager EntityManager}.
 *
 * <p>Los tiles se dibujan individualmente de izquierda a derecha y de arriba hacia abajo (forma tradicional) usando un
 * doble for, en donde el primero itera las filas y el segundo las columnas. Esto se hace tan rapido que no se nota.
 * Cada iteracion completa de renderizado aplicando el desplazamiento, mueve el mundo hacia una determinada direccion.
 * Es decir, que si por ejemplo el player se "mueve" a la derecha (+x), el mundo se desplaza hacia la izquierda (-x).
 * Esto genera la sensacion de que el player se esta moviendo por el mundo, pero la realidad es que eso no sucede, ya
 * que es el mundo el que genera esa sensacion aplicando los desplazamientos calculados por la posicion del player.
 *
 * <p>Un pequeño truco de eficiencia para cargar mapas de cualquier tamaño, pero mas especificamente de grandes tamaños,
 * es dibujar solo los tiles que esten dentro de la vista de la camara.
 *
 * <br><br>
 *
 * <h3>¿Como aplicar esa eficiencia?</h3>
 * Para el inicio del eje horizontal ({@code xStart}), toma el valor maximo entre 0 y el desplazamiento de x dividido
 * entre el ancho del tile para obtener ese desplazamiento en terminos de tiles y no en pixeles: {@code 272 / 64 = 4,25 = 4}.
 * El 0 se toma en cuenta en caso de que el desplazamiento de x sea negativo, evitando cualquier error. Para el final
 * del eje horizontal ({@code xEnd}), toma el valor minimo entre el ancho del mundo (21 tiles de x64, que son 1.344
 * pixeles) y el desplazamiento de x mas el ancho de la pantalla dividido entre el ancho del tile mas 1: {@code (272 + 800) / 64 + 1 = 16,75 + 1 = 17,75 = 17}.
 * El 21 (cantidad maxima de tiles) se toma en cuenta en caso de que el desplazamiento de x sea mayor al ancho de la
 * pantalla. Tambien se le suma uno ya que al castear el valor a entero, por defecto, siempre se redondea hacia abajo.
 * Todos estos calculos se aplican de igual manera al eje y.
 *
 * <p>Es notable el aumento de fps cuando se aplica este metodo, un aumento de casi 500 fps ejecutanto el juego en la
 * maquina de escritorio.
 *
 * <p>TODO Generar las entidades estaticas en posiciones aleatorias
 * <p>TODO Esta clase se podria llamara Map
 *
 * @author Juan
 */

public class World {

    private final Game game;
    private final Camera camera;
    private final EntityManager entityManager;

    private int spawnX, spawnY, width, height;
    private int[][] tiles;

    public World(Game game) {
        loadWorld();
        this.game = game;
        camera = game.getCamera();

        entityManager = new EntityManager(game, this, new Player(game, this, spawnX * Tile.TILE_WIDTH, spawnY * Tile.TILE_HEIGHT));
        entityManager.addEntity(new Tree(entityManager, game, this, 750, 450));
        entityManager.addEntity(new Tree(entityManager, game, this, 400, 450));
        entityManager.addEntity(new Rock(entityManager, game, this, 700, 350));
        // entityManager.addEntity(new Rock(game, this, entityManager, 400, 350));
    }

    /**
     * Actualiza las entidades.
     */
    public void tick() {
        entityManager.tick();
    }

    /**
     * Renderiza los tiles y las entidades aplicando el desplazamiento.
     *
     * @param g pincel.
     */
    public void render(Graphics g) {
        // Teniendo en cuenta la vista de la camara en la posicion inicial del player con desplazamiento (x=368, y=268)...
        int yStart = (int) Math.max(0, camera.getyOffset() / Tile.TILE_HEIGHT); // Obtiene la fila 5
        int yEnd = (int) Math.min(height, (camera.getyOffset() + game.getHeight()) / Tile.TILE_HEIGHT + 1); // Obtiene la fila 16
        int xStart = (int) Math.max(0, camera.getxOffset() / Tile.TILE_WIDTH); // Obtiene la columna 4
        int xEnd = (int) Math.min(width, (camera.getxOffset() + game.getWidth()) / Tile.TILE_WIDTH + 1); // Obtiene la columna 17
        /* Para el primer renderizado del mundo, solo se dibujan los tiles que van desde la fila 5 hasta la 16 y de
         * la columna 4 hasta la 17, es decir, que solo se dibujan los tiles que estan dentro de la vista de la camara.
         * Los tiles de la fila 16 y columna 17, se utilizan como limites pero no se dibuja ya que NO entran en la vista
         * de la camara. */
        for (int y = yStart; y < yEnd; y++) { // Fila
            for (int x = xStart; x < xEnd; x++) { // Columna
                Tile tile = getTile(x, y); // Obtiene el tile de la posicion actual de la camara
                /* En caso de aplicar el desplazamiento al player y no al mundo, lo que va a pasar es que el player no
                 * se va a poder "mover" ya que la sensacion de movimiento la genera los tiles. Aplicando el
                 * desplazamiento a los tiles, estos se van a dibujar 272 pixeles hacia la izquierda y 372 pixeles hacia
                 * arriba de su posicion original, centrando el mundo en la pantalla y sincronizandoce con la posicion
                 * del player. */
                tile.render(g, (int) (x * tile.texture.getWidth() - camera.getxOffset()), (int) (y * tile.texture.getHeight() - camera.getyOffset()), tile.texture.getWidth(), tile.texture.getHeight());
            }
        }

        entityManager.render(g);
    }

    /**
     * Carga el archivo como cadena y elimina los espacios en blanco para que solo se almacenen los caracteres numericos
     * en un array. Convierte el caracter de la posicion 0 (ancho), el de la posicion 1 (alto), el de la posicion 2 (eje
     * x del player), el de la posicion 3 (eje y del player) y el de los siguientes, (tiles) en enteros, con el objetivo
     * de usarlos como datos numericos para llenar la matriz de tiles.
     *
     * <p>TODO Cargar este metodo desde Utils.
     */
    private void loadWorld() {
        String file = Utils.loadFileAsString("game/worlds/world1.txt");
        String[] tokens = file.split("\\s+");
        width = Utils.parseInt(tokens[0]);
        height = Utils.parseInt(tokens[1]);
        spawnX = Utils.parseInt(tokens[2]);
        spawnY = Utils.parseInt(tokens[3]);

        tiles = new int[width][height];
        /* Agrega los tiles de izquierda a derecha en la matriz. La instruccion "x + y * width" calcula la fila y suma 4
         * para empezar despues del width, height, spawnX y spawnY. */
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 4]);
            }
        }
    }

    /**
     * Obtiene el tile de la posicion especificada.
     *
     * @param x posicion del tile en el eje x del mundo.
     * @param y posicion del tile en el eje y del mundo.
     * @return el tile en la posicion especificada.
     */
    public Tile getTile(int x, int y) {
        /* En caso de que el player salga de los limites del mundo, devuelve invisibleTile. Esto se hace para que el
         * player se pueda mover por tiles "invisibles" (blancos), ademas de evitar un ArrayIndexOutOfBoundsException. */
        if (x < 0 || y < 0 || x >= width || y >= height) return Tile.invisibleTile;
        Tile tile = Tile.tiles[tiles[x][y]];
        return tile != null ? tile : Tile.dirtTile;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
