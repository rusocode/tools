package gamedev;

import static utils.Global.*;

/**
 * <h1>Delta</h1>
 * El <b><i>Delta</i></b> (Δt, dt o delta time) se refiere al intervalo de tiempo entre dos {@link Tick actualizaciones}
 * consecutivas del motor de juego o del bucle de renderizacion. En esencia, <i>es el tiempo que ha transcurrido desde
 * el ultimo fotograma o ciclo de actualizacion hasta el fotograma o ciclo de actualizacion actual</i>. El concepto de
 * tiempo delta es esencial para garantizar un rendimiento uniforme y consistente en los videojuegos.
 * <p>
 * El tiempo delta se utiliza para ajustar la velocidad de los elementos en el juego en funcion del rendimiento del
 * sistema. Esto es especialmente importante porque diferentes sistemas pueden tener diferentes capacidades de
 * procesamiento y rendimiento. Si un juego no tuviera en cuenta el tiempo delta, los elementos del juego (como la
 * velocidad de movimiento de un personaje) podrian verse afectados por la velocidad del hardware y variarian en
 * diferentes sistemas.
 * <p>
 * En un juego con 24 FPS en una computadora lenta, el delta es de 0,041 segundos. Esto lleva a una acumulacion de
 * deltas que alcanzan 1 segundo, ejecutando 24 FPS. A menudo, esto provoca que la entidad salte (teletransporte) para
 * ajustar la distancia y completar la cantidad de FPS. En un hardware mas potente que ejecuta el juego a 60 FPS, el
 * delta es de 0,016 segundos, lo que permite movimientos mas fluidos. Por lo tanto, este enfoque se refiere a juegos
 * con independencia de fotogramas, en donde mantienen la misma velocidad en diferentes FPS. Para lograr esa
 * independencia de FPS, se emplea un <i>delta fijo</i> o <i>timestep fijo</i>, que se aplica tanto para los ciclos de
 * actualizaciones como para los de renderizado (AVERIGUAR BIEN!).
 * <br><br>
 * <h2>Escalonar la fisica con deltas fijos</h2>
 * Escalonar la fisica con deltas fijos implica establecer una cantidad fija de actualizaciones (ticks) por segundo.
 * La cantidad estandar suele ser 60 ticks por segundo, aunque puede variar. Algunos juegos como Minecraft y Quake3
 * optan por 20 ticks posiblemente para evitar sobrecargar la CPU.
 * <p>
 * Lo optimo es {@link Measure medir} el tiempo para el delta en nanosegundos, ya que es mas preciso para la CPU que los
 * milisegundos. Los nanosegundos no dependen del sistema operativo, sino del procesador, y se miden segun los ciclos de
 * reloj. Para calcular el tiempo entre cada actualizacion, se divide la cantidad de ticks por segundo entre 1E9
 * (1.000.000.000 de nanosegundos), que es aproximadamente 16.666.666 de nanosegundos, valor conocido como {@code nsPerTick},
 * que implica que el Game Loop espera ese tiempo antes de actualizar la fisica nuevamente.
 * <p>
 * La variable {@code unprocessed} cumple la funcion de llevar un seguimiento del tiempo no procesado o no utilizado
 * entre los ciclos de actualizacion del juego. Esta variable se utiliza para garantizar que las actualizaciones del
 * juego, especialmente las relacionadas con la logica y la simulacion, se realicen de manera coherente y controlada,
 * <i>independientemente de las variaciones en la velocidad de ejecucion del bucle de juego</i>. Esta variable hace
 * referencia al lag (retraso) entre dos ciclos, por lo que podria renombrarse como {@code lag}, pero eso ya es una
 * decision personal.
 * <p>
 * Aqui esta como funciona:
 * <ol>
 * <li>En el constructor de la clase Delta, se inicializa unprocessed en 0. Esto establece que no hay tiempo no
 * procesado acumulado al principio.
 * <li>En el metodo checkDelta(), se calcula el tiempo transcurrido desde el ultimo ciclo de actualizacion utilizando
 * System.nanoTime(). Luego, se divide esta diferencia de tiempo por nsPerTick para obtener un porcentaje decimal de
 * cuanto tiempo ha pasado en relacion con el tiempo necesario para un ciclo.
 * <li>Ese porcentaje de tiempo se acumula en la variable unprocessed. Esto significa que si el bucle de juego se
 * ejecuta mas rapido de lo esperado, el valor aumentara lentamente en cada ciclo. Por otro lado, si el bucle de juego
 * se ejecuta mas lento de lo esperado, el valor aumentara rapidamente en cada ciclo, ya que la diferencia de tiempo
 * entre los ciclos es mayor, por lo tanto se acumula un valor mas grande.
 * <li>La parte critica es el condicional if {@code (unprocessed >= 1)}. Si el valor acumulado en unprocessed es igual o
 * mayor a 1, significa que ha pasado al menos un ciclo completo y hay suficiente tiempo acumulado para realizar una
 * actualizacion. En este caso, el valor de unprocessed se reduce en 1 (para "consumir" ese ciclo) y se devuelve true
 * para indicar que es el momento de realizar una actualizacion.
 * <li>Si el valor acumulado en unprocessed es menor que 1, significa que no hay suficiente tiempo acumulado para
 * realizar una actualizacion. En este caso, se devuelve false para indicar que no es el momento de realizar una
 * actualizacion.
 * </ol>
 * <b>Esto hace posible que el juego se ejecute en cualquier dispositivo a la misma velocidad independientemente de los
 * FPS</b>.
 * <br><br>
 * Recursos:
 * <a href="https://www.parallelcube.com/es/2017/10/25/por-que-necesitamos-utilizar-delta-time/">¿Por que necesitamos utilizar Delta Time?</a>
 * <a href="https://www.youtube.com/watch?v=ix6FAPEF_HA">Sin ESTO los juegos se descontrolan</a>
 * <a href="https://stackoverflow.com/questions/26838286/delta-time-getting-60-updates-a-second-in-java">¿Como obtener 60 actualizaciones por segundo?</a>
 * <a href="https://stackoverflow.com/questions/88093/how-many-game-updates-per-second">¿Cuantas actualizaciones por segundo debo usar?</a>
 *
 * @author Juan Debenedetti
 */

