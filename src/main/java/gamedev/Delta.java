package gamedev;

/**
 * <h1>¿Por que necesitamos utilizar Delta Time?</h1>
 * Seguro que viste alguna vez un viejo juego funcionando en un dispositivo de alta gama a una gran velocidad, o el
 * caso contrario, un juego con unos requisitos elevados intentando funcionar en un viejo dispositivo con un resultado
 * horrible. Para solucionar este problema tenemos que hacer nuestro juego <i>framerate independiente</i>, y para
 * conseguirlo debemos aplicar el Delta Time.
 *
 * <p>Delta Time (Δt) es el tiempo transcurrido entre cada frame <a href="https://videojuegos.fandom.com/es/wiki/Renderizaci%C3%B3n">renderizado</a>
 * durante un segundo.
 *
 * <p>El tiempo para el delta se mide en nanosegundos ya que es una unidad mucho mas especifica para la CPU. Los
 * nanosegundos no dependen del sistema operativo, sino del procesador y se miden tomando como referencia los ciclos de
 * reloj del procesador. Hay 1.000.000.000 (1e9) de nanosegundos en un segundo, lo que indica que el tiempo entre cada
 * frame aplicando para este caso 60 ticks (60 actualizaciones por segundo), es igual a 1.000.000.000/60, que es
 * aproximadamente 16.666.666 de nanosegundos, valor conocido como {@code nsPerTick}. Esto significa que cada vez que se
 * actuliza el juego, el Game Loop espera 16.666.666 de nanosegundos antes de volver a llamar al metodo {@code tick()}.
 *
 * <br><br>
 *
 * <h2>¿Como encuentro el Delta Time?</h2>
 * Para encontrar el delta, es necesario calcular la diferencia de tiempo (en nanosegundos!) del actual y ultimo ciclo
 * de reloj acumulando el resultado en cada vuelta del Game Loop. Cuando el delta sea >= 1/60 de segundo o 16.666.666 de
 * nanosegundos para ser mas especificos, entonces actualiza nuevamente. Es importante eliminar 1/60 de segundo del
 * delta despues de actualizar, para que comience a contar desde el "desbordamiento" de tiempo. <b>Esto hace posible que
 * el juego se ejecute en cualquier dispositivo a la misma velocidad</b>.
 *
 * <p>La operacion {@code (currentTime - startTime) / nsPerTick}, solo esta ahi para hacer que el delta actue como un
 * porcentaje decimal de 1 de cuanto ha pasado del tiempo necesario. El 1 representa el 100% de 1/60 ticks.
 *
 * <p>La varible {@code timer} sirve de temporizador para mostrar la cantidad de ticks y frames cada 1 segundo.
 *
 * <p>TODO ¿A que se debe condicionar el delta con un ciclo {@code while()}?
 *
 * <p>Recursos:
 * <a href="https://www.parallelcube.com/es/2017/10/25/por-que-necesitamos-utilizar-delta-time/">¿Por que necesitamos utilizar Delta Time?</a>
 * <a href="https://stackoverflow.com/questions/26838286/delta-time-getting-60-updates-a-second-in-java">¿Como obtener 60 actualizaciones por segundo?</a>
 * <a href="https://stackoverflow.com/questions/37678799/how-to-get-accurate-deltatime-in-java">¿Como obtener el Delta Time preciso en Java?</a>
 * <a href="https://stackoverflow.com/questions/57710138/why-gameloops-render-more-times-than-updating#:~:text=A%20tick%20is%20whenever%20game,to%20a%20redstone%20circuit%20updating">¿Por que el game loop se renderiza mas veces de las que se actualiza?</a>
 * <a href="https://www.youtube.com/watch?v=3lSzfidowTE">Java Delta Time Tutorial</a>
 *
 * @author Juan Debenedetti
 */

public class Delta {
}
