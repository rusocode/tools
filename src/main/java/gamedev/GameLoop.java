package gamedev;

/**
 * https://www.reddit.com/r/gamedev/comments/8sci48/should_i_be_using_a_fixed_timestep_or_delta_time/
 * https://www.gamedev.net/forums/topic/673798-what-is-a-timestep/
 * https://www.reddit.com/r/gamedev/comments/22k6pl/fixed_time_step_vs_variable_time_step/
 *
 * El Game Loop (bucle del juego) se encarga de actualizar (tick) y dibujar (render) los frames en pantalla a una
 * velocidad constante independientemente del dispositivo en el que se este ejecutando el juego. El tiempo entre cada
 * frame se calcula usando el delta, que se obtiene de la diferencia del tiempo incial y el ultimo tiempo del reloj del
 * sistema medidos en nanosegundos.
 *
 * <p>Los ticks son las actualizaciones de la posicion por segundo, en este caso, actualiza la posicion del player 60
 * veces por segundo.
 *
 * <p>Los ticks se calculan en base al delta y el render en base a la velocidad del procesador.
 *
 * <p>El renderizado se elimina del bloque de actualizacion, ya que asi, se libera un monton de tiempo del CPU. El
 * resultado final es que el juego simula a una velocidad constante utilizando time steps fijos y seguros en una
 * variedad de hardware.
 *
 * <p>Recursos:
 * <a href="http://gameprogrammingpatterns.com/game-loop.html">Game Loop</a>
 * <a href="https://gamedev.stackexchange.com/questions/1589/when-should-i-use-a-fixed-or-variable-time-step">¿Cuando debo usar un timestep fijo o variable?</a>
 * <a href="https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency">Eficiencia del Game Loop</a>
 * <a href="https://gamedev.stackexchange.com/questions/132831/what-is-the-point-of-update-independent-rendering-in-a-game-loop">¿Cual es el punto de actualizar (tick) independientemente del render en el Game Loop?</a>
 * <a href="https://gamedev.stackexchange.com/questions/81608/what-is-a-tick-in-the-context-of-game-development#:~:text=A%20tick%20is%20a%20unit,%22%20and%20%22tock%22">¿Que es un "tick" en el contexto del desarrollo de juegos?</a>
 * <a href="https://gamedev.stackexchange.com/questions/96758/what-is-the-relationship-between-frames-per-second-and-a-game-loop">¿Cual es la relacion entre FPS y Game Loop?</a>
 */

public class GameLoop implements Runnable {

	private Thread thread;
	private boolean running, stopped;

	@Override
	public void run() {

		int fps = 60, ticks = 0, frames = 0;
		long startTime = System.nanoTime(), currentTime;
		double nsPerTick = 1e9 / fps, delta = 0, timer = 0;
		boolean shouldRender = false;

		while (isRunning()) {

			currentTime = System.nanoTime();
			delta += currentTime - startTime;
			timer += currentTime - startTime;
			startTime = currentTime;

			if (delta >= nsPerTick) {
				delta -= nsPerTick;
				ticks++;
				tick();
				shouldRender = true;
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (timer >= 1e9) {
				System.out.println(ticks + " ticks, " + frames + " fps");
				ticks = 0;
				frames = 0;
				timer = 0;
			}

			if (stopped) break;
		}
	}

	private void tick() {
	}

	private void render() {

	}

	private synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this, "Game Thread");
		thread.start();
	}

	private synchronized void stop() {
		if (!running) return;
		running = false;
		stopped = true;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized boolean isRunning() {
		return running;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GameLoop game = new GameLoop();
		game.start();
		game.sleep(10 * 1000);
		game.stop();
		if (!game.thread.isAlive()) System.out.println(game.thread.getName() + " muerto!");
	}

}
