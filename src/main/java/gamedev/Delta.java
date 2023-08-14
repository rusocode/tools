package gamedev;

/**
 * <h1>¿Por que necesitamos utilizar el tiempo Delta?</h1>
 * El <b><i>Delta Time</i></b> (Δt, dt o delta) se refiere al intervalo de tiempo entre dos actualizaciones consecutivas del motor de juego o
 * del bucle de renderizacion. En esencia, <i>es el tiempo que ha transcurrido desde el ultimo {@link FPS fotograma} o ciclo de
 * actualizacion hasta el fotograma o ciclo de actualizacion actual</i>. El concepto de tiempo delta es esencial para
 * garantizar un rendimiento uniforme y consistente en los videojuegos.
 * <p>
 * El tiempo delta se utiliza para ajustar la velocidad de los elementos en el juego en funcion del rendimiento del
 * sistema. Esto es especialmente importante porque diferentes sistemas pueden tener diferentes capacidades de
 * procesamiento y rendimiento. Si un juego no tuviera en cuenta el tiempo delta, los elementos del juego (como la
 * velocidad de movimiento de un personaje) podrian verse afectados por la velocidad del hardware y variarian en
 * diferentes sistemas.
 * <p>
 * En el caso de un juego con 24 FPS en una computadora lenta, el delta seria de 0,041 segundos. El juego procesa todo
 * en ese tiempo antes de volver a procesar en el siguiente frame. La acumulacion de estos delta alcanza 1 segundo,
 * ejecutando asi los 24 FPS. Dado que es una tasa de fotogramas baja, es posible que la entidad muestre un efecto de
 * "teletransportacion".
 * <p>
 * En una PC un poco mejor, con 60 FPS, el delta time es de 0,016 segundos. Con mas frames por segundo, los frames se
 * ejecutan con mayor frecuencia, resultando en un tiempo mas corto entre cada uno. Esto crea un movimiento mas fluido
 * para las entidades en el juego. Comparado con los 24 FPS anteriores, donde el delta era de 0,041 segundos, este delta
 * mas pequeño de 0,016 segundos genera un movimiento mas suave. Ver "Diferencia en la cantidad de FPS.png".
 * <p>
 * Los juegos "framerate independent" (independientes de la tasa de fotogramas) mantienen la misma velocidad sin
 * importar los FPS. Por ejemplo, una entidad se mueve igualmente a 30 FPS en una computadora lenta y 60 FPS en una
 * rapida. La diferencia radica en que a 30 FPS, la entidad salta para ajustar la distancia, mientras que a 60 FPS, el
 * movimiento es mas fluido. Esto se debe a la variabilidad de FPS segun el hardware. Por otro lado, en un juego
 * "framerate dependent" (dependiente de los fotogramas), es mas lento en computadoras lentas. Para lograr independencia
 * de fotogramas, se aplica un delta constante, que se refiere al <b>tiempo fijo transcurrido entre cada tick de
 * actualizacion</b>.
 * <br><br>
 * <h2>Escalonar la fisica con deltas constantes</h2>
 * Escalonar la fisica con deltas constantes implica establecer una cantidad fija de actualizaciones de fisica por
 * segundo, declarado como {@code ticksPerSec}. La cantidad estandar suele ser 60 ticks por segundo, aunque puede variar.
 * Algunos juegos como Minecraft y Quake3 optan por 20 ticks posiblemente para evitar sobrecargar la CPU. La
 * actualizacion de la fisica no esta directamente relacionada con la cantidad de veces que se dibuja en pantalla,
 * aunque estan conectados. Al definir la cantidad de ticks, se establece un intervalo fijo para las actualizaciones de
 * la fisica, independiente de la tasa de fotogramas. Esto asegura que, aunque los FPS varien (causando tirones o
 * fluidez), la velocidad del juego se mantendra constante.
 * <p>
 * Lo optimo es {@link Measure medir} el tiempo en nanosegundos para el delta, ya que es mas preciso para la CPU que los
 * milisegundos. Los nanosegundos no dependen del sistema operativo, sino del procesador, y se miden segun los ciclos de
 * reloj. Un segundo equivale a 1.000.000.000 de nanosegundos o 1e9. Para calcular el tiempo entre cada frame con 60
 * ticks, se divide 60 por 1.000.000.000, que es aproximadamente 16.666.666 nanosegundos, valor conocido como {@link Delta#delta},
 * que implica que el Game Loop espera ese tiempo antes de actualizar la fisica nuevamente.
 * <p>
 * El termino "unprocessed" (tiempo no procesado) se refiere al tiempo que ha transcurrido entre dos actualizaciones
 * consecutivas del bucle de juego, pero que aun no se ha utilizado para realizar una actualizacion. En un Game Loop, se
 * utiliza para asegurarse de que las actualizaciones del juego se realicen a una velocidad constante, incluso si el
 * bucle se ejecuta a una tasa de fotogramas variable.
 * <p>
 * El concepto es el siguiente:
 * <ol>
 * <li>Cuando el bucle de juego comienza un nuevo ciclo, se captura el tiempo actual (tiempo de inicio) y se calcula la
 * diferencia de tiempo desde el ultimo ciclo.
 * <li>Esta diferencia de tiempo se acumula al "tiempo no procesado". Esto significa que si el bucle de juego se ejecuta
 * mas rapido que el tiempo delta deseado (por ejemplo, si el hardware es rapido), habra un exceso de tiempo no
 * procesado.
 * <li>Luego, se verifica si el tiempo no procesado ha acumulado suficiente para realizar una actualizacion completa
 * (por ejemplo, si es igual o mayor al tiempo delta deseado). Si es asi, se realiza la actualizacion del juego (como
 * avanzar la simulacion de la fisica, actualizar la logica del juego, etc.).
 * <li>Despues de la actualizacion, se resta el tiempo delta del tiempo no procesado, lo que significa que el exceso de
 * tiempo acumulado se mantiene para el siguiente ciclo.
 * </ol>
 * El objetivo de este enfoque es asegurar que, incluso si la velocidad del bucle de juego varia debido a diferencias en
 * la velocidad de la CPU o el rendimiento del sistema, las actualizaciones del juego sigan ocurriendo a una tasa
 * constante. Esto es especialmente importante para mantener una experiencia de juego coherente y equilibrada en
 * diferentes dispositivos y condiciones de rendimiento.
 * <p>
 * Una mayor diferencia entre el tiempo actual y el tiempo desde el ultimo ciclo, significa pocos pasos largos, es decir
 * menos frames. Y una diferencia menor indica muchos pasos cortos, osea mas frames (fluidez). <b>Esto hace posible que
 * el juego se ejecute en cualquier dispositivo a la misma velocidad independientemente de los FPS</b>.
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

    private int timer;
    private long startTime;
    private final double delta;
    // Mantiene un registro del tiempo no procesado (sin usar) para controlar cuando se debe realizar una actualizacion
    private double unprocessed;

    public Delta(int ticksPerSec) {
        // Obtiene el tiempo inicial
        startTime = System.nanoTime();
        // Calcula el tiempo en nanosegundos que deberia pasar entre cada tick para alcanzar la frecuencia deseada
        delta = 1e9 / ticksPerSec;
    }

    /**
     * Comprueba si el tiempo transcurrido alcanzo el tiempo delta.
     *
     * @return true si el tiempo transcurrido alcanzo el tiempo delta, o false.
     */
    public boolean checkDelta() {
        // Obtiene el tiempo actual
        long currentTime = System.nanoTime();
        // Calcula el tiempo que ha pasado desde el ultimo ciclo y lo agrega al tiempo no procesado
        unprocessed += currentTime - startTime;
        // La varible sirve como temporizador para mostrar la cantidad de ticks y frames cada un segundo
        timer += (int) (currentTime - startTime);
        // Actualiza el tiempo de inicio para el siguiente ciclo
        startTime = currentTime;
        // Verifica si ha pasado suficiente tiempo no procesado para realizar una actualizacion
        if (unprocessed >= delta) {
            // Resta el tiempo delta del tiempo no procesado, para que el exceso de tiempo se acumule para el siguiente ciclo
            unprocessed -= delta;
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
