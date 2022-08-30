package game_development;

/**
 * <h2>Reloj del CPU</h2>
 * Denominado alternativamente como <b>velocidad del reloj</b>, <b>velocidad del procesador</b> y <b>frecuencia del reloj</b>,
 * la velocidad del reloj es la velocidad a la que el microprocesador ejecuta cada instruccion o cada vibracion del <a href="https://www.computerhope.com/jargon/c/clock.htm">reloj</a>.
 * La CPU requiere un numero fijo de ticks de reloj, o ciclos, para <a href="https://www.computerhope.com/jargon/e/execute.htm">ejecutar</a>
 * cada <a href="https://www.computerhope.com/jargon/c/compinst.htm">instruccion</a>. Tu CPU procesa muchas
 * instrucciones (calculos de bajo nivel como los aritmeticos) a partir de diferentes programas a cada segundo.
 *
 * <p>Un "ciclo" es tecnicamente un pulso sincronizado por un oscilador interno, pero, para nuestros fines, es una
 * unidad basica que ayuda a comprender la velocidad de una CPU. Durante cada ciclo, miles de millones de transistores
 * dentro del procesador se abren y cierran.
 *
 * <p>Cuanto mayor sea la <a href="https://www.computerhope.com/jargon/f/frequenc.htm">frecuencia</a> del reloj de la
 * CPU, mas operaciones logicas puede realizar por segundo. Entonces, a medida que aumenta la frecuencia del reloj de la
 * CPU, disminuye el tiempo requerido para realizar las tareas.
 *
 * <p>La velocidad del reloj se miden en Hertz, 1 MHz representa 1 millon de ciclos por segundo, o en GHz, 1 GHz
 * representa 1 mil millones de ciclos por segundo. Cuanto mayor sea la velocidad de la CPU, mejor sera el rendimiento
 * de una computadora, en un sentido general. Otros componentes como la memoria RAM, el disco duro, la placa base y la
 * cantidad de nucleos del procesador (por ejemplo, de dos o cuatro nucleos) tambien pueden mejorar la velocidad de la
 * computadora.
 *
 * <p>La velocidad de la CPU determina cuantos calculos puede realizar en un segundo de tiempo. Cuanto mayor sea la
 * velocidad, mas calculos puede realizar, lo que hace que la computadora sea mas rapida. Si bien hay varias marcas de
 * procesadores de computadora disponibles, incluidas Intel y AMD, todas usan el mismo estandar de velocidad de CPU para
 * determinar que velocidad ejecuta cada uno de sus procesadores. Si un procesador tiene dos o cuatro nucleos, el
 * rendimiento de la computadora puede aumentar incluso si la velocidad de la CPU sigue siendo la misma. Un procesador
 * de doble nucleo de 3,0 GHz seria capaz de realizar el doble de calculos que un procesador de un solo nucleo de 3,10
 * GHz.
 *
 * <br><br>
 *
 * <h3>Diferencia entre el reloj del sistema y el reloj del cpu</h3>
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
 * <p>Recursos:
 * <a href="https://www.intel.es/content/www/es/es/gaming/resources/cpu-clock-speed.html#:~:text=La%20velocidad%20de%20reloj%20mide,la%20velocidad%20de%20la%20CPU">¿Que es la velocidad de reloj?</a>
 * <a href="https://www.computerhope.com/jargon/c/clockspe.htm">Velocidad de reloj</a>
 * <a href="https://www.youtube.com/watch?v=Bmgga8ZUiL8">¿Que es la frecuencia de un CPU?</a>
 * <a href="https://cs.stackexchange.com/questions/32149/what-are-system-clock-and-cpu-clock-and-what-are-their-functions">Diferencia entre el reloj del sistema y el reloj del cpu</a>
 * <a href="https://stackoverflow.com/questions/43651954/what-is-a-clock-cycle-and-clock-speed">¿Que es un ciclo de reloj y la velocidad del reloj?</a>
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
