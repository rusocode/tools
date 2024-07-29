package concurrency;

/**
 * <p>
 * Un <i>synchronized block</i> (bloque sincronizado) marca un metodo o bloque de codigo como sincronizado usando la pabalabra
 * clave {@code synchronized}. Un bloque sincronizado solo se puede ejecutar en un unico hilo a la vez (dependiendo de como lo
 * uses). Por lo tanto, se pueden utilizar bloques sincronizados para evitar condiciones de carrera.
 * <h3>Java Concurrency Utilities</h3>
 * <p>
 * El mecanismo synchronized fue el primer mecanismo de Java para sincronizar el acceso a objetos compartidos por multiples
 * subprocesos. Sin embargo, el mecanismo synchronized no es muy avanzado. Es por eso que Java 5 tiene un conjunto completo de
 * clases de utilidades de concurrencia para ayudar a los desarrolladores a implementar un control de concurrencia mas detallado
 * que el que se obtiene con synchronized.
 * <h3>La palabra clave synchronized con Java</h3>
 * <p>
 * Los bloques sincronizados estan marcados con la palabra clave synchronized. Un bloque sincronizado esta sincronizado en algun
 * objeto. Todos los bloques sincronizados en el mismo objeto solo pueden tener un hilo ejecutandose dentro de ellos al mismo
 * tiempo. Todos los demas subprocesos que intentan ingresar al bloque sincronizado se bloquean hasta que el subproceso dentro del
 * bloque sincronizado sale del bloque.
 * <p>
 * La palabra clave synchronized se puede utilizar para marcar cuatro tipos diferentes de bloques:
 * <ol>
 * <li>Metodos de instancia
 * <li>Metodos estaticos
 * <li>Bloques de codigo dentro de metodos de instancia
 * <li>Bloques de codigo dentro de metodos estaticos
 * </ol>
 * <p>
 * Estos bloques estan sincronizados en diferentes objetos. El tipo de bloque sincronizado que necesita depende de la situacion
 * concreta.
 * <h3>Metodos de instancia sincronizados</h3>
 * <pre>{@code
 * public class MyCounter {
 *
 *     private int count;
 *
 *     public synchronized void add(int value) {
 *         count += value;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Los metodos de instancia sincronizados se bloquean en el objeto que los posee, permitiendo que solo un hilo los ejecute a la
 * vez por instancia. Esta sincronizacion aplica a todos los metodos sincronizados del mismo objeto, evitando que multiples hilos
 * accedan simultaneamente a cualquiera de estos metodos en una instancia especifica. Esto garantiza la exclusion mutua y ayuda a
 * prevenir problemas de concurrencia en el acceso a los datos del objeto.
 * <pre>{@code
 * public class MyCounter {
 *
 *     private int count;
 *
 *     public synchronized void add(int value) {
 *         count += value;
 *     }
 *
 *     public synchronized void subtract(int value) {
 *         count -= value;
 *     }
 *
 * }
 * }</pre>
 * <h3>Metodos estaticos sincronizados</h3>
 * <pre>{@code
 * public static MyStaticCounter {
 *
 *     private static int count;
 *
 *     public static synchronized void add(int value) {
 *         count += value;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Los metodos estaticos sincronizados se bloquean en el objeto de clase, permitiendo que solo un hilo ejecute cualquier metodo
 * estatico sincronizado de esa clase a la vez. Esto asegura la exclusion mutua para todos los metodos estaticos sincronizados de
 * una clase, ya que existe un unico objeto de clase por clase en la JVM.
 * <pre>{@code
 * public static MyStaticCounter {
 *
 *     private static int count;
 *
 *     public static synchronized void add(int value) {
 *         count += value;
 *     }
 *
 *     public static synchronized void subtract(int value) {
 *         count -= value;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Solo un hilo puede ejecutarse dentro de cualquiera de los dos metodos {@code add()} y {@code subtract()} en un momento dado. Si
 * el subproceso A esta ejecutando add(), entonces el subproceso B no puede ejecutar ni add() ni subtract() hasta que el subproceso
 * A haya salido de add().
 * <p>
 * Si los metodos estaticos sincronizados estan ubicados en diferentes clases, entonces un hilo puede ejecutarse dentro de los
 * metodos estaticos sincronizados de cada clase. Un hilo por clase independientemente del metodo sincronizado estatico que llame.
 * <h3>Bloques sincronizados en metodos de instancia</h3>
 * <p>
 * No es necesario sincronizar un metodo completo. A veces es preferible sincronizar solo una parte de un metodo. Los bloques
 * sincronizados dentro de los metodos hacen esto posible.
 * <p>
 * Aqui hay un bloque sincronizado de codigo dentro de un metodo no sincronizado:
 * <pre>{@code
 * public void add(int value) {
 *     synchronized (this) {
 *         count += value;
 *     }
 * }
 * }</pre>
 * <p>
 * Este ejemplo utiliza la construccion de bloque sincronizado para marcar un bloque de codigo como sincronizado. Este codigo
 * ahora se ejecutara como si fuera un metodo sincronizado.
 * <p>
 * Los bloques sincronizados usan un objeto monitor (especificado entre parentesis) para controlar el acceso concurrente. Solo un
 * hilo puede ejecutar codigo sincronizado en el mismo monitor simultaneamente. Los metodos de instancia sincronizados usan
 * implicitamente el objeto actual ("this") como monitor. Esta tecnica permite sincronizacion selectiva de secciones de codigo,
 * ofreciendo un control mas preciso que la sincronizacion de metodos completos.
 * <p>
 * Los dos ejemplos siguientes estan sincronizados en la instancia a la que se les llama. Por tanto, son equivalentes en cuanto a
 * sincronizacion:
 * <pre>{@code
 * public class MyClass {
 *
 *     public synchronized void log1(String msg1, String msg2) {
 *         log.writeln(msg1);
 *         log.writeln(msg2);
 *     }
 *
 *     public void log2(String msg1, String msg2) {
 *         synchronized (this) {
 *             log.writeln(msg1);
 *             log.writeln(msg2);
 *        }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Por lo tanto, solo se puede ejecutar un subproceso dentro de cualquiera de los dos bloques sincronizados en este ejemplo.
 * <p>
 * Si el segundo bloque sincronizado se hubiera sincronizado en un objeto diferente a {@code this}, entonces un hilo a la vez habria
 * podido ejecutarse dentro de cada metodo.
 * <h3>Bloques sincronizados en metodos estaticos</h3>
 * <p>
 * Los bloques sincronizados tambien se pueden utilizar dentro de metodos estaticos. A continuacion se muestran los mismos dos
 * ejemplos de la seccion anterior como metodos estaticos. Estos metodos se sincronizan en el objeto de clase de la clase a la que
 * pertenecen los metodos:
 * <pre>{@code
 * public class MyClass {
 *
 *     public static synchronized void log1(String msg1, String msg2) {
 *         log.writeln(msg1);
 *         log.writeln(msg2);
 *     }
 *
 *     public static void log2(String msg1, String msg2) {
 *         synchronized (MyClass.class) {
 *             log.writeln(msg1);
 *             log.writeln(msg2);
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Solo un hilo puede ejecutarse dentro de cualquiera de estos dos metodos al mismo tiempo.
 * <p>
 * Si el segundo bloque sincronizado se hubiera sincronizado en un objeto diferente a {@code MyClass.class}, entonces un hilo podria
 * ejecutarse dentro de cada metodo al mismo tiempo.
 * <h3>Bloques sincronizados en expresiones Lambda</h3>
 * <p>
 * Incluso es posible utilizar bloques sincronizados dentro de una expresion lambda, asi como dentro de clases anonimas.
 * <p>
 * A continuacion se muestra un ejemplo de una expresion lambda con un bloque sincronizado en su interior. Observe que el bloque
 * sincronizado esta sincronizado en el objeto de clase de la clase que contiene la expresion lambda. Tambien podria haberse
 * sincronizado en otro objeto, si eso hubiera tenido mas sentido (dado un caso de uso especifico), pero usar el objeto de clase
 * esta bien para este ejemplo.
 * <pre>{@code
 * import java.util.function.Consumer;
 *
 * public class SynchronizedExample {
 *
 *     public static void main(String[] args) {
 *
 *         Consumer<String> func = (String param) -> {
 *
 *         synchronized (SynchronizedExample.class) {
 *
 *             System.out.println(Thread.currentThread().getName() + " step 1: " + param);
 *
 *             try {
 *                 Thread.sleep((long) (Math.random() * 1000));
 *             } catch (InterruptedException e) {
 *                 e.printStackTrace();
 *             }
 *
 *             System.out.println(Thread.currentThread().getName() + " step 2: " + param);
 *       }
 *
 *     };
 *
 *
 *     Thread thread1 = new Thread(() -> {func.accept("Parameter");}, "Thread 1");
 *     Thread thread2 = new Thread(() -> {func.accept("Parameter");}, "Thread 2");
 *
 *     thread1.start();
 *     thread2.start();
 *   }
 *
 * }
 * }</pre>
 * <h3>Sincronizacion y visibilidad de datos</h3>
 * <p>
 * Sin el uso de la palabra clave synchronized (o la palabra clave volatile de Java), no hay garantia de que cuando un subproceso
 * cambia el valor de una variable compartida con otros subprocesos (por ejemplo, a traves de un objeto al que todos los
 * subprocesos tienen acceso), los otros subprocesos pueden ver el valor cambiado. No hay garantias sobre cuando una variable
 * guardada en un registro de CPU por un subproceso se "compromete" en la memoria principal, y no hay garantia sobre cuando
 * otros subprocesos "actualizan" una variable guardada en un registro de CPU desde la memoria principal.
 * <p>
 * La palabra clave synchronized cambia eso. Cuando un subproceso ingresa a un bloque sincronizado, actualizara los valores de
 * todas las variables visibles para el subproceso. Cuando un subproceso sale de un bloque sincronizado, todos los cambios en las
 * variables visibles para el subproceso se confirmaran en la memoria principal. Esto es similar a como funciona la palabra clave
 * volatil.
 * <h3>Sincronizacion y reordenamiento de instrucciones</h3>
 * <p>
 * El reordenamiento de instrucciones en Java es una tecnica de optimizacion utilizada por el compilador y la JVM para mejorar el
 * rendimiento. Sin embargo, en programacion concurrente, este reordenamiento puede causar problemas de sincronizacion. La palabra
 * clave synchronized no solo garantiza la exclusion mutua, sino que tambien impone restricciones sobre el reordenamiento de
 * instrucciones antes, durante y despues de los bloques sincronizados.
 * <p>
 * Estas restricciones son similares a las que impone la palabra clave volatile. El objetivo es asegurar que el codigo se
 * comporte de manera predecible en entornos multihilo, evitando que las optimizaciones del compilador alteren la logica prevista
 * del programa. Esto permite a los desarrolladores escribir codigo concurrente con la confianza de que se ejecutara como se espera,
 * sin efectos secundarios inesperados debido al reordenamiento de instrucciones.
 * <h3>En que objetos sincronizar</h3>
 * <p>
 * Al sincronizar bloques, es crucial elegir cuidadosamente los objetos de sincronizacion. Se desaconseja usar objetos String o
 * contenedores de tipos primitivos, ya que el compilador puede optimizarlos, resultando en el uso inadvertido de la misma
 * instancia en diferentes partes del codigo. Por ejemplo:
 * <pre>{@code
 * synchronized ("Hey") {
 *     // Haz algo aqui
 * }
 * }</pre>
 * <p>
 * Sincronizar en el literal String "Hey" podria llevar a que multiples bloques se sincronicen en el mismo objeto si se usa el
 * mismo literal en varios lugares. Un problema similar puede ocurrir con {@code Integer.valueOf(1)}, donde Java podria reutilizar
 * la misma instancia para valores identicos. Esto puede resultar en una sincronizacion no intencionada entre bloques que se suponia
 * eran independientes. Para evitar estos problemas y garantizar un comportamiento predecible, se recomienda sincronizar en {@code this}
 * o en un nuevo objeto creado con {@code new Object()}, ya que Java no los almacena en cache ni los reutiliza internamente.
 * <h3>Limitaciones y alternativas del bloque sincronizado</h3>
 * <p>
 * Los bloques sincronizados tienen varias limitaciones. Por ejemplo, un bloque sincronizado solo permite la entrada de un unico
 * hilo a la vez. Sin embargo, ¿que pasaria si dos subprocesos solo quisieran leer un valor compartido y no actualizarlo? Podria
 * ser seguro permitirlo. Como alternativa a un bloque sincronizado, puede proteger el codigo con un bloqueo de lectura/escritura
 * que tiene una semantica de bloqueo mas avanzada que un bloque sincronizado. Java en realidad viene con una clase {@code ReadWriteLock}
 * incorporada que puedes usar.
 * <p>
 * ¿Que sucede si desea permitir que N subprocesos ingresen a un bloque sincronizado, y no solo a uno? Podrias usar un {@code Semaphore}
 * para lograr ese comportamiento. En realidad, Java viene con la clase Semaphore incorporada.
 * <p>
 * Los bloques sincronizados no garantizan en que orden los subprocesos que esperan ingresar a ellos tendran acceso al bloque
 * sincronizado. ¿Que sucede si necesita garantizar que los subprocesos que intentan ingresar a un bloque sincronizado obtengan
 * acceso en la secuencia exacta en la que solicitaron acceso? Debe implementar la {@code Fairness} (equidad) usted mismo.
 * <p>
 * ¿Que pasa si solo tiene un hilo escribiendo en una variable compartida y otros hilos solo leen esa variable? Entonces es posible
 * que puedas usar una variable volatil sin ninguna sincronizacion.
 * <h3>Gastos generales de rendimiento del bloque sincronizado</h3>
 * <p>
 * Los bloques sincronizados conllevan una pequeña sobrecarga de rendimiento al entrar y salir de ellos. Aunque esta sobrecarga ha
 * disminuido con la evolucion de Java, sigue existiendo un costo minimo. Esta sobrecarga es principalmente preocupante cuando se
 * entra y sale frecuentemente de un bloque sincronizado dentro de un bucle cerrado. Para optimizar el rendimiento, se recomienda
 * mantener los bloques sincronizados lo mas pequeños posible, incluyendo solo las operaciones que realmente requieren
 * sincronizacion. Esto evita bloquear innecesariamente otros hilos y permite mayor paralelismo en el codigo. En resumen, la
 * estrategia es sincronizar unicamente las instrucciones absolutamente necesarias, minimizando asi el impacto en el rendimiento y
 * maximizando la eficiencia del codigo multihilo.
 * <h3>Reentrada de bloque sincronizado</h3>
 * <p>
 * Una vez que un hilo ha ingresado a un bloque sincronizado, se dice que el hilo "mantiene el bloqueo" en el objeto de monitoreo
 * en el que esta sincronizado el bloque sincronizado. Si el hilo llama a otro metodo que vuelve a llamar al primer metodo con el
 * bloque sincronizado dentro, el hilo que sostiene el bloqueo puede volver a ingresar al bloque sincronizado. No esta bloqueado
 * solo porque un hilo (en si mismo) sostiene el bloqueo. Solo si un hilo diferente sujeta el candado. Mira este ejemplo:
 * <pre>{@code
 * public class MyClass {
 *
 *     List<String> elements = new ArrayList<>();
 *
 *     public void count() {
 *         if (elements.size() == 0) return 0;
 *         synchronized (this) {
 *             elements.remove();
 *             return 1 + count();
 *         }
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Olvidese por un momento que la forma anterior de contar los elementos de una lista no tiene ningun sentido. Solo concentrese en
 * como dentro del bloque sincronizado dentro del metodo {@code count()} se llama al metodo count() de forma recursiva. Por lo
 * tanto, el hilo que llama a count() puede eventualmente ingresar al mismo bloque sincronizado varias veces. Esto esta permitido.
 * Esto es posible.
 * <p>
 * Sin embargo, tenga en cuenta que los diseños en los que un hilo ingresa en multiples bloques sincronizados pueden provocar un
 * <b>nested monitor lockout</b> (bloqueo del monitor anidado) si no diseña su codigo con cuidado.
 * <h3>Bloques sincronizados en configuraciones de cluster</h3>
 * <p>
 * Tenga en cuenta que un bloque sincronizado solo bloquea los subprocesos dentro de la misma JVM para que no ingresen a ese
 * bloque de codigo. Si tiene la misma aplicacion Java ejecutandose en varias JVM (en un cluster), entonces es posible que un
 * subproceso <i>dentro de cada JVM</i> ingrese a ese bloque sincronizado al mismo tiempo.
 * <p>
 * Si necesita sincronizacion entre todas las maquinas virtuales Java de un cluster, debera utilizar otros mecanismos de
 * sincronizacion ademas de un bloque sincronizado.
 * <h3>Cita de Sun</h3>
 * <p>
 * synchronized: <i>"Los metodos permiten una estrategia simple para prevenir la interferencia del hilo y los errores de
 * consistencia de la memoria: si un objeto es visible para mas de un hilo, todas las lecturas o escrituras en las variables de ese
 * objeto se realizan a traves de metodos sincronizados."</i>
 * <p>
 * En pocas palabras: cuando tiene dos subprocesos que leen y escriben en el mismo "recurso", digamos una variable nombrada {@code height},
 * debe asegurarse de que estos subprocesos accedan a la variable de forma atomica. Sin la palabra clave synchronized, es posible
 * que el hilo A no vea el cambio que se hizo en el hilo B a la variable {@code height} o, lo que es peor, puede que solo se haya
 * cambiado a la mitad. Esto no seria lo que logicamente esperas.
 * <p>
 * Links:
 * <a href="https://jenkov.com/tutorials/java-concurrency/synchronized.html">Java Synchronized Blocks</a>
 */

public class Synchronized extends Thread {

    private final Counter counter;

    public Synchronized(Counter counter) {
        this.counter = counter;
    }

    public void run() {
        for (int i = 0; i < 10; i++)
            counter.increment();
        System.out.println(counter.count);
    }

    private static class Counter {

        int count;

        public synchronized void increment() {
            count++;
        }

    }

    public static void main(String[] args) throws InterruptedException {
        /* Aqui hay un ejemplo que inicia 2 subprocesos y ambos llaman al metodo add en la misma instancia de Counter. Solo un hilo
         * a la vez podra llamar al metodo add en la misma instancia, porque el metodo esta sincronizado en la instancia a la que
         * pertenece. Si los dos subprocesos hubieran hecho referencia a dos instancias de Counter separadas, no habria habido
         * problemas para llamar a los metodos add() simultaneamente. Las llamadas habrian sido a diferentes objetos, por lo que
         * los metodos llamados tambien se sincronizarian en diferentes objetos (el objeto propietario del metodo). Por lo tanto
         * las llamadas no se bloquearian. */
        Counter counter = new Counter();
        Thread A = new Synchronized(counter);
        Thread B = new Synchronized(counter);
        A.start();
        B.start();
    }

}
