package gamedev;

/**
 * <h1>Game Loop</h1>
 * Para este caso, el Game Loop (bucle del juego) se encarga de actualizar y dibujar los frames en pantalla a una
 * velocidad constante independientemente del dispositivo en el que se este ejecutando el juego, interpolando el
 * renderizado de la actualizacion. Para que el juego se ejecute en cualquier dispositivo a la misma velocidad, se
 * utiliza el {@link Delta}. Apropiadamente, un ciclo del Game Loop se llama tick.
 *
 * <p>Extraccion de <a href="http://gameprogrammingpatterns.com/game-loop.html#play-catch-up">Game Loop</a>
 * <br>
 * El renderizado se interpola de la actualizacion, ya que asi, se libera un monton de tiempo del CPU. El resultado
 * final es que el juego simula a una velocidad constante utilizando timesteps fijos y seguros en una variedad de
 * hardware.
 *
 * <p>Extraccion de <a href="https://www.reddit.com/r/gamedev/comments/8sci48/should_i_be_using_a_fixed_timestep_or_delta_time/">¿Debo usar un timestep fijo o un tiempo delta?</a>
 * <br>
 * La <i>interpolacion</i> hace que el juego se ejecute a una velocidad de fotogramas variable, pero sus sistemas
 * fisicos y de red se actualizan 60 veces por segundo. Interpolaria entre valores conocidos (hace dos ticks y hace un
 * tick) para que el resto de su juego pueda ejecutarse lo mas rapido posible y la velocidad de fotogramas se desacople
 * del timestep fijo.
 *
 * <p>Extraccion de <a href="https://gamedev.stackexchange.com/questions/132831/what-is-the-point-of-update-independent-rendering-in-a-game-loop">¿Cual es el punto de actualizar el renderizado independiente en un bucle de juego?</a>
 * <br>
 * <b>2. Entonces, ¿Por que no simplemente bloquear la velocidad de fotogramas (por ejemplo, usando VSync) y seguir
 * ejecutando las actualizaciones de estado del juego y la renderizacion al mismo tiempo?</b>
 * <br>
 * El desacoplamiento de las tasas de actualizacion y renderizacion brinda mas flexibilidad para lidiar con la
 * variabilidad del rendimiento.
 *
 * <p><b>3. ¿La actualizacion en un intervalo de tiempo fijo no tiene los mismos problemas que (2)?</b>
 * <br>
 * a) La velocidad de actualizacion puede ser mas rapida que la velocidad de fotogramas renderizada. Ahora, el jugador
 * que renderiza a 120 fps puede obtener 2 actualizaciones por cuadro, mientras que el jugador con renderizado de
 * hardware de menor especificacion a 30 fps obtiene 8 actualizaciones por cuadro, y ambos juegos se ejecutan a la misma
 * velocidad de juego por segundo en tiempo real. El mejor hardware hace que parezca mas fluido, pero no altera
 * radicalmente el funcionamiento del juego.
 *
 * <p>Nota: Los ticks se calculan en base al delta y el render en base a la velocidad del procesador.
 *
 * <br><br>
 *
 * <h2>Timestep</h2>
 * "Step" es un proceso de calculo del siguiente estado del sistema. "Timestep" es el intervalo de tiempo durante el
 * cual la simulacion progresara durante el siguiente "step".
 *
 * <p>El timestep tienen que ver con la sincronizacion y se dividen en fijos e variables. Un timestep fijo es util para
 * garantizar una experiencia consistente cuando cambia la velocidad de fotogramas. Por ejemplo, la integracion numerica
 * produce diferentes resultados con diferentes timesteps.
 *
 * <br><br>
 *
 * <h2><a href="https://gamedev.stackexchange.com/questions/1589/when-should-i-use-a-fixed-or-variable-time-step">¿Timestep fijo o variable?</a></h2>
 * <b><i>Timestep variable</i></b>
 * <br>
 * Las actualizaciones de fisica reciben un argumento de "tiempo transcurrido desde la ultima actualizacion" y, por
 * lo tanto, dependen de la velocidad de fotogramas. Esto puede significar hacer calculos como {@code position += distancePerSecond * timeElapsed}.
 *
 * <p><i>Pros</i>: suave, mas fácil de codificar
 * <br>
 * <i>Contras</i>: no determinista, impredecible en pasos muy pequeños o grandes
 *
 * <p><b><i>Timestep fijo</i></b>
 * <br>
 * Es posible que las actualizaciones ni siquiera acepten un "tiempo transcurrido", ya que asumen que cada actualizacion
 * es por un periodo de tiempo fijo. Los calculos se pueden realizar como {@code position += distancePerUpdate}. El
 * ejemplo incluye una interpolacion durante el renderizado.
 *
 * <p><i>Pros</i>: predecible, determinista (¿mas facil de sincronizar con la red?), codigo de calculo mas claro
 * <br>
 * <i>Contras</i>: no sincronizado para monitorear v-sync (causa graficos nerviosos a menos que interpole), velocidad
 * de cuadro maxima limitada (a menos que interpole), dificil de trabajar dentro de marcos que asumen timesteps
 * variables (como Pyglet o Flixel).
 *
 * <p><i>Despues de analizar los pros y contras de cada timestep, quedan dos cuestiones importantes...</i>
 * <ul>
 * <li>¿Deberia vincularse la velocidad de paso de la fisica a la velocidad de fotogramas?
 * <li>¿Debe la fisica ser escalonada con deltas constantes?
 * </ul>
 *
 * <p>En <a href="https://gafferongames.com/post/fix_your_timestep/">Fix your Timestep!</a> de Glen Fiedler, dice
 * "Liberar la fisica". Eso significa que su tasa de actualizacion de fisica <b>no</b> debe estar vinculada a su tasa de
 * fotogramas.
 *
 * <p>"Entonces, lo que queremos es lo mejor de ambos mundos: un valor de tiempo delta fijo para la simulacion mas la
 * capacidad de renderizar a diferentes velocidades de cuadro. Estas dos cosas parecen completamente opuestas, y lo son,
 * a menos que podamos encontrar una manera de desacoplar la simulacion y la velocidad de fotogramas de renderizado."
 *
 * <p>"Aqui esta como hacerlo. Haga avanzar la simulacion fisica en pasos de tiempo de dt fijos y, al mismo tiempo,
 * asegurese de que se mantiene al dia con los valores del temporizador que provienen del renderizador para que la
 * simulacion avance a la velocidad correcta. Por ejemplo, si la velocidad de fotogramas de la pantalla es de 50 fps y
 * la simulacion se ejecuta a 100 fps, debemos realizar dos pasos fisicos en cada actualizacion de la pantalla. Facil."
 *
 * <p>En las recomendaciones de Erin Catto para Box2D, el tambien aboga por esto.
 *
 * <p>"Por lo tanto, no vincule el paso de tiempo a su velocidad de cuadros (a menos que realmente tenga que hacerlo)."
 *
 * <p><i>¿Debe vincularse la frecuencia de pasos de fisica con la velocidad de fotogramas?</i> No.
 * <hr/>
 * <p>Los pensamientos de Erin sobre el paso fijo frente al paso variable:
 *
 * <p>"Box2D utiliza un algoritmo computacional llamado integrador. Los integradores simulan las ecuaciones fisicas en
 * puntos de tiempo discretos. ... Tampoco nos gusta que el paso del tiempo cambie mucho. Un paso de tiempo variable
 * produce resultados variables, lo que dificulta la depuracion."
 *
 * <p>Los pensamientos de Glen sobre pasos fijos vs variables:
 *
 * <p><b>Arregla tu timestep o explota</b>
 *
 * <p>"... Si tiene una serie de restricciones de resorte realmente rigidas para los amortiguadores en una simulacion de
 * automovil, entonces pequeños cambios en dt pueden hacer que la simulacion explote. ..."
 *
 * <p><i>¿Debe la fisica ser escalonada con deltas constantes?</i> Si.
 * <hr/>
 *
 * <p>El articulo <a href="https://www.reddit.com/r/gamedev/comments/22k6pl/fixed_time_step_vs_variable_time_step/">Timestep fijo vs timestep variable</a> dice...
 *
 * <p>"La mayor ventaja del paso fijo es la consistencia. Puede grabar solo la entrada del jugador en cada cuadro,
 * reproducirlo y ver una recreacion perfecta de todo lo que sucedio. Tambien es mas facil de desarrollar de muchas
 * maneras, ya que no tiene que preocuparse por multiplicar dt (delta time) por todo."
 *
 * <p>"Ademas, el paso variable significa que el juego se mueve a una velocidad constante independientemente de la
 * velocidad de fotogramas, lo que es excelente para admitir una amplia variedad de hardware."
 *
 * <p>"La mayor desventaja del paso variable es que tiende a explotar a velocidades de cuadro realmente bajas. Una
 * vez que dt se vuelve excesivamente grande, los objetos comienzan a moverse grandes distancias entre cuadros, lo que,
 * a menos que sea MUY cuidadoso, generalmente resulta en colisiones perdidas y bucles de
 * retroalimentacion/sobrecorreccion similares a resortes que arruinan el juego espectacularmente. En muchos casos, es
 * ventajoso limitar el dt a un valor maximo, volviendo efectivamente al paso fijo si la velocidad de fotogramas es lo
 * suficientemente mala."
 *
 * <p>Recursos:
 * <a href="http://gameprogrammingpatterns.com/game-loop.html">Game Loop</a>
 * <a href="https://www.gamedev.net/forums/topic/673798-what-is-a-timestep/">¿Que es un timestep?</a>
 * <a href="https://www.reddit.com/r/gamedev/comments/22k6pl/fixed_time_step_vs_variable_time_step/">Timestep fijo vs timestep variable</a>
 * <a href="https://gamedev.stackexchange.com/questions/1589/when-should-i-use-a-fixed-or-variable-time-step">¿Cuando debo usar un timestep fijo o variable?</a>
 * <a href="https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency">Eficiencia del Game Loop</a>
 * <a href="https://gafferongames.com/post/fix_your_timestep/">¡Arregla tu timestep!</a>
 * <a href="https://www.reddit.com/r/gamedev/comments/8sci48/should_i_be_using_a_fixed_timestep_or_delta_time/">¿Debo usar un timestep fijo o un delta?</a>
 * <a href="https://gamedev.stackexchange.com/questions/132831/what-is-the-point-of-update-independent-rendering-in-a-game-loop">¿Cual es el punto de actualizar (tick) independientemente del render en el Game Loop?</a>
 * <a href="https://stackoverflow.com/questions/57710138/why-gameloops-render-more-times-than-updating#:~:text=A%20tick%20is%20whenever%20game,to%20a%20redstone%20circuit%20updating">¿Por que el game loop se renderiza mas veces de las que se actualiza?</a>
 */

public class GameLoop implements Runnable {

	private Thread thread;
	private boolean running, stopped;

	/* Otra forma de detener el loop es declarando running como una variable volatil. La palabra clave volatil prohibe
	 * que una variable se copie a la memoria local; la variable permanece en la memoria principal. Por lo tanto, el
	 * cambio en esa variable por parte de un subproceso sera visto por todos los otros. */
	// private volatile boolean running;

	private static final int TICKS = 60; // o UPS (updates per second)

	@Override
	public void run() {

		Delta delta = new Delta(TICKS);
		int ticks = 0, frames = 0;
		boolean shouldRender = false; // TODO Se podria renombrar como "interpolation"

		while (isRunning()) {

			if (delta.checkDelta()) {
				ticks++;
				tick();
				// Actualiza primero para tener algo que renderizar en la primera iteracion
				shouldRender = true;
			}

			// Desacopla el renderizado de la fisica
			if (shouldRender) {
				frames++;
				render();
			}

			if (delta.checkTimer()) {
				System.out.println(ticks + " ticks, " + frames + " fps");
				ticks = 0;
				frames = 0;
				delta.resetTimer();
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
