package gamedev;

/**
 * <h1>Reloj del CPU</h1>
 * En pocas palabras, el reloj del procesador (tambien conocido como «frecuencia de reloj» o «frecuencia») es la
 * velocidad a la que el procesador (circuito electrico) ejecuta cada instruccion por segundo.
 *
 * <p>Los procesadores funcionan por ciclos. Por ejemplo, a una calculadora que tiene una lista de operaciones para
 * calcular y devolver el resultado se le llama ciclo.
 *
 * <p>La frecuencia (ciclos por segundo) se regula desde un oscilador de cristal. Este es un componente electronico que
 * esta echo de cristal, normalmente de cuarzo y tiene la capacidad de mandar al procesador a funcionar de forma
 * periodica. Este componente se conoce como <i>reloj</i>.
 *
 * <p>La velocidad del reloj se miden en Hertz. Cuanto mayor sea la frecuencia del reloj, mas operaciones puede realizar
 * por segundo. Otros componentes como la memoria RAM, el disco duro, la placa base y la cantidad de nucleos del
 * procesador (por ejemplo, de dos o cuatro nucleos) pueden mejorar la velocidad de la computadora.
 *
 * <p>Normalmente el reloj tiene por defecto una frecuencia base de 1 GHz. Eso significa que de base, el reloj manda
 * al procesador a hacer mil millones de operaciones (ciclos) por segundo. Cada ciclo del procesador es un conjunto de
 * operaciones con un resultado. En su version mas basica podemos imaginar que es una suma, una resta, etc. Todas estas
 * operaciones son las que hacen funcionar a la computadora.
 *
 * <br><br>
 *
 * <h2>Reloj del sistema</h2>
 * El <a href="https://en.wikipedia.org/wiki/System_time">reloj del sistema</a> representa la nocion de un sistema
 * informatico del paso del tiempo. Este se mide mediante un reloj, que generalmente se implementa como un simple
 * recuento de la cantidad de ticks que han transcurrido desde una fecha de inicio arbitraria, denominada <a href="https://en.wikipedia.org/wiki/Epoch_(computing)">epoca</a>.
 *
 * <p>Un <b>tick</b> es una unidad arbitraria para medir el tiempo interno del sistema. Por lo general, hay un contador
 * interno del sistema operativo para los ticks; la hora y la fecha actuales utilizadas por varias funciones del sistema
 * operativo se derivan de ese contador.
 *
 * <p>El reloj del sistema generalmente se implementa como un temporizador de intervalo programable que interrumpe
 * periodicamente la CPU, que luego comienza a ejecutar una rutina de servicio de interrupcion del temporizador. Esta
 * rutina generalmente agrega un tick al reloj del sistema (un contador simple) y maneja otras tareas periodicas de
 * mantenimiento antes de regresar a la tarea que la CPU estaba ejecutando antes de la interrupcion.
 *
 * <p>Para obtener la hora del sistema en Java se utilizan los metodos {@code currentTimeMillis()} que devuelve el
 * tiempo actual en milisegundos y {@code nanoTime()} que devuelve el tiempo en nanosegundos. La epoca que
 * toma {@code currentTimeMillis()} es desde el <i>1 de enero de 1970</i> y el valor devuelto por {@code nanoTime()} es
 * arbitrario.
 *
 * <p>Estrechamente relacionado con el reloj del sistema esta el tiempo de CPU, que es un recuento del tiempo total de
 * CPU consumido por un proceso en ejecucion.
 *
 * <br><br>
 *
 * <h2>Diferencia entre el reloj del sistema y el reloj del cpu</h2>
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
 * <h2>Tiempo del CPU</h2>
 * El <a href="https://en.wikipedia.org/wiki/CPU_time">tiempo del CPU</a> es la cantidad de tiempo durante el cual se
 * utilizo el CPU para procesar instrucciones de un programa, a diferencia del <a href="https://en.wikipedia.org/wiki/Elapsed_real_time">tiempo de pared</a>,
 * que incluye, por ejemplo, la espera de E/S o entrar en el modo de bajo consumo (inactivo). El tiempo del CPU se mide
 * con el reloj del sistema. A menudo, es util medir el tiempo del CPU como un porcentaje de la capacidad de este, lo
 * que se denomina uso del CPU.
 *
 * <p>El uso del CPU se utiliza para cuantificar como se comparte el procesador entre los programas de computadora. El
 * alto uso del CPU por parte de un solo programa puede indicar que exige mucho poder de procesamiento o que puede
 * funcionar mal; por ejemplo, ha entrado en un bucle infinito.
 *
 * <p>Por el contrario, el tiempo de pared es el tiempo que transcurre desde el inicio de un programa hasta el final
 * medido por un reloj ordinario. El tiempo real incluye el tiempo de E/S, los retrasos multitarea y todos los demas
 * tipos de espera en los que incurre el programa.
 *
 * <p>Recursos:
 * <a href="https://www.youtube.com/watch?v=SeNrVQBRHqE">¡Antes de hacer Overclock, mira esto!</a>
 * <a href="https://www.intel.es/content/www/es/es/gaming/resources/cpu-clock-speed.html#:~:text=La%20velocidad%20de%20reloj%20mide,la%20velocidad%20de%20la%20CPU">¿Que es la velocidad de reloj?</a>
 * <a href="https://www.computerhope.com/jargon/c/clockspe.htm">Velocidad de reloj</a>
 * <a href="https://www.youtube.com/watch?v=Bmgga8ZUiL8">¿Que es la frecuencia de un CPU?</a>
 * <a href="https://cs.stackexchange.com/questions/32149/what-are-system-clock-and-cpu-clock-and-what-are-their-functions">Diferencia entre el reloj del sistema y el reloj del cpu</a>
 * <a href="https://stackoverflow.com/questions/43651954/what-is-a-clock-cycle-and-clock-speed">¿Que es un ciclo de reloj y la velocidad del reloj?</a>
 * <a href="https://stackoverflow.com/questions/7467245/cpu-execution-time-in-java">Tiempo de ejecucion de la CPU en Java</a>
 * <a href="https://serverfault.com/questions/48455/what-are-the-differences-between-wall-clock-time-user-time-and-cpu-time">¿Cual es la diferencia entre reloj de pared, hora del usuario y el tiempo del cpu?</a>
 *
 * @author Juan Debenedetti
 */

public class CpuClock {

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
		System.out.println(avaliableProcessors());
	}
}