public class Delta {

    private long startTime;
    private final double nsPerTick;
    // Mantiene un registro del tiempo no procesado (sin usar) para controlar cuando se debe realizar una actualizacion
    private double unprocessed;

    public Delta() {
        // Obtiene el tiempo inicial en nanosegundos
        startTime = System.nanoTime();
        // Calcula el tiempo en nanosegundos que deberia pasar entre cada tick para alcanzar la frecuencia deseada
        nsPerTick = 1E9 / TICKS_PER_SEC; // timestep fijo o delta
    }

    /**
     * Comprueba si el tiempo real alcanzo el timestep fijo.
     *
     * @return true si el tiempo real alcanzo el timestep fijo, o false.
     */
    public boolean checkTimestep() {
        // Verifica si se proceso el ciclo
        boolean processed = false;
        // Obtiene el tiempo actual en nanosegundos
        long currentTime = System.nanoTime();
        /* Se mide el tiempo desde el ciclo previo, se calcula un porcentaje en funcion de un valor constante
         * (nsPerTick) y se acumula en "unprocessed". Cuando este acumulado llega a 1/TICKS_PER_SEC (16.666.666 ns para
         * el caso de 60 ticks por segundo), se procede a actualizar la fisica del juego. Esto mide que tan atrasado
         * esta el reloj del juego en comparacion con el mundo real. */
        unprocessed += (currentTime - startTime) / nsPerTick;
        // Actualiza el tiempo de inicio para el siguiente ciclo
        startTime = currentTime;
        // Verifica si ha pasado suficiente tiempo no procesado para realizar una actualizacion
        while (unprocessed >= 1) {
            // El metodo ahora esta procesado por lo que se decrementa en 1
            unprocessed--;
            processed = true;
        }
        return processed;
    }

}
