package gamedev;

import static utils.Global.*;

/**
 * Este temporizador permite sincronizar la simulacion del juego utilizando un delta fijo (fixedDelta o fixedTimeStep),
 * garantizando un numero constante de actualizaciones (ticks) por segundo. La cantidad estandar suele ser 60 ticks por
 * segundo, aunque puede variar. Algunos juegos como Minecraft y Quake3 optan por 20 ticks posiblemente para evitar
 * sobrecargar la CPU.
 * <p>
 * El temporizador {@link Measure mide} el tiempo en nanosegundos para mayor precision y confiabilidad en la CPU. Los
 * nanosegundos no dependen del sistema operativo, sino del procesador, y se miden segun los ciclos de reloj. Para
 * calcular el tiempo entre cada actualizacion, se divide la cantidad de ticks por segundo entre 1E9 (1.000.000.000 de
 * nanosegundos), aproximadamente 16.666.666 nanosegundos, valor conocido como delta fijo. Esto implica que el Game Loop
 * espera ese tiempo antes de actualizar la simulacion nuevamente.
 * <p>
 * La variable {@code unprocessed} lleva un seguimiento del tiempo no procesado o no utilizado entre los ciclos de
 * actualizacion del juego. Garantiza que las actualizaciones del juego, en especial las relacionadas con la logica y la
 * simulacion, se realicen de manera coherente y controlada, <i>independientemente de las variaciones en la velocidad de
 * ejecucion del bucle del juego</i>. El termino "unprocessed" hace referencia al lag (retraso) entre dos ciclos, y
 * podria renombrarse como "lag".
 * <p>
 * Funcionamiento:
 * <ol>
 * <li>En el constructor de la clase GameTimer, "unprocessed" se inicializa en 0. Esto establece que no hay tiempo no
 * procesado acumulado al principio.
 * <li>En el metodo "shouldUpdate()", se calcula el tiempo transcurrido desde el ultimo ciclo de actualizacion
 * utilizando System.nanoTime(). Luego, se divide esta diferencia de tiempo por nsPerTick para obtener un porcentaje
 * decimal de cuanto tiempo ha pasado en relacion con el tiempo necesario para una actualizacion.
 * <li>Ese porcentaje de tiempo se acumula en la variable "unprocessed". Esto significa que si el bucle de juego se
 * ejecuta mas rapido de lo esperado, el valor aumentara lentamente en cada ciclo. Por otro lado, si el bucle de juego
 * se ejecuta mas lento de lo esperado, el valor aumentara rapidamente en cada ciclo, ya que la diferencia de tiempo
 * entre los ciclos es mayor, por lo que se acumula un valor mas grande.
 * <li>La parte critica es el condicional "if (unprocessed >= 1)". Si el valor acumulado en "unprocessed" es igual o
 * mayor a 1, significa que ha pasado al menos un ciclo completo y hay suficiente tiempo acumulado para realizar una
 * actualizacion. En este caso, el valor de "unprocessed" se reduce en 1 (para "consumir" ese ciclo) y se devuelve true
 * para indicar que es el momento de realizar una actualizacion.
 * <li>Si el valor acumulado en "unprocessed" es menor que 1, significa que no hay suficiente tiempo acumulado para
 * realizar una actualizacion. En este caso, se devuelve false para indicar que no es el momento de realizar una
 * actualizacion.
 * </ol>
 * <b>Esto hace posible que el juego se ejecute en cualquier dispositivo a la misma velocidad independientemente de las
 * variaciones en la velocidad de ejecucion del bucle del juego</b>.
 * <br><br>
 * Recursos:
 * <a href="https://www.parallelcube.com/es/2017/10/25/por-que-necesitamos-utilizar-delta-time/">¿Por que necesitamos utilizar Delta Time?</a>
 * <a href="https://www.youtube.com/watch?v=ix6FAPEF_HA">Sin ESTO los juegos se descontrolan</a>
 * <a href="https://stackoverflow.com/questions/26838286/delta-time-getting-60-updates-a-second-in-java">¿Como obtener 60 actualizaciones por segundo?</a>
 * <a href="https://stackoverflow.com/questions/88093/how-many-game-updates-per-second">¿Cuantas actualizaciones por segundo debo usar?</a>
 * <a href="https://forum.unity.com/threads/how-exactly-time-fixeddeltatime-time-deltatime-are-defined.322948/">fixedDeltaTime vs deltaTime</a>
 *
 * @author Juan Debenedetti
 */

public class GameTimer {

    private long startTime;
    private final double nsPerTick;
    // Mantiene un registro del tiempo no procesado (sin usar) para controlar cuando se debe realizar una actualizacion
    private double unprocessed;

    public GameTimer() {
        // Obtiene el tiempo inicial en nanosegundos
        startTime = System.nanoTime();
        // Calcula el tiempo en nanosegundos que deberia pasar entre cada tick para alcanzar la frecuencia deseada
        nsPerTick = 1E9 / TICKS_PER_SEC; // timestep fijo o delta
    }

    /**
     * Verifica si ha pasado el tiempo necesario para realizar una actualizacion del juego
     * en funcion del timestep fijo definido.
     *
     * @return true si se debe realizar una actualizacion del juego, o false.
     */
    public boolean shouldUpdate() {
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
