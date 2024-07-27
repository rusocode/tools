package concurrency;

/**
 * <p>
 * Una <i>race condition</i> (condicion de carrera) es un problema de concurrencia que puede ocurrir dentro de una seccion critica.
 * Una <i>critical section</i> (seccion critica) es una seccion de codigo que se ejecuta mediante multiples subprocesos y donde la
 * secuencia de ejecucion de los subprocesos marca la diferencia en el resultado de la ejecucion simultanea de la seccion critica.
 * <p>
 * Cuando el resultado de varios subprocesos que ejecutan una seccion critica puede diferir segun la secuencia en la que se
 * ejecutan los subprocesos, se dice que la seccion critica contiene una condicion de carrera. El termino condicion de carrera
 * surge de la metafora de que los hilos corren a traves de la seccion critica y que el resultado de esa carrera impacta el
 * resultado de ejecutar la seccion critica.
 * <h3>Dos tipos de condiciones de carrera</h3>
 * <p>
 * Las condiciones de carrera pueden ocurrir cuando dos o mas subprocesos leen y escriben la misma variable de acuerdo con uno de
 * estos dos patrones:
 * <ul>
 * <li>Leer-modificar-escribir
 * <li>Verificar y luego actuar
 * </ul>
 * <p>
 * El patron lectura-modificacion-escritura significa que dos o mas subprocesos primero leen una variable determinada, luego
 * modifican su valor y lo escriben nuevamente en la variable. Para que esto cause un problema, el nuevo valor debe depender de
 * una forma u otra del valor anterior. El problema que puede ocurrir es que si dos subprocesos leen el valor (en los registros de
 * la CPU), modifican el valor (en los registros de la CPU) y luego escriben los valores nuevamente.
 * <p>
 * El patron verificar y luego actuar significa que dos o mas subprocesos verifican una condicion determinada, por ejemplo, si un
 * Map contiene un valor determinado, y luego actuan en funcion de esa informacion, por ejemplo, tomando el valor del Map. El
 * problema puede ocurrir si dos subprocesos verifican el Map en busca de un valor determinado al mismo tiempo (ven que el valor
 * esta presente) y luego ambos subprocesos intentan tomar (eliminar) ese valor. Sin embargo, solo uno de los hilos puede tomar el
 * valor. El otro hilo recuperara un valor nulo. Esto tambien podria suceder si se usara Queue en lugar de un Map.
 * <h3>Lectura-Modificacion-Escritura secciones criticas</h3>
 * <p>
 * Como se menciono anteriormente, una seccion critica de lectura, modificacion y escritura puede generar condiciones de carrera.
 * En esta seccion se analiza mas de cerca por que es asi. A continuacion se muestra un ejemplo de codigo Java de seccion critica
 * de lectura, modificacion y escritura que puede fallar si se ejecuta con varios subprocesos simultaneamente:
 * <pre>{@code
 * public class Counter {
 *
 *    private int count = 0;
 *
 *    public void add(int value) {
 *        count += value;
 *    }
 *
 * }
 * }</pre>
 * <p>
 * Imaginese si dos subprocesos, A y B, estuvieran ejecutando el metodo {@code add()} en la misma instancia de la clase Counter.
 * No hay forma de saber cuando el sistema operativo cambia entre los dos subprocesos. El codigo del metodo add no se ejecuta como
 * una unica instruccion atomica mediante la maquina virtual Java. Mas bien se ejecuta como un conjunto de instrucciones mas
 * pequeñas, similar a esta:
 * <ol>
 * <li>Read count from memory into register.
 * <li>Add value to register.
 * <li>Write register to memory.
 * </ol>
 * Observe lo que sucede con la siguiente ejecucion mixta de los subprocesos A y B:
 * <pre>{@code
 * count = 0;
 *
 * A:  Reads count into a register (0)
 * B:  Reads count into a register (0)
 * B:  Adds value 2 to register
 * B:  Writes register value (2) back to memory. count now equals 2
 * A:  Adds value 3 to register
 * A:  Writes register value (3) back to memory. count now equals 3
 * }</pre>
 * <p>
 * Los dos hilos querian agregar los valores 2 y 3 al contador. Por lo tanto, el valor deberia haber sido 5 despues de que los dos
 * subprocesos completen la ejecucion. Sin embargo, dado que la ejecucion de los dos hilos esta entrelazada, el resultado acaba
 * siendo diferente.
 * <p>
 * En el ejemplo de secuencia de ejecucion enumerado anteriormente, ambos subprocesos leen el valor 0 de la memoria. Luego suman
 * sus valores individuales, 2 y 3, al valor y escriben el resultado en la memoria. En lugar de 5, el valor que queda en {@code count}
 * sera el valor escrito por el ultimo hilo para escribir su valor. En el caso anterior es el hilo A, pero tambien podria haber
 * sido el hilo B.
 * <h4>Condiciones de carrera en secciones criticas de lectura, modificacion y escritura</h4>
 * <p>
 * El codigo del metodo add() del ejemplo anterior contiene una seccion critica. Cuando varios subprocesos ejecutan esta seccion
 * critica, se producen condiciones de carrera.
 * <p>
 * Mas formalmente, la situacion en la que dos subprocesos compiten por el mismo recurso, donde la secuencia en la que se accede
 * al recurso es significativa, se denomina condiciones de carrera. Una seccion de codigo que conduce a condiciones de carrera se
 * llama seccion critica.
 * <h3>Secciones criticas de verificar y luego actuar</h3>
 * <p>
 * Como tambien se menciono anteriormente, una seccion critica de verificar y luego actuar tambien puede generar condiciones de
 * carrera. Si dos subprocesos verifican la misma condicion, entonces actuan sobre esa condicion de una manera que cambie la
 * condicion y pueda conducir a condiciones de carrera. Si dos subprocesos verifican la condicion al mismo tiempo y luego un
 * subproceso continua y cambia la condicion, esto puede hacer que el otro subproceso actue incorrectamente en esa condicion.
 * <p>
 * Para ilustrar como una seccion critica de verificar y luego actuar puede generar condiciones de carrera, observe el siguiente
 * ejemplo:
 * <pre>{@code
 * public class CheckThenActExample {
 *
 *     public void checkThenAct(Map<String, String> sharedMap) {
 *         if (sharedMap.containsKey("key")) {
 *             String val = sharedMap.remove("key");
 *             if (val == null) {
 *                 System.out.println("Value for 'key' was null");
 *             }
 *         } else {
 *             sharedMap.put("key", "value");
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Si dos o mas subprocesos llaman al metodo {@code checkThenAct()} en el mismo objeto CheckThenActExample, entonces dos o mas
 * subprocesos pueden ejecutar la declaracion if al mismo tiempo, evaluar {@code shareMap.containsKey("key")} como {@code true} y
 * asi pasar al bloque de codigo del cuerpo de la declaracion if. Alli, varios subprocesos pueden intentar eliminar el par key-value
 * almacenado para la key "key", pero solo uno de ellos podra hacerlo. El resto obtendra un valor {@code null}, ya que otro hilo
 * ya elimino el par key-value.
 * <h3>Prevencion de condiciones de carrera</h3>
 * <p>
 * Para evitar que se produzcan condiciones de carrera, debe asegurarse de que la seccion critica se ejecute como una instruccion
 * atomica. Eso significa que una vez que un solo hilo lo ejecuta, ningun otro hilo puede ejecutarlo hasta que el primer hilo haya
 * abandonado la seccion critica.
 * <p>
 * Las condiciones de carrera se pueden evitar mediante una sincronizacion adecuada de los subprocesos en las secciones criticas.
 * La sincronizacion de subprocesos se puede lograr utilizando un <b>synchronized block</b> de codigo Java. La sincronizacion de
 * subprocesos tambien se puede lograr utilizando otras construcciones de sincronizacion como bloqueos o variables atomicas como
 * java.util.concurrent.atomic.AtomicInteger.
 * <h3>Rendimiento de la seccion critica</h3>
 * <p>
 * Para secciones criticas mas pequeñas, puede funcionar hacer toda la seccion critica un bloque sincronizado. Pero, para secciones
 * criticas mas grandes, puede ser beneficioso dividir la seccion critica en secciones criticas mas pequeñas, para permitir que
 * varios subprocesos ejecuten cada una de ellas una seccion critica mas pequeña. Esto puede disminuir la contencion sobre el
 * recurso compartido y, por lo tanto, aumentar el rendimiento de la seccion critica total.
 * <p>
 * Aqui hay un ejemplo de codigo Java muy simplificado para mostrar lo que quiero decir:
 * <pre>{@code
 * public class TwoSums {
 *
 *     private int sum1 = 0;
 *     private int sum2 = 0;
 *
 *     public void add(int val1, int val2) {
 *         synchronized(this) {
 *             sum1 += val1;
 *             sum2 += val2;
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como el metodo add() agrega valores a dos variables miembro de suma diferentes. Para evitar condiciones de carrera, la
 * suma se ejecuta dentro de un bloque sincronizado. Con esta implementacion, solo un subproceso puede ejecutar la suma al mismo
 * tiempo.
 * <p>
 * Links:
 * <a href="https://jenkov.com/tutorials/java-concurrency/race-conditions-and-critical-sections.html">Race Conditions and Critical Sections</a>
 * <a href="https://www.netjstech.com/2015/06/race-condition-in-java-multi-threading.html#:~:text=Race%20condition%20in%20Java%20occurs,thus%20the%20name%20race%20condition">Race Condition in Java Multi-Threading</a>
 */

