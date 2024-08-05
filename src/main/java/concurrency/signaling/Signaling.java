package concurrency.signaling;

/**
 * <p>
 * Java ofrece mecanismos integrados para la comunicacion entre hilos mediante los metodos {@code wait()}, {@code notify()} y
 * {@code notifyAll()} de la clase Object. Estos permiten que los hilos se envien señales y esperen entre si, facilitando la
 * sincronizacion en escenarios como cuando un hilo necesita indicar a otro que los datos estan listos para ser procesados. Al ser
 * parte de Object, estas herramientas estan disponibles en todas las clases Java, proporcionando una solucion universal para la
 * coordinacion en programacion concurrente. Por ejemplo, un subproceso B podria esperar una señal del subproceso A que indique
 * que los datos estan listos para ser procesados.
 * <h3>wait(), notify() y notifyAll()</h3>
 * <p>
 * Un hilo que llama a wait() en cualquier objeto se vuelve inactivo hasta que otro hilo llama a notify() o notifyAll() en ese
 * objeto. Para llamar a wait(), notify() o notifyAll(), el hilo que llama primero debe obtener el bloqueo de ese objeto. En otras
 * palabras, el hilo que llama debe llamar a wait() o notify() desde dentro de un bloque sincronizado que este sincronizado en ese
 * objeto.
 * <p>
 * A continuacion se muestra una clase de ejemplo que se puede usar para que dos hilos pasen señales. Ambos hilos necesitan acceso
 * a la la misma instancia de esta clase. Un hilo llamara a {@code doWait()}, y el otro hilo llamara {@code doNotify()}.
 * <pre>{@code
 * public class MonitorObject {
 * }
 *
 * public class MyWaitNotify {
 *
 *     // Clase vacia que se usa como objeto de monitor para la sincronizacion
 *     MonitorObject myMonitorObject = new MonitorObject();
 *
 *     public void doWait() {
 *         synchronized (myMonitorObject) {
 *             try {
 *                 myMonitorObject.wait();
 *             } catch(InterruptedException e) {...}
 *         }
 *     }
 *
 *     public void doNotify() {
 *         synchronized (myMonitorObject) {
 *             myMonitorObject.notify();
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * El mecanismo de espera y notificacion en hilos funciona de la siguiente manera: Cuando un hilo llama a doWait(), entra en un
 * bloque sincronizado y luego invoca wait() en un objeto monitor, liberando el bloqueo y quedando bloqueado. Otro hilo puede
 * llamar a doNotify(), entrando en un bloque sincronizado del mismo objeto monitor y llamando a notify() para despertar un hilo
 * en espera. Sin embargo, el hilo despertado no puede salir de wait() hasta que el hilo que notifica libere el bloqueo al salir
 * de su bloque sincronizado. Una vez que esto ocurre, el hilo despertado puede finalmente salir de wait(), volver a entrar en el
 * bloque sincronizado y luego salir de el. Este proceso asegura una sincronizacion y comunicacion ordenada entre los hilos,
 * permitiendo un acceso controlado a los recursos compartidos.
 * <p>
 * <img src="signal object.png">
 * <p>
 * Varios hilos pueden invocar wait() en el mismo objeto monitor, quedando bloqueados hasta que se llame a notify() o notifyAll().
 * La diferencia clave es que notify() despierta solo a un hilo en espera, mientras que notifyAll() activa a todos los hilos
 * bloqueados. Es importante destacar que para llamar a wait(), notify() o notifyAll(), un hilo debe poseer el bloqueo de
 * sincronizacion del objeto en cuestion. Si se intenta invocar estos metodos sin tener el bloqueo, el sistema lanzara una
 * IllegalMonitorStateException. Este mecanismo asegura una coordinacion adecuada entre hilos y previene problemas de
 * concurrencia.
 * <h3>Señales Perdidas</h3>
 * <p>
 * El mecanismo de notificacion entre hilos tiene una limitacion importante: los metodos notify() y notifyAll() no almacenan las
 * llamadas si no hay hilos en espera cuando se invocan. Esto significa que si un hilo llama a notify() antes de que otro hilo
 * haya llamado a wait(), la señal de notificacion se pierde. Esta perdida de señales puede llevar a situaciones problematicas
 * donde un hilo queda esperando indefinidamente, sin recibir nunca la señal para despertarse. Para solucionar este problema, es
 * necesario implementar un sistema de almacenamiento de señales dentro de la propia clase que maneja la sincronizacion. Por
 * ejemplo, en una clase como MyWaitNotify, se deberia utilizar una variable miembro para guardar el estado de la señal de
 * notificacion, asegurando asi que ninguna señal se pierda y que los hilos puedan coordinar sus acciones de manera efectiva.
 * <pre>{@code
 * public class MyWaitNotify2 {
 *
 *     MonitorObject myMonitorObject = new MonitorObject();
 *     boolean wasSignalled;
 *
 *     public void doWait() {
 *         synchronized (myMonitorObject) {
 *             if (!wasSignalled) {
 *                 try {
 *                     myMonitorObject.wait();
 *                 } catch(InterruptedException e) {...}
 *             }
 *             // Limpia la seneal despues de que un hilo haya sido despertado, preparando el sistema para la proxima ronda de espera/notificacion
 *             wasSignalled = false;
 *         }
 *     }
 *
 *     public void doNotify() {
 *         synchronized (myMonitorObject) {
 *             wasSignalled = true;
 *             myMonitorObject.notify();
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como el metodo doNotify() ahora establece la variable wasSignalled en verdadero antes de llamar a notify(). Ademas,
 * observe como el metodo doWait() ahora verifica la variable wasSignalled antes de llamar a wait(). De hecho, solo llama a wait()
 * si no se recibio ninguna señal entre la llamada anterior a doWait() y esta.
 * <h3>Despertares espurios</h3>
 * <p>
 * Los despertares espurios son un fenomeno inesperado en la programacion concurrente donde los hilos pueden despertarse sin una
 * llamada explicita a notify() o notifyAll(). Esto puede causar problemas significativos en aplicaciones que dependen de una
 * sincronizacion precisa, como en el caso de la clase MyWaitNotify2. Para protegerse contra estos despertares no deseados, se
 * emplea una tecnica llamada "spin lock" o bloqueo de giro. En lugar de usar una simple declaracion if, se utiliza un bucle while
 * para verificar repetidamente la condicion de señalizacion. Este enfoque asegura que el hilo solo proceda cuando realmente haya
 * recibido la señal adecuada, evitando asi posibles errores de sincronizacion causados por despertares espurios. Esta
 * modificacion mejora la robustez y confiabilidad del mecanismo de espera y notificacion en entornos de programacion
 * concurrente.
 * <pre>{@code
 * public class MyWaitNotify3 {
 *
 *     MonitorObject myMonitorObject = new MonitorObject();
 *     boolean wasSignalled;
 *
 *     public void doWait() {
 *         synchronized (myMonitorObject) {
 *             while (!wasSignalled) {
 *                 try {
 *                     myMonitorObject.wait();
 *                 } catch(InterruptedException e) {...}
 *             }
 *             wasSignalled = false;
 *         }
 *     }
 *
 *     public void doNotify() {
 *         synchronized (myMonitorObject) {
 *             wasSignalled = true;
 *             myMonitorObject.notify();
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como la llamada wait() ahora esta anidada dentro de un bucle while en lugar de una declaracion if. Si el hilo en espera
 * se despierta sin haber recibido una señal, el miembro wasSignalled seguira siendo falso y el ciclo while se ejecutara una vez
 * mas, lo que hara que el hilo despertado vuelva a estar en espera.
 * <h3>Multiples hilos esperando las mismas señales</h3>
 * <p>
 * El bucle while tambien es una buena solucion si tiene varios subprocesos en espera, todos los cuales se activan usando
 * notifyAll(), pero solo se debe permitir que uno de ellos continue. Solo un subproceso a la vez podra obtener el bloqueo en el
 * objeto monitor, lo que significa que solo un subproceso puede salir de la llamada wait() y borrar el indicador wasSignalled.
 * Una vez que este hilo sale del bloque sincronizado en el metodo doWait(), los otros hilos pueden salir de la llamada wait() y
 * verificar la variable miembro wasSignalled dentro del ciclo while. Sin embargo, esta bandera fue borrada cuando el primer
 * subproceso se desperto, por lo que el resto de los subprocesos despertados vuelven a esperar hasta que llegue la siguiente
 * señal.
 * <h3>No llame a wait() en objetos globales o de cadenas constantes</h3>
 * <p>
 * Una version anterior de este texto tenia una edicion de la clase de ejemplo MyWaitNotify que usaba una cadena constante ("")
 * como objeto monitor. Asi es como se veia ese ejemplo:
 * <pre>{@code
 * public class MyWaitNotify {
 *
 *     String myMonitorObject = "";
 *     boolean wasSignalled;
 *
 *     public void doWait() {
 *         synchronized (myMonitorObject) {
 *             while (!wasSignalled) {
 *                 try {
 *                     myMonitorObject.wait();
 *                 } catch(InterruptedException e) {...}
 *             }
 *             // Limpia la señal
 *             wasSignalled = false;
 *         }
 *     }
 *
 *     public void doNotify() {
 *         synchronized (myMonitorObject) {
 *             wasSignalled = true;
 *             myMonitorObject.notify();
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * El problema con llamar a wait() y notify() en la cadena vacia, o cualquier otra cadena constante, es que la JVM/Compilador
 * traduce internamente cadenas constantes al mismo objeto. Eso significa que incluso si tiene dos instancias MyWaitNotify
 * diferentes, ambas hacen referencia a la misma instancia de cadena vacia. Esto tambien significa que los subprocesos que llaman
 * a doWait() en la primera instancia de MyWaitNotify corren el riesgo de ser despertados por llamadas de doNotify() en la segunda
 * instancia de MyWaitNotify.
 * <p>
 * La situacion se esquematiza en el siguiente diagrama:
 * <p>
 * <img src="no llame a wait() en objetos globales o de cadenas constantes.png">
 * <p>
 * La sincronizacion de hilos utilizando objetos compartidos como cadenas globales o constantes puede generar problemas sutiles
 * pero significativos. Aunque multiples hilos puedan llamar a wait() y notify() en el mismo objeto compartido, las señales se
 * almacenan individualmente en instancias separadas, lo que puede llevar a despertares espurios y perdida de señales. Este
 * problema se agrava cuando se usa notify() en lugar de notifyAll(), ya que solo un hilo se despierta, potencialmente ignorando
 * la señal destinada a otro hilo. Aunque notifyAll() podria resolver esto, su uso indiscriminado afecta negativamente el
 * rendimiento. La solucion recomendada es evitar el uso de objetos globales o constantes para mecanismos de wait()/notify(), y en
 * su lugar, utilizar objetos unicos para cada construccion que requiera sincronizacion. Por ejemplo, cada instancia de una clase
 * como MyWaitNotify3 deberia tener su propio objeto monitor, garantizando asi una sincronizacion precisa y evitando
 * interferencias no deseadas entre diferentes partes del sistema.
 * <p>
 * Links:
 * <a href="https://jenkov.com/tutorials/java-concurrency/thread-signaling.html">Thread Signaling in Java</a>
 */

public class Signaling {

    final MonitorObject myMonitorObject = new MonitorObject();
    boolean wasSignalled;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> System.out.println("Thread 1 running!"));
        Thread t2 = new Thread(() -> System.out.println("Thread 2 running!"));
        t1.start();
        t2.start();
    }

    public void doWait() {
        synchronized (myMonitorObject) {
            while (!wasSignalled) {
                try {
                    myMonitorObject.wait();
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            wasSignalled = false;
        }
    }

    public void doNotify() {
        synchronized (myMonitorObject) {
            wasSignalled = true;
            myMonitorObject.notify();
        }
    }

    private static class MonitorObject {

    }

}
