package concurrency.deadlock;

/**
 * En algunas situaciones es posible evitar puntos muertos. Describire tres tecnicas en este texto:
 * <ol>
 * <li>Lock Ordering
 * <li>Lock Timeout
 * <li>Deadlock Detection
 * </ol>
 * <h3>Lock Ordering</h3>
 * <p>
 * El deadlock ocurre cuando varios subprocesos necesitan los mismos bloqueos pero los obtienen en diferentes orden.
 * <p>
 * Si se asegura de que todos los bloqueos se realicen siempre en el mismo orden en cualquier subproceso, no se produciran
 * interbloqueos. Mira este ejemplo:
 * <pre>{@code
 * Thread 1:
 *     lock A
 *     lock B
 *
 * Thread 2:
 *     wait for A
 *     lock C (when A locked)
 *
 * Thread 3:
 *     wait for A
 *     wait for B
 *     wait for C
 *
 * }</pre>
 * <p>
 * Si un hilo, como el Hilo 3, necesita varios candados, debe tomarlos en el orden decidido. No puede tomar un candado mas
 * adelante en la secuencia hasta que haya obtenido los candados anteriores.
 * <p>
 * Por ejemplo, ni el subproceso 2 ni el subproceso 3 pueden bloquear C hasta que hayan bloqueado A primero. Dado que el
 * subproceso 1 contiene el bloqueo A, los subprocesos 2 y 3 deben esperar primero hasta que se desbloquee el bloqueo A. Luego
 * deben lograr bloquear A, antes de poder intentar bloquear B o C.
 * <p>
 * La ordenacion de bloqueos es un mecanismo de prevencion de interbloqueos simple pero eficaz. Sin embargo, solo se puede
 * utilizar si conoce todos los candados necesarios antes de tomar cualquiera de ellos. Este no es siempre el caso.
 * <h3>Lock Timeout</h3>
 * <p>
 * Otro mecanismo de prevencion de interbloqueo es poner un tiempo de espera a los intentos de bloqueo, lo que significa que un
 * hilo que intenta obtener un bloqueo solo lo intentara durante un tiempo antes de darse por vencido. Si un hilo no logra tomar
 * todos los bloqueos necesarios dentro del tiempo de espera dado, realizara una copia de seguridad, liberara todos los bloqueos
 * tomados, esperara un periodo de tiempo aleatorio y luego volvera a intentarlo. La cantidad aleatoria de tiempo de espera sirve
 * para dar a otros subprocesos que intentan tomar los mismos bloqueos la oportunidad de tomar todos los bloqueos y, por lo tanto,
 * permitir que la aplicacion continue ejecutandose sin bloquearse.
 * <p>
 * A continuacion se muestra un ejemplo de dos subprocesos que intentan tomar los mismos dos bloqueos en diferente orden, donde
 * los subprocesos realizan una copia de seguridad y lo vuelven a intentar:
 * <pre>{@code
 * Thread 1 locks A
 * Thread 2 locks B
 *
 * Thread 1 attempts to lock B but is blocked
 * Thread 2 attempts to lock A but is blocked
 *
 * Thread 1's lock attempt on B times out
 * Thread 1 backs up and releases A as well
 * Thread 1 waits randomly (e.g. 257 millis) before retrying
 *
 * Thread 2's lock attempt on A times out
 * Thread 2 backs up and releases B as well
 * Thread 2 waits randomly (e.g. 43 millis) before retrying
 * }</pre>
 * <p>
 * En el escenario descrito, se explica como el mecanismo de tiempo de espera puede afectar la toma de candados entre hilos. El
 * hilo 2 intenta tomar los candados antes que el hilo 1, lo que probablemente le permita asegurar ambos. Sin embargo, se advierte
 * que el agotamiento del tiempo de espera no necesariamente indica un estancamiento, sino que podria deberse a que un hilo tarda
 * mucho en completar su tarea. Se señala que con mas hilos compitiendo por los mismos recursos, aumenta el riesgo de intentos
 * simultaneos de toma de candados, incluso con tiempos de espera aleatorios. Esto es especialmente problematico con un mayor
 * numero de hilos. Ademas, se menciona una limitacion de Java: no es posible establecer un tiempo de espera para entrar en un
 * bloque sincronizado. Para superar esto, se sugiere crear una clase de bloqueo personalizada o utilizar las construcciones de
 * concurrencia de Java 5 en el paquete java.util.concurrency.
 * <h3>Deadlock Detection</h3>
 * <p>
 * La deteccion de interbloqueos es un mecanismo de prevencion de interbloqueos mas potente dirigido a casos en los que no es
 * posible ordenar los bloqueos y no es factible el tiempo de espera de los bloqueos.
 * <p>
 * Cada vez que un hilo <b>toma</b> un bloqueo, se anota en una estructura de datos (map, graph etc.) de hilos y bloqueos. Ademas,
 * cada vez que un hilo <b>solicita</b> un bloqueo, esto tambien se indica en esta estructura de datos.
 * <p>
 * Cuando un subproceso solicita un bloqueo pero la solicitud es denegada, el subproceso puede atravesar el grafico de bloqueo
 * para comprobar si hay interbloqueos. Por ejemplo, si un subproceso A solicita el bloqueo 7, pero el subproceso B mantiene el
 * bloqueo 7, entonces el subproceso A puede verificar si el subproceso B ha solicitado alguno de los bloqueos que posee el
 * subproceso A (si corresponde). Si el subproceso B lo ha solicitado, se ha producido un deadlock (el subproceso A tomo el
 * bloqueo 1 y solicito el bloqueo 7; el subproceso B tomo el bloqueo 7 y solicito el bloqueo 1).
 * <p>
 * A continuacion se muestra un grafico de bloqueos tomados y solicitados por 4 subprocesos (A, B, C y D). Una estructura de datos
 * como esta que se puede utilizar para detectar puntos muertos.
 * <p>
 * <img src="deadlock detection.png">
 * <p>
 * Entonces, ¿que hacen los hilos si se detecta un deadlock?
 * <p>
 * Una posible accion es liberar todos los bloqueos, hacer una copia de seguridad, esperar un periodo de tiempo aleatorio y luego
 * volver a intentarlo. Esto es similar al mecanismo de tiempo de espera de bloqueo mas simple, excepto que los subprocesos solo
 * realizan una copia de seguridad cuando realmente se produce un deadlock. No solo porque se agoto el tiempo de espera de sus
 * solicitudes de bloqueo. Sin embargo, si muchos subprocesos compiten por los mismos bloqueos, pueden terminar repetidamente en
 * un deadlock incluso si retroceden y esperan.
 * <p>
 * Una mejor opcion es determinar o asignar una prioridad de los subprocesos para que solo uno (o algunos) realicen una copia de
 * seguridad. El resto de los hilos continuan tomando los bloqueos que necesitan como si no se hubiera producido ningun punto
 * muerto. Si la prioridad asignada a los subprocesos es fija, los mismos subprocesos siempre tendran mayor prioridad. Para evitar
 * esto, puede asignar la prioridad aleatoriamente cada vez que se detecte un deadlock.
 * <p>
 * Links: <a href="https://jenkov.com/tutorials/java-concurrency/deadlock-prevention.html">Deadlock Prevention</a>
 */

public class DeadlockPrevention {

}