public class RaceCondition implements Runnable {

    // Variables miembro del objeto (recurso compartido)
    private int count = 0;

    @Override
    public void run() {
        // Seccion critica
        // synchronized (this) {
        if (Thread.currentThread().getName().equals("Thread-A")) add(3);
        if (Thread.currentThread().getName().equals("Thread-B")) add(2);
        // }
    }

    private void add(int value) {
        count += value;
    }

    /**
     * <p>
     * Detiene el main thread brevemente, permitiendo que los subprocesos A y B tengan tiempo para ejecutarse y competir entre
     * si (condicion de carrera). Sin esta pausa, el hilo principal podria continuar inmediatamente y leer un valor de 'count'
     * igual a 0, ya que se ejecutaria en paralelo con los subprocesos A y B antes de que estos pudieran modificar la variable
     * compartida.
     * <p>
     * El uso de sleep() simula retrasos que ocurririan en un sistema real con multiples procesos y usuarios. En produccion,
     * el cambio de contexto entre subprocesos competidores es impredecible debido a la carga del sistema. Esto hace que los
     * errores por condiciones de carrera sean dificiles de detectar y reproducir, ya que dependen de patrones de ejecucion
     * especificos que pueden no ocurrir durante las pruebas. La simulacion ayuda a exponer estos problemas potenciales en un
     * entorno controlado. */
    private void waitUntilAllThreadsFinished() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        RaceCondition instance = new RaceCondition();
        /* Crea 2 subprocesos en donde se le pasa la instancia compartida y un nombre a cada uno. Por lo tanto, al compartir la
         * misma instancia, comparten la visibilidad de los datos en memoria. Esto se hace a proposito para generar una condicion
         * de carrera. Si ambos subprocesos usaran un objeto diferente cada uno, entonces no habria problema de constistencia, ya
         * que los metodos se llamarian en diferentes instancias y por lo tanto estarian sincronizados. */
        Thread A = new Thread(instance, "Thread-A");
        Thread B = new Thread(instance, "Thread-B");
        // Ejecuta los subprocesos conduciendo a una condicion de carrera en caso de que la seccion critica no este sincronizada
        A.start();
        B.start();
        instance.waitUntilAllThreadsFinished();
        System.out.println("Count value after threads are executed = " + instance.getCount());
        // Intentar varias veces ya que es dificil de reproducir el problema, siempre que no se utilice el bloque synchronized en la seccion critica
        if (instance.getCount() != 5) System.out.println("A race condition occurred!");
    }

}
