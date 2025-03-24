package com.punkipunk.game_test;

import com.punkipunk.game_test.display.Display;
import com.punkipunk.game_test.gfx.Assets;
import com.punkipunk.game_test.gfx.Camera;
import com.punkipunk.game_test.input.KeyManager;
import com.punkipunk.game_test.input.MouseManager;
import com.punkipunk.game_test.states.GameState;
import com.punkipunk.game_test.states.MenuState;
import com.punkipunk.game_test.states.State;
import com.punkipunk.game_test.states.StateManager;
import com.punkipunk.game_test.utils.Timestep;

import java.awt.*;
import java.awt.image.*;

/**
 * Clase principal.
 *
 * @author Juan Debenedetti
 */

public class Game implements Runnable {

	private Thread thread;
	private BufferStrategy buffer; // Es como una pantalla oculta que se usa para de evitar parpadedos
	private Graphics g; // Seria como un pincel para dibujar en el lienzo

	private Camera camera;
	private KeyManager keyManager;
	private MouseManager mouseManager;
	private Display display;
	public State gameState, menuState; // TODO private

	private final String title;
	private final int width, height;
	private boolean running;

	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	/**
	 * Game loop.
	 */
	@Override
	public void run() {

		init();

		/* Aplicando 30 ticks y speed a 6 en una pantalla de s60Hz (mi pobre monitor) se produce el efecto stuttering.
		 * Para este caso, el efecto se da sin la caida de FPS, lo cual es todavia una sensacion mas desagradable ya que
		 * parece como un salto de fotogramas literalmente. */
		int ticks = 60, frames = 0;
		Timestep timestep = new Timestep(ticks);
		boolean shouldRender = false;

		while (isRunning()) {

			if (timestep.checkDelta()) {
				tick();
				shouldRender = true; // Actualiza primero para tener algo que renderizar en el primer ciclo
			}

			// Limita la sobrecarga del Game Loop
			/* try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} */

			// Interpola el renderizado de la fisica para aprovechar la variabilidad de GPUs
			if (shouldRender) {
				frames++;
				render();
			}

			if (timestep.checkTimer()) {
				display.getFrame().setTitle("FPS: " + frames);
				frames = 0;
				timestep.resetTimer();
			}

		}

		stop();

	}

	/**
	 * Inicializa los recursos, crea la camara, crea el manejador de teclas, crea el manejador del mouse, crea la
	 * ventana, agrega los oyentes del teclado y mouse a la ventana, crea los estados y establece uno.
	 */
	private void init() {
		Assets.init();

		camera = new Camera(this, 0, 0); // Crea la camara con vista en 0,0 a partir de la esquina superior izquierda

		keyManager = new KeyManager();
		mouseManager = new MouseManager();

		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);

		gameState = new GameState(this);
		menuState = new MenuState(this);
		StateManager.getInstance().setState(gameState);
	}

	private void tick() {
		keyManager.tick();
		/* ¿Por que no llama al metodo tick() directamente desde el objeto GameState?
		 * Creo que usa getState() porque solo quiere marcar el estado actual. Usar gameState.tick() funcionara, pero
		 * si tiene varios estados, como la mayoria de los juegos, necesitara una forma de determinar en que estado se
		 * encuentra, por lo tanto, usa getState(). */
		if (StateManager.getInstance().getState() != null) StateManager.getInstance().getState().tick();
	}

	private void render() {
		// Obtiene el buffer del lienzo
		buffer = display.getCanvas().getBufferStrategy();
		if (buffer == null) {
			/* Crea 3 buffers para el lienzo en caso de que no haya uno. Tenga en cuenta que cuanto mas alto vaya, mas
			 * potencia de procesamiento necesitara, por lo que 10k seria un buffer muy, muy malo. */
			display.getCanvas().createBufferStrategy(3);
			return;
		}

		// Obtiene el pincel
		g = buffer.getDrawGraphics();

		// Limpia la ventana usando el color de fondo actual
		g.clearRect(0, 0, width, height);

		if (StateManager.getInstance().getState() != null) StateManager.getInstance().getState().render(g);

		// Hace visible el buffer
		buffer.show();

		// Elimina este contexto de graficos y libera cualquier recurso del sistema que este utilizando
		g.dispose();

	}

	/**
	 * Ejecuta el juego.
	 */
	public synchronized void start() {
		// En caso de que el juego ya este ejecutado, no se ejecuta
		if (running) return;
		running = true;
		thread = new Thread(this, "Game Thread");
		thread.start();
	}

	/**
	 * Detiene el juego.
	 */
	public synchronized void stop() {
		// En caso de que el juego ya este detenido, no se detiene
		if (!running) return;
		running = false;

		System.out.println("Se detuvo el game loop!");

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!thread.isAlive()) System.out.println("Se mato al subproceso " + thread.getName());
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public Camera getCamera() {
		return camera;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
