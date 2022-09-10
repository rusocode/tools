package gamedev;

/**
 * <h1>¿Por que necesitamos utilizar el Delta Time?</h1>
 * Los juegos <b>framerate independent</b> (independencia de fotogramas), son juegos que corren a la misma velocidad sin
 * importar la tasa de {@link FPS}. Por ejemplo, una entidad (jugador, zombie, etc.) puede moverse a 30 FPS en una
 * computadora lenta y 60 FPS en una rapida, pero ambos recorren la misma distancia a la misma velocidad. La diferencia
 * esta en que a 30 FPS, la entidad da como "saltos" para equilibrar (que ironico) la distancia que necesita alcanzar, y
 * a 60 FPS, el movimiento es mas fluido ya que se renderizan mas frames por segundo. Esa cantidad variable de FPS
 * depende del hardware del dispositivo en el que se este ejecutando el juego. Por otra parte, un juego <b>framerate
 * dependent</b> (dependencia de fotogramas) se procesa a la mitad de la velocidad en una computadora lenta, dando el
 * horrible efecto del <a href="https://www.xataka.com/basics/lag-que-a-que-puede-deberse">lag</a>. Para hacer el juego
 * framerate independent se aplica el <b><i>Delta Time</i></b> (abreviado Δt, dt o delta).
 *
 * <p>Delta Time es el <b>tiempo transcurrido entre cada frame renderizado</b>.
 *
 * <p>Por ejemplo si el juego en tu PC va a 10 FPS (por que es un microondas), el delta seria de 0,1 seg. Cuando el
 * juego procesa todo lo relacionado con el juego una vez, tarda 0,1 seg hasta volver a procesarlo en el siguiente frame
 * y van transcurriendo todos los delta hasta que suman 1 segundo. Ahi ya se han ejecutado esos 10 FPS. Al ser una tasa
 * de fotogramas muy baja, puede causar que la entidad se "teletransporte" causando perdida de colisiones con otros
 * objetos.
 *
 * <p>Por otro lado si tu PC es un poco mejor y corre el juego a 30 FPS, el delta en este caso sera de 0,033 seg. Como
 * hay muchos mas frames por segundo, se estan ejecutando frames con mucha mas frecuencia. Eso significa que el tiempo
 * entre dos frames sera mucho mas pequeño, y como hay tantos frames, en cuanto termina uno, enseguida ya llega el
 * siguiente. De ahi que ahora el delta sea de 0,033 seg, mucho mas pequeño que antes que era de 0,1 seg. Esto genera
 * un "movimiento" de la entidad mas fluido.
 *
 * <br><br>
 *
 * <h2>Escalonar la fisica con deltas constantes (<i>Fixed Timestep</i>)</h2>
 * Antes que nada, necesitamos saber cuantos {@link Tick ticks} por segundo se van a aplicar a la fisica del juego. Para
 * este caso se toman 60 ticks, osea, 60 actualizaciones por segundo. La fisica del juego se puede actualizar a 30, 60 o
 * 80 veces por segundo, pero la cantidad estandar suele ser de 60. Algunos juegos como Minecraft y Quake3 usan 20 ticks
 * (¿Para no acaparar demasiado la CPU quiza?). La fisica no tiene nada que ver con la cantidad de veces que se dibuja
 * en pantalla, aunque esten estrechamente relacionados.
 *
 * <p>Lo ideal es {@link Measure medir} el tiempo para el delta en nanosegundos ya que es una unidad mucho mas
 * especifica para la CPU que los milisegundos. Los nanosegundos no dependen del sistema operativo, sino del procesador
 * y se miden tomando como referencia los ciclos de reloj del procesador. Un segundo equivale a 1.000.000.000 de
 * nanosegundos o 1e9. Entonces, para saber la cantidad de tiempo en nanosegundos que hay entre cada frame aplicando 60
 * ticks, se calcula 60/1.000.000.000, que es aproximadamente 16.666.666 de nandosegundos, valor conocido como {@link Delta#delta}
 * (fixed timestep o paso del tiempo fijo para este caso, ya que la otra alternativa es usar un paso del tiempo
 * variable, aunque no es recomendable). Esto significa que cada vez que se actuliza la fisica del juego, el Game Loop
 * espera 16.666.666 de nanosegundos antes de volver a hacerlo.
 *
 * <p>Ahora para comprobar si el Game Loop alcanzo el tiempo delta, es necesario calcular la diferencia de tiempo en
 * nanosegundos del sistema actual e inicial acumulando el resultado en cada ciclo del Game Loop en la variable {@link Delta#elapsed}.
 * Cuando el tiempo transcurrido del ciclo sea igual o mayor al tiempo delta, entonces actualiza la fisica. Es
 * importante eliminar el tiempo del delta del tiempo transcurrido del ciclo despues de actualizar, para que comience a
 * contar desde el "desbordamiento" de tiempo hasta que alcance el delta nuevamente. <b>Esto hace posible que el juego
 * se ejecute en cualquier dispositivo a la misma velocidad independientemente de los FPS</b>.
 *
 * <p>En algunos casos vas a encontrar la operacion {@code (currentTime - startTime) / delta}. Esta operacion solo
 * esta ahi para hacer que el delta actue como un porcentaje decimal de 1 de cuanto ha pasado del tiempo necesario. El 1
 * representa el 100% del tiempo delta.
 *
 * <p>La varible {@code timer} sirve como temporizador para mostrar la cantidad de ticks y frames cada un segundo.
 *
 * <p><i>Nota: Esta clase esta relacionada con la clase {@link GameLoop}, pero las separe para explicar diferentes
 * conceptos y quede mas legible.</i>
 *
 * <p>Recursos:
 * <a href="https://www.parallelcube.com/es/2017/10/25/por-que-necesitamos-utilizar-delta-time/">¿Por que necesitamos utilizar Delta Time?</a>
 * <a href="https://www.youtube.com/watch?v=ix6FAPEF_HA">Sin ESTO los juegos se descontrolan</a>
 * <a href="https://stackoverflow.com/questions/26838286/delta-time-getting-60-updates-a-second-in-java">¿Como obtener 60 actualizaciones por segundo?</a>
 * <a href="https://stackoverflow.com/questions/88093/how-many-game-updates-per-second">¿Cuantas actualizaciones por segundo debo usar?</a>
 *
 * @author Juan Debenedetti
 */

public class Delta {

	private int timer;
	private long startTime;
	private final double delta;
	private double elapsed, lag; // lag = retraso

	public Delta(final int ticks) {
		startTime = System.nanoTime();
		delta = 1e9 / ticks;
	}

	/**
	 * Comprueba si el Game Loop alcanzo el tiempo delta.
	 *
	 * @return true si el Game Loop alcanzo el tiempo delta, o false.
	 */
	public boolean checkDelta() {
		long currentTime = System.nanoTime();
		elapsed = currentTime - startTime;
		lag += elapsed;
		timer += currentTime - startTime;
		startTime = currentTime;
		if (lag >= delta) {
			lag -= delta;
			return true;
		} else return false;
	}

	/**
	 * Comprueba si el timer alcanzo 1 segundo.
	 *
	 * @return true si el timer alcanzo 1 segundo, o false.
	 */
	public boolean checkTimer() {
		return timer >= 1e9;
	}

	/**
	 * Resetea el timer a 0.
	 */
	public void resetTimer() {
		timer = 0;
	}


}
