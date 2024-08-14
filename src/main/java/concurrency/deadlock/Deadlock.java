package concurrency.deadlock;

/**
 * <p>
 * Un punto muerto es cuando dos o mas subprocesos estan bloqueados esperando obtener bloqueos que algunos de los otros
 * subprocesos en el punto muerto estan manteniendo. El punto muerto puede ocurrir cuando varios subprocesos necesitan los mismos
 * bloqueos al mismo tiempo, pero los obtienen en diferentes orden.
 * <p>
 * A continuacion se muestra un ejemplo de una situacion de punto muerto:
 * <p>
 * Si el subproceso 1 bloquea A e intenta bloquear B, y el subproceso 2 ya ha bloqueado B e intenta bloquear A, surge un punto
 * muerto. El hilo 1 nunca podra obtener B y el hilo 2 nunca podra obtener A. Ademas, ninguno de los dos lo sabra jamas.
 * Permaneceran bloqueados en cada uno de sus objetos, A y B, para siempre. Esta situacion es un punto muerto.
 * <p>
 * La situacion se ilustra a continuacion:
 * <pre>{@code
 * Thread 1 locks A, waits for B
 * Thread 2 locks B, waits for A
 * }</pre>
 * <p>
 * A continuacion se muestra un ejemplo de una clase TreeNode que llama a metodos sincronizados en diferentes instancias:
 * <pre>{@code
 * public class TreeNode {
 *
 *     TreeNode parent;
 *     List childs = new ArrayList();
 *
 *     public synchronized void addChild(TreeNode child) {
 *         if (!childs.contains(child)) {
 *             childs.add(child);
 *             child.setParentOnly(this);
 *         }
 *     }
 *
 *     public synchronized void addChildOnly(TreeNode child) {
 *         if (!childs.contains(child) childs.add(child);
 *     }
 *
 *     public synchronized void setParent(TreeNode parent) {
 *         this.parent = parent;
 *         parent.addChildOnly(this);
 *     }
 *
 *     public synchronized void setParentOnly(TreeNode parent) {
 *         this.parent = parent;
 *     }
 * }
 * }</pre>
 * <p>
 * Si un subproceso (1) llama al metodo parent.addChild(child) al mismo tiempo que otro subproceso (2) llama al metodo
 * child.setParent(parent), en las mismas instancias principal e secundaria, puede producirse un punto muerto. Aqui hay un
 * pseudocodigo que ilustra esto:
 * <pre>{@code
 * Thread 1: parent.addChild(child); // Locks parent
 *           --> child.setParentOnly(parent);
 *
 * Thread 2: child.setParent(parent); // Locks child
 *           --> parent.addChildOnly()
 * }</pre>
 * <p>
 * El escenario describe un caso clasico de interbloqueo entre dos hilos. El hilo 1 bloquea el objeto parent al llamar a
 * {@code addChild()}, mientras el hilo 2 bloquea el objeto child al llamar a {@code setParent()}. Esto resulta en que ambos
 * objetos quedan bloqueados por hilos diferentes. Cuando el hilo 1 intenta acceder al objeto child y el hilo 2 al objeto parent,
 * ambos se bloquean mutuamente, creando un estancamiento. Este interbloqueo solo ocurre cuando los hilos ejecutan sus llamadas
 * simultaneamente sobre las mismas instancias de parent e child. Es importante notar que el codigo puede funcionar correctamente
 * durante mucho tiempo antes de que se produzca un interbloqueo. Para que ocurra, los hilos deben tomar los candados exactamente
 * al mismo tiempo; si un hilo se adelanta ligeramente, el interbloqueo no se produce. Debido a la naturaleza impredecible de la
 * programacion de hilos, es imposible predecir *cuando* ocurrira un interbloqueo, solo que existe la posibilidad de que *puede*
 * ocurrir.
 * <h3>Puntos muertos mas complicados</h3>
 * <p>
 * El punto muerto tambien puede incluir mas de dos subprocesos. Esto hace que sea mas dificil de detectar. A continuacion se
 * muestra un ejemplo en el que cuatro subprocesos se han estancado:
 * <pre>{@code
 * Thread 1 locks A, waits for B
 * Thread 2 locks B, waits for C
 * Thread 3 locks C, waits for D
 * Thread 4 locks D, waits for A
 * }</pre>
 * <p>
 * El hilo 1 espera al hilo 2, el hilo 2 espera al hilo 3, el hilo 3 espera al hilo 4 y el hilo 4 espera al hilo 1.
 * <h3>Puntos muertos en la base de datos</h3>
 * <p>
 * Una situacion mas complicada en la que pueden producirse interbloqueos es la transaccion de una base de datos. Una transaccion
 * de base de datos puede constar de muchas solicitudes de actualizacion de SQL. Cuando un registro se actualiza durante una
 * transaccion, ese registro se bloquea para actualizaciones de otras transacciones, hasta que se complete la primera transaccion.
 * Por lo tanto, cada solicitud de actualizacion dentro de la misma transaccion puede bloquear algunos registros en la base de
 * datos.
 * <p>
 * Si se ejecutan varias transacciones al mismo tiempo que necesitan actualizar los mismos registros, existe el riesgo de que
 * terminen en un punto muerto.
 * <p>
 * Por ejemplo:
 * <pre>{@code
 * Transaction 1, request 1, locks record 1 for update
 * Transaction 2, request 1, locks record 2 for update
 * Transaction 1, request 2, tries to lock record 2 for update
 * Transaction 2, request 2, tries to lock record 1 for update
 * }</pre>
 * <p>
 * Dado que los bloqueos se toman en diferentes solicitudes y no todos los bloqueos necesarios para una transaccion determinada se
 * conocen de antemano, es dificil detectar o evitar interbloqueos en las transacciones de la base de datos.
 * <p>
 * Links:
 * <a href="https://jenkov.com/tutorials/java-concurrency/deadlock.html">Deadlock</a>
 */

public class Deadlock {

}