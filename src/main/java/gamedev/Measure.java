package gamedev;

/**
 * <h1>Exactitud vs Precision</h1>
 * Si solo esta buscando mediciones extremadamente precisas del tiempo transcurrido, use {@code nanoTime()}.
 * {@code currentTimeMillis()} le dara el tiempo transcurrido mas preciso posible en milisegundos desde la epoca, pero
 * {@code nanoTime()} le dara un tiempo preciso en nanosegundos, relativo a algun punto arbitrario.
 *
 * <p>La llamada a {@code nanoTime()} es relativamente costosa. {@code currentTimeMillis()} se ejecuta en algunos (5-6)
 * relojes de CPU, {@code nanoTime()} depende de la arquitectura subyacente y puede tener mas de 100 relojes de CPU.
 *
 * <p>Para medir cuanto tiempo tarda en ejecutarse un codigo:
 *
 * <pre>{@code
 * long startTime = System.nanoTime();
 * // ... el codigo que se mide ...
 * long estimatedTime = System.nanoTime() - startTime;
 * }</pre>
 *
 * <p>Para comparar el tiempo transcurrido con un tiempo de espera, utilice
 * <pre>{@code if (System.nanoTime() - startTime >= timeoutNanos)}</pre>
 * en vez de <pre>{@code
 * if (System.nanoTime() >= startTime + timeoutNanos)}</pre>
 * debido a la posibilidad de desbordamiento numerico.
 *
 * <p>{@code nanoTime()}, como dice el documento, es un temporizador de precision. {@code currentTimeMillis()} NO ES UN
 * TEMPORIZADOR, es el <b>reloj de pared</b> del sistema. {@code nanoTime()} siempre producira un tiempo transcurrido
 * positivo, {@code currentTimeMillis()} no lo hara (por ejemplo, si cambia la fecha, golpea un segundo bisiesto, etc.).
 * Esta es una distincion extremadamente importante para algunos tipos de sistemas.
 *
 * <p><i>Otra cosa a mencionar...</i>
 *
 * <p>No es seguro comparar los resultados de las llamadas a {@code nanoTime()} entre diferentes JVM, cada JVM puede
 * tener un tiempo de "origen" independiente.
 *
 * <p>{@code currentTimeMillis()} devolvera el mismo valor (aproximado) entre JVM, porque esta vinculado a la hora del
 * reloj de pared.
 *
 * <p>Recursos:
 * <a href="https://stackoverflow.com/questions/351565/system-currenttimemillis-vs-system-nanotime">currentTimeMillis() vs nanoTime()</a>
 * <a href="https://stackoverflow.com/questions/19052316/why-is-system-nanotime-way-slower-in-performance-than-system-currenttimemill">¿Por que nanoTime() es mucho mas lento (en rendimiento) que currentTimeMillis()?</a>
 */

public class Measure {

	/**
	 * Devuelve la hora actual en milisegundos. Tenga en cuenta que, si bien la unidad de tiempo del valor devuelto es
	 * un milisegundo, la granularidad del valor depende del sistema operativo subyacente y puede ser mayor.
	 *
	 * @return la diferencia, medida en milisegundos, entre la hora actual y la medianoche del 1 de enero de 1970 UTC.
	 */
	private static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * Devuelve el valor actual de la fuente de tiempo de alta resolucion de la maquina virtual Java en ejecucion, en
	 * nanosegundos. Este metodo solo se puede utilizar para medir el tiempo transcurrido y no esta relacionado con
	 * ninguna otra nocion de tiempo del sistema o del reloj de pared.
	 *
	 * @return el valor actual de la fuente de tiempo de alta resolucion de la maquina virtual Java en ejecucion, en
	 * nanosegundos.
	 */
	private static long getNanoTime() {
		return System.nanoTime();
	}

	public static void main(String[] args) {
		System.out.println(getNanoTime());
	}

}
