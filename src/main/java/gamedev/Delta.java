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
 * framerate independent se aplica el <b><i>Delta Time</i></b> (abreviado Δt, dt o Delta).
 *
 * <p>Delta Time es el <b>tiempo transcurrido entre cada frame renderizado durante un segundo</b>.
 *
 * <p>A una tasa de fotogramas muy bajas, el delta puede llegar a ser muy grande. Por ejemplo, a 5 FPS, delta es 0,2.
 * Por lo tanto, la entidad que se mueve a 500 pixeles por segundo se mueve 100 pixeles por fotograma. Esto puede causar
 * que se "teletransporte" causando perdida de colisiones con otros objetos.
 *
 * <br><br>
 *
 * <h2>¿Como calcular el Delta?</h2>
 * Antes que nada necesitamos saber cuantos {@link Tick ticks} por segundo se van a aplicar a al fisica. Para este caso
 * se toman 60 ticks, osea, 60 actualizaciones por segundo. La fisica del juego se puede actualizar a 30, 60 o 80 veces
 * por segundo, pero la cantidad estandar suele ser de 60. Algunos juegos como Minecraft y Quake3 usan 20 ticks (¿Para
 * no acaparar demasiado la CPU quiza?). Recuerden que la fisica no tiene nada que ver con la cantidad de veces que se
 * dibuja en pantalla, aunque esten estrechamente relacionados.
 *
 * <p>Lo ideal es {@link Measure medir} el tiempo para el delta en nanosegundos ya que es una unidad mucho mas
 * especifica para la CPU que los milisegundos. Los nanosegundos no dependen del sistema operativo, sino del procesador
 * y se miden tomando como referencia los ciclos de reloj del procesador. Un segundo equivale a 1.000.000.000 de
 * nanosegundos o 1e9. Entonces, para saber la cantidad de tiempo en nanosegundos que hay entre cada frame aplicando 60
 * ticks, se calcula 60/1.000.000.000, que es aproximadamente 16.666.666 de nandosegundos, valor conocido como {@link Delta#nsPerTick}
 * (nanosegundos por tick). Esto significa que cada vez que se actuliza la fisica del juego, el Game Loop espera
 * 16.666.666 de nanosegundos antes de volver a hacerlo.
 *
 * <p>Ahora para encontrar el delta, es necesario calcular la diferencia de tiempo en nanosegundos del sistema actual e
 * inicial acumulando el resultado en cada vuelta del Game Loop. Cuando el delta sea >= 1/60 de segundo o 16.666.666 de
 * nanosegundos para ser mas especificos, entonces actualiza nuevamente. Es importante eliminar 1/60 de segundo del
 * delta despues de actualizar, para que comience a contar desde el "desbordamiento" de tiempo hasta que alcance 1/60 de
 * segundo nuevamente. <b>Esto hace posible que el juego se ejecute en cualquier dispositivo a la misma velocidad sin
 * importar los FPS</b>.
 *
 * <p>En algunos casos vas a encontrar la operacion {@code (currentTime - startTime) / nsPerTick}. Esta operacion solo
 * esta ahi para hacer que el delta actue como un porcentaje decimal de 1 de cuanto ha pasado del tiempo necesario. El 1
 * representa el 100% de 1/60 ticks.
 *
 * <p>La varible {@code timer} sirve como temporizador para mostrar la cantidad de ticks y frames cada un segundo.
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
	private final double nsPerTick;
	private double delta;

	public Delta(int ticks) {
		startTime = System.nanoTime();
		nsPerTick = 1e9 / ticks;
	}

	/**
	 * Comprueba si el delta alcanzo el timestep.
	 *
	 * @return true si el delta alcanzo el timestep, o false.
	 */
	public boolean checkDelta() {
		long currentTime = System.nanoTime();
		delta += currentTime - startTime;
		timer += currentTime - startTime;
		startTime = currentTime;
		if (delta >= nsPerTick) {
			delta -= nsPerTick;
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
