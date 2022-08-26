package game_development;

/**
 * <h2>Tiempo del CPU</h2>
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
 * <p> Recursos:
 * <a href="https://www.intel.es/content/www/es/es/gaming/resources/cpu-clock-speed.html#:~:text=La%20velocidad%20de%20reloj%20mide,la%20velocidad%20de%20la%20CPU">¿Que es la velocidad de reloj de la CPU?</a>
 * <a href="https://www.youtube.com/watch?v=Bmgga8ZUiL8">¿Que es la frecuencia de un CPU?</a>
 * <a href="https://stackoverflow.com/questions/7467245/cpu-execution-time-in-java">Tiempo de ejecucion de la CPU en Java</a>
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
