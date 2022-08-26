package game_development;

/**
 * <h3>Tiempo del sistema</h3>
 * El <a href="https://en.wikipedia.org/wiki/System_time">tiempo del sistema</a> representa la nocion de un sistema
 * informatico del paso del tiempo. Este se mide mediante un <a href="https://www.intel.es/content/www/es/es/gaming/resources/cpu-clock-speed.html#:~:text=La%20velocidad%20de%20reloj%20mide,la%20velocidad%20de%20la%20CPU">reloj del sistema</a>,
 * que generalmente se implementa como un simple recuento de la cantidad de <i>ticks</i> que han transcurrido desde una
 * fecha de inicio arbitraria, denominada <a href="https://en.wikipedia.org/wiki/Epoch_(computing)">epoca</a>.
 *
 * <p>Una epoca es una fecha y hora a partir de la cual una computadora mide el tiempo del sistema.
 *
 * <p>El reloj del sistema generalmente se implementa como un temporizador de intervalo programable que interrumpe
 * periodicamente la CPU, que luego comienza a ejecutar una rutina de servicio de interrupcion del temporizador. Esta
 * rutina generalmente agrega un tick al reloj del sistema (un contador simple) y maneja otras tareas periodicas de
 * mantenimiento antes de regresar a la tarea que la CPU estaba ejecutando antes de la interrupcion.
 *
 * <p>Para recuperar la hora del sistema en Java se utilizan los metodos {@code System.currentTimeMillis()} que devuelve
 * el tiempo actual en milisegundos y el metodo {@code System.nanoTime()} que devuelve el tiempo en nanosegundos. La
 * epoca que toma {@code currentTimeMillis()} es desde el <i>1 de enero de 1970</i> y el valor devuelto por
 * {@code nanoTime()} es arbitrario.
 *
 * <p>Estrechamente relacionado con el tiempo del sistema esta el tiempo de CPU, que es un recuento del tiempo total de
 * CPU consumido por un proceso en ejecucion.
 *
 * <br><br>
 *
 * <h3>Tiempo del CPU</h3>
 * El <a href="https://en.wikipedia.org/wiki/CPU_time">tiempo de CPU</a> (o tiempo de proceso) es la cantidad de tiempo
 * durante el cual se utilizo una unidad central de procesamiento (CPU) para procesar instrucciones de un programa, a
 * diferencia del tiempo de pared, que incluye, por ejemplo, la espera de entrada/salida (I/O) o entrar en el modo de
 * bajo consumo (inactivo). El tiempo de la CPU se mide en ticks de reloj (tiempo de sistema) o segundos. A menudo, es
 * util medir el tiempo de la CPU como un porcentaje de la capacidad de la CPU, lo que se denomina uso de la CPU.
 *
 * <p>El uso de la CPU se utiliza para cuantificar como se comparte el procesador entre los programas de computadora. El
 * alto uso de la CPU por parte de un solo programa puede indicar que exige mucho poder de procesamiento o que puede
 * funcionar mal; por ejemplo, ha entrado en un bucle infinito.
 *
 * <p>Por el contrario, el <a href="https://en.wikipedia.org/wiki/Elapsed_real_time">tiempo real</a> (tiempo de reloj de
 * pared) es el tiempo que transcurre desde el inicio de un programa hasta el final medido por un reloj ordinario. El
 * tiempo real incluye el tiempo de E/S, los retrasos multitarea y todos los demas tipos de espera en los que incurre el
 * programa.
 *
 * <br><br>
 *
 * <h3>Diferencia entre el tiempo de sistema y el tiempo de CPU</h3>
 * El reloj del sistema es necesario para sincronizar todos los componentes de la placa base, lo que significa que
 * todos hacen su trabajo solo si el reloj esta alto; nunca cuando esta bajo. Y debido a que la velocidad del reloj se
 * establece por encima del tiempo mas largo que cualquier señal necesita para propagarse a traves de cualquier
 * circuito en la placa, este sistema evita que las señales lleguen antes de que otras señales esten listas y, por lo
 * tanto, mantiene todo seguro y sincronizado. El reloj de la CPU tiene el mismo proposito, pero solo se usa en el
 * propio chip. Debido a que la CPU necesita realizar mas operaciones por vez que la placa base, el reloj de la CPU es
 * mucho mas alto. Y como no queremos tener otro oscilador (por ejemplo, porque tambien tendrian que estar
 * sincronizados), la CPU simplemente toma el reloj del sistema y lo multiplica por un numero, que esta fijo o
 * desbloqueado (en ese caso, el usuario puede cambiar el multiplicador para hacer overclocking o underclockear la CPU).
 *
 * <br><br>
 *
 * <h3>Exactitud vs Precision</h3>
 * Si solo esta buscando mediciones extremadamente precisas del tiempo transcurrido, use {@code nanoTime()}.
 * {@code currentTimeMillis()} le dara el tiempo transcurrido mas preciso posible en milisegundos desde la epoca, pero
 * {@code nanoTime()} le dara un tiempo preciso en nanosegundos, relativo a algun punto arbitrario.
 *
 * <p>La llamada a {@code nanoTime()} es relativamente costosa. {@code currentTimeMillis()} se ejecuta en algunos (5-6)
 * relojes de CPU, {@code nanoTime()} depende de la arquitectura subyacente y puede tener mas de 100 relojes de CPU.
 *
 * <p>De la Documentacion de Java:
 *
 * <blockquote>
 * {@code public static long nanoTime()}
 *
 * <br><br>
 *
 * <p>Devuelve el valor actual del temporizador del sistema disponible mas preciso, en nanosegundos.
 *
 * <p>Este metodo solo se puede utilizar para medir el tiempo transcurrido y no esta relacionado con ninguna otra
 * nocion de tiempo del sistema o del reloj de pared. El valor devuelto representa nanosegundos desde algun tiempo de
 * <i>origen</i> fijo pero arbitrario (quizas en el futuro, por lo que los valores pueden ser negativos). Este metodo
 * proporciona una precision de nanosegundos, pero no necesariamente una precision de nanosegundos. No se garantiza la
 * frecuencia con la que cambian los valores. Las diferencias en llamadas sucesivas que abarcan mas de aproximadamente
 * 292 años (2<sup>63</sup> nanosegundos) no calcularan con precision el tiempo transcurrido debido al desbordamiento
 * numerico.
 * </blockquote>
 *
 * <p>Por ejemplo, para medir cuanto tiempo tarda en ejecutarse un codigo:
 *
 * <pre>{@code
 * long startTime = System.nanoTime();
 * // ... el codigo que se mide ...
 * long estimatedTime = System.nanoTime() - startTime;
 * }</pre>
 *
 * <p>Esta respuesta es tecnicamente correcta al elegir {@code nanoTime()} pero pasa por alto por completo un punto
 * extremadamente importante. {@code nanoTime()}, como dice el documento, es un temporizador de precision.
 * {@code currentTimeMillis()} NO ES UN TEMPORIZADOR, es el "reloj de pared".
 * {@code nanoTime()} siempre producira un tiempo transcurrido positivo, {@code currentTimeMillis} no lo hara (por
 * ejemplo, si cambia la fecha, golpea un segundo bisiesto, etc.) Esta es una distincion extremadamente importante para
 * algunos tipos de sistemas.
 *
 * <p>Otra cosa a mencionar...
 *
 * <p> No es seguro comparar los resultados de las llamadas {@code System.nanoTime()} entre diferentes JVM, cada JVM
 * puede tener un tiempo de "origen" independiente.
 *
 * <p>{@code System.currentTimeMillis()} devolvera el mismo valor (aproximado) entre JVM, porque esta vinculado a la
 * hora del reloj de pared del sistema.
 *
 * <p> Si desea calcular la cantidad de tiempo transcurrido entre dos eventos, como un cronometro, use {@code nanoTime()}
 * los cambios en el reloj de pared del sistema hacen que {@code currentTimeMillis()} sea incorrecto para este caso de
 * uso.
 *
 * <p> Ver tambien: <a href="http://docs.oracle.com/javase/8/docs/api/java/lang/System.html#nanoTime--">JavaDoc System.nanoTime()</a> y <a href="http://docs.oracle.com/javase/8/docs/api/java/lang/System.html#currentTimeMillis--">JavaDoc System.currentTimeMillis()</a> para mas informacion.
 *
 * <p> Recursos:
 * <a href="https://www.intel.es/content/www/es/es/gaming/resources/cpu-clock-speed.html#:~:text=La%20velocidad%20de%20reloj%20mide,la%20velocidad%20de%20la%20CPU">¿Que es la velocidad de reloj de la CPU?</a>
 * <a href="https://www.youtube.com/watch?v=Bmgga8ZUiL8">¿Que es la frecuencia de un CPU?</a>
 * <a href="https://stackoverflow.com/questions/351565/system-currenttimemillis-vs-system-nanotime">currentTimeMillis vs nanoTime</a>
 * <a href="https://stackoverflow.com/questions/7467245/cpu-execution-time-in-java">Tiempo de ejecucion de la CPU en Java</a>
 * <a href="https://www.quora.com/What-is-the-amount-of-times-that-a-while-loop-runs-per-second-in-C">¿Cual es la cantidad de veces que se ejecuta un ciclo while() por segundo en C#?</a>
 */

