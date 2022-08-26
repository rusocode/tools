package game_development;

/**
 * <h2>Reloj del sistema</h2>
 * El <a href="https://en.wikipedia.org/wiki/System_time">reloj del sistema</a> representa la nocion de un sistema
 * informatico del paso del tiempo. Este se mide mediante un reloj del sistema, que generalmente se implementa como un
 * simple recuento de la cantidad de <i>ticks</i> que han transcurrido desde una fecha de inicio arbitraria, denominada
 * <a href="https://en.wikipedia.org/wiki/Epoch_(computing)">epoca</a>.
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
 * <h2>Exactitud vs Precision</h2>
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
 * <p>Si desea calcular la cantidad de tiempo transcurrido entre dos eventos, como un cronometro, use {@code nanoTime()}
 * los cambios en el reloj de pared del sistema hacen que {@code currentTimeMillis()} sea incorrecto para este caso de
 * uso.
 *
 * <p>Ver tambien: <a href="http://docs.oracle.com/javase/8/docs/api/java/lang/System.html#nanoTime--">JavaDoc System.nanoTime()</a> y <a href="http://docs.oracle.com/javase/8/docs/api/java/lang/System.html#currentTimeMillis--">JavaDoc System.currentTimeMillis()</a> para mas informacion.
 *
 * <p>Recursos:
 * <a href="https://stackoverflow.com/questions/351565/system-currenttimemillis-vs-system-nanotime">System.currentTimeMillis() vs System.nanoTime()</a>
 * <a href="https://stackoverflow.com/questions/19052316/why-is-system-nanotime-way-slower-in-performance-than-system-currenttimemill">¿Por que System.nanoTime() es mucho mas lento (en rendimiento) que System.currentTimeMillis()?</a>
 */

public class SystemClock {
}