public class CpuClock implements Runnable {

	private int c;
	private boolean stopped;

	@Override
	public void run() {
		while (!isStopped()) c++;
	}

	private synchronized void stop() {
		stopped = true;
	}

	private synchronized boolean isStopped() {
		return stopped;
	}

	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Muestra la cantidad de veces que se ejecuta el ciclo while por segundo usando un subproceso.
	 *
	 * <p>El resultado depende del codigo maquina en el que se compila el codigo, de la intensidad del CPU que este
	 * ocurriendo durante el ciclo, de la velocidad del procesador y de como se ejecute el ciclo.
	 *
	 * <p>Nota: Nunca confie en la velocidad del ciclo para el tiempo. Si necesita cronometraje, use temporizadores
	 * (timer).
	 */
	private void whileLoopPerSec1() {
		new Thread(this).start();
		sleep();
		System.out.println(c + " ejecuciones en un segundo!");
		stop();
	}

	/**
	 * Muestra la cantidad de veces que se ejecuta el ciclo while por segundo usando {@code currentTimeMillis()} para
	 * condicionar el tiempo cada 1 segundo.
	 *
	 * <p>El ciclo se ejecuta mucho mas rapido de esta forma ya que los subprocesos son costosos y requieren mas tiempo.
	 */
	private static void whileLoopPerSec2() {
		int c = 0;
		long lastTime = System.currentTimeMillis();
		while (true) {
			c++;
			if (System.currentTimeMillis() - lastTime > 1000) {
				lastTime += 1000;
				System.out.println(c + " ejecuciones en un segundo!");
				c = 0;
			}
		}
	}

	/**
	 * Devuelve el numero de procesadores disponibles para la maquina virtual Java en el momento en que se llama al
	 * metodo, por lo que no necesariamente devolvera la cantidad de procesadores reales en un sistema.
	 *
	 * <p>Este valor puede cambiar durante una invocacion particular de la maquina virtual. Por lo tanto, las
	 * aplicaciones que son sensibles a la cantidad de procesadores disponibles deben sondear ocasionalmente esta
	 * propiedad y ajustar su uso de recursos de manera adecuada.
	 *
	 * <p>Para obtener los cantidad de procesadores reales del sistema, lo mas probable es que haya que realizar
	 * llamadas al sistema operativo utilizando llamadas nativas a traves de Java Native Interface (JNI).
	 *
	 * @return el numero maximo de procesadores disponibles para la maquina virtual; nunca mas pequeño que uno.
	 */
	private static int avaliableProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}

	public static void main(String[] args) {
		// System.out.println("Procesadores disponibles: " + avaliableProcessors());
		// new CpuClock().whileLoopPerSec();
		whileLoopPerSec2();
	}


}
